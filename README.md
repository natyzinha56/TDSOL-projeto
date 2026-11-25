# ReFood — Transformando desperdício em solidariedade

MVP acadêmico de uma plataforma web para conectar estabelecimentos doadores a instituições sociais.

## Tecnologias
- Front-end: HTML, CSS e JavaScript
- Back-end: Java 17 + Spring Boot
- Banco: MySQL
- Documentação da API: Swagger UI / OpenAPI

## Como executar
1. Instale Java 17+, Maven e MySQL.
2. Crie o banco (ou deixe `createDatabaseIfNotExist=true` criar):
   `CREATE DATABASE refood_db;`
3. Ajuste `src/main/resources/application.properties` ou defina as variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD`.
4. Na pasta do projeto, execute: `mvn spring-boot:run`
5. Abra `http://localhost:8080` para acessar o site.
6. Abra `http://localhost:8080/swagger-ui/index.html` para testar a API.

## Endpoints principais
- `GET/POST /api/usuarios`
- `GET/POST /api/alimentos`
- `GET /api/alimentos?status=DISPONIVEL`
- `PATCH /api/alimentos/{id}/status?status=DOADO`
- `GET/POST /api/solicitacoes`
- `PATCH /api/solicitacoes/{id}/status?status=CONCLUIDA`
- `GET /api/painel/indicadores`

## Fluxo de demonstração
1. Cadastre um usuário com `tipo=DOADOR` em `/api/usuarios`.
2. Cadastre um usuário com `tipo=INSTITUICAO`.
3. Cadastre um alimento informando `doador: { "id": ID_DO_DOADOR }`.
4. Crie uma solicitação informando `alimento: { "id": ID_ALIMENTO }`, `instituicao: { "id": ID_INSTITUICAO }` e `quantidadeSolicitada`.
5. Atualize a solicitação para `ACEITA` e depois `CONCLUIDA`.

## Observações importantes
- Esta é uma primeira versão acadêmica/MVP para demonstração, não um sistema pronto para uso público.
- A autenticação real, autorização por perfil, recuperação de senha, upload de imagens, notificações e trilha de auditoria ainda precisam ser implementados antes de produção.
- A plataforma não certifica a segurança dos alimentos. Os responsáveis devem verificar validade, conservação, transporte, rastreabilidade e requisitos sanitários aplicáveis. Não cadastrar ou distribuir alimentos impróprios para consumo.
- O endpoint de criação impede validade vencida, mas a equipe deve validar também as condições reais de armazenamento e transporte.
