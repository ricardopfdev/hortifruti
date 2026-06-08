# Publicar no Render

Guia para colocar o HortiFrúti online usando [Render](https://render.com) e o repositório GitHub.

> O Render **não mostra Java** na lista de linguagens. Use **Docker** — o projeto já inclui `Dockerfile` na raiz.

## Opção A — Blueprint (mais fácil)

1. Repositório no GitHub: `ricardopfdev/hortifruti`
2. No Render: **New** → **Blueprint**
3. Conecte o GitHub e selecione `hortifruti`
4. O Render lê `render.yaml` e cria **Postgres** + **Web Service (Docker)**
5. **Apply** → aguarde o build (5–15 min na primeira vez)
6. URL: ex. `https://hortifruti.onrender.com`

Senhas geradas automaticamente em **Environment**:

- `HORTIFRUTI_ADMIN_PASSWORD`
- `HORTIFRUTI_ATENDENTE_PASSWORD`

## Opção B — Manual (passo a passo)

### 1. Criar Postgres

1. **New** → **Postgres**
2. Nome: `hortifruti-db` · Database: `hortifruti` · Plano: **Free**
3. **Create Database**

Anote em **Connections** (Internal): Host, Port, Database, User, Password.

### 2. Criar Web Service

1. **New** → **Web Services**
2. Conecte o GitHub → repositório `hortifruti`
3. Configuração:

| Campo | Valor |
|-------|--------|
| Name | `hortifruti` |
| Language | **Docker** |
| Dockerfile | `./Dockerfile` (padrão) |
| Build Command | *(deixe vazio — o Docker cuida disso)* |
| Start Command | *(deixe vazio)* |

### 3. Variáveis de ambiente

Em **Environment** do Web Service:

| Variável | Valor |
|----------|--------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DB_HOST` | host Internal do Postgres |
| `DB_PORT` | `5432` |
| `DB_NAME` | `hortifruti` |
| `SPRING_DATASOURCE_USERNAME` | usuário do Postgres |
| `SPRING_DATASOURCE_PASSWORD` | senha do Postgres |
| `HORTIFRUTI_ADMIN_USERNAME` | `admin` |
| `HORTIFRUTI_ADMIN_PASSWORD` | senha forte |
| `HORTIFRUTI_ATENDENTE_USERNAME` | `atendente` |
| `HORTIFRUTI_ATENDENTE_PASSWORD` | senha forte |

### 4. Deploy

**Create Web Service** → aguarde build e deploy.

## Depois do deploy

- Acesse `https://SEU-SERVICO.onrender.com/login`
- Admin: `admin` + `HORTIFRUTI_ADMIN_PASSWORD`
- Dados online ficam no Postgres **do Render**, não no seu PC

## Plano gratuito

- **Cold start:** ~1 min após ficar parado
- **Postgres free:** expira em 90 dias (Render avisa)
- **HTTPS:** incluso em `.onrender.com`

## Problemas comuns

| Erro | Solução |
|------|---------|
| Build Docker falhou | Logs → Build; confira se `Dockerfile` está na raiz do repo |
| App não sobe | Logs → Runtime; confira `DB_*` e senha Postgres |
| 502 / timeout | Cold start ou aumente o plano |
| Login falha | Confira `HORTIFRUTI_ADMIN_PASSWORD` |

## Arquivos de deploy

| Arquivo | Função |
|---------|--------|
| `Dockerfile` | Build e execução Java 21 |
| `.dockerignore` | Arquivos ignorados no build |
| `render.yaml` | Blueprint Render |
| `application-prod.properties` | Perfil produção |
