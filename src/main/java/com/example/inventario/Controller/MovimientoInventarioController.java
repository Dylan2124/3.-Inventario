package com.example.inventario.Controller;

import com.example.inventario.model.MovimientoInventario;
import com.example.inventario.service.MovimientoInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;

    // GET /api/movimientos - Trae todo el historial
    @GetMapping
    public ResponseEntity<List<MovimientoInventario>> obtenerTodos() {
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

    // 2. GET /api/movimientos/1 (Trae un movimiento por su ID)
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventario> obtenerPorId(@PathVariable Long id) {
        return movimientoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Devuelve 404 si no existe
    }

    // POST /api/movimientos (Guarda un nuevo movimiento)
    @PostMapping
    public ResponseEntity<MovimientoInventario> guardar(@RequestBody MovimientoInventario movimiento) {
        MovimientoInventario nuevoMovimiento = movimientoService.guardar(movimiento);
        // Devuelve el código 201 Created que viste antes
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMovimiento);
    }

    // Consultar el historial de un producto por su ID /api/movimientos/stock/101
    @GetMapping("/stock/{idStock}")
    public ResponseEntity<?> buscarPorStock(@PathVariable Long idStock) {
        List<MovimientoInventario> movimientos = movimientoService.buscarMovimientosPorStock(idStock);


        if (movimientos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron movimientos con ID: " + idStock);
        }
        return ResponseEntity.ok(movimientos);
    }


}


