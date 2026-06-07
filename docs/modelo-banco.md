# Modelo de Banco de Dados

O schema é versionado com **Flyway** em `src/main/resources/db/migration/`.

## Tabela: familias

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT (PK) | Identificador |
| nome_completo | VARCHAR | Nome do responsável |
| cpf | VARCHAR(11) | CPF único |
| telefone | VARCHAR | Contato |
| endereco | VARCHAR | Endereço |
| quantidade_moradores | INTEGER | Mínimo 1 |
| ativa | BOOLEAN | Família ativa no projeto |
| status_fila | VARCHAR | `NA_FILA` ou `ATENDIDO` |
| prioridade | INTEGER | Ordem na fila |
| numero_senha | INTEGER | Senha do dia (nullable) |
| data_cadastro | DATE | Data de cadastro |

## Tabela: entregas

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT (PK) | Identificador |
| familia_id | BIGINT (FK) | Referência à família |
| senha | INTEGER | Senha utilizada na entrega |
| data_entrega | TIMESTAMP | Data/hora da entrega |
