-- Agregar campos de habilitación para personas y unidades funcionales
ALTER TABLE personas ADD COLUMN habilitado BOOLEAN DEFAULT TRUE NOT NULL;
ALTER TABLE unidades_funcionales ADD COLUMN habilitada BOOLEAN DEFAULT TRUE NOT NULL;
