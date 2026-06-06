public long familiasNaFila() {
    return repository.countByStatusFila("AGUARDANDO");
}

public long familiasAtendidas() {
    return repository.countByStatusFila("ATENDIDA");
}

public List<Familia> filaTop5() {
    return repository.findByStatusFilaOrderByPrioridadeAsc("AGUARDANDO")
            .stream()
            .limit(5)
            .toList();
}