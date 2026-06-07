package com.hortifruti.repository;

import com.hortifruti.entity.Entrega;
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
}
