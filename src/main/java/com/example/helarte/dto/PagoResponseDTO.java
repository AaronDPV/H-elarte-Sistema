package com.example.helarte.dto;

import com.example.helarte.model.Pago;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class PagoResponseDTO {

    private Long id;
    private String codigoOperacion;
    private Long reservaId;
    private String codigoReserva;
    private String cliente;
    private String servicio;
    private Double monto;
    private String metodoPago;
    private String estado;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaPago;

    public PagoResponseDTO() {
    }

    public PagoResponseDTO(Long id, String codigoOperacion, Long reservaId, String codigoReserva,
                           String cliente, String servicio, Double monto, String metodoPago,
                           String estado, LocalDateTime fechaPago) {
        this.id = id;
        this.codigoOperacion = codigoOperacion;
        this.reservaId = reservaId;
        this.codigoReserva = codigoReserva;
        this.cliente = cliente;
        this.servicio = servicio;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.fechaPago = fechaPago;
    }

    public static PagoResponseDTO fromEntity(Pago p) {
        if (p == null) return null;
        return new PagoResponseDTO(
                p.getId(),
                p.getCodigoOperacion(),
                p.getReserva().getId(),
                p.getReserva().getCodigoReserva(),
                p.getReserva().getUsuario().getUser(),
                p.getReserva().getServicio().getNombre(),
                p.getMonto(),
                p.getMetodoPago(),
                p.getEstado(),
                p.getFechaPago()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoOperacion() {
        return codigoOperacion;
    }

    public void setCodigoOperacion(String codigoOperacion) {
        this.codigoOperacion = codigoOperacion;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }
}

