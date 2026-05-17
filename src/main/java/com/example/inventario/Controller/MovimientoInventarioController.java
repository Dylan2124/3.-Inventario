package com.example.inventario.Controller;

import com.example.inventario.dto.MovimientoInventarioRequestDTO;
import com.example.inventario.dto.MovimientoInventarioResponseDTO;
import com.example.inventario.service.MovimientoInventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;

    // GET /api/movimientos -> Obtener todos los registros de movimientos
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<MovimientoInventarioResponseDTO> lista = movimientoService.obtenerTodos();
        if (lista.isEmpty()){
            return ResponseEntity.ok("No se encontraron movimientos");
        }
        return ResponseEntity.ok(lista);
    }

    // GET /api/movimientos/{id} -> Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<MovimientoInventarioResponseDTO> movimiento = movimientoService.obtenerPorId(id);

        if (movimiento.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró el movimiento con ID: " + id));
        }

        return ResponseEntity.ok(movimiento.get());
    }

    // POST /api/movimientos -> Guardar nuevo movimiento
    @PostMapping
    public ResponseEntity<MovimientoInventarioResponseDTO> guardar(@Valid @RequestBody MovimientoInventarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.guardar(dto));
    }

    // DELETE /api/movimientos/{id} -> Eliminar movimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (movimientoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede eliminar. No existe el movimiento con ID: " + id));
        }
        movimientoService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "El movimiento con ID " + id + " se eliminó con éxito."));
    }

    // GET /api/movimientos/stock/{idStock} -> Consultar el historial de un producto por su ID de Stock
    @GetMapping("/stock/{idStock}")
    public ResponseEntity<?> buscarPorStock(@PathVariable Long idStock) {
        List<MovimientoInventarioResponseDTO> movimientos = movimientoService.buscarMovimientosPorStock(idStock);
        if (movimientos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontraron movimientos con ID de stock: " + idStock));
        }
        return ResponseEntity.ok(movimientos);
    }
}