package com.example.helarte.repository;

import com.example.helarte.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query("SELECT p FROM Pago p WHERE p.reserva.id = :reservaId")
    Optional<Pago> findByReservaId(@Param("reservaId") Long reservaId);

    @Query("SELECT COUNT(p) > 0 FROM Pago p WHERE p.reserva.id = :reservaId")
    boolean existsByReservaId(@Param("reservaId") Long reservaId);

    @Query("SELECT p FROM Pago p WHERE p.reserva.usuario.id = :usuarioId ORDER BY p.fechaPago DESC")
    List<Pago> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    boolean existsByCodigoOperacion(String codigoOperacion);
}

