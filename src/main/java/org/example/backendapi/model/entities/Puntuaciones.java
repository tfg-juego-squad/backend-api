package org.example.backendapi.model.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "puntuaciones")
public class Puntuaciones {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "prueba_id", nullable = false)
    private Prueba prueba;

    @Column(name = "puntos_obtenidos", nullable = false)
    private Integer puntosObtenidos;

    @ColumnDefault("current_timestamp()")
    @Column(name = "fecha_completado")
    private Instant fechaCompletado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    public Integer getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public void setPuntosObtenidos(Integer puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }

    public Instant getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(Instant fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

}