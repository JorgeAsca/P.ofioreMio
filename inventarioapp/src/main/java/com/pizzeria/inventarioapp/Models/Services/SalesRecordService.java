package com.pizzeria.inventarioapp.Models.Services;

import com.pizzeria.inventarioapp.Models.Entity.*;
import com.pizzeria.inventarioapp.Models.Repository.*;
import com.pizzeria.inventarioapp.dto.SalesRecordDTO;
import com.pizzeria.inventarioapp.dto.SoldItemDTO;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SalesRecordService {

    private final SalesRecordRepository salesRecordRepository;
    private final UserRepository userRepository; // Para asignar el usuario al SalesRecord
    private final MenuItemRepository menuItemRepository; // Para obtener el Menu
    private final ProductRepository ProductRepository;

    @Autowired
    public SalesRecordService(SalesRecordRepository salesRecordRepository,
                              UserRepository userRepository,
                              MenuItemRepository menuItemRepository,
                              ProductRepository productRepository) {
        this.salesRecordRepository = salesRecordRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.ProductRepository = productRepository;
    }

    // --- Métodos de conversión DTO <-> Entidad (privados) ---
    private SoldItemDTO convertToSoldItemDTO(SoldItem entity) {
        return new SoldItemDTO(
                entity.getSoldItemId(),
                entity.getMenuItem().getMenuItemId(),
                entity.getQuantitySold(),
                entity.getPriceAtSale()
        );
    }

    private SalesRecordDTO convertToSalesRecordDTO(SalesRecord entity) {
        SalesRecordDTO dto = new SalesRecordDTO(
                entity.getSalesRecordId(),
                entity.getSaleDate(),
                entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null, // Asumiendo que Usuarios tiene getIdUsuario()
                entity.getNotes()
        );
        if (entity.getSoldItems() != null) {
            dto.setSoldItems(entity.getSoldItems().stream()
                    .map(this::convertToSoldItemDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }


    @Transactional // De importancia! para manejar transacciones
    public SalesRecordDTO createSalesRecord(SalesRecordDTO salesRecordDTO) {
        SalesRecord salesRecord = new SalesRecord();
        salesRecord.setSaleDate(salesRecordDTO.getSaleDate()); 
        salesRecord.setNotes(salesRecordDTO.getNotes());

        if (salesRecordDTO.getUserId() != null) {
            Usuarios user = userRepository.findById(salesRecordDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + salesRecordDTO.getUserId()));
            salesRecord.setUsuario(user);
        }

        Set<SoldItem> soldItemEntities = new HashSet<>();
        if (salesRecordDTO.getSoldItems() == null || salesRecordDTO.getSoldItems().isEmpty()) {
            throw new IllegalArgumentException("Una venta debe tener al menos un ítem vendido.");
        }

        for (SoldItemDTO itemDTO : salesRecordDTO.getSoldItems()) {
            Menu menu = menuItemRepository.findById(itemDTO.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Ítem del menú no encontrado con ID: " + itemDTO.getMenuItemId()));

            SoldItem soldItemEntity = new SoldItem();
            soldItemEntity.setSalesRecord(salesRecord); // Establecer relación bidireccional
            soldItemEntity.setMenuItem(menu);
            soldItemEntity.setQuantitySold(itemDTO.getQuantitySold());
            soldItemEntity.setPriceAtSale(itemDTO.getPriceAtSale() != null ? itemDTO.getPriceAtSale() : menu.getSalePrice()); // Usar precio del menú si no se especifica
            soldItemEntities.add(soldItemEntity);

            // --- LÓGICA DE DESCUENTO DE INVENTARIO ---
            if (menu.getIngredients() == null || menu.getIngredients().isEmpty()) {
                System.out.println("Advertencia: El ítem del menú '" + menu.getItemName() + "' no tiene ingredientes definidos, no se descontará stock.");
                
            } else {
                 for (MenuItemIngrediente ingredienteReceta : menu.getIngredients()) {
                    Productos productoInventario = ingredienteReceta.getProduct();
                    BigDecimal cantidadNecesariaPorUnidad = ingredienteReceta.getQuantityRequired();
                    BigDecimal cantidadTotalADescontar = cantidadNecesariaPorUnidad.multiply(BigDecimal.valueOf(itemDTO.getQuantitySold()));

                    BigDecimal stockActual = productoInventario.getCurrentStock();
                    if (stockActual.compareTo(cantidadTotalADescontar) < 0) {
                        // No hay suficiente stock
                        throw new RuntimeException("Stock insuficiente para el producto: " + productoInventario.getNombreProducto() +
                                ". Necesario: " + cantidadTotalADescontar + ", Disponible: " + stockActual);
                    }
                    productoInventario.setCurrentStock(stockActual.subtract(cantidadTotalADescontar));
                    ProductRepository.save(productoInventario); // Guardar el producto actualizado
                }
            }
        }
        salesRecord.setSoldItems(soldItemEntities);
        SalesRecord savedSalesRecord = salesRecordRepository.save(salesRecord);
        return convertToSalesRecordDTO(savedSalesRecord);
    }

    @Transactional(readOnly = true)
    public List<SalesRecordDTO> getAllSalesRecords() {
        return salesRecordRepository.findAll().stream()
                .map(this::convertToSalesRecordDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SalesRecordDTO> getSalesRecordById(Integer id) {
        return salesRecordRepository.findById(id)
                .map(this::convertToSalesRecordDTO);
    }
}
