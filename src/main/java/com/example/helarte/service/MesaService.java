package com.example.helarte.service;

import com.example.helarte.dto.MesaDTO;
import com.example.helarte.exception.BadRequestException;
import com.example.helarte.exception.ResourceNotFoundException;
import com.example.helarte.model.Mesa;
import com.example.helarte.repository.MesaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    // Inyección de dependencias por constructor
    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Transactional(readOnly = true)
    public List<MesaDTO> listarTodas() {
        return mesaRepository.findAll()
                .stream()
                .map(MesaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MesaDTO> listarDisponibles() {
        return mesaRepository.findByEstado("DISPONIBLE")
                .stream()
                .map(MesaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MesaDTO obtenerPorId(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + id));
        return MesaDTO.fromEntity(mesa);
    }

    @Transactional(readOnly = true)
    public Mesa obtenerEntidadPorId(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + id));
    }

    @Transactional
    public MesaDTO crear(MesaDTO dto) {
        if (mesaRepository.existsByNumeroMesa(dto.getNumeroMesa())) {
            throw new BadRequestException("Ya existe una mesa con el número " + dto.getNumeroMesa());
        }

        Mesa mesa = dto.toEntity();
        mesa.setId(null);
        if (mesa.getEstado() == null || mesa.getEstado().trim().isEmpty()) {
            mesa.setEstado("DISPONIBLE");
        }
        Mesa guardada = mesaRepository.save(mesa);
        return MesaDTO.fromEntity(guardada);
    }

    @Transactional
    public MesaDTO actualizar(Long id, MesaDTO dto) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + id));

        if (!mesa.getNumeroMesa().equals(dto.getNumeroMesa()) && mesaRepository.existsByNumeroMesa(dto.getNumeroMesa())) {
            throw new BadRequestException("Ya existe otra mesa con el número " + dto.getNumeroMesa());
        }

        mesa.setNumeroMesa(dto.getNumeroMesa());
        mesa.setCapacidad(dto.getCapacidad());
        mesa.setUbicacion(dto.getUbicacion());
        if (dto.getEstado() != null) {
            mesa.setEstado(dto.getEstado().toUpperCase());
        }

        Mesa actualizada = mesaRepository.save(mesa);
        return MesaDTO.fromEntity(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + id));
        mesaRepository.delete(mesa);
    }

    @Transactional(readOnly = true)
    public List<MesaDTO> buscarDisponiblesParaCapacidad(Integer personas) {
        return mesaRepository.findMesasDisponiblesParaCapacidad(personas)
                .stream()
                .map(MesaDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
