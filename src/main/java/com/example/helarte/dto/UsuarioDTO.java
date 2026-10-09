package com.example.helarte.dto;

import com.example.helarte.model.Usuario;

public class UsuarioDTO {

    private Long id;
    private String user;
    private String email;
    private String rol;
    private String telefono;
    private Boolean activo;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String user, String email, String rol, String telefono, Boolean activo) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.rol = rol;
        this.telefono = telefono;
        this.activo = activo;
    }

    public static UsuarioDTO fromEntity(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUser(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getTelefono(),
                usuario.getActivo()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
