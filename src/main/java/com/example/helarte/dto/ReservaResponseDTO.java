package com.example.helarte.dto;

import com.example.helarte.model.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservaResponseDTO {

    private Long id;
    private String codigoReserva;
    private UsuarioDTO usuario;
    private MesaDTO mesa;
    private ServicioDTO servicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaReserva;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaReserva;

    private Integer cantidadPersonas;
    private String estado;
    private String observaciones;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;

    public ReservaResponseDTO() {
    }

    public ReservaResponseDTO(Long id, String codigoReserva, UsuarioDTO usuario, MesaDTO mesa,
                              ServicioDTO servicio, LocalDate fechaReserva, LocalTime horaReserva,
                              Integer cantidadPersonas, String estado, String observaciones,
                              LocalDateTime fechaCreacion) {
        this.id = id;
        this.codigoReserva = codigoReserva;
        this.usuario = usuario;
        this.mesa = mesa;
        this.servicio = servicio;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
    }

    public static ReservaResponseDTO fromEntity(Reserva r) {
        if (r == null) return null;
        return new ReservaResponseDTO(
                r.getId(),
                r.getCodigoReserva(),
                UsuarioDTO.fromEntity(r.getUsuario()),
                MesaDTO.fromEntity(r.getMesa()),
                ServicioDTO.fromEntity(r.getServicio()),
                r.getFechaReserva(),
                r.getHoraReserva(),
                r.getCantidadPersonas(),
                r.getEstado(),
                r.getObservaciones(),
                r.getFechaCreacion()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public MesaDTO getMesa() {
        return mesa;
    }

    public void setMesa(MesaDTO mesa) {
        this.mesa = mesa;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalTime getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(LocalTime horaReserva) {
        this.horaReserva = horaReserva;
    }

    public Integer getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(Integer cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
