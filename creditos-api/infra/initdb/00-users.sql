-- Script de configuração para usuário dev (já é o usuário principal)
-- Garantir que o usuário dev tem todas as permissões necessárias
GRANT ALL ON SCHEMA public TO dev;
ALTER SCHEMA public OWNER TO dev;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dev;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO dev;
