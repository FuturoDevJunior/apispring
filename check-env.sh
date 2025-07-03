#!/bin/bash

echo "🔍 VERIFICAÇÃO DO AMBIENTE DE DESENVOLVIMENTO"
echo "=============================================="

# Java
echo -n "Java: "
java_version=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}')
if [[ $java_version == 21.* ]]; then
    echo "✅ $java_version"
else
    echo "❌ $java_version (esperado: 21.x)"
fi

# Maven
echo -n "Maven: "
mvn_version=$(mvn -version 2>/dev/null | head -n 1 | awk '{print $3}')
if [[ $mvn_version == 3.9.* ]]; then
    echo "✅ $mvn_version"
else
    echo "❌ $mvn_version (esperado: 3.9.x)"
fi

# Node.js
echo -n "Node.js: "
node_version=$(node --version 2>/dev/null | sed 's/v//')
if [[ $node_version == 20.* ]]; then
    echo "✅ $node_version"
else
    echo "❌ $node_version (esperado: 20.x)"
fi

# Angular CLI
echo -n "Angular CLI: "
ng_version=$(ng version 2>/dev/null | grep "Angular CLI" | awk '{print $3}' || echo "não instalado")
if [[ $ng_version == 20.* ]]; then
    echo "✅ $ng_version"
else
    echo "❌ $ng_version (esperado: 20.x)"
fi

# Docker
echo -n "Docker: "
docker_version=$(docker --version 2>/dev/null | awk '{print $3}' | sed 's/,//')
if [[ $docker_version == 2* ]]; then
    echo "✅ $docker_version"
else
    echo "❌ $docker_version (esperado: 2x.x)"
fi

# Docker Compose
echo -n "Docker Compose: "
compose_version=$(docker compose version 2>/dev/null | awk '{print $4}' | sed 's/v//')
if [[ $compose_version == 2.* ]]; then
    echo "✅ $compose_version"
else
    echo "❌ $compose_version (esperado: 2.x)"
fi

echo ""
echo "🎯 RESUMO:"
echo "- Todas as ferramentas devem estar na versão correta"
echo "- Execute 'brew install' ou 'apt-get install' para atualizações necessárias"
