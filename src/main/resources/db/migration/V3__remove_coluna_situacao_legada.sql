-- Coluna legada criada por Hibernate antes do Flyway; a entidade usa status_fila e ativa.
ALTER TABLE familias DROP COLUMN IF EXISTS situacao;
