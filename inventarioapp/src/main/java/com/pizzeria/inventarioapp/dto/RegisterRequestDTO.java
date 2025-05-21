package com.pizzeria.inventarioapp.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    
    private String nombreUsuario;

    
    private String contrasena; 

    
    private String rol; 
}
