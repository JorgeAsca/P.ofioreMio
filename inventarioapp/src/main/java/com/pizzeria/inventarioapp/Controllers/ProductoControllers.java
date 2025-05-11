package com.pizzeria.inventarioapp.Controllers;

import com.pizzeria.inventarioapp.Models.Entity.Productos;
import com.pizzeria.inventarioapp.Models.Services.ProductoService;
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
    public ResponseEntity<List<Productos>> getAllProductos() {
        List<Productos> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos); // Devuelve 200 OK con la lista de productos
    }

    // Empoint para obtener un producto por su id
    @GetMapping("/{id}")
    public ResponseEntity<Productos> getProductoById(@PathVariable Integer id) {
        Optional<Productos> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)// Devuelve 200 OK con el producto encontrado
                .orElseGet(() -> ResponseEntity.notFound().build()); // Devuelve 404 Not Found
    }

    // Empoint para crear un nuevo producto
    @PostMapping
    public ResponseEntity<Productos> createProducto(@RequestBody Productos producto) {
        // @RequestBody indica que el cuerpo de la solicutud HTTP (Json) se convertira en un objeto Producto
        Productos nuevoProducto = productoService.createProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);// Devuelve 201 Created con el nuevo producto 
    }

    // Empoint para actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Productos> updateProducto(@PathVariable Integer id, @RequestBody Productos productoDetails){
        Optional<Productos> updatedProducto = productoService.updateProducto(id, productoDetails);
        return updatedProducto.map(ResponseEntity::ok) 
                .orElseGet(() -> ResponseEntity.notFound().build()); 

    } 

    // Empoint para eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        boolean isDeleted = productoService.deleteProducto(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found si el producto no se encuentra
        }
    }
}
