package com.example.helarte.repository;

import com.example.helarte.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    List<Usuario> findByRol(@Param("rol") String rol);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.user) LIKE LOWER(CONCAT('%', :filtro, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Usuario> buscarPorFiltro(@Param("filtro") String filtro);
}