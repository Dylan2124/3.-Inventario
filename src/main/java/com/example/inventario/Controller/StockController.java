package com.example.inventario.Controller;

import com.example.inventario.dto.StockRequestDTO;
import com.example.inventario.dto.StockResponseDTO;
import com.example.inventario.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Tag(name = "Gestión de Stock", description = "Endpoints para la administración del stock de piezas") // SWAGGER
public class StockController {

    private final StockService stockService;

    @GetMapping
    @Operation(summary = "Obtener todo el stock", description = "Retorna una lista con todos los registros de stock y sus enlaces de navegación.")
    public ResponseEntity<?> obtenerTodos() {
        List<StockResponseDTO> lista = stockService.obtenerTodos();
        if (lista.isEmpty()){
            return ResponseEntity.ok(Map.of("mensaje", "No se encontraron stocks"));
        }

        // HATEOAS: Le agregamos a cada elemento un link para ver su propio detalle
        lista.forEach(stock ->
                stock.add(linkTo(methodOn(StockController.class).obtenerPorId(stock.getIdStock())).withSelfRel())
        );

        // HATEOAS: Envolvemos la lista completa y le damos un link principal
        CollectionModel<StockResponseDTO> modelo = CollectionModel.of(lista,
                linkTo(methodOn(StockController.class).obtenerTodos()).withSelfRel());

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener stock por ID", description = "Busca un registro de stock específico en la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe el stock con ese ID")
    })
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<StockResponseDTO> stockOpt = stockService.obtenerPorId(id);

        if (stockOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró el registro de stock con ID: " + id));
        }

        StockResponseDTO stock = stockOpt.get();
        // HATEOAS: Links de navegación para un solo elemento
        stock.add(linkTo(methodOn(StockController.class).obtenerPorId(id)).withSelfRel());
        stock.add(linkTo(methodOn(StockController.class).obtenerTodos()).withRel("todos-los-stocks"));

        return ResponseEntity.ok(stock);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo registro de stock")
    public ResponseEntity<StockResponseDTO> guardar(@Valid @RequestBody StockRequestDTO dto) {
        StockResponseDTO nuevoStock = stockService.guardar(dto);

        // HATEOAS: Le decimos al usuario dónde puede ver el stock que acaba de crear
        nuevoStock.add(linkTo(methodOn(StockController.class).obtenerPorId(nuevoStock.getIdStock())).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoStock);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar stock existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody StockRequestDTO dto) {
        Optional<StockResponseDTO> actualizadoOpt = stockService.actualizar(id, dto);

        if (actualizadoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede actualizar. No existe el registro con ID: " + id));
        }

        StockResponseDTO actualizado = actualizadoOpt.get();
        // HATEOAS
        actualizado.add(linkTo(methodOn(StockController.class).obtenerPorId(id)).withSelfRel());
        actualizado.add(linkTo(methodOn(StockController.class).obtenerTodos()).withRel("todos-los-stocks"));

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro de stock")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (stockService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede eliminar. No existe el registro con ID: " + id));
        }
        stockService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "El registro de stock con ID " + id + " se eliminó con éxito."));
    }
}