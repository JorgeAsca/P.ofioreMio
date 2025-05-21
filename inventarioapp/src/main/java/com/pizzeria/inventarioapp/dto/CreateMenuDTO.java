package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;


public class CreateMenuDTO {

    // @NotBlank
    private String itemName;

    private String description;

    
    private BigDecimal salePrice;

    private Boolean isAvailable = true; 

    
    private Set<CreateMenuItemIngredienteDTO> ingredients = new HashSet<>();

    public CreateMenuDTO() {
    }

    // Getters y Setters (¡Muy importantes!)
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public Set<CreateMenuItemIngredienteDTO> getIngredients() { return ingredients; }
    public void setIngredients(Set<CreateMenuItemIngredienteDTO> ingredients) { this.ingredients = ingredients; }
}