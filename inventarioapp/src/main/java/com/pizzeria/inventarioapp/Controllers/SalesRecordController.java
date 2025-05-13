package com.pizzeria.inventarioapp.Controllers;
import com.pizzeria.inventarioapp.dto.SalesRecordDTO;
import com.pizzeria.inventarioapp.Models.Services.SalesRecordService; // Ajusta el nombre del paquete si es diferente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sales")
public class SalesRecordController {

    private final SalesRecordService salesRecordService;

    @Autowired
    public SalesRecordController(SalesRecordService salesRecordService) {
        this.salesRecordService = salesRecordService;
    }

    @PostMapping
    public ResponseEntity<?> createSalesRecord(@RequestBody SalesRecordDTO salesRecordDTO) {
        try {
            SalesRecordDTO newSalesRecord = salesRecordService.createSalesRecord(salesRecordDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSalesRecord);
        } catch (RuntimeException e) {
            // Considera un manejo de errores más específico y DTOs de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<SalesRecordDTO>> getAllSalesRecords() {
        List<SalesRecordDTO> salesRecords = salesRecordService.getAllSalesRecords();
        return ResponseEntity.ok(salesRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesRecordDTO> getSalesRecordById(@PathVariable Integer id) {
        Optional<SalesRecordDTO> salesRecordDTO = salesRecordService.getSalesRecordById(id);
        return salesRecordDTO.map(ResponseEntity::ok)
                             .orElseGet(() -> ResponseEntity.notFound().build());
    }
}