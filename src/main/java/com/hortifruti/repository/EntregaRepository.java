package com.hortifruti.repository;

import com.hortifruti.entity.Entrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findAllByOrderByDataEntregaDesc();

    long countByDataEntregaBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT COALESCE(MAX(e.senha), 0) FROM Entrega e WHERE e.dataEntrega >= :inicio AND e.dataEntrega < :fim")
    Integer findMaxSenhaNoDia(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    boolean existsByFamiliaId(Long familiaId);

    void deleteByFamilia_Id(Long familiaId);

    @Query("""
            SELECT e FROM Entrega e JOIN e.familia f
            WHERE (:busca = '' OR LOWER(f.nomeCompleto) LIKE LOWER(CONCAT('%', :busca, '%'))
                              OR f.cpf LIKE CONCAT('%', :busca, '%'))
              AND (:senha IS NULL OR e.senha = :senha)
              AND (:dataInicio IS NULL OR e.dataEntrega >= :dataInicio)
              AND (:dataFim IS NULL OR e.dataEntrega <= :dataFim)
            """)
    Page<Entrega> buscarComFiltros(@Param("busca") String busca,
                                   @Param("senha") Integer senha,
                                   @Param("dataInicio") LocalDateTime dataInicio,
                                   @Param("dataFim") LocalDateTime dataFim,
                                   Pageable pageable);
}
