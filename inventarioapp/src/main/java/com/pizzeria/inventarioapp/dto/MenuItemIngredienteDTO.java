package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;

public class MenuItemIngredienteDTO {
    private Integer menuItemIngredientId; // Opcional, más útil para respuestas que para peticiones de creación
    private Integer productoId; // ID del producto (ingrediente)
    private String nombreProducto; // Opcional, para mostrar en respuestas
    private BigDecimal quantityRequired;
    private String unitRequired;

    // Constructores, Getters, Setters
    public MenuItemIngredienteDTO() {}

    // Constructor para crear desde la entidad (útil en el servicio)
    public MenuItemIngredienteDTO(Integer menuItemIngredientId, Integer productoId, String nombreProducto, BigDecimal quantityRequired, String unitRequired) {
        this.menuItemIngredientId = menuItemIngredientId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.quantityRequired = quantityRequired;
        this.unitRequired = unitRequired;
    }
    
    // Constructor para recibir en peticiones (sin el id del ingrediente mismo, ni el nombre del producto)
    public MenuItemIngredienteDTO(Integer productoId, BigDecimal quantityRequired, String unitRequired) {
        this.productoId = productoId;
        this.quantityRequired = quantityRequired;
        this.unitRequired = unitRequired;
    }


    // Getters y Setters (omito por brevedad, pero debes añadirlos)
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