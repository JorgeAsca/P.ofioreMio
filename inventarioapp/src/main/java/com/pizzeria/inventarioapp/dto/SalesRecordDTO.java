package com.pizzeria.inventarioapp.dto;

import java.util.Date; 
import java.util.List;
import java.util.ArrayList; // Importa ArrayList

public class SalesRecordDTO {
    private Integer salesRecordId;
    private Date saleDate; // Considera usar String "yyyy-MM-dd" para la entrada y convertir en el servicio
    private Integer userId; // "Opcional" ID del usuario que realizó (Hazta que se implemente la autenticación)
    private String notes;
    private List<SoldItemDTO> soldItems = new ArrayList<>();

    // Constructores
    public SalesRecordDTO() {}

    // Para respuestas
    public SalesRecordDTO(Integer salesRecordId, Date saleDate, Integer userId, String notes) {
        this.salesRecordId = salesRecordId;
        this.saleDate = saleDate;
        this.userId = userId;
        this.notes = notes;
    }

    // Getters y Setters
    public Integer getSalesRecordId() { return salesRecordId; }
    public void setSalesRecordId(Integer salesRecordId) { this.salesRecordId = salesRecordId; }
    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<SoldItemDTO> getSoldItems() { return soldItems; }
    public void setSoldItems(List<SoldItemDTO> soldItems) { this.soldItems = soldItems; }
}