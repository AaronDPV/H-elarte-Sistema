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
import com.example.helarte.repository.PagoRepository;
import com.example.helarte.repository.ReservaRepository;
import com.example.helarte.repository.ServicioRepository;
import com.example.helarte.repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final PagoRepository pagoRepository;

    // Inyección de dependencias por constructor
    public ReservaService(ReservaRepository reservaRepository,
                          UsuarioRepository usuarioRepository,
                          MesaRepository mesaRepository,
                          ServicioRepository servicioRepository,
                          PagoRepository pagoRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.mesaRepository = mesaRepository;
        this.servicioRepository = servicioRepository;
        this.pagoRepository = pagoRepository;
    }

    private static final int DIAS_RETENCION_CANCELADAS = 5;

    @Transactional
    public List<ReservaResponseDTO> listarTodas() {
        purgarCanceladasMasDeCincoDias();
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

    @Transactional
    public List<ReservaResponseDTO> listarPorUsuario(Long usuarioId) {
        purgarCanceladasMasDeCincoDias();
        return reservaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
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
    public List<ReservaResponseDTO> listarActivasPorFecha(LocalDate fecha) {
        return reservaRepository.findActivasPorFecha(fecha)
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

        // Validar conflicto de reserva en la misma mesa y rango horario (incluyendo duración del servicio)
        validarCruceDeHorario(mesa, servicio, dto.getFechaReserva(), dto.getHoraReserva(), null);

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

        validarCruceDeHorario(mesa, servicio, dto.getFechaReserva(), dto.getHoraReserva(), reserva.getId());

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

        String estadoUpper = nuevoEstado.toUpperCase();
        reserva.setEstado(estadoUpper);
        if ("CANCELADA".equals(estadoUpper)) {
            reserva.setFechaCancelacion(LocalDateTime.now());
        } else {
            reserva.setFechaCancelacion(null);
        }
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
        reserva.setFechaCancelacion(LocalDateTime.now());
        reservaRepository.save(reserva);

        // Si la reserva ya estaba pagada, el pago pasa a REEMBOLSADO
        pagoRepository.findByReservaId(id).ifPresent(pago -> {
            if ("COMPLETADO".equals(pago.getEstado())) {
                pago.setEstado("REEMBOLSADO");
                pagoRepository.save(pago);
            }
        });
    }

    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
        pagoRepository.findByReservaId(id).ifPresent(pagoRepository::delete);
        reservaRepository.delete(reserva);
    }

    @Transactional
    public int eliminarCanceladasEnGrupo(List<Long> ids) {
        List<Reserva> aEliminar;
        if (ids == null || ids.isEmpty()) {
            aEliminar = reservaRepository.findByEstado("CANCELADA");
        } else {
            aEliminar = reservaRepository.findAllById(ids).stream()
                    .filter(r -> "CANCELADA".equalsIgnoreCase(r.getEstado()))
                    .collect(Collectors.toList());
        }
        for (Reserva r : aEliminar) {
            pagoRepository.findByReservaId(r.getId()).ifPresent(pagoRepository::delete);
        }
        reservaRepository.deleteAll(aEliminar);
        return aEliminar.size();
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public int purgarCanceladasMasDeCincoDias() {
        LocalDateTime limite = LocalDateTime.now().minusDays(DIAS_RETENCION_CANCELADAS);
        List<Reserva> vencidas = reservaRepository.findCanceladasAntiguas(limite);
        if (vencidas.isEmpty()) {
            return 0;
        }
        for (Reserva r : vencidas) {
            pagoRepository.findByReservaId(r.getId()).ifPresent(pagoRepository::delete);
        }
        reservaRepository.deleteAll(vencidas);
        return vencidas.size();
    }

    private void validarCruceDeHorario(Mesa mesa, Servicio servicio, LocalDate fecha, java.time.LocalTime horaInicio, Long reservaIdIgnorar) {
        int duracionNueva = (servicio.getDuracionMinutos() != null && servicio.getDuracionMinutos() >= 15)
                ? servicio.getDuracionMinutos() : 60;
        int inicioNuevoMin = horaInicio.getHour() * 60 + horaInicio.getMinute();
        int finNuevoMin = inicioNuevoMin + duracionNueva;

        List<Reserva> reservasDia = reservaRepository.findActivasPorMesaYFecha(mesa.getId(), fecha);
        for (Reserva existente : reservasDia) {
            if (reservaIdIgnorar != null && reservaIdIgnorar.equals(existente.getId())) {
                continue;
            }
            int duracionExistente = (existente.getServicio() != null
                    && existente.getServicio().getDuracionMinutos() != null
                    && existente.getServicio().getDuracionMinutos() >= 15)
                    ? existente.getServicio().getDuracionMinutos() : 60;
            int inicioExistenteMin = existente.getHoraReserva().getHour() * 60 + existente.getHoraReserva().getMinute();
            int finExistenteMin = inicioExistenteMin + duracionExistente;

            if (inicioNuevoMin < finExistenteMin && inicioExistenteMin < finNuevoMin) {
                throw new BadRequestException("La mesa #" + mesa.getNumeroMesa() +
                        " ya cuenta con una reserva para la fecha " + fecha +
                        " que se cruza con el horario " + horaInicio);
            }
        }
    }
}
