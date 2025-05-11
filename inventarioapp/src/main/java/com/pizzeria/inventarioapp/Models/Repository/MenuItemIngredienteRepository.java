package com.pizzeria.inventarioapp.Models.Repository;

import com.pizzeria.inventarioapp.Models.Entity.Menu;
import com.pizzeria.inventarioapp.Models.Entity.MenuItemIngrediente;
import com.pizzeria.inventarioapp.Models.Entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemIngredienteRepository extends JpaRepository<MenuItemIngrediente, Integer> {
    
    //Metodo para encontrar tododos los ingredientes de un menu
    List<MenuItemIngrediente> findByMenu(Menu menu);

}
