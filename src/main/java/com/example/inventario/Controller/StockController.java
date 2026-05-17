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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // GET /api/stock -> Obtener todos los registros de stock
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<StockResponseDTO> lista = stockService.obtenerTodos();
        if (lista.isEmpty()){
            return ResponseEntity.ok("No se encontraron stocks");
        }
        return ResponseEntity.ok(lista);
    }

    // GET /api/stock/{id} -> Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<StockResponseDTO> stock = stockService.obtenerPorId(id);

        if (stock.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró el registro de stock con ID: " + id));
        }

        return ResponseEntity.ok(stock.get());
    }

    // POST /api/stock -> Guardar nuevo stock
    @PostMapping
    public ResponseEntity<StockResponseDTO> guardar(@Valid @RequestBody StockRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.guardar(dto));
    }

    // PUT /api/stock/{id} -> Actualizar stock existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody StockRequestDTO dto) {
        Optional<StockResponseDTO> actualizado = stockService.actualizar(id, dto);

        if (actualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede actualizar. No existe el registro de stock con ID: " + id));
        }

        return ResponseEntity.ok(actualizado.get());
    }

    // DELETE /api/stock/{id} -> Eliminar stock
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (stockService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede eliminar. No existe el registro de stock con ID: " + id));
        }
        stockService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "El registro de stock con ID " + id + " se eliminó con éxito."));
    }
}