package com.pizzeria.inventarioapp.Controllers;

import com.pizzeria.inventarioapp.Models.Entity.Usuarios;
import com.pizzeria.inventarioapp.Models.Services.AuthService;
import com.pizzeria.inventarioapp.dto.AuthResponseDTO;
import com.pizzeria.inventarioapp.dto.LoginRequestDTO;
import com.pizzeria.inventarioapp.dto.RegisterRequestDTO;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth") 
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(/*@Valid*/ @RequestBody RegisterRequestDTO registerRequest) {
        try {
            Usuarios nuevoUsuario = authService.registerUser(registerRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente: " + nuevoUsuario.getNombreUsuario());
        } catch (RuntimeException e) {
           
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequest) { 
    try {
        AuthResponseDTO authResponse = authService.loginUser(loginRequest);
        // Si quieres loguear aquí lo que se va a enviar:
        System.out.println("AuthController: Enviando respuesta de login: Token=" + authResponse.getToken() + ", Rol=" + authResponse.getRol()); // <--- AÑADE ESTE LOG
        return ResponseEntity.ok(authResponse);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
}
