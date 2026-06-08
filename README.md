# Sistema HortiFrúti

Sistema web para gerenciamento de famílias atendidas por um projeto social de distribuição de alimentos.

## Funcionalidades

| Área | Descrição |
|------|-----------|
| **Cadastro público** | Famílias se cadastram em `/cadastro` com endereço completo; senha de acesso é gerada automaticamente |
| **Login** | Admin, atendentes (usuário + senha) e famílias (CPF + senha de 5 caracteres) |
| **Meu cadastro** | Família logada consulta seus dados em `/meu-cadastro` |
| **Recuperar senha** | Família recupera senha informando CPF e endereço cadastrado |
| **Famílias** | Admin cadastra, edita, exclui e registra entregas |
| **Atendentes** | Admin cadastra ajudantes, altera senhas e exclui contas |
| **Fila** | Geração de senha do dia, atendimento, painel público e baixa por senha |
| **Entregas** | Histórico de entregas realizadas |
| **Dashboard** | Resumo para admin e atendentes |

Documentação detalhada em [`docs/`](docs/):

- [Requisitos funcionais](docs/requisitos.md)
- [Regras de negócio](docs/regras-negocio.md)
- [Modelo de banco de dados](docs/modelo-banco.md)
- [Perfis de acesso e rotas](docs/perfis-e-rotas.md)

## Perfis de usuário

| Perfil | Como entra | Senha |
|--------|------------|-------|
| **Administrador** | Usuário `admin` | Senha completa (ex.: `admin123`) — configurada em `env.local` |
| **Atendente** | Usuário cadastrado pelo admin | Senha completa (mín. 6 caracteres ou gerada pelo sistema) |
| **Família** | CPF (com ou sem pontuação) | 5 caracteres: 1 letra + 4 números (ex.: `K1234`) |

> O administrador **não** é cadastrado no banco; fica no arquivo de configuração. Atendentes ficam na tabela `atendentes`.

## Executar no IntelliJ (passo a passo)

### 1. PostgreSQL

Crie o banco `hortifruti` no PostgreSQL local.

Com Docker (opcional):

```powershell
docker compose up -d
```

Senha padrão do Docker: `hortifruti`

### 2. Credenciais locais

Escolha **uma** opção:

**Opção A — `env.local` (recomendado no IntelliJ):**

```powershell
Copy-Item env.local.example env.local
```

Edite `env.local`:

- `SPRING_DATASOURCE_PASSWORD` — senha do PostgreSQL
- `HORTIFRUTI_ADMIN_*` e `HORTIFRUTI_ATENDENTE_*` — usuários do sistema

A Run Configuration **HortifrutiApplication** carrega este arquivo automaticamente (perfil `local`).

**Opção B — perfil local:**

```powershell
Copy-Item src\main\resources\application-local.properties.example src\main\resources\application-local.properties
```

Edite as credenciais em `application-local.properties`.

> `env.local` e `application-local.properties` **não** vão para o GitHub (estão no `.gitignore`).

### 3. Rodar

1. Abra a pasta `horti-flow` no IntelliJ
2. **Build → Rebuild Project** (após atualizar o código)
3. Run **HortifrutiApplication** (Shift+F10)
4. Acesse http://localhost:8080

### Login (desenvolvimento — padrão)

| Perfil | Usuário | Senha |
|--------|---------|-------|
| Administrador | `admin` | `admin123` |
| Atendente | `atendente` | `atendente123` |

O atendente padrão é criado automaticamente na primeira execução (migration + seed).

**Links úteis:**

| URL | Acesso |
|-----|--------|
| http://localhost:8080/login | Login |
| http://localhost:8080/cadastro | Cadastro de família (público) |
| http://localhost:8080/recuperar-senha | Recuperar senha (público) |
| http://localhost:8080/fila/painel | Painel de senhas (público) |

### Testes

```powershell
.\mvnw.cmd test
```

## Estrutura do projeto

```
horti-flow/
├── src/main/java/com/hortifruti/
│   ├── config/          # Segurança, encoders de senha, seed de atendentes
│   ├── controller/      # Controllers web (Thymeleaf)
│   ├── entity/          # Entidades JPA
│   ├── repository/      # Repositórios Spring Data
│   ├── service/         # Regras de negócio
│   └── validation/      # CPF, endereço, senhas
├── src/main/resources/
│   ├── db/migration/    # Flyway (V1–V5)
│   └── templates/       # Páginas HTML
├── docs/                # Documentação do sistema
├── env.local.example    # Modelo de variáveis de ambiente
└── README.md
```

## Dados e privacidade

- **Código** → versionado no GitHub
- **Cadastros de famílias** → ficam apenas no PostgreSQL local (não vão para o Git)
- **Senhas reais** → ficam em `env.local` (não versionado)

## Tecnologias

Java 21 · Spring Boot 4 · Spring Security · JPA · PostgreSQL · Flyway · Thymeleaf · AdminLTE 4 · Maven
