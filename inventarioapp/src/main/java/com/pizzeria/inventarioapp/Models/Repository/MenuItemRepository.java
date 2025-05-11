package com.pizzeria.inventarioapp.Models.Repository;

import com.pizzeria.inventarioapp.Models.Entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MenuItemRepository extends JpaRepository<Menu, Integer> {
    // Metodo para buscar un item por su nombre
    Optional<Menu> findByItemName(String itemName);

    // Metodo para buscar un item por su categoria
    List<Menu> findByIsAvailableTrue();
    

    
}
