package com.example.helarte.service;

import com.example.helarte.dto.PagoRequestDTO;
import com.example.helarte.dto.PagoResponseDTO;
import com.example.helarte.exception.BadRequestException;
import com.example.helarte.exception.ResourceNotFoundException;
import com.example.helarte.model.Pago;
import com.example.helarte.model.Reserva;
import com.example.helarte.model.Usuario;
import com.example.helarte.repository.PagoRepository;
import com.example.helarte.repository.ReservaRepository;
import com.example.helarte.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private static final Set<String> METODOS_VALIDOS = Set.of("TARJETA", "YAPE", "PLIN", "EFECTIVO");

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias por constructor
    public PagoService(PagoRepository pagoRepository,
                       ReservaRepository reservaRepository,
                       UsuarioRepository usuarioRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PagoResponseDTO procesarPago(PagoRequestDTO dto, String emailAutenticado, boolean esAdminOEmpleado) {
        Reserva reserva = reservaRepository.findById(dto.getReservaId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + dto.getReservaId()));

        if (!esAdminOEmpleado && !reserva.getUsuario().getEmail().equalsIgnoreCase(emailAutenticado)) {
            throw new BadRequestException("No tiene permisos para pagar esta reserva");
        }

        String metodo = dto.getMetodoPago().trim().toUpperCase();
        if (!METODOS_VALIDOS.contains(metodo)) {
            throw new BadRequestException("Método de pago no válido. Use: TARJETA, YAPE, PLIN o EFECTIVO");
        }

        if ("CANCELADA".equals(reserva.getEstado())) {
            throw new BadRequestException("No se puede pagar una reserva cancelada");
        }

        if (pagoRepository.existsByReservaId(reserva.getId())) {
            throw new BadRequestException("La reserva " + reserva.getCodigoReserva() + " ya fue pagada");
        }

        // Simulación de pasarela: el pago siempre se aprueba, el monto sale del servicio reservado
        Pago pago = new Pago();
        pago.setCodigoOperacion(generarCodigoOperacion());
        pago.setReserva(reserva);
        pago.setMonto(reserva.getServicio().getPrecio());
        pago.setMetodoPago(metodo);
        pago.setEstado("COMPLETADO");

        Pago guardado = pagoRepository.save(pago);

        // El pago exitoso confirma automáticamente la reserva
        reserva.setEstado("CONFIRMADA");
        reservaRepository.save(reserva);

        return PagoResponseDTO.fromEntity(guardado);
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> listarTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(PagoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerPorReservaId(Long reservaId, String emailAutenticado, boolean esAdminOEmpleado) {
        Pago pago = pagoRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un pago para la reserva con ID: " + reservaId));

        if (!esAdminOEmpleado && !pago.getReserva().getUsuario().getEmail().equalsIgnoreCase(emailAutenticado)) {
            throw new BadRequestException("No tiene permisos para ver este pago");
        }
        return PagoResponseDTO.fromEntity(pago);
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> listarPorUsuarioEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return pagoRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(PagoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private String generarCodigoOperacion() {
        String fechaStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String codigo;
        do {
            codigo = "PAG-" + fechaStr + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        } while (pagoRepository.existsByCodigoOperacion(codigo));
        return codigo;
    }
}

