package com.example.inventario.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoInventarioResponseDTO extends RepresentationModel<MovimientoInventarioResponseDTO> { // <-- HERENCIA

    private Long idMovimiento;
    private Long idStock;
    private String tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fechaMovimiento;
}