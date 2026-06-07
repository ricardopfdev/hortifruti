package com.hortifruti.repository;

import com.hortifruti.entity.Familia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Long> {

    List<Familia> findByStatusFilaOrderByPrioridadeAsc(String statusFila);

    List<Familia> findByStatusFilaOrderByNumeroSenhaAsc(String statusFila);

    long countByStatusFila(String statusFila);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    Optional<Familia> findByNumeroSenhaAndStatusFila(Integer numeroSenha, String statusFila);

    @Query("SELECT COALESCE(MAX(f.prioridade), 0) FROM Familia f")
    Integer findMaxPrioridade();

    @Query("""
            SELECT f FROM Familia f
            WHERE (:busca = '' OR LOWER(f.nomeCompleto) LIKE LOWER(CONCAT('%', :busca, '%'))
                              OR f.cpf LIKE CONCAT('%', :busca, '%'))
              AND (:statusFila = '' OR f.statusFila = :statusFila)
              AND (:ativa IS NULL OR f.ativa = :ativa)
            """)
    Page<Familia> buscarComFiltros(@Param("busca") String busca,
                                    @Param("statusFila") String statusFila,
                                    @Param("ativa") Boolean ativa,
                                    Pageable pageable);
}
