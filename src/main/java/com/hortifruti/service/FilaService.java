package com.hortifruti.service;

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
public class FilaService {

    private final FamiliaRepository familiaRepository;
    private final EntregaRepository entregaRepository;

    public FilaService(FamiliaRepository familiaRepository, EntregaRepository entregaRepository) {
        this.familiaRepository = familiaRepository;
        this.entregaRepository = entregaRepository;
    }

    public List<Familia> listarNaFila() {
        return familiaRepository.findByStatusFilaOrderByPrioridadeAsc(StatusFila.NA_FILA);
    }

    @Transactional
    public Familia gerarSenha(Long familiaId) {
        Familia familia = familiaRepository.findById(familiaId)
                .orElseThrow(() -> new IllegalArgumentException("Família não encontrada"));

        if (!Boolean.TRUE.equals(familia.getAtiva())) {
            throw new IllegalArgumentException("Família inativa não pode receber senha");
        }

        if (!StatusFila.NA_FILA.equals(familia.getStatusFila())) {
            throw new IllegalArgumentException("Família já foi atendida");
        }

        if (familia.getNumeroSenha() != null) {
            return familia;
        }

        familia.setNumeroSenha(proximoNumeroSenha());
        return familiaRepository.save(familia);
    }

    public Familia buscarPorSenha(Integer senha) {
        return familiaRepository
                .findByNumeroSenhaAndStatusFila(senha, StatusFila.NA_FILA)
                .orElseThrow(() -> new IllegalArgumentException("Senha não encontrada ou já atendida"));
    }

    public List<Familia> painelSenhas() {
        return familiaRepository.findByStatusFilaOrderByNumeroSenhaAsc(StatusFila.NA_FILA)
                .stream()
                .filter(f -> f.getNumeroSenha() != null)
                .toList();
    }

    public int proximoNumeroSenha() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(LocalTime.MAX);

        int maxEntrega = entregaRepository.findMaxSenhaNoDia(inicio, fim);
        int maxFila = familiaRepository.findByStatusFilaOrderByNumeroSenhaAsc(StatusFila.NA_FILA)
                .stream()
                .map(Familia::getNumeroSenha)
                .filter(n -> n != null)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        return Math.max(maxEntrega, maxFila) + 1;
    }

    @Transactional
    public int reiniciarDistribuicao() {
        List<Familia> familias = familiaRepository.findAll();
        int reiniciadas = 0;

        for (Familia familia : familias) {
            if (!Boolean.TRUE.equals(familia.getAtiva())) {
                continue;
            }
            familia.setStatusFila(StatusFila.NA_FILA);
            familia.setNumeroSenha(null);
            reiniciadas++;
        }

        familiaRepository.saveAll(familias);
        return reiniciadas;
    }
}
