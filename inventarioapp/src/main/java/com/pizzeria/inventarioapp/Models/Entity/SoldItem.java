package com.pizzeria.inventarioapp.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "SoldItems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoldItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sold_item_id")
    private Integer soldItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_record_id", nullable = false)
    private SalesRecord salesRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private Menu menuItem;

    @Column(name = "quantity_sold", nullable = false)
    private Integer quantitySold;

    @Column(name = "price_at_sale", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtSale; // Precio al momento de la venta
}