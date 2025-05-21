package com.pizzeria.inventarioapp.Models.Services;

import com.pizzeria.inventarioapp.Models.Entity.Usuarios;
import com.pizzeria.inventarioapp.Models.Repository.UserRepository;
import com.pizzeria.inventarioapp.dto.AuthResponseDTO;
import com.pizzeria.inventarioapp.dto.LoginRequestDTO;
import com.pizzeria.inventarioapp.dto.RegisterRequestDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
   

    @Autowired
    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder
                       ) { 
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        
    }

    @Transactional
    public Usuarios registerUser(RegisterRequestDTO registerRequest) {
        
        if (userRepository.findByNombreUsuario(registerRequest.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("Error: El nombre de usuario '" + registerRequest.getNombreUsuario() + "' ya está en uso.");
        }

        
        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setNombreUsuario(registerRequest.getNombreUsuario());
        
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(registerRequest.getContrasena()));
        
        nuevoUsuario.setRole(registerRequest.getRol().toUpperCase()); 

        
        return userRepository.save(nuevoUsuario);
    }

    public AuthResponseDTO loginUser(LoginRequestDTO loginRequest) {
   
    Usuarios usuario = userRepository.findByNombreUsuario(loginRequest.getNombreUsuario())
            .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado..."));

    
    if (!passwordEncoder.matches(loginRequest.getContrasena(), usuario.getPasswordHash())) {
        throw new RuntimeException("Error: Contraseña incorrecta.");
    }

    
    String token = "simulated-jwt-token-for-" + usuario.getNombreUsuario(); 
    
    System.out.println("AuthService: Usuario autenticado - Nombre: " + usuario.getNombreUsuario() + ", Rol desde entidad: " + usuario.getRole()); // <--- AÑADE ESTE LOG

    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken(token);
    response.setNombreUsuario(usuario.getNombreUsuario());
    response.setRol(usuario.getRole()); 

    System.out.println("AuthService: Devolviendo AuthResponseDTO con rol: " + response.getRol()); // <--- AÑADE ESTE LOG
    return response;
}
}
