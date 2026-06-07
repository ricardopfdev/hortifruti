package com.hortifruti.service;

import com.hortifruti.entity.Familia;
import com.hortifruti.model.FiltroFamilia;
import com.hortifruti.model.StatusFila;
import com.hortifruti.repository.EntregaRepository;
import com.hortifruti.repository.FamiliaRepository;
import com.hortifruti.validation.CpfUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FamiliaService {

    private final FamiliaRepository familiaRepository;
    private final EntregaRepository entregaRepository;

    public FamiliaService(FamiliaRepository familiaRepository, EntregaRepository entregaRepository) {
        this.familiaRepository = familiaRepository;
        this.entregaRepository = entregaRepository;
    }

    public List<Familia> listarTodas() {
        return familiaRepository.findAll();
    }

    public Page<Familia> listar(FiltroFamilia filtro) {
        PageRequest pageable = PageRequest.of(
                filtro.getPage(),
                filtro.getSize(),
                Sort.by("nomeCompleto").ascending()
        );
        return familiaRepository.buscarComFiltros(
                filtro.termoBusca(),
                filtro.statusFilaFiltro(),
                filtro.ativaFiltro(),
                pageable
        );
    }

    public Familia buscarPorId(Long id) {
        return familiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Família não encontrada"));
    }

    @Transactional
    public Familia salvar(Familia familia) {
        validarMoradores(familia);
        validarCpfDuplicado(familia);

        if (familia.getId() == null) {
            familia.setDataCadastro(LocalDate.now());
            familia.setStatusFila(StatusFila.NA_FILA);
            familia.setPrioridade(familiaRepository.findMaxPrioridade() + 1);
            if (familia.getAtiva() == null) {
                familia.setAtiva(true);
            }
        } else {
            Familia existente = buscarPorId(familia.getId());
            familia.setStatusFila(existente.getStatusFila());
            familia.setPrioridade(existente.getPrioridade());
            familia.setNumeroSenha(existente.getNumeroSenha());
            if (familia.getDataCadastro() == null) {
                familia.setDataCadastro(existente.getDataCadastro());
            }
            if (familia.getAtiva() == null) {
                familia.setAtiva(existente.getAtiva());
            }
        }

        return familiaRepository.save(familia);
    }

    @Transactional
    public void deletar(Long id) {
        if (!familiaRepository.existsById(id)) {
            throw new IllegalArgumentException("Família não encontrada");
        }
        if (entregaRepository.existsByFamiliaId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir família com entregas registradas. Desative-a em vez de excluir.");
        }
        familiaRepository.deleteById(id);
    }

    public long totalFamilias() {
        return familiaRepository.count();
    }

    public long familiasNaFila() {
        return familiaRepository.countByStatusFila(StatusFila.NA_FILA);
    }

    public long familiasAtendidas() {
        return familiaRepository.countByStatusFila(StatusFila.ATENDIDO);
    }

    public List<Familia> filaTop5() {
        return familiaRepository
                .findByStatusFilaOrderByPrioridadeAsc(StatusFila.NA_FILA)
                .stream()
                .limit(5)
                .toList();
    }

    public List<Familia> filaComSenha() {
        return familiaRepository
                .findByStatusFilaOrderByNumeroSenhaAsc(StatusFila.NA_FILA)
                .stream()
                .filter(f -> f.getNumeroSenha() != null)
                .toList();
    }

    public long familiasHoje() {
        LocalDate hoje = LocalDate.now();
        return familiaRepository.findAll()
                .stream()
                .filter(f -> f.getDataCadastro() != null && f.getDataCadastro().isEqual(hoje))
                .count();
    }

    private void validarMoradores(Familia familia) {
        if (familia.getQuantidadeMoradores() == null || familia.getQuantidadeMoradores() < 1) {
            throw new IllegalArgumentException("A família deve ter pelo menos 1 morador");
        }
    }

    private void validarCpfDuplicado(Familia familia) {
        String cpf = CpfUtil.normalizar(familia.getCpf());
        if (!CpfUtil.isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        Long id = familia.getId() != null ? familia.getId() : -1L;
        if (familiaRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new IllegalArgumentException("CPF já cadastrado para outra família");
        }
        familia.setCpf(cpf);
    }
}
