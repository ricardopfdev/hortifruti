package com.hortifruti.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductionDatabaseUrlResolverTest {

    @Test
    void montaUrlAPartirDeDbHostPortName() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("DB_HOST", "dpg-example.render.com");
        env.setProperty("DB_PORT", "5432");
        env.setProperty("DB_NAME", "hortifruti");

        String url = ProductionDatabaseUrlResolver.resolveJdbcUrl(env);

        assertEquals("jdbc:postgresql://dpg-example.render.com:5432/hortifruti?sslmode=require", url);
    }

    @Test
    void converteDatabaseUrlDoRender() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("DATABASE_URL", "postgresql://user:secret@dpg-example.render.com:5432/hortifruti");

        String url = ProductionDatabaseUrlResolver.resolveJdbcUrl(env);

        assertEquals("jdbc:postgresql://dpg-example.render.com:5432/hortifruti?sslmode=require", url);
    }

    @Test
    void resolveUsuarioESenhaDaDatabaseUrl() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("DATABASE_URL", "postgresql://admin:senha123@dpg-example.render.com:5432/hortifruti");

        var props = ProductionDatabaseUrlResolver.resolve(env);

        assertTrue(props.get("spring.datasource.url").contains("dpg-example.render.com"));
        assertEquals("admin", props.get("spring.datasource.username"));
        assertEquals("senha123", props.get("spring.datasource.password"));
    }
}
