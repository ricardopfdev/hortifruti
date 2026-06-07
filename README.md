# Sistema HortiFrúti

Sistema web desenvolvido para gerenciamento de famílias atendidas por um projeto social de distribuição de alimentos.

## Objetivo

Digitalizar o cadastro das famílias beneficiadas e controlar a distribuição de alimentos de forma organizada e transparente.

## Funcionalidades

- Cadastro de famílias
- Consulta de famílias
- Controle de fila de distribuição com geração de senha
- Painel de senhas para exibição no atendimento
- Baixa por senha após entrega
- Registro e histórico de entregas
- Dashboard com indicadores

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- AdminLTE 4
- HTML5, CSS3, JavaScript
- Maven
- Git e GitHub

## Arquitetura

Projeto desenvolvido utilizando arquitetura em camadas:

- Controller
- Service
- Repository
- Entity

## Banco de Dados

PostgreSQL — crie o banco antes de executar:

```sql
CREATE DATABASE hortifruti;
```

## Como Executar

### 1. Clonar o repositório

```bash
git clone https://github.com/ricardopfdev/hortifruti.git
cd hortifruti
```

### 2. Configurar credenciais locais

```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

Edite `application-local.properties` com usuário e senha do seu PostgreSQL local.

> **Importante:** o arquivo `application-local.properties` não vai para o GitHub.

### 3. Executar no IntelliJ IDEA

1. Abra a pasta `hortifruti` como projeto (File → Open)
2. Aguarde o Maven baixar as dependências
3. Execute `HortifrutiApplication.java`

### 4. Executar pelo terminal

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

### 5. Acessar o sistema

Abra no navegador: **http://localhost:8080**

## Páginas principais

| URL | Descrição |
|-----|-----------|
| `/dashboard` | Indicadores gerais |
| `/familias` | Cadastro e listagem |
| `/fila` | Fila de atendimento |
| `/fila/painel` | Painel de senhas (monitor) |
| `/fila/baixa` | Confirmar entrega por senha |
| `/entregas` | Histórico de entregas |

## Git e GitHub

Branch principal do código: **`master`**

```bash
git add .
git commit -m "sua mensagem"
git push
```
