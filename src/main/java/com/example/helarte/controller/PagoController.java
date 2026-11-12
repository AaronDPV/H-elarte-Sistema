package com.example.helarte.controller;

import com.example.helarte.dto.PagoRequestDTO;
import com.example.helarte.dto.PagoResponseDTO;
import com.example.helarte.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    // Inyección de dependencias por constructor
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> procesarPago(
            @Valid @RequestBody PagoRequestDTO dto,
            Authentication authentication) {
        PagoResponseDTO pago = pagoService.procesarPago(dto, authentication.getName(), esAdminOEmpleado(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/mis-pagos")
    public ResponseEntity<List<PagoResponseDTO>> listarMisPagos(Authentication authentication) {
        return ResponseEntity.ok(pagoService.listarPorUsuarioEmail(authentication.getName()));
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<PagoResponseDTO> obtenerPorReserva(
            @PathVariable Long reservaId,
            Authentication authentication) {
        return ResponseEntity.ok(
                pagoService.obtenerPorReservaId(reservaId, authentication.getName(), esAdminOEmpleado(authentication)));
    }

    private boolean esAdminOEmpleado(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(rol -> rol.equals("ROLE_ADMIN") || rol.equals("ROLE_EMPLEADO") || rol.equals("ROLE_COLABORADOR"));
    }
}

