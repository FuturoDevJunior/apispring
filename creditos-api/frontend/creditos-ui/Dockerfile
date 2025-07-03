# -------- Estágio 1: build Angular ----------
FROM node:20.19-alpine AS build

# Desliga hooks Husky e Yarn
ENV HUSKY=0 CI=true

WORKDIR /app
COPY package*.json ./

# 1. Instala TODAS as deps (dev + prod). Isso garante o Angular CLI
RUN npm ci --no-progress

# 2. Copia código-fonte e gera build de produção
COPY . .
RUN npx ng build --configuration production --output-path=dist

# -------- Estágio 2: runtime Nginx ----------
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Copiar configuração customizada para Nginx
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
HEALTHCHECK CMD wget -qO- http://localhost | grep -q "</html>"
CMD ["nginx", "-g", "daemon off;"]
