package com.pizzeria.inventarioapp.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProductoDTO {
    private Integer productoId;
    private String nombreProducto;
    private String descripcionProducto;
    private BigDecimal currentStock;
    private String unitOfMeasure;
    private CategoriaDTO nombreCategoria;
    private SubcategoriaDTO subcategoriaId; // O solo subcategoryId
    private BigDecimal minStockAlert;
    private String imageUrl; // Nuevo campo
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructores
    public ProductoDTO() {
    }

    public ProductoDTO(Integer productoId, String nombreProducto, String descripcionProducto,
            BigDecimal currentStock, String unitOfMeasure, BigDecimal minStockAlert,
            String imageUrl, Timestamp createdAt, Timestamp updatedAt,
            CategoriaDTO categoria, SubcategoriaDTO subcategoria) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.currentStock = currentStock;
        this.unitOfMeasure = unitOfMeasure;
        this.minStockAlert = minStockAlert;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.nombreCategoria = categoria;
        this.subcategoriaId = subcategoria;
    }

    // Getters y Setters
    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public CategoriaDTO getCategoria() {
        return nombreCategoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.nombreCategoria = categoria;
    }

    public SubcategoriaDTO getSubcategoria() {
        return subcategoriaId;
    }

    public void setSubcategoria(SubcategoriaDTO subcategoria) {
        this.subcategoriaId = subcategoria;
    }

    public BigDecimal getMinStockAlert() {
        return minStockAlert;
    }

    public void setMinStockAlert(BigDecimal minStockAlert) {
        this.minStockAlert = minStockAlert;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

}
