package com.hortifruti.repository;

import com.hortifruti.entity.Atendente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtendenteRepository extends JpaRepository<Atendente, Long> {

    Optional<Atendente> findByUsernameAndAtivoTrue(String username);

    Optional<Atendente> findByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    List<Atendente> findAllByOrderByNomeCompletoAsc();
}
