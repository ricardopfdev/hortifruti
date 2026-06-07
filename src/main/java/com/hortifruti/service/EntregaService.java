package com.hortifruti.service;

import com.hortifruti.entity.Entrega;
import com.hortifruti.entity.Familia;
import com.hortifruti.model.StatusFila;
import com.hortifruti.repository.EntregaRepository;
import com.hortifruti.repository.FamiliaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final FamiliaRepository familiaRepository;

    public EntregaService(EntregaRepository entregaRepository, FamiliaRepository familiaRepository) {
        this.entregaRepository = entregaRepository;
        this.familiaRepository = familiaRepository;
    }

    public List<Entrega> listarTodas() {
        return entregaRepository.findAllByOrderByDataEntregaDesc();
    }

    public long entregasHoje() {
        LocalDate hoje = LocalDate.now();
        return entregaRepository.countByDataEntregaBetween(
                hoje.atStartOfDay(),
                hoje.atTime(LocalTime.MAX)
        );
    }

    @Transactional
    public Entrega registrarEntrega(Familia familia) {
        validarFamiliaParaEntrega(familia);

        Integer senha = familia.getNumeroSenha();
        if (senha == null) {
            throw new IllegalArgumentException("Gere uma senha antes de registrar a entrega");
        }

        Entrega entrega = new Entrega(familia, senha);
        entregaRepository.save(entrega);

        familia.setStatusFila(StatusFila.ATENDIDO);
        familia.setNumeroSenha(null);
        familiaRepository.save(familia);

        return entrega;
    }

    @Transactional
    public Entrega registrarPorSenha(Integer senha) {
        Familia familia = familiaRepository
                .findByNumeroSenhaAndStatusFila(senha, StatusFila.NA_FILA)
                .orElseThrow(() -> new IllegalArgumentException("Senha não encontrada ou já atendida"));

        return registrarEntrega(familia);
    }

    private void validarFamiliaParaEntrega(Familia familia) {
        if (!Boolean.TRUE.equals(familia.getAtiva())) {
            throw new IllegalArgumentException("Família inativa não pode receber alimentos");
        }

        if (!StatusFila.NA_FILA.equals(familia.getStatusFila())) {
            throw new IllegalArgumentException("Família já foi atendida");
        }
    }
}
