#!/usr/bin/env bash

echo "Verificando versões das ferramentas..."

# Java
java -version | grep "openjdk version" || echo "Java não instalado"

# Maven
mvn -v | grep "Apache Maven 3.9.7" || echo "Maven 3.9.7 não instalado corretamente"

# Node.js
node -v | grep "v20" || echo "Node.js 20 não instalado corretamente"

# Angular CLI
ng version | grep "Angular CLI: 20.0.6" || echo "Angular CLI 20.0.6 não instalado corretamente"

# PostgreSQL
psql --version | grep "psql (PostgreSQL) 17.5" || echo "PostgreSQL 17.5 não instalado corretamente"

echo "Verificação concluída."

