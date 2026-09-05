package com.example.helarte.controller;

import com.example.helarte.model.Usuario;
import com.example.helarte.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestBody Usuario usuario) {
        // Hasheo de la contraseña antes de insertar en BD
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignación de rol por defecto si es necesario
        if(usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }

        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente con contraseña encriptada";
    }
}