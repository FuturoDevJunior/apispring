#!/bin/bash

set -e

echo "🚀 EXECUÇÃO DO PREFLIGHT CHECK"
echo "=============================="

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para logging
log_success() { echo -e "${GREEN}✅ $1${NC}"; }
log_warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }
log_error() { echo -e "${RED}❌ $1${NC}"; }

# 1. Verificar se Docker está rodando
echo "🐳 Verificando Docker..."
if ! docker info >/dev/null 2>&1; then
    log_error "Docker não está rodando"
    exit 1
fi
log_success "Docker está rodando"

# 2. Verificar se banco está disponível
echo "💾 Verificando banco de dados..."
cd creditos-api/infra
if ! docker compose -f docker-compose.db.yml ps | grep -q "healthy"; then
    log_warning "Iniciando banco de dados..."
    docker compose -f docker-compose.db.yml up -d
    sleep 15
fi

# Verificar se o banco tem dados
count=$(docker compose -f docker-compose.db.yml exec -T db psql -U postgres -d creditos -c 'SELECT count(*) FROM credito;' | grep -o '[0-9]*' | head -1)
if [[ $count -lt 3 ]]; then
    log_error "Banco deve ter pelo menos 3 registros, encontrados: $count"
    exit 1
fi
log_success "Banco PostgreSQL funcionando com $count registros"

# 3. Executar testes do backend
echo "🧪 Executando testes do backend..."
cd ../backend/creditos-api
if ! ./mvnw -q test; then
    log_error "Testes do backend falharam"
    exit 1
fi
log_success "Todos os testes do backend passaram"

# 4. Verificar cobertura de testes
if [[ ! -f target/site/jacoco/index.html ]]; then
    log_error "Relatório de cobertura não encontrado"
    exit 1
fi
log_success "Relatório de cobertura gerado"

# 5. Build do frontend
echo "🎨 Build do frontend Angular..."
cd ../../frontend/creditos-ui
if ! ng build --configuration production >/dev/null 2>&1; then
    log_error "Build do frontend falhou"
    exit 1
fi

# Verificar tamanho do bundle
size=$(du -h dist/creditos-ui/main-*.js | cut -f1 | sed 's/K//')
if [[ ${size%.*} -gt 500 ]]; then
    log_warning "Bundle muito grande: ${size}K (máximo recomendado: 500K)"
else
    log_success "Build do frontend concluído (${size}K)"
fi

# 6. Verificar estrutura de arquivos obrigatórios
echo "📁 Verificando arquivos obrigatórios..."
cd ../../../

required_files=(
    "README.md"
    "CHANGELOG.md" 
    "LICENSE"
    "check-env.sh"
    "creditos-api/backend/creditos-api/pom.xml"
    "creditos-api/frontend/creditos-ui/package.json"
    "creditos-api/infra/docker-compose.yml"
    "creditos-api/infra/docker-compose.db.yml"
)

for file in "${required_files[@]}"; do
    if [[ ! -f "$file" ]]; then
        log_error "Arquivo obrigatório não encontrado: $file"
        exit 1
    fi
done
log_success "Todos os arquivos obrigatórios presentes"

# 7. Verificar se não há arquivos desnecessários no Git
echo "📝 Verificando arquivos no Git..."
if git ls-files | grep -E '\.(class|jar|log|tmp)$|target/|node_modules/|\.DS_Store' >/dev/null; then
    log_warning "Arquivos desnecessários encontrados no Git:"
    git ls-files | grep -E '\.(class|jar|log|tmp)$|target/|node_modules/|\.DS_Store' | head -5
else
    log_success "Nenhum arquivo desnecessário no Git"
fi

# 8. Verificar commits recentes
echo "📝 Verificando histórico Git..."
if [[ $(git log --oneline | wc -l) -lt 5 ]]; then
    log_warning "Histórico de commits muito pequeno"
else
    log_success "Histórico de commits adequado"
fi

echo ""
echo "🎯 RESUMO DO PREFLIGHT CHECK"
echo "============================"
log_success "✅ Ambiente verificado"
log_success "✅ Banco de dados funcionando"
log_success "✅ Testes do backend passando"
log_success "✅ Frontend buildando corretamente"
log_success "✅ Arquivos obrigatórios presentes"
log_success "✅ Estrutura do projeto adequada"

echo ""
echo "🚀 PROJETO PRONTO PARA ENTREGA!"
echo "================================"
echo ""
echo "📋 Próximos passos:"
echo "1. Execute: git add . && git commit -m 'feat: projeto completo para avaliação'"
echo "2. Execute: git tag -a v1.0.0 -m 'Release v1.0.0'"
echo "3. Execute: git push origin main && git push origin v1.0.0"
echo ""
echo "🔗 URLs importantes:"
echo "- Frontend: http://localhost:4200 (ng serve)"
echo "- API: http://localhost:8081 (mvn spring-boot:run -Dspring-boot.run.profiles=dev)"
echo "- Swagger: http://localhost:8081/swagger-ui.html"
echo "- Health: http://localhost:8081/actuator/health"
echo ""

exit 0
