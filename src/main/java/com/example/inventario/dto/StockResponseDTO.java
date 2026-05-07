package com.example.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * LibroResponseDTO: Sin anotaciones de validación.
 * Este DTO es de SALIDA, el servidor lo construye,
 * viene del cliente y por eso No necesita @Valid
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {

    private Long idStock;
    private Long idProducto;
    private Integer cantidadDisponible;
    private Integer cantidadReservada;

}
