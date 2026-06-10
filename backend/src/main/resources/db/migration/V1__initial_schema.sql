-- Estructura de Tablas Generales
CREATE TABLE barrios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL
);

CREATE TABLE unidades_funcionales (
    id BIGSERIAL PRIMARY KEY,
    barrio_id BIGINT REFERENCES barrios(id),
    identificador VARCHAR(20) NOT NULL,
    tipo_unidad VARCHAR(20) NOT NULL
);

-- Tabla Padre para Herencia de Personas
CREATE TABLE personas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    tipo_persona VARCHAR(30) NOT NULL
);

-- Tablas Hijas para Herencia JOINED
CREATE TABLE residentes (
    id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE,
    unidad_funcional_id BIGINT REFERENCES unidades_funcionales(id)
);

CREATE TABLE proveedores (
    id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE,
    tipo_servicio VARCHAR(50) NOT NULL
);

CREATE TABLE personal_seguridad (
    id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

CREATE TABLE personal_mantenimiento (
    id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

CREATE TABLE visitantes (
    id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

CREATE TABLE administradores (
    id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

-- Tabla de Autorizaciones de Ingreso (creadas por Residentes)
CREATE TABLE autorizaciones_ingreso (
    id BIGSERIAL PRIMARY KEY,
    residente_id BIGINT REFERENCES residentes(id),
    visitante_id BIGINT REFERENCES visitantes(id),
    fecha_desde TIMESTAMP NOT NULL,
    fecha_hasta TIMESTAMP NOT NULL,
    utilizada BOOLEAN DEFAULT FALSE
);

-- Tabla de Visitas / Log de Entrada y Salida
CREATE TABLE visitas (
    id BIGSERIAL PRIMARY KEY,
    visitante_id BIGINT REFERENCES visitantes(id),
    autorizacion_id BIGINT REFERENCES autorizaciones_ingreso(id),
    fecha_ingreso TIMESTAMP NOT NULL,
    fecha_salida TIMESTAMP,
    estado VARCHAR(20) NOT NULL,
    registrado_por_seguridad_id BIGINT REFERENCES personal_seguridad(id)
);

-- Tabla de Reclamos / Incidentes
CREATE TABLE reclamos (
    id BIGSERIAL PRIMARY KEY,
    residente_id BIGINT REFERENCES residentes(id),
    tipo_reclamo VARCHAR(30) NOT NULL,
    descripcion TEXT NOT NULL,
    prioridad VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responsable_id BIGINT REFERENCES personas(id)
);

-- Tabla de Tareas de Mantenimiento derivadas de un Reclamo o creadas por Admin
CREATE TABLE tareas_mantenimiento (
    id BIGSERIAL PRIMARY KEY,
    reclamo_id BIGINT REFERENCES reclamos(id) ON DELETE SET NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    responsable_id BIGINT REFERENCES personas(id)
);

-- Historial de Estado de Reclamos
CREATE TABLE historial_estado_reclamos (
    id BIGSERIAL PRIMARY KEY,
    reclamo_id BIGINT REFERENCES reclamos(id) ON DELETE CASCADE,
    estado_anterior VARCHAR(20),
    estado_nuevo VARCHAR(20) NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion TEXT
);
