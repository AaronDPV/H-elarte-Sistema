package com.example.helarte.service;

import com.example.helarte.dto.ServicioDTO;
import com.example.helarte.exception.ResourceNotFoundException;
import com.example.helarte.model.Servicio;
import com.example.helarte.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    // Inyección de dependencias por constructor
    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> listarTodos() {
        return servicioRepository.findAll()
                .stream()
                .map(ServicioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> listarActivos() {
        return servicioRepository.findByActivoTrue()
                .stream()
                .map(ServicioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicioDTO obtenerPorId(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
        return ServicioDTO.fromEntity(servicio);
    }

    @Transactional(readOnly = true)
    public Servicio obtenerEntidadPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
    }

    @Transactional
    public ServicioDTO crear(ServicioDTO dto) {
        Servicio servicio = dto.toEntity();
        servicio.setId(null);
        if (servicio.getActivo() == null) {
            servicio.setActivo(true);
        }
        Servicio guardado = servicioRepository.save(servicio);
        return ServicioDTO.fromEntity(guardado);
    }

    @Transactional
    public ServicioDTO actualizar(Long id, ServicioDTO dto) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setDuracionMinutos(dto.getDuracionMinutos());
        if (dto.getActivo() != null) {
            servicio.setActivo(dto.getActivo());
        }

        Servicio actualizado = servicioRepository.save(servicio);
        return ServicioDTO.fromEntity(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
        servicioRepository.delete(servicio);
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> buscarPorNombre(String nombre) {
        return servicioRepository.buscarPorNombre(nombre)
                .stream()
                .map(ServicioDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
