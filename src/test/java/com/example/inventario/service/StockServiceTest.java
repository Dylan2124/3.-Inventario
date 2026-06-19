package com.example.inventario.service;

import com.example.inventario.client.NotificacionClient;
import com.example.inventario.dto.NotificacionRequestDTO;
import com.example.inventario.dto.StockRequestDTO;
import com.example.inventario.dto.StockResponseDTO;
import com.example.inventario.model.Stock;
import com.example.inventario.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

// Importaciones estáticas para las comprobaciones (Asserts) y simulaciones (When/Verify)
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // <-- Le dice a JUnit que use las herramientas de Mockito
class StockServiceTest {

    @Mock
    private StockRepository stockRepository; // <-- Clon falso de la base de datos

    @Mock
    private NotificacionClient notificacionClient; // <-- Clon falso del microservicio de Notificaciones

    @InjectMocks
    private StockService stockService; // <-- El servicio REAL donde se inyectarán los clones falsos

    // Objetos que usaremos como "fichas de juego" en los tests
    private Stock stockFalso;
    private StockRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // [ARRANGE GLOBAL]: Antes de CADA test, creamos datos limpios en memoria
        stockFalso = new Stock();
        stockFalso.setIdStock(1L);
        stockFalso.setIdProducto(100L);
        stockFalso.setCantidadDisponible(50);
        stockFalso.setCantidadReservada(5);

        requestDTO = new StockRequestDTO();
        requestDTO.setIdProducto(100L);
        requestDTO.setCantidadDisponible(50);
        requestDTO.setCantidadReservada(5);
    }

    @Test
    @DisplayName("Debe guardar un stock de manera exitosa")
    void guardarStockExito() {
        // 1. ARRANGE (Preparar el escenario)
        // Le ordenamos al clon de la BD: "Cuando el servicio te pida guardar, tú devuelve nuestro stockFalso"
        when(stockRepository.save(any(Stock.class))).thenReturn(stockFalso);

        // 2. ACT (Ejecutar la acción real)
        StockResponseDTO resultado = stockService.guardar(requestDTO);

        // 3. ASSERT (Comprobar los resultados)
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdStock());
        assertEquals(50, resultado.getCantidadDisponible());

        // Verificación de seguridad: Comprobar que el servicio realmente usó la BD exactamente 1 vez
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    @DisplayName("Debe buscar un stock por ID y encontrarlo")
    void obtenerPorIdExito() {
        // 1. ARRANGE
        // Simulamos que al buscar el ID 1, la BD sí lo encuentra y lo devuelve
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stockFalso));

        // 2. ACT
        Optional<StockResponseDTO> resultado = stockService.obtenerPorId(1L);

        // 3. ASSERT
        assertTrue(resultado.isPresent(), "El resultado debería contener datos");
        assertEquals(100L, resultado.get().getIdProducto());
        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe actualizar el stock y disparar la llamada Feign a Notificaciones")
    void actualizarStockYNotificarExito() {
        // 1. ARRANGE
        StockRequestDTO datosNuevos = new StockRequestDTO();
        datosNuevos.setIdProducto(100L);
        datosNuevos.setCantidadDisponible(20); // Cambiamos el stock a 20
        datosNuevos.setCantidadReservada(5);

        Stock stockActualizado = new Stock();
        stockActualizado.setIdStock(1L);
        stockActualizado.setIdProducto(100L);
        stockActualizado.setCantidadDisponible(20);
        stockActualizado.setCantidadReservada(5);

        // Configuramos los mocks para la simulación en cadena
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stockFalso));
        when(stockRepository.save(any(Stock.class))).thenReturn(stockActualizado);

        // 2. ACT
        Optional<StockResponseDTO> resultado = stockService.actualizar(1L, datosNuevos);

        // 3. ASSERT
        assertTrue(resultado.isPresent());
        assertEquals(20, resultado.get().getCantidadDisponible(), "El stock debió bajar a 20");

        // ¡PRUEBA REINA DE MICROSERVICIOS!: Verificamos que se ejecutó la llamada OpenFeign a Notificaciones
        verify(notificacionClient, times(1)).enviarNotificacion(any(NotificacionRequestDTO.class));
    }
}