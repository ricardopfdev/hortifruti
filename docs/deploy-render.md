# Publicar no Render

Guia para colocar o HortiFrúti online usando [Render](https://render.com) e o repositório GitHub.

## Opção A — Blueprint (mais fácil)

1. Faça push deste repositório no GitHub (já deve estar em `ricardopfdev/hortifruti`).
2. No Render: **New** → **Blueprint**.
3. Conecte o GitHub e selecione o repositório `hortifruti`.
4. O Render lê o arquivo `render.yaml` e cria:
   - banco **Postgres**
   - **Web Service** Java
5. Clique em **Apply**.
6. Aguarde o build (5–10 min na primeira vez).
7. Anote a URL gerada (ex.: `https://hortifruti.onrender.com`).

As senhas de admin e atendente são geradas automaticamente. Veja em **Environment** do Web Service:

- `HORTIFRUTI_ADMIN_PASSWORD`
- `HORTIFRUTI_ATENDENTE_PASSWORD`

## Opção B — Manual (passo a passo)

### 1. Criar Postgres

1. **New** → **Postgres**
2. Nome: `hortifruti-db`
3. Database: `hortifruti`
4. Plano: **Free** (ou pago, se preferir)
5. **Create Database**

Anote em **Connections** (Internal):

- Host, Port, Database, User, Password

### 2. Criar Web Service

1. **New** → **Web Services**
2. Conecte o GitHub → repositório `hortifruti`
3. Configuração:

| Campo | Valor |
|-------|--------|
| Name | `hortifruti` |
| Runtime | **Java** |
| Build Command | `./mvnw clean package -DskipTests` |
| Start Command | `java -jar target/hortifruti-0.0.1-SNAPSHOT.jar` |

### 3. Variáveis de ambiente

Em **Environment** do Web Service, adicione:

| Variável | Valor |
|----------|--------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DB_HOST` | host do Postgres (Internal) |
| `DB_PORT` | `5432` |
| `DB_NAME` | `hortifruti` |
| `SPRING_DATASOURCE_USERNAME` | usuário do Postgres |
| `SPRING_DATASOURCE_PASSWORD` | senha do Postgres |
| `HORTIFRUTI_ADMIN_USERNAME` | `admin` |
| `HORTIFRUTI_ADMIN_PASSWORD` | senha forte (ex.: 12+ caracteres) |
| `HORTIFRUTI_ATENDENTE_USERNAME` | `atendente` |
| `HORTIFRUTI_ATENDENTE_PASSWORD` | senha forte |

### 4. Deploy

Clique em **Create Web Service** e aguarde o build.

## Depois do deploy

- Acesse a URL do Render (ex.: `https://hortifruti.onrender.com/login`).
- Login admin: usuário `admin` + senha definida em `HORTIFRUTI_ADMIN_PASSWORD`.
- Cadastros feitos online ficam no Postgres **do Render**, não no seu PC.

## Plano gratuito — o que esperar

- **Cold start:** após ~15 min sem acesso, a primeira abertura pode demorar ~1 minuto.
- **Postgres free:** expira após 90 dias (Render avisa antes); faça backup se for usar em produção real.
- **HTTPS:** Render já inclui certificado SSL na URL `.onrender.com`.

## Problemas comuns

| Erro | Solução |
|------|---------|
| Build falhou | Veja **Logs** → Build; confira Java 21 (`system.properties`) |
| App não sobe | Logs → Runtime; confira variáveis `DB_*` e senha do Postgres |
| 502 / timeout | Aguarde cold start ou aumente o plano |
| Login não funciona | Confira `HORTIFRUTI_ADMIN_PASSWORD` no painel Environment |

## Arquivos de deploy no projeto

- `render.yaml` — blueprint Render
- `application-prod.properties` — perfil produção (porta, SSL, cache)
- `system.properties` — Java 21
