package com.pizzeria.inventarioapp.Models.Services;

import com.pizzeria.inventarioapp.Models.Entity.Categoria;
import com.pizzeria.inventarioapp.Models.Entity.Subcategoria;
import com.pizzeria.inventarioapp.Models.Repository.CategoryRepository;
import com.pizzeria.inventarioapp.Models.Repository.SubCateogryRepository; // Si necesitas interactuar con él directamente
import com.pizzeria.inventarioapp.dto.CategoriaDTO;
import com.pizzeria.inventarioapp.dto.SubcategoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoryRepository categoryRepository;
    private final SubCateogryRepository subCateogryRepository;

    @Autowired
    public CategoriaService(CategoryRepository categoryRepository, SubCateogryRepository subCateogryRepository) {
        this.categoryRepository = categoryRepository;
        this.subCateogryRepository = subCateogryRepository;
    }

    // Metodos para CategoriaDTO
    @Transactional(readOnly = true)
    public List<CategoriaDTO> getAllCategorias() {
        return categoryRepository.findAll().stream()
                .map(this::convertToCategoriaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoriaDTO> getcategoriaById(Integer id) {
        return categoryRepository.findById(id)
                .map(this::convertToCategoriaDTO);
    }

    @Transactional
    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(categoriaDTO.getCategoryName());
        // Aqui no voy a agregar subcategorias al crear la categoria principal
        Categoria savedCategoria = categoryRepository.save(categoria);
        return convertToCategoriaDTO(savedCategoria);
    }

    @Transactional
    public Optional<CategoriaDTO> updateCategoria(Integer id, CategoriaDTO categoriaDTO) {
        return categoryRepository.findById(id)
                .map(existingCategoria -> {
                    existingCategoria.setNombreCategoria(categoriaDTO.getCategoryName());
                    Categoria updatedCategoria = categoryRepository.save(existingCategoria);
                    return convertToCategoriaDTO(updatedCategoria);
                });

    }

    @Transactional
    public boolean deleteCategoria(Integer id) {
        if (categoryRepository.existsById(id)) {
            // Considera que sucede con las subcategorias y productos asociados
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Metodos para SubcategoriaDTO relacionados a Categoria

    @Transactional(readOnly = true)
    public Optional<List<SubcategoriaDTO>> getSubcategoriasByCategoriaId(Integer categoriaId) {
        Optional<Categoria> categoria = categoryRepository.findById(categoriaId);
        if(categoria.isPresent()) {
            // Accede al "Get" subcategoria dentro de la transaction para asegurar la caraga si es lazy
            Set<Subcategoria> subcategorias = categoria.get().getSubcategorias();
            if(subcategorias == null) { // Por si la categoria no esta inicializada
                return Optional.of(List.of());
            }
            List<SubcategoriaDTO> dtos = subcategorias.stream()
                    .map(this::convertToSubcategoriaDTO)
                    .collect(Collectors.toList());
            return Optional.of(dtos);
        }
        return Optional.empty(); // Categoria no encontrada 
    }

    // Metodos para crear una subcategoria asociada a una categoria
    @Transactional
    public Optional<SubcategoriaDTO> createSubcateoriaForCategoria(Integer categoriaId, SubcategoriaDTO subcategoriaDTO){
        Optional<Categoria> categoriaOpt = categoryRepository.findById(categoriaId);
        if(categoriaOpt.isPresent()) {
            Subcategoria subcategoria = new Subcategoria();
            subcategoria.setNombreSubcategoria(subcategoriaDTO.getSubcategoryName());
            subcategoria.setCategoria(categoriaOpt.get()); // Acá se le asigna la categoria padre 
            Subcategoria savedSubcategoria = subCateogryRepository.save(subcategoria);
            return Optional.of(convertToSubcategoriaDTO(savedSubcategoria));
        }
        return Optional.empty(); // categoria no encontrada
    }

    // Metodos de conversión

    private CategoriaDTO convertToCategoriaDTO(Categoria categoria){
        return new CategoriaDTO(categoria.getCategoriaId(), categoria.getNombreCategoria());

    }

    private SubcategoriaDTO convertToSubcategoriaDTO(Subcategoria subcategoria) {
        return new SubcategoriaDTO(subcategoria.getSubcategoriaId(), subcategoria.getNombreSubcategoria());
    }


    }

