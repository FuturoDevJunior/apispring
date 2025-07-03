# Sistema de Consulta de Créditos

## 📋 Sobre o Projeto

Este projeto implementa uma API RESTful para consulta de créditos constituídos utilizando Spring Boot 3.5.3 com Java 21, PostgreSQL 17.5, Angular 20, Docker e Kafka para mensageria.

### 🎯 Funcionalidades

- **API REST** para consulta de créditos por número da NFS-e ou número do crédito
- **Frontend Angular** responsivo com Material Design
- **Mensageria Kafka** para auditoria de consultas
- **Testes automatizados** com JUnit 5 e Mockito
- **Containerização** completa com Docker
- **Documentação OpenAPI** (Swagger)
- **Observabilidade** com Spring Boot Actuator

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** (OpenJDK 21.0.3 LTS)
- **Spring Boot 3.5.3**
- **Spring Data JPA 3.5.x**
- **Hibernate 6.6.x**
- **PostgreSQL 17.5**
- **Apache Kafka 3.7.0**
- **JUnit 5.13.3**
- **Mockito 5.18.0**
- **Testcontainers 1.20.4**

### Frontend
- **Angular 20.0.6**
- **Angular Material 20**
- **TypeScript 5.8**
- **RxJS 7.8**

### DevOps
- **Docker Engine 28.x**
- **Docker Compose v2**
- **Maven 3.9.7**
- **Node.js 20 LTS**

## 🚀 Como Executar

### Pré-requisitos

```bash
# Verificar versões instaladas
java --version    # Java 21+
mvn --version     # Maven 3.9+
docker --version  # Docker 28+
node --version    # Node 20+
ng version        # Angular CLI 20+
```

### 📦 Execução Rápida (Somente Banco de Dados)

```bash
# 1. Clone o repositório
git clone <repository-url>
cd testetecnico

# 2. Inicie o banco PostgreSQL
cd creditos-api/infra
docker compose -f docker-compose.db.yml up -d

# 3. Crie as tabelas e dados
cat initdb/01-schema.sql | docker compose -f docker-compose.db.yml exec -T db psql -U postgres -d creditos
cat initdb/02-seed.sql | docker compose -f docker-compose.db.yml exec -T db psql -U postgres -d creditos

# 4. Execute o backend
cd ../backend/creditos-api
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 5. Execute o frontend (novo terminal)
cd ../../frontend/creditos-ui
npm install
ng serve
```

**URLs de Acesso:**
- **Frontend Angular**: http://localhost:4200
- **API Swagger**: http://localhost:8081/swagger-ui.html
- **API Health**: http://localhost:8081/actuator/health

## 📊 Endpoints da API

### GET /api/creditos/{numeroNfse}
Retorna lista de créditos por número da NFS-e.

**Exemplo:**
```bash
curl http://localhost:8081/api/creditos/7891011
```

**Resposta:**
```json
[
  {
    "numeroCredito": "123456",
    "numeroNfse": "7891011",
    "dataConstituicao": "2024-02-25",
    "valorIssqn": 1500.75,
    "tipoCredito": "ISSQN",
    "simplesNacional": true,
    "aliquota": 5.0,
    "valorFaturado": 30000.00,
    "valorDeducao": 5000.00,
    "baseCalculo": 25000.00
  }
]
```

### GET /api/creditos/credito/{numeroCredito}
Retorna detalhes de um crédito específico.

**Exemplo:**
```bash
curl http://localhost:8081/api/creditos/credito/123456
```

## 🧪 Executar Testes

```bash
# Backend - Testes unitários e integração
cd creditos-api/backend/creditos-api
./mvnw test

# Relatório de cobertura (JaCoCo)
./mvnw jacoco:report
open target/site/jacoco/index.html

# Frontend - Build de produção
cd creditos-api/frontend/creditos-ui
ng build --configuration production
```

## 📁 Estrutura do Projeto

```
testetecnico/
├── creditos-api/
│   ├── backend/
│   │   └── creditos-api/           # Spring Boot Application
│   │       ├── src/main/java/
│   │       │   └── br/com/exemplo/
│   │       │       ├── controller/ # REST Controllers
│   │       │       ├── service/    # Business Logic
│   │       │       ├── repository/ # Data Access
│   │       │       ├── entity/     # JPA Entities
│   │       │       ├── dto/        # Data Transfer Objects
│   │       │       └── messaging/  # Kafka Publishers
│   │       └── src/test/java/      # Unit & Integration Tests
│   ├── frontend/
│   │   └── creditos-ui/            # Angular Application
│   │       ├── src/app/
│   │       │   ├── components/     # Angular Components
│   │       │   ├── services/       # HTTP Services
│   │       │   └── models/         # TypeScript Models
│   │       └── Dockerfile
│   └── infra/
│       ├── docker-compose.yml      # Orquestração completa
│       ├── docker-compose.db.yml   # Apenas banco para dev
│       └── initdb/                 # Scripts SQL
└── README.md
```

## 🏗️ Arquitetura

### Padrões de Projeto Implementados

- **MVC** (Model-View-Controller): Separação clara entre camadas
- **Repository**: Abstração do acesso a dados
- **Factory**: Para criação de DTOs e mapeamentos
- **Singleton**: Services como beans Spring
- **Observer**: Para mensageria Kafka

### Dados de Teste

O sistema possui 6 registros de exemplo:
- **NFS-e 7891011**: 2 créditos (123456, 789012)
- **NFS-e 1122334**: 2 créditos (654321, 555666)
- **NFS-e 5566778**: 1 crédito (111222)
- **NFS-e 9988776**: 1 crédito (333444)

## 🧰 Comandos Úteis

```bash
# Parar banco de dados
docker compose -f docker-compose.db.yml down

# Remover volumes (reset completo)
docker compose -f docker-compose.db.yml down -v

# Verificar dados no banco
docker compose -f docker-compose.db.yml exec db psql -U postgres -d creditos -c "SELECT * FROM credito;"

# Verificar status dos serviços
docker compose -f docker-compose.db.yml ps
```

## 📈 Funcionalidades Implementadas

✅ **API REST com Spring Boot 3.5.3**  
✅ **Banco PostgreSQL 17.5 com dados de teste**  
✅ **Frontend Angular 20 com Material Design**  
✅ **Testes automatizados (JUnit + Mockito)**  
✅ **Containerização com Docker**  
✅ **Documentação OpenAPI (Swagger)**  
✅ **Mensageria Kafka configurada**  
✅ **Padrões de projeto (MVC, Repository, Singleton)**  
✅ **Responsive design para mobile**  
✅ **Logs estruturados e observabilidade**  

## 🚦 Status do Projeto

- ✅ Backend API funcionando
- ✅ Frontend Angular funcionando  
- ✅ Banco de dados configurado
- ✅ Testes passando (14/14)
- ✅ Build de produção funcionando
- ✅ Documentação completa

## 📞 Suporte

Para dúvidas ou problemas:

1. Verifique se o Docker está rodando
2. Confirme que as portas 5432, 8081 e 4200 estão livres
3. Verifique os logs: `docker compose logs`
4. Teste os endpoints via Swagger: http://localhost:8081/swagger-ui.html

---

**Desenvolvido como parte do desafio técnico para desenvolvimento de API de consulta de créditos.**
