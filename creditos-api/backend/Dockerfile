# Dockerfile multi-stage para API Spring Boot
# Stage 1: Build da aplicação
FROM maven:3.9.7-eclipse-temurin-21 AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos do projeto
COPY creditos-api/pom.xml ./
COPY creditos-api/src ./src

# Fazer download das dependências (camada separada para cache)
RUN mvn dependency:go-offline -B

# Compilar e gerar o JAR
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

# Definir diretório de trabalho
WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -g 1001 appgroup && \
    adduser -u 1001 -G appgroup -s /bin/sh -D appuser

# Copiar o JAR da aplicação do stage anterior
COPY --from=build /app/target/creditos-api-*.jar /app/app.jar

# Definir proprietário do arquivo
RUN chown appuser:appgroup /app/app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta da aplicação
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
