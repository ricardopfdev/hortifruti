package com.hortifruti.service;

import java.security.SecureRandom;

public final class SenhaUtil {

    private static final String LETRAS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    private SenhaUtil() {
    }

    /**
     * Senha de acesso: 5 caracteres — 1 letra e 4 números, em posição aleatória.
     */
    public static String gerarSenhaAcesso() {
        char letra = LETRAS.charAt(RANDOM.nextInt(LETRAS.length()));
        int posicaoLetra = RANDOM.nextInt(5);
        StringBuilder senha = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            if (i == posicaoLetra) {
                senha.append(letra);
            } else {
                senha.append(RANDOM.nextInt(10));
            }
        }
        return senha.toString();
    }

    /** Senha de atendentes: 8 caracteres alfanuméricos. */
    public static String gerarSenhaStaff() {
        String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        StringBuilder senha = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            senha.append(caracteres.charAt(RANDOM.nextInt(caracteres.length())));
        }
        return senha.toString();
    }
}
