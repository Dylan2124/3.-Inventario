package com.example.inventario.Controller;

import com.example.inventario.dto.MovimientoInventarioRequestDTO;
import com.example.inventario.dto.MovimientoInventarioResponseDTO;
import com.example.inventario.service.MovimientoInventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// IMPORTACIONES MÁGICAS DE HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@Tag(name = "Gestión de Movimientos", description = "Endpoints para el historial de entradas y salidas de stock")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;

    @GetMapping
    @Operation(summary = "Obtener todos los movimientos")
    public ResponseEntity<?> obtenerTodos() {
        List<MovimientoInventarioResponseDTO> lista = movimientoService.obtenerTodos();
        if (lista.isEmpty()){
            return ResponseEntity.ok(Map.of("mensaje", "No se encontraron movimientos"));
        }

        // HATEOAS
        lista.forEach(mov ->
                mov.add(linkTo(methodOn(MovimientoInventarioController.class).obtenerPorId(mov.getIdMovimiento())).withSelfRel())
        );

        CollectionModel<MovimientoInventarioResponseDTO> modelo = CollectionModel.of(lista,
                linkTo(methodOn(MovimientoInventarioController.class).obtenerTodos()).withSelfRel());

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<MovimientoInventarioResponseDTO> movimientoOpt = movimientoService.obtenerPorId(id);

        if (movimientoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró el movimiento con ID: " + id));
        }

        MovimientoInventarioResponseDTO movimiento = movimientoOpt.get();
        // HATEOAS
        movimiento.add(linkTo(methodOn(MovimientoInventarioController.class).obtenerPorId(id)).withSelfRel());
        movimiento.add(linkTo(methodOn(MovimientoInventarioController.class).obtenerTodos()).withRel("todos-los-movimientos"));
        // Link cruzado genial: ¡Lleva al usuario al stock de este movimiento!
        movimiento.add(linkTo(methodOn(StockController.class).obtenerPorId(movimiento.getIdStock())).withRel("ver-stock-asociado"));

        return ResponseEntity.ok(movimiento);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo movimiento")
    public ResponseEntity<MovimientoInventarioResponseDTO> guardar(@Valid @RequestBody MovimientoInventarioRequestDTO dto) {
        MovimientoInventarioResponseDTO nuevoMov = movimientoService.guardar(dto);

        nuevoMov.add(linkTo(methodOn(MovimientoInventarioController.class).obtenerPorId(nuevoMov.getIdMovimiento())).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMov);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un movimiento")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (movimientoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede eliminar. No existe el movimiento con ID: " + id));
        }
        movimientoService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "El movimiento con ID " + id + " se eliminó con éxito."));
    }

    @GetMapping("/stock/{idStock}")
    @Operation(summary = "Historial de movimientos de un Stock específico")
    public ResponseEntity<?> buscarPorStock(@PathVariable Long idStock) {
        List<MovimientoInventarioResponseDTO> movimientos = movimientoService.buscarMovimientosPorStock(idStock);
        if (movimientos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontraron movimientos con ID de stock: " + idStock));
        }

        // HATEOAS
        movimientos.forEach(mov ->
                mov.add(linkTo(methodOn(MovimientoInventarioController.class).obtenerPorId(mov.getIdMovimiento())).withSelfRel())
        );

        CollectionModel<MovimientoInventarioResponseDTO> modelo = CollectionModel.of(movimientos,
                linkTo(methodOn(MovimientoInventarioController.class).buscarPorStock(idStock)).withSelfRel());

        return ResponseEntity.ok(modelo);
    }
}