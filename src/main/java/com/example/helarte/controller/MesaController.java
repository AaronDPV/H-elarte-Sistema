package com.example.helarte.controller;

import com.example.helarte.dto.MesaDTO;
import com.example.helarte.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    private final MesaService mesaService;

    // Inyección de dependencias por constructor
    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    public ResponseEntity<List<MesaDTO>> listarMesas() {
        List<MesaDTO> mesas = mesaService.listarTodas();
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<MesaDTO>> listarMesasDisponibles() {
        List<MesaDTO> mesas = mesaService.listarDisponibles();
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/capacidad")
    public ResponseEntity<List<MesaDTO>> buscarPorCapacidad(@RequestParam Integer personas) {
        List<MesaDTO> mesas = mesaService.buscarDisponiblesParaCapacidad(personas);
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaDTO> obtenerPorId(@PathVariable Long id) {
        MesaDTO mesa = mesaService.obtenerPorId(id);
        return ResponseEntity.ok(mesa);
    }

    @PostMapping
    public ResponseEntity<MesaDTO> registrarMesa(@Valid @RequestBody MesaDTO dto) {
        MesaDTO creada = mesaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MesaDTO> actualizarMesa(@PathVariable Long id, @Valid @RequestBody MesaDTO dto) {
        MesaDTO actualizada = mesaService.actualizar(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarMesa(@PathVariable Long id) {
        mesaService.eliminar(id);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Mesa eliminada correctamente con ID: " + id);
        return ResponseEntity.ok(respuesta);
    }
}
