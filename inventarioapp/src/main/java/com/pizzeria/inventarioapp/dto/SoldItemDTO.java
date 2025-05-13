package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;

public class SoldItemDTO {
    
    private Integer soldItemId;
    private Integer menuItemId; // ID del Menu (pizza) vendido
    private Integer quantitySold;
    private BigDecimal priceAtSale; // "Opcional" precio al momento de la venta

    // Constructores
    public SoldItemDTO() {}

    // Para peticiones de creación
    public SoldItemDTO(Integer menuItemId, Integer quantitySold, BigDecimal priceAtSale) {
        this.menuItemId = menuItemId;
        this.quantitySold = quantitySold;
        this.priceAtSale = priceAtSale;
    }

    // Para respuestas (incluyendo el ID)
    public SoldItemDTO(Integer soldItemId, Integer menuItemId, Integer quantitySold, BigDecimal priceAtSale) {
        this.soldItemId = soldItemId;
        this.menuItemId = menuItemId;
        this.quantitySold = quantitySold;
        this.priceAtSale = priceAtSale;
    }

    // Getters y Setters
    public Integer getSoldItemId() { return soldItemId; }
    public void setSoldItemId(Integer soldItemId) { this.soldItemId = soldItemId; }
    public Integer getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Integer menuItemId) { this.menuItemId = menuItemId; }
    public Integer getQuantitySold() { return quantitySold; }
    public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }
    public BigDecimal getPriceAtSale() { return priceAtSale; }
    public void setPriceAtSale(BigDecimal priceAtSale) { this.priceAtSale = priceAtSale; }
}