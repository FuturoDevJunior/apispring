# -------- Estágio 1: Build Angular -----------
FROM node:20.19-alpine AS build
WORKDIR /app

# Copiar somente package.json para cache eficiente
COPY package*.json ./

# Instalar dependências **sem** scripts pós-install (husky)
ARG CI=true
ENV HUSKY=0
RUN npm ci --omit=dev --ignore-scripts

# Copiar código e gerar build de prod
COPY . .
RUN npx ng build --configuration=production

# -------- Estágio 2: nginx -----------
FROM nginx:alpine
COPY --from=build /app/dist/creditos-ui /usr/share/nginx/html

# Copiar configuração customizada para Nginx
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
