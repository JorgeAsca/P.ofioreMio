package com.pizzeria.inventarioapp.dto;

public class SubcategoriaDTO {
    private Integer subcategoryId;
    private String subcategoryName;

    public SubcategoriaDTO() {
    }

    public SubcategoriaDTO(Integer subcategoryId, String subcategoryName) {
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
    }

    // Getters and Setters
    public Integer getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Integer subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }
}
