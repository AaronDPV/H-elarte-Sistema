package com.example.helarte.controller;

import com.example.helarte.dto.ServicioDTO;
import com.example.helarte.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    // Inyección de dependencias por constructor
    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listarServicios() {
        List<ServicioDTO> servicios = servicioService.listarActivos();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ServicioDTO>> listarTodosLosServicios() {
        List<ServicioDTO> servicios = servicioService.listarTodos();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> obtenerPorId(@PathVariable Long id) {
        ServicioDTO servicio = servicioService.obtenerPorId(id);
        return ResponseEntity.ok(servicio);
    }

    @PostMapping
    public ResponseEntity<ServicioDTO> registrarServicio(@Valid @RequestBody ServicioDTO dto) {
        ServicioDTO creado = servicioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable Long id, @Valid @RequestBody ServicioDTO dto) {
        ServicioDTO actualizado = servicioService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminar(id);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Servicio eliminado correctamente con ID: " + id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ServicioDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<ServicioDTO> servicios = servicioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(servicios);
    }
}
