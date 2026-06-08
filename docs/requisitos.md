# Requisitos Funcionais

## Famílias

| ID | Requisito |
|----|-----------|
| RF01 | Cadastrar famílias (admin ou cadastro público) |
| RF02 | Editar famílias |
| RF03 | Excluir famílias |
| RF04 | Registrar entregas de alimentos |
| RF05 | Cadastro público com endereço (rua, número, bairro, cidade) |
| RF06 | Revisão dos dados antes de finalizar o cadastro público |
| RF07 | Gerar senha de acesso (5 caracteres) e senha da fila ao cadastrar |
| RF08 | Família consultar próprio cadastro após login (`/meu-cadastro`) |
| RF09 | Recuperar senha informando CPF e endereço cadastrado |

## Fila e entregas

| ID | Requisito |
|----|-----------|
| RF10 | Controlar fila de atendimento (prioridade, status) |
| RF11 | Gerar senha numérica do dia para família na fila |
| RF12 | Marcar família como atendida |
| RF13 | Painel público exibindo senhas do dia |
| RF14 | Baixa de atendimento informando número da senha |
| RF15 | Reiniciar fila (somente administrador) |
| RF16 | Listar histórico de entregas |

## Sistema e usuários

| ID | Requisito |
|----|-----------|
| RF17 | Exibir dashboard com resumo |
| RF18 | Autenticar administrador, atendentes e famílias |
| RF19 | Administrador cadastrar, editar e excluir atendentes |
| RF20 | Administrador alterar senha dos atendentes |
| RF21 | Exibir página de erro 403 com ações conforme o perfil logado |
