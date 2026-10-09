package com.example.helarte.controller;

import com.example.helarte.dto.CambiarEstadoReservaDTO;
import com.example.helarte.dto.ReservaRequestDTO;
import com.example.helarte.dto.ReservaResponseDTO;
import com.example.helarte.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    // Inyección de dependencias por constructor
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        List<ReservaResponseDTO> reservas = reservaService.listarTodas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long id) {
        ReservaResponseDTO reserva = reservaService.obtenerPorId(id);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<ReservaResponseDTO>> listarMisReservas(Authentication authentication) {
        String email = authentication.getName();
        List<ReservaResponseDTO> reservas = reservaService.listarPorUsuarioEmail(email);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<ReservaResponseDTO> reservas = reservaService.listarPorFecha(fecha);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/estado")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorEstado(@RequestParam String estado) {
        List<ReservaResponseDTO> reservas = reservaService.listarPorEstado(estado);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @Valid @RequestBody ReservaRequestDTO dto,
            Authentication authentication) {
        String email = (authentication != null) ? authentication.getName() : null;
        ReservaResponseDTO creada = reservaService.crear(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequestDTO dto) {
        ReservaResponseDTO actualizada = reservaService.actualizar(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoReservaDTO dto) {
        ReservaResponseDTO actualizada = reservaService.cambiarEstado(id, dto.getEstado());
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelarReserva(
            @PathVariable Long id,
            Authentication authentication) {
        String email = (authentication != null) ? authentication.getName() : "";
        boolean esAdminOEmpleado = (authentication != null) && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(rol -> rol.equals("ROLE_ADMIN") || rol.equals("ROLE_EMPLEADO") || rol.equals("ROLE_COLABORADOR"));

        reservaService.cancelar(id, email, esAdminOEmpleado);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Reserva con ID " + id + " cancelada exitosamente");
        return ResponseEntity.ok(respuesta);
    }
}
