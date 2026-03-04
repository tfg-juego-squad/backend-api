package org.example.backendapi.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "inventario")
public class Inventario {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "nombre_objeto", nullable = false, length = 100)
    private String nombreObjeto;

    @ColumnDefault("current_timestamp()")
    @Column(name = "fecha_adquisicion")
    private Instant fechaAdquisicion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public Instant getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Instant fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

}