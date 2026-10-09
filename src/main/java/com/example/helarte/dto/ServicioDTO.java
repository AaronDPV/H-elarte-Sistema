package com.example.helarte.dto;

import com.example.helarte.model.Servicio;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ServicioDTO {

    private Long id;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private Double precio;

    private Integer duracionMinutos;

    private Boolean activo;

    public ServicioDTO() {
    }

    public ServicioDTO(Long id, String nombre, String descripcion, Double precio, Integer duracionMinutos, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
        this.activo = activo;
    }

    public static ServicioDTO fromEntity(Servicio s) {
        if (s == null) return null;
        return new ServicioDTO(
                s.getId(),
                s.getNombre(),
                s.getDescripcion(),
                s.getPrecio(),
                s.getDuracionMinutos(),
                s.getActivo()
        );
    }

    public Servicio toEntity() {
        Servicio s = new Servicio();
        s.setId(this.id);
        s.setNombre(this.nombre);
        s.setDescripcion(this.descripcion);
        s.setPrecio(this.precio);
        s.setDuracionMinutos(this.duracionMinutos);
        s.setActivo(this.activo != null ? this.activo : true);
        return s;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
