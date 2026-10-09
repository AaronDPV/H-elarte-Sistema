package com.example.helarte.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaRequestDTO {

    private Long usuarioId;

    @NotNull(message = "El id de la mesa es obligatorio")
    private Long mesaId;

    @NotNull(message = "El id del servicio es obligatorio")
    private Long servicioId;

    @NotNull(message = "La fecha de reserva es obligatoria")
    @FutureOrPresent(message = "La fecha de reserva debe ser hoy o una fecha futura")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaReserva;

    @NotNull(message = "La hora de reserva es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaReserva;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 comensal")
    private Integer cantidadPersonas;

    private String observaciones;

    public ReservaRequestDTO() {
    }

    public ReservaRequestDTO(Long usuarioId, Long mesaId, Long servicioId, LocalDate fechaReserva,
                             LocalTime horaReserva, Integer cantidadPersonas, String observaciones) {
        this.usuarioId = usuarioId;
        this.mesaId = mesaId;
        this.servicioId = servicioId;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.cantidadPersonas = cantidadPersonas;
        this.observaciones = observaciones;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getMesaId() {
        return mesaId;
    }

    public void setMesaId(Long mesaId) {
        this.mesaId = mesaId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
