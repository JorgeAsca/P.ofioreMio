package com.pizzeria.inventarioapp.Models.Services;

import com.pizzeria.inventarioapp.Models.Entity.Categoria;
import com.pizzeria.inventarioapp.Models.Entity.Subcategoria;
import com.pizzeria.inventarioapp.Models.Repository.CategoryRepository;
import com.pizzeria.inventarioapp.Models.Repository.SubCateogryRepository;
import com.pizzeria.inventarioapp.dto.CategoriaDTO;
import com.pizzeria.inventarioapp.dto.ProductoDTO;
import com.pizzeria.inventarioapp.dto.SubcategoriaDTO;
import com.pizzeria.inventarioapp.Models.Entity.Productos;
import com.pizzeria.inventarioapp.Models.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCateogryRepository subcategoryRepository; 

    @Autowired
    public ProductoService(ProductRepository productRepository,
                           CategoryRepository categoryRepository,
                           SubCateogryRepository subcategoryRepository) { 
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    // --- MÉTODO DE CONVERSIÓN A DTO ---
    private ProductoDTO convertToProductoDTO(Productos producto) {
        if (producto == null) {
            return null;
        }
        ProductoDTO dto = new ProductoDTO();
        dto.setProductoId(producto.getProductoId());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcionProducto(producto.getDescripcionProducto());
        dto.setCurrentStock(producto.getCurrentStock());
        dto.setUnitOfMeasure(producto.getUnitOfMeasure());
        dto.setMinStockAlert(producto.getMinStockAlert());
        dto.setImageUrl(producto.getImagenUrl());
        dto.setCreatedAt(producto.getCreatedAt());
        dto.setUpdatedAt(producto.getUpdatedAt());

        if (producto.getCategoria() != null) {
            // Accede a los campos aquí para asegurar carga si es LAZY
            Categoria catEntidad = producto.getCategoria();
            dto.setCategoria(new CategoriaDTO(catEntidad.getCategoriaId(), catEntidad.getNombreCategoria()));
        }

        if (producto.getSubcategoria() != null) {
            // Accede a los campos aquí para asegurar carga si es LAZY
            Subcategoria subcatEntidad = producto.getSubcategoria();
            dto.setSubcategoria(new SubcategoriaDTO(subcatEntidad.getSubcategoriaId(), subcatEntidad.getNombreSubcategoria()));
        }
        return dto;
    }
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> getAllProductos() { 
        return productRepository.findAll().stream()
                .map(this::convertToProductoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductoDTO> getProductoById(Integer id) { 
        return productRepository.findById(id)
                .map(this::convertToProductoDTO);
    }

    @Transactional
    public ProductoDTO createProducto(Productos productoInput) { 
        Productos nuevoProducto = new Productos();
        nuevoProducto.setNombreProducto(productoInput.getNombreProducto());
        nuevoProducto.setDescripcionProducto(productoInput.getDescripcionProducto());
        nuevoProducto.setCurrentStock(productoInput.getCurrentStock());
        nuevoProducto.setUnitOfMeasure(productoInput.getUnitOfMeasure());
        nuevoProducto.setMinStockAlert(productoInput.getMinStockAlert()); // Nombre correcto del campo DTO/Entidad
        nuevoProducto.setImagenUrl(productoInput.getImagenUrl());

        if (productoInput.getCategoria() != null && productoInput.getCategoria().getCategoriaId() != null) {
            Categoria cat = categoryRepository.findById(productoInput.getCategoria().getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoInput.getCategoria().getCategoriaId()));
            nuevoProducto.setCategoria(cat);
        }

        if (productoInput.getSubcategoria() != null && productoInput.getSubcategoria().getSubcategoriaId() != null) {
            Subcategoria subcat = subcategoryRepository.findById(productoInput.getSubcategoria().getSubcategoriaId())
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada con ID: " + productoInput.getSubcategoria().getSubcategoriaId()));
            nuevoProducto.setSubcategoria(subcat);
        }
        
        Productos savedProducto = productRepository.save(nuevoProducto);
        return convertToProductoDTO(savedProducto); 
    }
    
    @Transactional
    public Optional<ProductoDTO> updateProducto(Integer id, Productos productoDetails) { 
        return productRepository.findById(id).map(existingProducto -> {
            existingProducto.setNombreProducto(productoDetails.getNombreProducto());
            existingProducto.setDescripcionProducto(productoDetails.getDescripcionProducto());
            existingProducto.setCurrentStock(productoDetails.getCurrentStock());
            existingProducto.setUnitOfMeasure(productoDetails.getUnitOfMeasure());
            existingProducto.setMinStockAlert(productoDetails.getMinStockAlert());
            existingProducto.setImagenUrl(productoDetails.getImagenUrl());

            if (productoDetails.getCategoria() != null && productoDetails.getCategoria().getCategoriaId() != null) {
                Categoria cat = categoryRepository.findById(productoDetails.getCategoria().getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoDetails.getCategoria().getCategoriaId()));
                existingProducto.setCategoria(cat);
            } else {
                existingProducto.setCategoria(null);
            }

            if (productoDetails.getSubcategoria() != null && productoDetails.getSubcategoria().getSubcategoriaId() != null) {
                Subcategoria subcat = subcategoryRepository.findById(productoDetails.getSubcategoria().getSubcategoriaId())
                    .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada con ID: " + productoDetails.getSubcategoria().getSubcategoriaId()));
                existingProducto.setSubcategoria(subcat);
            } else {
                existingProducto.setSubcategoria(null);
            }

            Productos updatedProducto = productRepository.save(existingProducto);
            return convertToProductoDTO(updatedProducto); 
        });
    }

    @Transactional
    public boolean deleteProducto(Integer id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}