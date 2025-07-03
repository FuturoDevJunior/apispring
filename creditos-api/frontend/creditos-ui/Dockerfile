# ----- stage 1: build Angular -----
FROM node:20.19-alpine AS build
WORKDIR /app

# caching de dependências
COPY package*.json ./
# ① pula scripts pós-install (husky) ② omite devDeps
RUN npm ci --omit=dev --ignore-scripts

# copia código, compila produção
COPY . .
RUN npx ng build --configuration production

# Estágio 2 - Servidor Nginx para servir o aplicativo
FROM nginx:alpine

COPY --from=build /app/dist/creditos-ui /usr/share/nginx/html

# Copiar configuração customizada para Nginx
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
