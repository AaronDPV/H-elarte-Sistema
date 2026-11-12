package com.example.helarte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PagoRequestDTO {

    @NotNull(message = "El id de la reserva es obligatorio")
    private Long reservaId;

    @NotBlank(message = "El método de pago es obligatorio (TARJETA, YAPE, PLIN o EFECTIVO)")
    private String metodoPago;

    public PagoRequestDTO() {
    }

    public PagoRequestDTO(Long reservaId, String metodoPago) {
        this.reservaId = reservaId;
        this.metodoPago = metodoPago;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}

