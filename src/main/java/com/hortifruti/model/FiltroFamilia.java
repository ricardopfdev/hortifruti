package com.hortifruti.model;

import com.hortifruti.validation.CpfUtil;

public class FiltroFamilia {

    private String busca = "";
    private String statusFila = "";
    private String ativa = "";
    private int page = 0;
    private int size = 10;

    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
    }

    public String getStatusFila() {
        return statusFila;
    }

    public void setStatusFila(String statusFila) {
        this.statusFila = statusFila;
    }

    public String getAtiva() {
        return ativa;
    }

    public void setAtiva(String ativa) {
        this.ativa = ativa;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(page, 0);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size <= 0 ? 10 : Math.min(size, 50);
    }

    public String termoBusca() {
        if (busca == null || busca.isBlank()) {
            return "";
        }
        String termo = busca.trim();
        String cpfDigits = CpfUtil.normalizar(termo);
        if (!cpfDigits.isEmpty() && cpfDigits.length() <= 11 && termo.matches("[0-9.\\-\\s]+")) {
            return cpfDigits;
        }
        return termo;
    }

    public Boolean ativaFiltro() {
        if (ativa == null || ativa.isBlank()) {
            return null;
        }
        return Boolean.parseBoolean(ativa);
    }

    public String statusFilaFiltro() {
        return statusFila != null ? statusFila.trim() : "";
    }
}
