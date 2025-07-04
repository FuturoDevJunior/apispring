# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v1.0.0] - 2024-01-15

### 🎯 Sprint 6 - Finalização e Entrega
- **Observabilidade**: Actuator endpoints habilitados (/health, /metrics)
- **Documentação**: OpenAPI/Swagger UI disponível em /swagger-ui.html
- **README**: Documentação completa com instruções de instalação e uso
- **CHANGELOG**: Documentação de mudanças seguindo padrão Keep a Changelog
- **LICENSE**: Licença MIT adicionada
- **Release**: Tag v1.0.0 criada com release notes

### Fixed
- CI/CD pipeline Docker build paths corrected for frontend and backend
- Repository name normalization for GHCR OCI compliance
- Angular CLI command resolution with npx fallback
- GitHub Actions updated to latest stable versions
- Container registry permissions and authentication
- Bundle size validation and artifact upload

### 🧪 Sprint 5 - Containerização e CI/CD
- **Docker**: Containerização completa com multi-stage builds
- **Docker Compose**: Orquestração de serviços com healthchecks
- **Nginx**: Configuração otimizada para SPA Angular
- **Environment**: Profiles separados para desenvolvimento e produção

### 🎨 Sprint 4 - Frontend Angular 20
- **Angular Material**: Interface responsiva com Material Design
- **Formulário**: Busca por NFS-e ou número do crédito
- **Tabela**: Exibição responsiva dos resultados
- **Services**: HTTP client para consumo da API
- **Build**: Otimização para produção (137kB transferido)

### 🚀 Sprint 3 - Backend Spring Boot
- **API REST**: Endpoints GET /api/creditos/{numeroNfse} e /api/creditos/credito/{numeroCredito}
- **JPA/Hibernate**: Mapeamento objeto-relacional
- **Validation**: Validação de entrada com Bean Validation
- **Error Handling**: Tratamento global de exceções
- **CORS**: Configuração para integração com frontend

### 🧪 Sprint 2 - Testes Automatizados
- **Testes Unitários**: JUnit 5 + Mockito para Services e Controllers
- **Testes de Integração**: Testcontainers para testes com PostgreSQL real
- **Cobertura**: JaCoCo configurado com relatórios detalhados
- **CI/CD**: Pipeline automatizado no GitHub Actions

### 💾 Sprint 1 - Banco de Dados
- **PostgreSQL 17.5**: Banco de dados principal
- **Schema**: Tabela `credito` com índices otimizados
- **Seed Data**: 6 registros de exemplo para testes
- **Docker**: Containerização com scripts de inicialização

### 🔄 Sprint 0 - Mensageria Kafka
- **Kafka 3.7.0**: Broker para auditoria de consultas
- **Publisher**: Eventos enviados a cada consulta realizada
- **Topic**: `consulta-creditos` para logs de auditoria
- **Integration**: Integração assíncrona com Spring Kafka

## [Unreleased]

### 🚀 Próximas Funcionalidades
- Autenticação JWT
- Cache Redis
- Métricas avançadas com Micrometer
- Dashboard administrativo
- Exportação de relatórios (PDF/Excel)

## Tipos de Mudanças
- `Added` para novas funcionalidades
- `Changed` para mudanças em funcionalidades existentes
- `Deprecated` para funcionalidades que serão removidas
- `Removed` para funcionalidades removidas
- `Fixed` para correções de bugs
- `Security` para vulnerabilidades

## Versionamento

Este projeto segue o [Semantic Versioning](https://semver.org/):
- **MAJOR**: Mudanças incompatíveis na API
- **MINOR**: Novas funcionalidades compatíveis
- **PATCH**: Correções de bugs compatíveis
