package com.pizzeria.inventarioapp.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer productoId;

    @Column(name = "nombre_producto", nullable = false, length = 255)
    private String nombreProducto;

    @Lob //Campos de texto largos
    @Column(name = "descripcion_producto")
    private String descripcionProducto;

    @Column(name = "current_stock", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @Column(name = "unit_of_measure", nullable = false, length = 50)
    private String unitOfMeasure;

    @ManyToOne(fetch = FetchType.LAZY) //Carga perezosa para mejorar el rendimiento
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "id_subcategoria")
    private Subcategoria subcategoria;

    @Column(name = "alerta_minima", precision = 10, scale = 2)
    private BigDecimal minStockAlert;

    @Column(name = "imagen_Url", length = 1024)
    private String imagenUrl;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
    
}
