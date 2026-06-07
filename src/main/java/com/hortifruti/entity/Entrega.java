package com.hortifruti.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "familia_id", nullable = false)
    private Familia familia;

    @Column(nullable = false)
    private Integer senha;

    @Column(nullable = false)
    private LocalDateTime dataEntrega;

    public Entrega() {
        this.dataEntrega = LocalDateTime.now();
    }

    public Entrega(Familia familia, Integer senha) {
        this.familia = familia;
        this.senha = senha;
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

    public Integer getSenha() {
        return senha;
    }

    public void setSenha(Integer senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
}
