package com.pizzeria.inventarioapp.Controllers; 

import com.pizzeria.inventarioapp.dto.MenuDTO;
import com.pizzeria.inventarioapp.Models.Services.MenuService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/menu") // Ruta base para los ítems del menú
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        List<MenuDTO> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Integer id) {
        Optional<MenuDTO> menuDTO = menuService.getMenuById(id);
        return menuDTO.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MenuDTO> createMenu(@RequestBody MenuDTO menuDTO) {
        try {
            MenuDTO nuevoMenu = menuService.createMenu(menuDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMenu);
        } catch (RuntimeException e) {
            // Captura simple para producto no encontrado, puedes hacer un manejo de errores más específico
            return ResponseEntity.badRequest().body(null); // O un DTO de error con el mensaje e.getMessage()
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> updateMenu(@PathVariable Integer id, @RequestBody MenuDTO menuDTO) {
         try {
            Optional<MenuDTO> updatedMenu = menuService.updateMenu(id, menuDTO);
            return updatedMenu.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Integer id) {
        boolean isDeleted = menuService.deleteMenu(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}