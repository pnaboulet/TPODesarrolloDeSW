-- Asociar barrio al personal (personas de tipo SEGURIDAD, MANTENIMIENTO, PROVEEDOR)
ALTER TABLE personas ADD COLUMN barrio_id BIGINT REFERENCES barrios(id);
