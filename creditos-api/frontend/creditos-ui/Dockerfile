# Dockerfile para o Frontend Angular

# Estágio 1 - Build
FROM node:20.14.0-alpine AS build

WORKDIR /app

# Copiar arquivos necessários e instalar dependências
COPY package*.json ./
RUN npm install -g @angular/cli@20.0.6 && npm install

# Copiar todo o código da aplicação e criar build de produção
COPY . .
RUN ng build --configuration production

# Estágio 2 - Servidor Nginx para servir o aplicativo
FROM nginx:alpine

COPY --from=build /app/dist/creditos-ui /usr/share/nginx/html

# Copiar configuração customizada para Nginx
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
