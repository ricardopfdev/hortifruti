package com.hortifruti.service;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SenhaUtilTest {

    @RepeatedTest(50)
    void gerarSenhaAcessoTemCincoCaracteresComUmaLetraEQuatroNumeros() {
        String senha = SenhaUtil.gerarSenhaAcesso();

        assertThat(senha).hasSize(5);
        assertThat(senha.chars().filter(Character::isLetter).count()).isEqualTo(1);
        assertThat(senha.chars().filter(Character::isDigit).count()).isEqualTo(4);
        assertThat(senha).matches("[A-Z0-9]{5}");
    }

    @Test
    void letraPodeEstarEmQualquerPosicao() {
        boolean letraNoInicio = false;
        boolean letraNoMeio = false;
        boolean letraNoFim = false;

        for (int i = 0; i < 200; i++) {
            String senha = SenhaUtil.gerarSenhaAcesso();
            if (Character.isLetter(senha.charAt(0))) {
                letraNoInicio = true;
            }
            if (Character.isLetter(senha.charAt(2))) {
                letraNoMeio = true;
            }
            if (Character.isLetter(senha.charAt(4))) {
                letraNoFim = true;
            }
        }

        assertThat(letraNoInicio).isTrue();
        assertThat(letraNoMeio).isTrue();
        assertThat(letraNoFim).isTrue();
    }
}
