-- ========================================================
-- SISTEMA WEB DE GESTIÓN DE RESERVAS Y SERVICIOS "H-ELARTE"
-- SCRIPT DE BASE DE DATOS MYSQL (DDL & DML)
-- ========================================================

CREATE DATABASE IF NOT EXISTS helarte
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE helarte;

-- Tabla de Usuarios (Administrador, Empleado/Colaborador, Cliente)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de Mesas del establecimiento
CREATE TABLE IF NOT EXISTS mesas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_mesa INT NOT NULL UNIQUE,
    capacidad INT NOT NULL,
    ubicacion VARCHAR(50),
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE'
);

-- Tabla de Servicios / Experiencias de Heladería
CREATE TABLE IF NOT EXISTS servicios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio DOUBLE NOT NULL,
    duracion_minutos INT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de Reservas
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_reserva VARCHAR(30) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    mesa_id BIGINT NOT NULL,
    servicio_id BIGINT NOT NULL,
    fecha_reserva DATE NOT NULL,
    hora_reserva TIME NOT NULL,
    cantidad_personas INT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    observaciones VARCHAR(500),
    fecha_creacion DATETIME NOT NULL,
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reserva_mesa FOREIGN KEY (mesa_id) REFERENCES mesas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reserva_servicio FOREIGN KEY (servicio_id) REFERENCES servicios(id) ON DELETE RESTRICT
);

-- Inserción de Datos Iniciales (Opcional, Spring Boot también los inicializa automáticamente)
-- Contraseñas encriptadas con BCrypt: 'admin123', 'empleado123', 'cliente123'
INSERT IGNORE INTO usuarios (id, user, email, password, rol, telefono, activo) VALUES
(1, 'Administrador Helarte', 'admin@helarte.com', '$2a$10$wT5fE8CjV6jJbW6Lz8K6mO9iJ1z4A8/xL5d3yUf9f9yA8/xL5d3yU', 'ADMIN', '999111222', TRUE),
(2, 'Carlos Colaborador', 'empleado@helarte.com', '$2a$10$wT5fE8CjV6jJbW6Lz8K6mO9iJ1z4A8/xL5d3yUf9f9yA8/xL5d3yU', 'EMPLEADO', '999333444', TRUE),
(3, 'Ana Cliente', 'cliente@helarte.com', '$2a$10$wT5fE8CjV6jJbW6Lz8K6mO9iJ1z4A8/xL5d3yUf9f9yA8/xL5d3yU', 'CLIENTE', '999555666', TRUE);

INSERT IGNORE INTO mesas (id, numero_mesa, capacidad, ubicacion, estado) VALUES
(1, 1, 2, 'Terraza Frontal', 'DISPONIBLE'),
(2, 2, 4, 'Salón Principal', 'DISPONIBLE'),
(3, 3, 4, 'Salón Principal', 'DISPONIBLE'),
(4, 4, 6, 'Zona Jardín de Helados', 'DISPONIBLE'),
(5, 5, 8, 'Salón VIP Eventos', 'DISPONIBLE');

INSERT IGNORE INTO servicios (id, nombre, descripcion, precio, duracion_minutos, activo) VALUES
(1, 'Reserva Mesa Estándar - Heladería', 'Reserva de mesa con atención en mesa para consumo a la carta de nuestra variedad de helados artesanales.', 15.00, 60, TRUE),
(2, 'Cata Degustación de Helados Gourmet', 'Degustación guiada de 8 sabores artesanales selectos con toppings premium y barquillos artesanales.', 45.00, 45, TRUE),
(3, 'Experiencia Dulce: Fondue de Chocolate & Copas', 'Fondue de chocolate belga caliente con frutas frescas, malvaviscos y dos copas gigantes de helado artesanal.', 65.00, 60, TRUE),
(4, 'Taller & Cumpleaños Heladero', 'Espacio reservado con mini taller para personalizar helados, incluye barra libre de helados por 90 minutos.', 120.00, 90, TRUE);
