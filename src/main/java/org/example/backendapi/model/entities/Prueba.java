package org.example.backendapi.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pruebas")
public class Prueba {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "puntuacion_maxima", nullable = false)
    private Integer puntuacionMaxima;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Integer getPuntuacionMaxima() {
        return puntuacionMaxima;
    }

    public void setPuntuacionMaxima(Integer puntuacionMaxima) {
        this.puntuacionMaxima = puntuacionMaxima;
    }

}