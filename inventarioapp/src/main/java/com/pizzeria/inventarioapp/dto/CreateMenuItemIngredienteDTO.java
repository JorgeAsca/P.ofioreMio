package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;
// Considera añadir validaciones de Bean Validation aquí si quieres (@NotNull, @Positive, etc.)
// import jakarta.validation.constraints.*;

public class CreateMenuItemIngredienteDTO {

    // @NotNull // Ejemplo de validación
    private Integer productoId; // ID del Producto (ingrediente)

    // @NotNull
    // @DecimalMin(value = "0.0", inclusive = false) // Debe ser mayor que 0
    private BigDecimal quantityRequired;

    // @NotBlank // Ejemplo de validación
    private String unitRequired;

    // Constructor por defecto (necesario para Jackson)
    public CreateMenuItemIngredienteDTO() {
    }

    // Constructor con todos los campos (opcional, pero útil)
    public CreateMenuItemIngredienteDTO(Integer productoId, BigDecimal quantityRequired, String unitRequired) {
        this.productoId = productoId;
        this.quantityRequired = quantityRequired;
        this.unitRequired = unitRequired;
    }

    // Getters y Setters (¡Muy importantes!)
    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public BigDecimal getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(BigDecimal quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public String getUnitRequired() {
        return unitRequired;
    }

    public void setUnitRequired(String unitRequired) {
        this.unitRequired = unitRequired;
    }
}