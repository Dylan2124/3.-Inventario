package com.example.inventario.service;

import com.example.inventario.dto.StockRequestDTO;
import com.example.inventario.dto.StockResponseDTO;
import com.example.inventario.model.Stock;
import com.example.inventario.repository.StockRepository;

// --- IMPORTS DE FEIGN Y LOMBOK ---
import com.example.inventario.client.NotificacionClient;
import com.example.inventario.dto.NotificacionRequestDTO;
import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    //CLIENTE FEIGN
    private final NotificacionClient notificacionClient;

    private StockResponseDTO mapToDTO(Stock stock) {
        return new StockResponseDTO(
                stock.getIdStock(),
                stock.getIdProducto(),
                stock.getCantidadDisponible(),
                stock.getCantidadReservada()
        );
    }

    public List<StockResponseDTO> obtenerTodos() {
        return stockRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<StockResponseDTO> obtenerPorId(Long id) {
        return stockRepository.findById(id).map(this::mapToDTO);
    }

    public StockResponseDTO guardar(StockRequestDTO dto) {
        Stock stock = new Stock();
        stock.setIdProducto(dto.getIdProducto());
        stock.setCantidadDisponible(dto.getCantidadDisponible());
        stock.setCantidadReservada(dto.getCantidadReservada());

        Stock guardado = stockRepository.save(stock);
        return mapToDTO(guardado);
    }

    // ACTUALIZAR
    public Optional<StockResponseDTO> actualizar(Long id, StockRequestDTO dto) {
        return stockRepository.findById(id).map(existente -> {

            existente.setIdProducto(dto.getIdProducto());
            existente.setCantidadDisponible(dto.getCantidadDisponible());
            existente.setCantidadReservada(dto.getCantidadReservada());

            Stock actualizado = stockRepository.save(existente);
            try {
                log.info("Llamando a ms-notificaciones para reportar actualización de stock...");

                NotificacionRequestDTO aviso = new NotificacionRequestDTO(
                        1L,
                        0L,
                        "INFO_INVENTARIO",
                        "Se actualizó el stock del producto " + actualizado.getIdProducto() + ". Cantidad disponible: " + actualizado.getCantidadDisponible()
                );

                notificacionClient.enviarNotificacion(aviso);
                log.info("¡Aviso enviado a Notificaciones con éxito!");

            } catch (Exception e) {
                log.warn("TOLERANCIA A FALLOS: No se pudo conectar con ms-notificaciones: {}", e.getMessage());
            }

            return mapToDTO(actualizado);
        });
    }

    public void eliminar(Long id) {
        stockRepository.deleteById(id);
    }
}