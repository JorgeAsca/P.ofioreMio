package com.pizzeria.inventarioapp.Models.Services;

import com.pizzeria.inventarioapp.Models.Entity.Menu;
import com.pizzeria.inventarioapp.Models.Entity.MenuItemIngrediente;
import com.pizzeria.inventarioapp.Models.Entity.Productos;
import com.pizzeria.inventarioapp.Models.Repository.MenuItemIngredienteRepository;
import com.pizzeria.inventarioapp.Models.Repository.MenuItemRepository;
import com.pizzeria.inventarioapp.Models.Repository.ProductRepository;
import com.pizzeria.inventarioapp.dto.MenuDTO;
import com.pizzeria.inventarioapp.dto.MenuItemIngredienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final ProductRepository productRepository;
    private final MenuItemIngredienteRepository menuItemIngredienteRepository;

    @Autowired
    public MenuService(MenuItemRepository menuItemRepository, ProductRepository productRepository, MenuItemIngredienteRepository menuItemIngredienteRepository) {
        this.menuItemRepository = menuItemRepository;
        this.productRepository = productRepository;
        this.menuItemIngredienteRepository = menuItemIngredienteRepository;
    }

    // Metodos de conversión DTOS --- Entidades privadas
    private MenuItemIngredienteDTO convertToMenuItemIngredienteDTO(MenuItemIngrediente menuItemIngrediente) {
        return new MenuItemIngredienteDTO(
                menuItemIngrediente.getMenuItemIngredientId(),
                menuItemIngrediente.getProduct().getProductoId(),
                menuItemIngrediente.getProduct().getNombreProducto(),
                menuItemIngrediente.getQuantityRequired(),
                menuItemIngrediente.getUnitRequired()
        );
    }

    private MenuDTO convertToMenuDTO(Menu entity){
        MenuDTO dto = new MenuDTO(
                entity.getMenuItemId(),
                entity.getItemName(),
                entity.getDescription(),
                entity.getSalePrice(),
                entity.getIsAvailable(),
                entity.getCreatedAt()
        );
        if (entity.getIngredients() != null) { // Asegurar que los ingredientes se carguen si es LAZY y estamos en transacción
            dto.setIngredients(entity.getIngredients().stream()
                    .map(this::convertToMenuItemIngredienteDTO)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    // Metodos del Serivicio 
    @Transactional(readOnly = true)
    public List<MenuDTO> getAllMenus(){
        return menuItemRepository.findAll().stream()
                .map(this::convertToMenuDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MenuDTO> getMenuById(Integer id) {
        return menuItemRepository.findById(id)
                .map(this::convertToMenuDTO);
    }

    @Transactional
    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setItemName(menuDTO.getItemName());
        menu.setDescription(menuDTO.getDescription());
        menu.setSalePrice(menuDTO.getSalePrice());
        menu.setIsAvailable(menuDTO.getIsAvailable() != null ? menuDTO.getIsAvailable() : true);

       

        if (menuDTO.getIngredients() != null && !menuDTO.getIngredients().isEmpty()) {
            Set<MenuItemIngrediente> ingredients = new HashSet<>();
            for (MenuItemIngredienteDTO ingDTO : menuDTO.getIngredients()) {
                Productos producto = productRepository.findById(ingDTO.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + ingDTO.getProductoId())); // O manejo de error más elegante

                MenuItemIngrediente ingrediente = new MenuItemIngrediente();
                ingrediente.setMenu(menu); // Establecer la relación bidireccional
                ingrediente.setProduct(producto);
                ingrediente.setQuantityRequired(ingDTO.getQuantityRequired());
                ingrediente.setUnitRequired(ingDTO.getUnitRequired());
                ingredients.add(ingrediente);
            }
            menu.setIngredients(ingredients);
        }
        Menu savedMenuWithIngredients = menuItemRepository.save(menu); // Guardar el menú con sus ingredientes asociados (gracias a CascadeType.ALL)
        return convertToMenuDTO(savedMenuWithIngredients);
    }

    @Transactional
    public Optional<MenuDTO> updateMenu(Integer id, MenuDTO menuDTO) {
        return menuItemRepository.findById(id)
            .map(existingMenu -> {
                existingMenu.setItemName(menuDTO.getItemName());
                existingMenu.setDescription(menuDTO.getDescription());
                existingMenu.setSalePrice(menuDTO.getSalePrice());
                existingMenu.setIsAvailable(menuDTO.getIsAvailable() != null ? menuDTO.getIsAvailable() : existingMenu.getIsAvailable());

                // Manejo de ingredientes: una forma simple es borrar los antiguos y añadir los nuevos.
                
                if (existingMenu.getIngredients() != null) {
                    
                    menuItemIngredienteRepository.deleteAll(existingMenu.getIngredients()); // Borrar ingredientes antiguos explícitamente
                    existingMenu.getIngredients().clear(); // Limpiar la colección en la entidad
                } else {
                    existingMenu.setIngredients(new HashSet<>());
                }
                
                menuItemRepository.saveAndFlush(existingMenu); // Guardar y sincronizar para que la limpieza de ingredientes se aplique

                if (menuDTO.getIngredients() != null && !menuDTO.getIngredients().isEmpty()) {
                    for (MenuItemIngredienteDTO ingDTO : menuDTO.getIngredients()) {
                        Productos producto = productRepository.findById(ingDTO.getProductoId())
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + ingDTO.getProductoId()));
                        MenuItemIngrediente ingrediente = new MenuItemIngrediente();
                        ingrediente.setMenu(existingMenu);
                        ingrediente.setProduct(producto);
                        ingrediente.setQuantityRequired(ingDTO.getQuantityRequired());
                        ingrediente.setUnitRequired(ingDTO.getUnitRequired());
                        existingMenu.getIngredients().add(ingrediente);
                    }
                }
                Menu updatedMenu = menuItemRepository.save(existingMenu);
                return convertToMenuDTO(updatedMenu);
            });
    }

    @Transactional
    public boolean deleteMenu(Integer id) {
        if (menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id); // CascadeType.ALL en Menu->ingredients debería eliminar los MenuItemIngrediente también
            return true;
        }
        return false;
    }


    
}
