# Perfis de Acesso e Rotas

## Perfis

| Perfil | Role Spring | Origem |
|--------|-------------|--------|
| Administrador | `ADMIN` | `HORTIFRUTI_ADMIN_*` em `env.local` |
| Atendente | `ATENDENTE` | Tabela `atendentes` |
| Família | `FAMILIA` | Tabela `usuarios` (CPF) |

Após login bem-sucedido:

- **Família** → redireciona para `/meu-cadastro`
- **Admin / Atendente** → redireciona para `/dashboard`

## Rotas públicas (sem login)

| Rota | Descrição |
|------|-----------|
| `/login` | Tela de login |
| `/cadastro` | Formulário de cadastro de família |
| `/cadastro/revisar` | Revisão antes de finalizar |
| `/recuperar-senha` | Recuperação de senha |
| `/fila/painel` | Painel público de senhas |
| `/erro/403` | Página de acesso negado |

## Rotas por perfil

### Família

| Rota | Descrição |
|------|-----------|
| `/meu-cadastro` | Visualizar dados do cadastro |

### Admin e Atendente

| Rota | Descrição |
|------|-----------|
| `/dashboard` | Painel inicial |
| `/fila` | Fila de atendimento |
| `/fila/baixa` | Baixa por senha |
| `/entregas` | Histórico de entregas |

### Somente Admin

| Rota | Descrição |
|------|-----------|
| `/familias` | Listar e gerenciar famílias |
| `/familias/novo` | Nova família |
| `/familias/editar/{id}` | Editar família |
| `/atendentes` | Listar atendentes |
| `/atendentes/novo` | Novo atendente |
| `/atendentes/editar/{id}` | Editar / alterar senha |
| `POST /fila/reiniciar` | Reiniciar fila do dia |

## Menu lateral (AdminLTE)

O menu exibe itens conforme o perfil logado:

- **Família:** Meu Cadastro
- **Admin / Atendente:** Dashboard, Fila, Painel, Baixa, Entregas
- **Admin:** Famílias, Atendentes
