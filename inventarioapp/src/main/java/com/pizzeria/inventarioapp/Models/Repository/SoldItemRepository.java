package com.pizzeria.inventarioapp.Models.Repository;

import com.pizzeria.inventarioapp.Models.Entity.Menu;
import com.pizzeria.inventarioapp.Models.Entity.SalesRecord;
import com.pizzeria.inventarioapp.Models.Entity.SoldItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SoldItemRepository extends JpaRepository<SoldItem, Integer> {

    // Método para encontrar todos los ítems vendidos en un registro de venta específico
    List<SoldItem> findBySalesRecord(SalesRecord salesRecord);

    // Método para encontrar todos los registros de venta de un ítem del menú específico
    List<SoldItem> findByMenuItem(Menu menuItem);
}