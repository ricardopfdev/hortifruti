package com.hortifruti.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Arrays;
import java.util.Map;

public class RenderEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE = "renderDatabaseConfig";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            return;
        }

        Map<String, Object> datasourceProperties = Map.copyOf(ProductionDatabaseUrlResolver.resolve(environment));
        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE, datasourceProperties));
    }
}
