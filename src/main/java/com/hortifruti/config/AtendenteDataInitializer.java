package com.hortifruti.config;

import com.hortifruti.service.AtendenteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AtendenteDataInitializer implements ApplicationRunner {

    private final AtendenteService atendenteService;
    private final String atendenteUsername;
    private final String atendentePassword;

    public AtendenteDataInitializer(AtendenteService atendenteService,
                                    @Value("${hortifruti.security.atendente.username}") String atendenteUsername,
                                    @Value("${hortifruti.security.atendente.password}") String atendentePassword) {
        this.atendenteService = atendenteService;
        this.atendenteUsername = atendenteUsername;
        this.atendentePassword = atendentePassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        atendenteService.criarSeNaoExistir(
                atendenteUsername,
                "Atendente padrão",
                atendentePassword);
    }
}
