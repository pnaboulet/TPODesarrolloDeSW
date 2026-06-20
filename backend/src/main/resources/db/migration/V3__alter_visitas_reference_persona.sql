-- Alter foreign key constraint on visitas table to reference personas(id) instead of visitantes(id)
-- This allows logging access (visitas) for residents, providers, security staff, and visitors.
ALTER TABLE visitas DROP CONSTRAINT IF EXISTS fk_visitas_visitante;
ALTER TABLE visitas DROP CONSTRAINT IF EXISTS visitas_visitante_id_fkey;
ALTER TABLE visitas ADD CONSTRAINT fk_visitas_persona FOREIGN KEY (visitante_id) REFERENCES personas(id) ON DELETE CASCADE;
