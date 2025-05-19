package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;

public class MenuItemIngredienteDTO {
    private Integer menuItemIngredientId; // El ID de la propia entrada de ingrediente en la receta
    private Integer productoId; // ID del Producto
    private String nombreProducto; // Nombre del Producto (para mostrar fácilmente en el frontend)
    private BigDecimal quantityRequired;
    private String unitRequired;

    public MenuItemIngredienteDTO() {
    }

    public MenuItemIngredienteDTO(Integer menuItemIngredientId, Integer productoId, String nombreProducto, BigDecimal quantityRequired, String unitRequired) {
        this.menuItemIngredientId = menuItemIngredientId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.quantityRequired = quantityRequired;
        this.unitRequired = unitRequired;
    }

    // Getters y Setters (¡Muy importantes!)
    public Integer getMenuItemIngredientId() { return menuItemIngredientId; }
    public void setMenuItemIngredientId(Integer menuItemIngredientId) { this.menuItemIngredientId = menuItemIngredientId; }
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public BigDecimal getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(BigDecimal quantityRequired) { this.quantityRequired = quantityRequired; }
    public String getUnitRequired() { return unitRequired; }
    public void setUnitRequired(String unitRequired) { this.unitRequired = unitRequired; }
}