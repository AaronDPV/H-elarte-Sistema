package com.example.helarte.dto;

import jakarta.validation.constraints.Size;

public class UsuarioUpdateDTO {

    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String user;

    private String rol;

    private String telefono;

    private Boolean activo;

    private String password;

    public UsuarioUpdateDTO() {
    }

    public UsuarioUpdateDTO(String user, String rol, String telefono, Boolean activo, String password) {
        this.user = user;
        this.rol = rol;
        this.telefono = telefono;
        this.activo = activo;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
