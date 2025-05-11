package com.pizzeria.inventarioapp.Models.Repository;

import com.pizzeria.inventarioapp.Models.Entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Productos, Integer> {
    List<Productos> findByCategoria(com.pizzeria.inventarioapp.Models.Entity.Categoria categoria);
    
}
