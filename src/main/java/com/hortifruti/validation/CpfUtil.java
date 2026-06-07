package com.hortifruti.validation;

public final class CpfUtil {

    private CpfUtil() {
    }

    public static String normalizar(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("\\D", "");
    }

    public static boolean isValid(String cpf) {
        String digits = normalizar(cpf);

        if (digits.length() != 11) {
            return false;
        }

        if (digits.chars().distinct().count() == 1) {
            return false;
        }

        int primeiroDigito = calcularDigito(digits.substring(0, 9), 10);
        int segundoDigito = calcularDigito(digits.substring(0, 9) + primeiroDigito, 11);

        return digits.equals(digits.substring(0, 9) + primeiroDigito + segundoDigito);
    }

    private static int calcularDigito(String base, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * (pesoInicial - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
