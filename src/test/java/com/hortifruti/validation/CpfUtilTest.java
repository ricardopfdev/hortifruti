package com.hortifruti.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CpfUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "52998224725",
            "529.982.247-25",
            "39053344705",
            "11144477735"
    })
    void aceitaCpfsValidos(String cpf) {
        assertTrue(CpfUtil.isValid(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "123",
            "1234567890",
            "123456789012",
            "11111111111",
            "00000000000",
            "52998224724",
            "abc"
    })
    void rejeitaCpfsInvalidos(String cpf) {
        assertFalse(CpfUtil.isValid(cpf));
    }

    @Test
    void normalizarRemovePontuacao() {
        assertEquals("39053344705", CpfUtil.normalizar("390.533.447-05"));
    }
}
