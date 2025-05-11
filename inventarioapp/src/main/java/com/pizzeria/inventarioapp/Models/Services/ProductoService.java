package com.pizzeria.inventarioapp.Models.Services;

import com.pizzeria.inventarioapp.Models.Entity.Productos;
import com.pizzeria.inventarioapp.Models.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductRepository productRepository;
    @Autowired //Inyeccion de las dependencias
    public ProductoService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    //Metodo para obtener todos los productos
    @Transactional(readOnly = true)
    public List<Productos> getAllProductos() {
        return productRepository.findAll();
    }

    //Metodo para obtener un producto por su id
    @Transactional(readOnly = true)
    public Optional<Productos> getProductoById(Integer id) {
        return productRepository.findById(id);
    }

    //Metodo para crear un nuevo producto
    @Transactional //Transaccion de escritura
    public Productos createProducto(Productos producto) {
        return productRepository.save(producto);
    }
    
    //Metodo para actualizar un producto
    @Transactional
    public Optional<Productos> updateProducto(Integer id, Productos productoDetails) {
        return productRepository.findById(id).map(producto -> {
            producto.setNombreProducto(productoDetails.getNombreProducto());
            producto.setDescripcionProducto(productoDetails.getDescripcionProducto());
            producto.setCurrentStock(productoDetails.getCurrentStock());
            producto.setUnitOfMeasure(productoDetails.getUnitOfMeasure());
            producto.setCategoria(productoDetails.getCategoria());
            producto.setSubcategoria(productoDetails.getSubcategoria());
            producto.setMinStockAlert(productoDetails.getMinStockAlert());
            return productRepository.save(producto);
        });
    }

    //Metodo para eliminar un producto
    @Transactional
    public boolean deleteProducto(Integer id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false; // Lanza una excepción si el prducto no se encuentra
    }
}
