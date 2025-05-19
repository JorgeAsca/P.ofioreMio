package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
// Considera añadir validaciones de Bean Validation aquí
// import jakarta.validation.constraints.*;
// import jakarta.validation.Valid; // Para validar la colección anidada

public class CreateMenuDTO {

    // @NotBlank
    private String itemName;

    private String description; // Opcional

    // @NotNull
    // @DecimalMin(value = "0.0")
    private BigDecimal salePrice;

    private Boolean isAvailable = true; // Valor por defecto

    // @NotEmpty // Asegura que la lista de ingredientes no esté vacía si se proporciona
    // @Valid    // Para que se validen los DTOs anidados
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