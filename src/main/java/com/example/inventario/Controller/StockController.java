package com.example.inventario.Controller;

import com.example.inventario.dto.StockRequestDTO;
import com.example.inventario.dto.StockResponseDTO;
import com.example.inventario.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // GET /api/stock -> Obtener todos los registros de stock
    @GetMapping
    public ResponseEntity<List<StockResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(stockService.obtenerTodos());
    }

    // GET /api/stock/{id} -> Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO> obtenerPorId(@PathVariable Long id) {
        return stockService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/stock -> Guardar nuevo stock
    @PostMapping
    public ResponseEntity<StockResponseDTO> guardar(@Valid @RequestBody StockRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.guardar(dto));
    }

    // PUT /api/stock/{id} -> Actualizar stock existente
    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody StockRequestDTO dto) {
        return stockService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/stock/{id} -> Eliminar stock.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (stockService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        stockService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}