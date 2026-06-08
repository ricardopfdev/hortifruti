package com.hortifruti.config;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

final class ProductionDatabaseUrlResolver {

    private ProductionDatabaseUrlResolver() {
    }

    static Map<String, String> resolve(Environment env) {
        String jdbcUrl = resolveJdbcUrl(env);
        String username = firstNonBlank(
                env.getProperty("SPRING_DATASOURCE_USERNAME"),
                env.getProperty("DB_USER"));
        String password = firstNonBlank(
                env.getProperty("SPRING_DATASOURCE_PASSWORD"),
                env.getProperty("DB_PASSWORD"));

        String databaseUrl = firstNonBlank(
                env.getProperty("DATABASE_URL"),
                env.getProperty("SPRING_DATASOURCE_URL"));

        if (databaseUrl != null && databaseUrl.startsWith("postgres")
                && (!StringUtils.hasText(username) || !StringUtils.hasText(password))) {
            Credentials credentials = parseCredentials(databaseUrl);
            if (!StringUtils.hasText(username)) {
                username = credentials.username();
            }
            if (!StringUtils.hasText(password)) {
                password = credentials.password();
            }
        }

        if (!StringUtils.hasText(jdbcUrl)) {
            throw new IllegalStateException(
                    "Configure a conexão com o Postgres no Render: vincule o banco ao Web Service "
                            + "ou defina SPRING_DATASOURCE_URL / DB_HOST, DB_PORT e DB_NAME.");
        }
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalStateException(
                    "Configure SPRING_DATASOURCE_USERNAME e SPRING_DATASOURCE_PASSWORD no Render.");
        }

        Map<String, String> properties = new LinkedHashMap<>();
        properties.put("spring.datasource.url", jdbcUrl);
        properties.put("spring.datasource.username", username);
        properties.put("spring.datasource.password", password);
        return properties;
    }

    static String resolveJdbcUrl(Environment env) {
        String explicitJdbc = env.getProperty("SPRING_DATASOURCE_URL");
        if (StringUtils.hasText(explicitJdbc) && explicitJdbc.startsWith("jdbc:")) {
            return ensureSslMode(explicitJdbc);
        }

        String databaseUrl = env.getProperty("DATABASE_URL");
        if (StringUtils.hasText(databaseUrl)) {
            return toJdbcUrl(databaseUrl);
        }

        if (StringUtils.hasText(explicitJdbc) && explicitJdbc.startsWith("postgres")) {
            return toJdbcUrl(explicitJdbc);
        }

        String host = env.getProperty("DB_HOST");
        String port = env.getProperty("DB_PORT", "5432");
        String name = env.getProperty("DB_NAME");
        if (StringUtils.hasText(host) && StringUtils.hasText(name)) {
            return ensureSslMode("jdbc:postgresql://" + host + ":" + port + "/" + name);
        }

        return null;
    }

    static String toJdbcUrl(String databaseUrl) {
        if (databaseUrl.startsWith("jdbc:")) {
            return ensureSslMode(databaseUrl);
        }

        URI uri = URI.create(databaseUrl.replace("postgres://", "http://")
                .replace("postgresql://", "http://"));

        String host = uri.getHost();
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String path = uri.getPath();
        String database = path != null && path.length() > 1 ? path.substring(1) : "";

        if (!StringUtils.hasText(host) || !StringUtils.hasText(database)) {
            throw new IllegalStateException("DATABASE_URL inválida: " + databaseUrl);
        }

        return ensureSslMode("jdbc:postgresql://" + host + ":" + port + "/" + database);
    }

    private static Credentials parseCredentials(String databaseUrl) {
        URI uri = URI.create(databaseUrl.replace("postgres://", "http://")
                .replace("postgresql://", "http://"));
        String userInfo = uri.getUserInfo();
        if (!StringUtils.hasText(userInfo)) {
            return new Credentials(null, null);
        }

        String[] parts = userInfo.split(":", 2);
        String username = decode(parts[0]);
        String password = parts.length > 1 ? decode(parts[1]) : "";
        return new Credentials(username, password);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String ensureSslMode(String jdbcUrl) {
        if (jdbcUrl.contains("sslmode=")) {
            return jdbcUrl;
        }
        return jdbcUrl + (jdbcUrl.contains("?") ? "&" : "?") + "sslmode=require";
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private record Credentials(String username, String password) {
    }
}
