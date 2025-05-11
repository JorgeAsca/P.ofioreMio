package com.pizzeria.inventarioapp.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "MenuItemIngredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_ingredient_id")
    private Integer menuItemIngredientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Productos product; // El ingrediente

    @Column(name = "quantity_required", nullable = false, precision = 10, scale = 3) // scale = 3 para más precisión si es necesario (ej: 0.005 kg)
    private BigDecimal quantityRequired;

    @Column(name = "unit_required", nullable = false, length = 50)
    private String unitRequired; // Ej: "kg", "gramos", "unidades"

}