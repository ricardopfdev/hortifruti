# Modelo de Banco de Dados

O schema é versionado com **Flyway** em `src/main/resources/db/migration/`.

| Migration | Descrição |
|-----------|-----------|
| V1 | Tabelas `familias` e `entregas` |
| V2 | Tabela `usuarios` (login das famílias) |
| V3 | Remove coluna legada `situacao` |
| V4 | Endereço detalhado em `familias` (número, bairro, cidade) |
| V5 | Tabela `atendentes` |

## Tabela: familias

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT (PK) | Identificador |
| nome_completo | VARCHAR | Nome do responsável |
| cpf | VARCHAR(11) | CPF único (somente dígitos) |
| telefone | VARCHAR | Contato |
| endereco | VARCHAR | Logradouro (rua) |
| numero | VARCHAR(20) | Número |
| bairro | VARCHAR | Bairro |
| cidade | VARCHAR | Cidade |
| quantidade_moradores | INTEGER | Mínimo 1 |
| ativa | BOOLEAN | Família ativa no projeto |
| status_fila | VARCHAR | `NA_FILA` ou `ATENDIDO` |
| prioridade | INTEGER | Ordem na fila |
| numero_senha | INTEGER | Senha do dia na fila (nullable) |
| data_cadastro | DATE | Data de cadastro |

## Tabela: usuarios

Login das famílias (CPF como username).

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT (PK) | Identificador |
| username | VARCHAR(11) | CPF (único) |
| password_hash | VARCHAR | Hash BCrypt da senha de acesso |
| senha_acesso | VARCHAR(50) | Senha legível (5 caracteres) |
| role | VARCHAR | Sempre `FAMILIA` |
| familia_id | BIGINT (FK, único) | Referência à família |
| ativo | BOOLEAN | Conta ativa |
| criado_em | TIMESTAMP | Data de criação |

## Tabela: atendentes

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT (PK) | Identificador |
| username | VARCHAR(50) | Login (único) |
| nome_completo | VARCHAR | Nome do atendente |
| password_hash | VARCHAR | Hash BCrypt |
| senha_acesso | VARCHAR(50) | Senha legível |
| ativo | BOOLEAN | Conta ativa |
| criado_em | TIMESTAMP | Data de criação |

## Tabela: entregas

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT (PK) | Identificador |
| familia_id | BIGINT (FK) | Referência à família |
| senha | INTEGER | Senha utilizada na entrega |
| data_entrega | TIMESTAMP | Data/hora da entrega |

## Diagrama simplificado

```
familias 1──1 usuarios
familias 1──N entregas
atendentes (independente — login de staff)
admin (não persiste no banco — configuração)
```
