# Sistema HortiFrúti

Sistema web para gerenciamento de famílias atendidas por um projeto social de distribuição de alimentos.

## Executar no IntelliJ (passo a passo)

### 1. PostgreSQL

Crie o banco `hortifruti` no PostgreSQL local.

Com Docker (opcional):

```powershell
docker compose up -d
```

Senha padrão do Docker: `hortifruti`

### 2. Senha do banco

Escolha **uma** opção:

**Opção A — `env.local` (recomendado no IntelliJ):**

```powershell
Copy-Item env.local.example env.local
```

Edite `env.local` e coloque a senha do seu PostgreSQL em `SPRING_DATASOURCE_PASSWORD`.

A Run Configuration **HortifrutiApplication** carrega este arquivo automaticamente.

**Opção B — perfil local:**

```powershell
Copy-Item src\main\resources\application-local.properties.example src\main\resources\application-local.properties
```

Edite a senha em `application-local.properties`.

### 3. Rodar

1. Abra a pasta `horti-flow` no IntelliJ
2. Run **HortifrutiApplication** (Shift+F10)
3. Acesse http://localhost:8080

### Login (desenvolvimento)

| Perfil | Usuário | Senha |
|--------|---------|-------|
| Administrador | `admin` | `admin123` |
| Atendente | `atendente` | `atendente123` |

Painel público de senhas: http://localhost:8080/fila/painel

### Testes

```powershell
.\mvnw.cmd test
```

## Tecnologias

Java 21 · Spring Boot · JPA · PostgreSQL · Flyway · Thymeleaf · AdminLTE 4 · Maven
