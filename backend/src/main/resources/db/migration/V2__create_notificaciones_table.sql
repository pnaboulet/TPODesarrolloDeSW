CREATE TABLE notificaciones (
    id BIGSERIAL PRIMARY KEY,
    mensaje TEXT NOT NULL,
    destinatario_email VARCHAR(100),
    fecha_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leida BOOLEAN DEFAULT FALSE
);
