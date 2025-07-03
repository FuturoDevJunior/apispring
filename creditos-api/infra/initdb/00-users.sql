-- Script de criação e configuração do usuário dev
CREATE USER dev WITH PASSWORD 'dev';
-- Alterando owner da database existente ao invés de criar nova
ALTER DATABASE creditos OWNER TO dev;
GRANT ALL ON SCHEMA public TO dev;
ALTER SCHEMA public OWNER TO dev;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dev;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO dev;
