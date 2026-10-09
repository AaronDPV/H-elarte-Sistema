package com.example.helarte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CambiarEstadoReservaDTO {

    @NotBlank(message = "El nuevo estado es obligatorio")
    @Pattern(regexp = "^(?i)(PENDIENTE|CONFIRMADA|ATENDIDA|CANCELADA)$",
            message = "El estado debe ser: PENDIENTE, CONFIRMADA, ATENDIDA o CANCELADA")
    private String estado;

    public CambiarEstadoReservaDTO() {
    }

    public CambiarEstadoReservaDTO(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
