package com.example.helarte.service;

import com.example.helarte.dto.ReservaRequestDTO;
import com.example.helarte.dto.ReservaResponseDTO;
import com.example.helarte.exception.BadRequestException;
import com.example.helarte.exception.ResourceNotFoundException;
import com.example.helarte.model.Mesa;
import com.example.helarte.model.Reserva;
import com.example.helarte.model.Servicio;
import com.example.helarte.model.Usuario;
import com.example.helarte.repository.MesaRepository;
import com.example.helarte.repository.ReservaRepository;
import com.example.helarte.repository.ServicioRepository;
import com.example.helarte.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MesaRepository mesaRepository;
    private final ServicioRepository servicioRepository;

    // Inyección de dependencias por constructor
    public ReservaService(ReservaRepository reservaRepository,
                          UsuarioRepository usuarioRepository,
                          MesaRepository mesaRepository,
                          ServicioRepository servicioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.mesaRepository = mesaRepository;
        this.servicioRepository = servicioRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarTodas() {
        return reservaRepository.findAll()
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservaResponseDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
        return ReservaResponseDTO.fromEntity(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarPorUsuarioEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return listarPorUsuario(usuario.getId());
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarPorFecha(LocalDate fecha) {
        return reservaRepository.findByFechaReserva(fecha)
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarPorEstado(String estado) {
        return reservaRepository.findByEstado(estado.toUpperCase())
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservaResponseDTO crear(ReservaRequestDTO dto, String emailAutenticado) {
        // Resolver usuario: si viene especificado y el usuario tiene permisos, o por email autenticado
        Usuario usuario;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getUsuarioId()));
        } else if (emailAutenticado != null) {
            usuario = usuarioRepository.findByEmail(emailAutenticado)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado: " + emailAutenticado));
        } else {
            throw new BadRequestException("Debe indicarse el usuario asociado a la reserva");
        }

        Mesa mesa = mesaRepository.findById(dto.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + dto.getMesaId()));

        Servicio servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + dto.getServicioId()));

        if (!Boolean.TRUE.equals(servicio.getActivo())) {
            throw new BadRequestException("El servicio seleccionado no está disponible actualmente");
        }

        if (mesa.getCapacidad() < dto.getCantidadPersonas()) {
            throw new BadRequestException("La mesa #" + mesa.getNumeroMesa() +
                    " tiene capacidad para " + mesa.getCapacidad() +
                    " personas y se solicitaron " + dto.getCantidadPersonas());
        }

        // Validar conflicto de reserva en la misma mesa y horario
        List<Reserva> conflictos = reservaRepository.findConflictosMesa(mesa.getId(), dto.getFechaReserva(), dto.getHoraReserva());
        if (!conflictos.isEmpty()) {
            throw new BadRequestException("La mesa #" + mesa.getNumeroMesa() +
                    " ya cuenta con una reserva para la fecha " + dto.getFechaReserva() +
                    " a las " + dto.getHoraReserva());
        }

        // Generar código de reserva único
        String fechaStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String codigoReserva = "HEL-" + fechaStr + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Reserva reserva = new Reserva();
        reserva.setCodigoReserva(codigoReserva);
        reserva.setUsuario(usuario);
        reserva.setMesa(mesa);
        reserva.setServicio(servicio);
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setHoraReserva(dto.getHoraReserva());
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setObservaciones(dto.getObservaciones());
        reserva.setEstado("PENDIENTE");
        reserva.setFechaCreacion(LocalDateTime.now());

        Reserva guardada = reservaRepository.save(reserva);
        return ReservaResponseDTO.fromEntity(guardada);
    }

    @Transactional
    public ReservaResponseDTO actualizar(Long id, ReservaRequestDTO dto) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));

        Mesa mesa = mesaRepository.findById(dto.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + dto.getMesaId()));

        Servicio servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + dto.getServicioId()));

        if (mesa.getCapacidad() < dto.getCantidadPersonas()) {
            throw new BadRequestException("La capacidad de la mesa #" + mesa.getNumeroMesa() + " es insuficiente");
        }

        reserva.setMesa(mesa);
        reserva.setServicio(servicio);
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setHoraReserva(dto.getHoraReserva());
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setObservaciones(dto.getObservaciones());

        Reserva actualizada = reservaRepository.save(reserva);
        return ReservaResponseDTO.fromEntity(actualizada);
    }

    @Transactional
    public ReservaResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));

        reserva.setEstado(nuevoEstado.toUpperCase());
        Reserva actualizada = reservaRepository.save(reserva);
        return ReservaResponseDTO.fromEntity(actualizada);
    }

    @Transactional
    public void cancelar(Long id, String emailUsuario, boolean esAdminOEmpleado) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));

        if (!esAdminOEmpleado && !reserva.getUsuario().getEmail().equalsIgnoreCase(emailUsuario)) {
            throw new BadRequestException("No tiene permisos para cancelar esta reserva");
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }

    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
        reservaRepository.delete(reserva);
    }
}
