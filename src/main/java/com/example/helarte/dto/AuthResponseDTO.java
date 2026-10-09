package com.example.helarte.dto;

public class AuthResponseDTO {

    private String mensaje;
    private Long id;
    private String user;
    private String email;
    private String rol;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String mensaje, Long id, String user, String email, String rol) {
        this.mensaje = mensaje;
        this.id = id;
        this.user = user;
        this.email = email;
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
}
