package com.example.inventario.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * StockResponseDTO: Sin anotaciones de validación.
 * Este DTO es de SALIDA, el servidor lo construye,
 * no viene del cliente y por eso no necesita @Valid
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO extends RepresentationModel<StockResponseDTO> { // <-- HERENCIA

    private Long idStock;
    private Long idProducto;
    private Integer cantidadDisponible;
    private Integer cantidadReservada;
}