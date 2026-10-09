package com.example.helarte.dto;

import com.example.helarte.model.Mesa;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MesaDTO {

    private Long id;

    @NotNull(message = "El número de mesa es obligatorio")
    @Min(value = 1, message = "El número de mesa debe ser positivo")
    private Integer numeroMesa;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad mínima es de 1 persona")
    private Integer capacidad;

    private String ubicacion;

    private String estado;

    public MesaDTO() {
    }

    public MesaDTO(Long id, Integer numeroMesa, Integer capacidad, String ubicacion, String estado) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    public static MesaDTO fromEntity(Mesa m) {
        if (m == null) return null;
        return new MesaDTO(
                m.getId(),
                m.getNumeroMesa(),
                m.getCapacidad(),
                m.getUbicacion(),
                m.getEstado()
        );
    }

    public Mesa toEntity() {
        Mesa m = new Mesa();
        m.setId(this.id);
        m.setNumeroMesa(this.numeroMesa);
        m.setCapacidad(this.capacidad);
        m.setUbicacion(this.ubicacion);
        m.setEstado(this.estado != null ? this.estado : "DISPONIBLE");
        return m;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = COMPAT(ubicacion);
    }

    private String COMPAT(String val) {
        return val;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
