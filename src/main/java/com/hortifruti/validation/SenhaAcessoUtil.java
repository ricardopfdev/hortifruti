package com.hortifruti.validation;

public final class SenhaAcessoUtil {

    private SenhaAcessoUtil() {
    }

    public static boolean isSenhaFamilia(String senha) {
        return senha != null && senha.length() == 5 && senha.matches("[A-Za-z0-9]{5}");
    }

    public static String normalizarEntrada(String senha) {
        if (senha == null) {
            return "";
        }
        String trimmed = senha.trim();
        if (isSenhaFamilia(trimmed)) {
            return trimmed.toUpperCase();
        }
        return trimmed;
    }
}
