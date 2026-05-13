package com.example.inventario.service;

import com.example.inventario.dto.StockRequestDTO;
import com.example.inventario.dto.StockResponseDTO;
import com.example.inventario.model.Stock;
import com.example.inventario.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    // Mapeo convierte la Entidad (BD) a DTO (Respuesta)
    private StockResponseDTO mapToDTO(Stock stock) {
        return new StockResponseDTO(
                stock.getIdStock(),
                stock.getIdProducto(),
                stock.getCantidadDisponible(),
                stock.getCantidadReservada()
        );
    }

    // Obtener todos
    public List<StockResponseDTO> obtenerTodos() {
        return stockRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public Optional<StockResponseDTO> obtenerPorId(Long id) {
        return stockRepository.findById(id).map(this::mapToDTO);
    }

    // Guardar
    public StockResponseDTO guardar(StockRequestDTO dto) {
        Stock stock = new Stock();
        stock.setIdProducto(dto.getIdProducto());
        stock.setCantidadDisponible(dto.getCantidadDisponible());
        stock.setCantidadReservada(dto.getCantidadReservada());

        Stock guardado = stockRepository.save(stock);
        return mapToDTO(guardado);
    }

    // Actualizar
    public Optional<StockResponseDTO> actualizar(Long id, StockRequestDTO dto) {
        return stockRepository.findById(id).map(existente -> {
            existente.setIdProducto(dto.getIdProducto());
            existente.setCantidadDisponible(dto.getCantidadDisponible());
            existente.setCantidadReservada(dto.getCantidadReservada());
            return mapToDTO(stockRepository.save(existente));
        });
    }

    // Eliminar
    public void eliminar(Long id) {
        stockRepository.deleteById(id);
    }
}