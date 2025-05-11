package com.pizzeria.inventarioapp.Models.Repository;

import com.pizzeria.inventarioapp.Models.Entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuarios, Integer> {
    //Metodo para buscar un usuario por su nombre de usuario
    Optional<Usuarios> findByNombreUsuario(String nombreUsuario);

    

    
}
