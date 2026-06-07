package com.hortifruti.model;

import com.hortifruti.validation.CpfUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class FiltroEntrega {

    private String busca = "";
    private int page = 0;
    private int size = 10;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
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

    public Integer senhaFiltro() {
        String termo = termoBusca();
        if (termo.matches("\\d{1,3}")) {
            return Integer.parseInt(termo);
        }
        return null;
    }
}
