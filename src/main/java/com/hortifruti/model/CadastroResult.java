package com.hortifruti.model;

import com.hortifruti.entity.Familia;

public record CadastroResult(Familia familia, String username, String senhaGerada, Integer senhaRetirada) {
}
