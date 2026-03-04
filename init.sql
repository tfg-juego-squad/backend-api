-- 1. Tabla de Usuarios
CREATE TABLE usuarios (
                          id VARCHAR(36) PRIMARY KEY,
                          nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
                          hash_contrasena VARCHAR(255) NOT NULL,
                          rol VARCHAR(20) DEFAULT 'ALUMNO',
                          fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de Pruebas
CREATE TABLE pruebas (
                         id VARCHAR(36) PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         descripcion TEXT,
                         puntuacion_maxima INT NOT NULL
);

-- 3. Tabla de Puntuaciones
CREATE TABLE puntuaciones (
                              id VARCHAR(36) PRIMARY KEY,
                              usuario_id VARCHAR(36) NOT NULL,
                              prueba_id VARCHAR(36) NOT NULL,
                              puntos_obtenidos INT NOT NULL,
                              fecha_completado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_puntuaciones_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                              CONSTRAINT fk_puntuaciones_prueba FOREIGN KEY (prueba_id) REFERENCES pruebas(id) ON DELETE CASCADE
);

-- 4. Tabla de Inventario
CREATE TABLE inventario (
                            id VARCHAR(36) PRIMARY KEY,
                            usuario_id VARCHAR(36) NOT NULL,
                            nombre_objeto VARCHAR(100) NOT NULL,
                            fecha_adquisicion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_inventario_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);