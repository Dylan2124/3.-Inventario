package com.example.inventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStock;

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @Column(name = "cantidad_reservada")
    private Integer cantidadReservada;
}
