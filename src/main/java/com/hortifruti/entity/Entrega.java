package com.hortifruti.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Familia familia;

    private LocalDateTime dataEntrega;

    public Entrega() {
        this.dataEntrega = LocalDateTime.now();
    }

    public Entrega(Familia familia) {
        this.familia = familia;
        this.dataEntrega = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
}