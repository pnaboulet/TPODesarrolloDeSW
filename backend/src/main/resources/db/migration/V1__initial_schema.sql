-- Estructura de Tablas Generales
CREATE TABLE barrios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL
);

CREATE TABLE unidades_funcionales (
    id SERIAL PRIMARY KEY,
    barrio_id INT REFERENCES barrios(id),
    identificador VARCHAR(20) NOT NULL,
    tipo_unidad VARCHAR(20) NOT NULL
);

-- Tabla Padre para Herencia de Personas
CREATE TABLE personas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    tipo_persona VARCHAR(30) NOT NULL
);

-- Tablas Hijas para Herencia JOINED
CREATE TABLE residentes (
    id INT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE,
    unidad_funcional_id INT REFERENCES unidades_funcionales(id)
);

CREATE TABLE proveedores (
    id INT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE,
    tipo_servicio VARCHAR(50) NOT NULL
);

CREATE TABLE personal_seguridad (
    id INT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

CREATE TABLE personal_mantenimiento (
    id INT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

CREATE TABLE visitantes (
    id INT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

CREATE TABLE administradores (
    id INT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE
);

-- Tabla de Autorizaciones de Ingreso (creadas por Residentes)
CREATE TABLE autorizaciones_ingreso (
    id SERIAL PRIMARY KEY,
    residente_id INT REFERENCES residentes(id),
    visitante_id INT REFERENCES visitantes(id),
    fecha_desde TIMESTAMP NOT NULL,
    fecha_hasta TIMESTAMP NOT NULL,
    utilizada BOOLEAN DEFAULT FALSE
);

-- Tabla de Visitas / Log de Entrada y Salida
CREATE TABLE visitas (
    id SERIAL PRIMARY KEY,
    visitante_id INT REFERENCES visitantes(id),
    autorizacion_id INT REFERENCES autorizaciones_ingreso(id),
    fecha_ingreso TIMESTAMP NOT NULL,
    fecha_salida TIMESTAMP,
    estado VARCHAR(20) NOT NULL,
    registrado_por_seguridad_id INT REFERENCES personal_seguridad(id)
);

-- Tabla de Reclamos / Incidentes
CREATE TABLE reclamos (
    id SERIAL PRIMARY KEY,
    residente_id INT REFERENCES residentes(id),
    tipo_reclamo VARCHAR(30) NOT NULL,
    descripcion TEXT NOT NULL,
    prioridad VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responsable_id INT REFERENCES personas(id)
);

-- Tabla de Tareas de Mantenimiento derivadas de un Reclamo o creadas por Admin
CREATE TABLE tareas_mantenimiento (
    id SERIAL PRIMARY KEY,
    reclamo_id INT REFERENCES reclamos(id) ON DELETE SET NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    responsable_id INT REFERENCES personas(id)
);

-- Historial de Estado de Reclamos
CREATE TABLE historial_estado_reclamos (
    id SERIAL PRIMARY KEY,
    reclamo_id INT REFERENCES reclamos(id) ON DELETE CASCADE,
    estado_anterior VARCHAR(20),
    estado_nuevo VARCHAR(20) NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion TEXT
);
