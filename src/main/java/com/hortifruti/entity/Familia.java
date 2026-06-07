package com.hortifruti.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "familias")
public class Familia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Column(nullable = false)
    private String nomeCompleto;

    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false)
    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    @Column(nullable = false)
    private String endereco;

    @Min(value = 1, message = "A família deve ter pelo menos 1 morador")
    @Column(nullable = false)
    private Integer quantidadeMoradores;

    @Column(nullable = false)
    private Boolean ativa = true;

    @Column(nullable = false)
    private String statusFila;

    @Column(nullable = false)
    private Integer prioridade;

    private Integer numeroSenha;

    @Column(nullable = false)
    private LocalDate dataCadastro;

    public Familia() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getQuantidadeMoradores() {
        return quantidadeMoradores;
    }

    public void setQuantidadeMoradores(Integer quantidadeMoradores) {
        this.quantidadeMoradores = quantidadeMoradores;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public String getStatusFila() {
        return statusFila;
    }

    public void setStatusFila(String statusFila) {
        this.statusFila = statusFila;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public Integer getNumeroSenha() {
        return numeroSenha;
    }

    public void setNumeroSenha(Integer numeroSenha) {
        this.numeroSenha = numeroSenha;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
