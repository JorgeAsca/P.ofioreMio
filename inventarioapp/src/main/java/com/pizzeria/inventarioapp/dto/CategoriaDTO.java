package com.pizzeria.inventarioapp.dto;

// Utilizo DTOs para evitar problemas en la serialización JSON

// con Relaciones 

import java.util.Set;

public class CategoriaDTO {
    private Integer categoryId;
    private String categoryName;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Integer categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    

}
