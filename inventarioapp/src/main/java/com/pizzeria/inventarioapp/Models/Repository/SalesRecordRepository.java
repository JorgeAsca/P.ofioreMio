package com.pizzeria.inventarioapp.Models.Repository;

import com.pizzeria.inventarioapp.Models.Entity.Menu;
import com.pizzeria.inventarioapp.Models.Entity.SalesRecord;
import com.pizzeria.inventarioapp.Models.Entity.SoldItem;
import com.pizzeria.inventarioapp.Models.Entity.Usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Integer> {

    // Método para encontrar registros de venta por fecha
    List<SalesRecord> findBySaleDate(Date saleDate); // Asegúrate que el tipo coincida con tu entidad (Date o LocalDate)

    // Método para encontrar registros de venta entre dos fechas
    List<SalesRecord> findBySaleDateBetween(Date startDate, Date endDate);

    // Método para encontrar registros de venta por usuario
    List<SalesRecord> findByUsuario(Usuarios usuario);
    
}
