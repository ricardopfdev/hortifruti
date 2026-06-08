package com.hortifruti.validation;

import com.hortifruti.entity.Familia;

public final class EnderecoUtil {

    private EnderecoUtil() {
    }

    public static String formatarCompleto(Familia familia) {
        if (familia == null) {
            return "";
        }
        return String.format("%s, %s - %s - %s",
                nullSafe(familia.getEndereco()),
                nullSafe(familia.getNumero()),
                nullSafe(familia.getBairro()),
                nullSafe(familia.getCidade()));
    }

    public static String normalizarTelefone(String telefone) {
        if (telefone == null) {
            return "";
        }
        return telefone.replaceAll("\\D", "");
    }

    private static String nullSafe(String value) {
        return value != null ? value : "";
    }
}
