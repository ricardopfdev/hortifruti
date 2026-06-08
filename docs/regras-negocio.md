# Regras de Negócio

## Famílias e CPF

| ID | Regra |
|----|-------|
| RN01 | CPF não pode ser duplicado |
| RN02 | Família deve possuir pelo menos 1 morador |
| RN03 | Somente famílias ativas participam do fluxo de atendimento |
| RN04 | CPF deve ter 11 dígitos válidos (com dígitos verificadores) |
| RN05 | Endereço de cadastro exige rua, número, bairro e cidade |
| RN06 | Recuperação de senha valida CPF **e** endereço (rua, número, bairro, cidade) |

## Senhas

| ID | Regra |
|----|-------|
| RN07 | Senha de acesso da família: **5 caracteres** — 1 letra + 4 números, em posição aleatória |
| RN08 | Senha da família é normalizada para maiúsculas no login (ex.: `k1234` → `K1234`) |
| RN09 | Senha de admin e atendentes: texto livre (mín. 6 caracteres se definida manualmente) |
| RN10 | Senha gerada para atendente novo: 8 caracteres alfanuméricos |
| RN11 | Somente o **administrador** pode alterar senha de atendentes |
| RN12 | Administrador é configurado via `env.local` / properties — não é editável pela interface |

## Atendentes

| ID | Regra |
|----|-------|
| RN13 | Usuário de login do atendente: 3–50 caracteres (`a-z`, `0-9`, `.`, `-`, `_`) |
| RN14 | Usuário de login não pode ser alterado após o cadastro |
| RN15 | Usuário reservado `admin` não pode ser usado por atendentes |
| RN16 | Atendente padrão (`atendente` / `atendente123`) é criado na primeira execução se não existir |

## Fila

| ID | Regra |
|----|-------|
| RN17 | Família nova entra automaticamente na fila com prioridade |
| RN18 | Senha do dia (fila) é numérica, exibida com 3 dígitos (ex.: `007`) |
