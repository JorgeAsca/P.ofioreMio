package com.pizzeria.inventarioapp.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Integer menuItemId;

    @Column(name = "item_name", nullable = false, unique = true, length = 255)
    private String itemName;

    @Lob
    private String description;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "is_available")
    private Boolean isAvailable = true; // Valor por defecto

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    // Relación: Un ítem del menú está compuesto por muchos ingredientes (su receta)
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // EAGER puede ser útil para cargar la receta siempre
    private Set<MenuItemIngrediente> ingredients;

    // Relación inversa: Un ítem del menú puede estar en muchos ítems vendidos
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.PERSIST)
    private Set<SoldItem> soldItems;


}
