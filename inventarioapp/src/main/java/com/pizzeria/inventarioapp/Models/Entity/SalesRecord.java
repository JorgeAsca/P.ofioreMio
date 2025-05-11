package com.pizzeria.inventarioapp.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Date; // O java.time.LocalDate si prefieres
import java.util.Set;

@Entity
@Table(name = "SalesRecords")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_record_id")
    private Integer salesRecordId;

    @Temporal(TemporalType.DATE) // Solo la fecha, sin hora
    @Column(name = "sale_date", nullable = false)
    private Date saleDate; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Puede ser nullable si así lo definiste en el SQL
    private Usuarios usuario;

    @Lob
    private String notes;

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private Timestamp recordedAt;

    // Relación: Un registro de venta contiene muchos ítems vendidos
    @OneToMany(mappedBy = "salesRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SoldItem> soldItems;

}
