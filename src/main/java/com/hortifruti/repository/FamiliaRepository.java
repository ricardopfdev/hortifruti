package com.hortifruti.repository;

import com.hortifruti.entity.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Long> {

    List<Familia> findByStatusFilaOrderByPrioridadeAsc(String statusFila);

    long countByStatusFila(String statusFila);
}