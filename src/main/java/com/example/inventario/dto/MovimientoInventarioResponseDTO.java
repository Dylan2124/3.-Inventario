package com.example.inventario.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoInventarioResponseDTO {

    private Long idMovimiento;
    private Long idStock;
    private String tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fechaMovimiento;
}
