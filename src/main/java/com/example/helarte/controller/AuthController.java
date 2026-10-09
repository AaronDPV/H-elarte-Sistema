package com.example.helarte.controller;

import com.example.helarte.dto.AuthResponseDTO;
import com.example.helarte.dto.UsuarioCreateDTO;
import com.example.helarte.dto.UsuarioDTO;
import com.example.helarte.exception.ResourceNotFoundException;
import com.example.helarte.model.Usuario;
import com.example.helarte.repository.UsuarioRepository;
import com.example.helarte.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias por constructor
    public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioDTO creado = usuarioService.crear(dto);
        AuthResponseDTO response = new AuthResponseDTO(
                "Usuario registrado exitosamente con credenciales seguras",
                creado.getId(),
                creado.getUser(),
                creado.getEmail(),
                creado.getRol()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        AuthResponseDTO response = new AuthResponseDTO(
                "Inicio de sesión exitoso. Bienvenido(a) " + usuario.getUser() + " (" + usuario.getRol() + ")",
                usuario.getId(),
                usuario.getUser(),
                usuario.getEmail(),
                usuario.getRol()
        );

        return ResponseEntity.ok(response);
    }
}