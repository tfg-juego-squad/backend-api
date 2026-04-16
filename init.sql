-- 1. Tabla de Roles
CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Tabla de Aulas
CREATE TABLE aulas (
                       id VARCHAR(36) PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL,
                       codigo_invitacion VARCHAR(10) UNIQUE,
                       profesor_id VARCHAR(36) NOT NULL
);

-- 3. Tabla de Usuarios
CREATE TABLE usuarios (
                          id VARCHAR(36) PRIMARY KEY,
                          nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
                          hash_contrasena VARCHAR(255) NOT NULL,
                          fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          aula_id VARCHAR(36) NULL
);

-- 4. Tabla Intermedia Usuarios-Roles
CREATE TABLE usuarios_roles (
                                usuario_id VARCHAR(36) NOT NULL,
                                rol_id INT NOT NULL,
                                PRIMARY KEY (usuario_id, rol_id)
);

-- 5. Tabla de Pruebas
CREATE TABLE pruebas (
                         id VARCHAR(36) PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         descripcion TEXT,
                         puntuacion_maxima INT NOT NULL
);

-- 6. Tabla de Puntuaciones
CREATE TABLE puntuaciones (
                              id VARCHAR(36) PRIMARY KEY,
                              usuario_id VARCHAR(36) NOT NULL,
                              prueba_id VARCHAR(36) NOT NULL,
                              puntos_obtenidos INT NOT NULL,
                              fecha_completado TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Tabla de Inventario
CREATE TABLE inventario (
                            id VARCHAR(36) PRIMARY KEY,
                            usuario_id VARCHAR(36) NOT NULL,
                            nombre_objeto VARCHAR(100) NOT NULL,
                            fecha_adquisicion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Relaciones de Aulas y Usuarios (Referencia Circular Resuelta)
ALTER TABLE aulas
    ADD CONSTRAINT fk_aulas_profesor FOREIGN KEY (profesor_id) REFERENCES usuarios(id) ON DELETE CASCADE;

ALTER TABLE usuarios
    ADD CONSTRAINT fk_usuarios_aula FOREIGN KEY (aula_id) REFERENCES aulas(id) ON DELETE SET NULL;

-- Relaciones de la tabla intermedia Usuarios-Roles
ALTER TABLE usuarios_roles
    ADD CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_ur_rol FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE;

-- Relaciones de Puntuaciones
ALTER TABLE puntuaciones
    ADD CONSTRAINT fk_puntuaciones_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_puntuaciones_prueba FOREIGN KEY (prueba_id) REFERENCES pruebas(id) ON DELETE CASCADE;

-- Relaciones de Inventario
ALTER TABLE inventario
    ADD CONSTRAINT fk_inventario_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE;

-- Insertamos los roles por defecto ignorando si ya existen (por seguridad)
INSERT IGNORE INTO roles (nombre) VALUES
                                      ('ROL_PROFESOR'),
                                      ('ROL_ESTUDIANTE');