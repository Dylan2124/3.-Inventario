package com.example.inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Este DTO es de ENTRADA: lo que el cliente envía en el body
 * del POST o PUT.
 *
 * REGLA CLARA:
 *   Las validaciones (@NotBlak, @Positive, etc...)
 *   viven AQUÍ y SOLO AQUÍ y NO en la entidad Libro.java
 *   Cuando el Controller usa @Valid, Spring valida
 *   este DTO. Si falla GlobalExceptionHandler (que lo vamos a construir)
 *   captura el error y devuelve un 400 con el mapa
 *   {"campo": "mensaje de error"}
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer cantidadDisponible;

    @NotNull(message = "La cantidad reservada es obligatoria")
    @PositiveOrZero(message = "La cantidad reservada no puede ser negativo")
    private Integer cantidadReservada;
}
