package com.pizzeria.inventarioapp.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "subcategoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subcategoria")
    private Integer subcategoriaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false) 
    private Categoria categoria;

    @Column(name = "nombre_subcategoria", nullable = false, unique = true, length = 255)
    private String nombreSubcategoria;

    // Relación inversa (Una subcategoria puede tener muchos productos)
    @OneToMany(mappedBy = "subcategoria", cascade = CascadeType.PERSIST) 
    private Set<Productos> productos;
}

