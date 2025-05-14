package com.pizzeria.inventarioapp.Controllers;

import com.pizzeria.inventarioapp.Models.Entity.Productos;
import com.pizzeria.inventarioapp.Models.Services.CategoriaService;
import com.pizzeria.inventarioapp.Models.Services.ProductoService;
import com.pizzeria.inventarioapp.dto.ProductoDTO;
import com.pizzeria.inventarioapp.dto.SubcategoriaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/productos")
public class ProductoControllers {

    private final ProductoService productoService;

    @Autowired
    public ProductoControllers(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() { // Devuelve List<ProductoDTO>
        List<ProductoDTO> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Integer id) { // Devuelve ProductoDTO
        Optional<ProductoDTO> productoDTO = productoService.getProductoById(id);
        return productoDTO.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> createProducto(@RequestBody Productos productoInput) { // Recibe la entidad, pero devuelve DTO
        // El servicio se encarga de la lógica y la conversión a DTO
        ProductoDTO nuevoProductoDTO = productoService.createProducto(productoInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProductoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Integer id, @RequestBody Productos productoDetails){ // Recibe entidad, devuelve DTO
        Optional<ProductoDTO> updatedProductoDTO = productoService.updateProducto(id, productoDetails);
        return updatedProductoDTO.map(ResponseEntity::ok) 
                .orElseGet(() -> ResponseEntity.notFound().build()); 
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        boolean isDeleted = productoService.deleteProducto(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

