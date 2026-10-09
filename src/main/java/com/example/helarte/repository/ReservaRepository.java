package com.example.helarte.repository;

import com.example.helarte.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByCodigoReserva(String codigoReserva);

    boolean existsByCodigoReserva(String codigoReserva);

    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva = :fecha ORDER BY r.horaReserva ASC")
    List<Reserva> findByFechaReserva(@Param("fecha") LocalDate fecha);

    @Query("SELECT r FROM Reserva r WHERE r.usuario.id = :usuarioId ORDER BY r.fechaReserva DESC, r.horaReserva DESC")
    List<Reserva> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT r FROM Reserva r WHERE r.estado = :estado ORDER BY r.fechaReserva DESC")
    List<Reserva> findByEstado(@Param("estado") String estado);

    @Query("SELECT r FROM Reserva r WHERE r.mesa.id = :mesaId AND r.fechaReserva = :fecha AND r.horaReserva = :hora AND r.estado <> 'CANCELADA'")
    List<Reserva> findConflictosMesa(@Param("mesaId") Long mesaId,
                                    @Param("fecha") LocalDate fecha,
                                    @Param("hora") LocalTime hora);
}
