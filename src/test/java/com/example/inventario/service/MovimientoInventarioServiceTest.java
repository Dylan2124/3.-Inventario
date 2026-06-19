package com.example.inventario.service;

import com.example.inventario.dto.MovimientoInventarioRequestDTO;
import com.example.inventario.dto.MovimientoInventarioResponseDTO;
import com.example.inventario.model.MovimientoInventario;
import com.example.inventario.repository.MovimientoInventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoInventarioServiceTest {

    @Mock
    private MovimientoInventarioRepository movimientoRepository;

    @InjectMocks
    private MovimientoInventarioService movimientoService;

    private MovimientoInventario movimientoFalso;
    private MovimientoInventarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // [ARRANGE GLOBAL] Preparamos los datos base antes de cada test
        LocalDateTime ahora = LocalDateTime.now();

        movimientoFalso = new MovimientoInventario();
        movimientoFalso.setIdMovimiento(1L);
        movimientoFalso.setIdStock(100L);
        movimientoFalso.setTipoMovimiento("ENTRADA");
        movimientoFalso.setCantidad(50);
        movimientoFalso.setFechaMovimiento(ahora);

        requestDTO = new MovimientoInventarioRequestDTO();
        // Nota: Asumimos que tu DTO tiene estos setters basados en cómo los usas en el Service
        requestDTO.setIdStock(100L);
        requestDTO.setTipoMovimiento("ENTRADA");
        requestDTO.setCantidad(50);
        requestDTO.setFechaMovimiento(ahora);
    }

    @Test
    @DisplayName("Debe obtener todos los movimientos")
    void obtenerTodosExito() {
        // 1. ARRANGE
        when(movimientoRepository.findAll()).thenReturn(Arrays.asList(movimientoFalso));

        // 2. ACT
        List<MovimientoInventarioResponseDTO> resultado = movimientoService.obtenerTodos();

        // 3. ASSERT
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdMovimiento());
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener un movimiento por ID")
    void obtenerPorIdExito() {
        // 1. ARRANGE
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimientoFalso));

        // 2. ACT
        Optional<MovimientoInventarioResponseDTO> resultado = movimientoService.obtenerPorId(1L);

        // 3. ASSERT
        assertTrue(resultado.isPresent());
        assertEquals(100L, resultado.get().getIdStock());
        verify(movimientoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe guardar un movimiento exitosamente")
    void guardarExito() {
        // 1. ARRANGE
        when(movimientoRepository.save(any(MovimientoInventario.class))).thenReturn(movimientoFalso);

        // 2. ACT
        MovimientoInventarioResponseDTO resultado = movimientoService.guardar(requestDTO);

        // 3. ASSERT
        assertNotNull(resultado);
        assertEquals("ENTRADA", resultado.getTipoMovimiento());
        assertEquals(50, resultado.getCantidad());
        verify(movimientoRepository, times(1)).save(any(MovimientoInventario.class));
    }

    @Test
    @DisplayName("Debe eliminar un movimiento por ID")
    void eliminarExito() {
        // 1. ARRANGE
        // doNothing() se usa para métodos void como deleteById
        doNothing().when(movimientoRepository).deleteById(1L);

        // 2. ACT
        movimientoService.eliminar(1L);

        // 3. ASSERT
        verify(movimientoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe buscar movimientos por ID de Stock")
    void buscarMovimientosPorStockExito() {
        // 1. ARRANGE
        when(movimientoRepository.findByIdStock(100L)).thenReturn(Arrays.asList(movimientoFalso));

        // 2. ACT
        List<MovimientoInventarioResponseDTO> resultado = movimientoService.buscarMovimientosPorStock(100L);

        // 3. ASSERT
        assertFalse(resultado.isEmpty());
        assertEquals(100L, resultado.get(0).getIdStock());
        verify(movimientoRepository, times(1)).findByIdStock(100L);
    }
}