package com.hortifruti.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CpfUtilFormatarTest {

    @Test
    void formatarCpfValido() {
        assertThat(CpfUtil.formatar("52998224725")).isEqualTo("529.982.247-25");
    }
}
