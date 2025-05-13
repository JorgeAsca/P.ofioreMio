package com.pizzeria.inventarioapp.Controllers;

import com.pizzeria.inventarioapp.dto.CategoriaDTO;
import com.pizzeria.inventarioapp.dto.SubcategoriaDTO;
import com.pizzeria.inventarioapp.Models.Services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // GET /api/v1/categorias
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias() {
        List<CategoriaDTO> categorias = categoriaService.getAllCategorias();
        return ResponseEntity.ok(categorias);
    }

    // GET /api/v1/categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Integer id) {
        Optional<CategoriaDTO> categoriaDTO = categoriaService.getcategoriaById(id);
        return categoriaDTO.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/v1/categorias
    @PostMapping
    public ResponseEntity<CategoriaDTO> createCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        // Para el POST, recibimos un DTO y el servicio también podría aceptar/devolver DTO.
        // El DTO para la creación no necesitaría el ID.
        CategoriaDTO nuevaCategoria = categoriaService.createCategoria(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    // PUT /api/v1/categorias/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO categoriaDTO) {
        Optional<CategoriaDTO> updatedCategoria = categoriaService.updateCategoria(id, categoriaDTO);
        return updatedCategoria.map(ResponseEntity::ok)
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/categorias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        boolean isDeleted = categoriaService.deleteCategoria(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/v1/categorias/{id}/subcategorias
    @GetMapping("/{id}/subcategorias")
    public ResponseEntity<List<SubcategoriaDTO>> getSubcategoriasByCategoria(@PathVariable("id") Integer categoriaId) {
        Optional<List<SubcategoriaDTO>> subcategorias = categoriaService.getSubcategoriasByCategoriaId(categoriaId);
        return subcategorias.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build()); // 404 si la categoría no existe
    }

    // POST /api/v1/categorias/{id}/subcategorias
    @PostMapping("/{id}/subcategorias")
    public ResponseEntity<SubcategoriaDTO> createSubcategoriaForCategoria(@PathVariable("id") Integer categoriaId, @RequestBody SubcategoriaDTO subcategoriaDTO) {
        Optional<SubcategoriaDTO> nuevaSubcategoria = categoriaService.createSubcateoriaForCategoria(categoriaId, subcategoriaDTO);
        return nuevaSubcategoria.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 si la categoría padre no existe
    }
}