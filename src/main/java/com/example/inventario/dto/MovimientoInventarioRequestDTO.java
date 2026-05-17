package com.example.inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioRequestDTO {


    @NotNull(message = "El ID del producto es obligatorio")
    private Long idStock;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento;

    @NotNull(message = "La cantidad es obligatoria")
    @PositiveOrZero(message = "La cantidad no puede ser negativo")
    private Integer cantidad;

    @NotNull(message = "La fecha del movimiento no puede ser nula")
    private LocalDateTime fechaMovimiento;

}


