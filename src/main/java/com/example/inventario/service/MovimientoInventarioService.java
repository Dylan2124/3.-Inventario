package com.example.inventario.service;


import com.example.inventario.model.MovimientoInventario;
import com.example.inventario.repository.MovimientoInventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;

    // 1. SELECT * FROM movimiento_inventario (Trae todo el historial)
    public List<MovimientoInventario> obtenerTodos() {
        return movimientoRepository.findAll();
    }

    // 2. SELECT * FROM movimiento_inventario WHERE id = ? (Busca un movimiento puntual)
    public Optional<MovimientoInventario> obtenerPorId(Long id){
        return movimientoRepository.findById(id);
    }

    // 3. INSERT (Guarda un nuevo movimiento)
    public MovimientoInventario guardar(MovimientoInventario movimiento){
        return movimientoRepository.save(movimiento);
    }

    // 4. DELETE (Borra un movimiento si nos equivocamos)
    public void eliminar(Long id){
        movimientoRepository.deleteById(id);
    }

    // 5. NUESTRO MÉTODO ESPECIAL: Traer el historial de un producto en específico
    public List<MovimientoInventario> buscarMovimientosPorStock(Long idStock) {
        return movimientoRepository.findByIdStock(idStock);
    }
}
