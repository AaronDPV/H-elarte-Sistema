package com.example.helarte.repository;

import com.example.helarte.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByActivoTrue();

    @Query("SELECT s FROM Servicio s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Servicio> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT s FROM Servicio s WHERE s.precio <= :precioMax AND s.activo = true")
    List<Servicio> buscarPorPrecioMaximo(@Param("precioMax") Double precioMax);
}
