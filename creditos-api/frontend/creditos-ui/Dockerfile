# Dockerfile para o Frontend Angular

# Estágio 1 - Build
FROM node:20.19.1-alpine AS build

WORKDIR /app

# Evita scripts Husky que dependem de .git
ENV HUSKY=0

# Dependências primeiro para cache
COPY package*.json ./
RUN npm ci --omit=dev

# Copiar código fonte
COPY . .

# Build de produção
RUN npx -y @angular/cli@20.0.5 build --configuration production

# Estágio 2 - Servidor Nginx para servir o aplicativo
FROM nginx:alpine

COPY --from=build /app/dist/creditos-ui /usr/share/nginx/html

# Copiar configuração customizada para Nginx
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
