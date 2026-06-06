package com.hortifruti.service;

import com.hortifruti.entity.Entrega;
import com.hortifruti.entity.Familia;
import com.hortifruti.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    public void registrarEntrega(Familia familia) {
        Entrega entrega = new Entrega(familia);
        entregaRepository.save(entrega);
    }
}
