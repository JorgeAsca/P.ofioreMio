package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.HashSet;

public class MenuDTO {
    private Integer menuItemId;
    private String itemName;
    private String description;
    private BigDecimal salePrice;
    private Boolean isAvailable;
    private Timestamp createdAt;
    // Aquí usaremos MenuItemIngredienteDTO para mostrar los detalles de la receta
    private Set<MenuItemIngredienteDTO> ingredients = new HashSet<>();

    public MenuDTO() {
    }

    public MenuDTO(Integer menuItemId, String itemName, String description, BigDecimal salePrice, Boolean isAvailable, Timestamp createdAt) {
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.description = description;
        this.salePrice = salePrice;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    // Getters y Setters (¡Muy importantes!)
    public Integer getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Integer menuItemId) { this.menuItemId = menuItemId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Set<MenuItemIngredienteDTO> getIngredients() { return ingredients; }
    public void setIngredients(Set<MenuItemIngredienteDTO> ingredients) { this.ingredients = ingredients; }
}