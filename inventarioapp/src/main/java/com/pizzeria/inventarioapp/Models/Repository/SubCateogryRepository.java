package com.pizzeria.inventarioapp.Models.Repository;

import com.pizzeria.inventarioapp.Models.Entity.Categoria;
import com.pizzeria.inventarioapp.Models.Entity.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCateogryRepository extends JpaRepository<Subcategoria, Integer> {
    Optional<Subcategoria> findByNombreSubcategoria(String nombreSubcategoria);
    List<Subcategoria> findByCategoria(Categoria categoria);

    
}
