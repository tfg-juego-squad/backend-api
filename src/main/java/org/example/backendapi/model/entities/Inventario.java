package org.example.backendapi.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
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
}