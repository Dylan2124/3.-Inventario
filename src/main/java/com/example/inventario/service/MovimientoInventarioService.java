package com.example.inventario.service;

import com.example.inventario.dto.MovimientoInventarioResponseDTO;
import com.example.inventario.dto.MovimientoInventarioRequestDTO;
import com.example.inventario.model.MovimientoInventario;
import com.example.inventario.repository.MovimientoInventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;

    // ── MAPEO ────────────────────────────────────────────────────────
    private MovimientoInventarioResponseDTO mapToDTO(MovimientoInventario movimiento) {
        return new MovimientoInventarioResponseDTO(
                movimiento.getIdMovimiento(),
                movimiento.getIdStock(),
                movimiento.getTipoMovimiento(),
                movimiento.getCantidad(),
                movimiento.getFechaMovimiento()
        );
    }

    // ── GET OBTENER TODOS ────────────────────────────────────────────────
    public List<MovimientoInventarioResponseDTO> obtenerTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ── OBTENER POR ID ───────────────────────────────────────────────
    public Optional<MovimientoInventarioResponseDTO> obtenerPorId(Long id) {
        return movimientoRepository.findById(id).map(this::mapToDTO);
    }

    // ── GUARDAR ──────────────────────────────────────────────────────
    public MovimientoInventarioResponseDTO guardar(MovimientoInventarioRequestDTO dto) {
        MovimientoInventario movimiento = new MovimientoInventario();

        movimiento.setIdStock(dto.getIdStock());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setFechaMovimiento(dto.getFechaMovimiento());

        MovimientoInventario guardado = movimientoRepository.save(movimiento);
        return mapToDTO(guardado);
    }

    // ── ELIMINAR ─────────────────────────────────────────────────────
    public void eliminar(Long id) {
        movimientoRepository.deleteById(id);
    }

    // ── BUSCAR POR STOCK ────────────────────────────
    public List<MovimientoInventarioResponseDTO> buscarMovimientosPorStock(Long idStock) {
        return movimientoRepository.findByIdStock(idStock)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}