package com.example.helarte.service;

import com.example.helarte.dto.UsuarioCreateDTO;
import com.example.helarte.dto.UsuarioDTO;
import com.example.helarte.dto.UsuarioUpdateDTO;
import com.example.helarte.exception.BadRequestException;
import com.example.helarte.exception.ResourceNotFoundException;
import com.example.helarte.model.Usuario;
import com.example.helarte.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias por constructor
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return UsuarioDTO.fromEntity(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerEntidadPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario obtenerEntidadPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Transactional
    public UsuarioDTO crear(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("El correo ya se encuentra registrado: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setUser(dto.getUser());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());

        String rol = (dto.getRol() != null && !dto.getRol().trim().isEmpty())
                ? dto.getRol().toUpperCase()
                : "CLIENTE";
        usuario.setRol(rol);
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioDTO.fromEntity(guardado);
    }

    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (dto.getUser() != null && !dto.getUser().trim().isEmpty()) {
            usuario.setUser(dto.getUser());
        }
        if (dto.getTelefono() != null) {
            usuario.setTelefono(dto.getTelefono());
        }
        if (dto.getRol() != null && !dto.getRol().trim().isEmpty()) {
            usuario.setRol(dto.getRol().toUpperCase());
        }
        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return UsuarioDTO.fromEntity(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorFiltro(String filtro) {
        return usuarioRepository.buscarPorFiltro(filtro)
                .stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarPorRol(String rol) {
        return usuarioRepository.findByRol(rol.toUpperCase())
                .stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
