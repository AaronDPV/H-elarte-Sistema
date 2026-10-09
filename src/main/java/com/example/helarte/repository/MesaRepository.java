package com.example.helarte.repository;

import com.example.helarte.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    Optional<Mesa> findByNumeroMesa(Integer numeroMesa);

    boolean existsByNumeroMesa(Integer numeroMesa);

    @Query("SELECT m FROM Mesa m WHERE m.estado = :estado")
    List<Mesa> findByEstado(@Param("estado") String estado);

    @Query("SELECT m FROM Mesa m WHERE m.capacidad >= :personas AND m.estado = 'DISPONIBLE'")
    List<Mesa> findMesasDisponiblesParaCapacidad(@Param("personas") Integer personas);
}
