-- ==========================================
-- SCRIPT DE INICIALIZACIÓN: DHL ECO-POINTS
-- Base de Datos: PostgreSQL
-- ==========================================

-- 1. Crear Tabla de Usuarios (Clientes finales)
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    saldo_ecopuntos INT DEFAULT 0
);

-- 2. Crear Tabla de Puntos de Recogida (Locales y Lockers)
CREATE TABLE IF NOT EXISTS puntos_recogida (
    id_punto VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    zona_postal VARCHAR(10) NOT NULL,
    distancia_media_km DECIMAL(5,2),
    es_locker_24h BOOLEAN DEFAULT FALSE,
    capacidad_maxima INT NOT NULL,
    ocupacion_actual INT DEFAULT 0
);

-- 3. Crear Tabla de Rutas Futuras 
CREATE TABLE IF NOT EXISTS rutas (
    id_ruta VARCHAR(50) PRIMARY KEY,
    fecha DATE NOT NULL,
    zona_postal VARCHAR(10) NOT NULL,
    id_punto VARCHAR(20) REFERENCES puntos_recogida(id_punto), -- NULL si es a domicilio
    paquetes_actuales INT DEFAULT 0,
    capacidad_maxima INT NOT NULL,
    costo_base_euros DECIMAL(8,2) NOT NULL,
    emisiones_base_co2 DECIMAL(8,2) NOT NULL
);

-- 4. Crear Tabla de Pedidos (Los paquetes de los clientes)
CREATE TABLE IF NOT EXISTS pedidos (
    id_pedido VARCHAR(50) PRIMARY KEY,
    id_usuario UUID REFERENCES usuarios(id_usuario),
    id_ruta VARCHAR(50) REFERENCES rutas(id_ruta),
    peso_kg DECIMAL(5,2),
    es_envio_eco BOOLEAN DEFAULT FALSE,
    puntos_otorgados INT DEFAULT 0
);

-- ==========================================
-- INSERCIÓN DE DATOS DE MUESTRA (MOCK DATA)
-- ==========================================

-- Insertar a nuestro usuario de prueba (Carlos Martínez, con saldo de 1450 EcoPuntos)
INSERT INTO usuarios (id_usuario, nombre, email, saldo_ecopuntos) 
VALUES ('11111111-1111-1111-1111-111111111111', 'Carlos Martínez', 'carlos@demo.com', 1450)
ON CONFLICT (email) DO NOTHING;

-- Insertar los Puntos de Recogida de la zona 28931 (Nombres sincronizados con el frontend)
INSERT INTO puntos_recogida (id_punto, nombre, zona_postal, distancia_media_km, es_locker_24h, capacidad_maxima, ocupacion_actual) VALUES
('P1', 'Punto Alternativo 1', '28931', 1.8, FALSE, 80, 30),
('P2', 'Punto Alternativo 2', '28931', 2.9, FALSE, 40, 38), 
('P3', 'Punto Alternativo 3', '28931', 4.1, TRUE, 50, 10);

-- Insertar Rutas futuras para la zona 28931 (Escenario base + proyecciones de 7 días)
INSERT INTO rutas (id_ruta, fecha, zona_postal, id_punto, paquetes_actuales, capacidad_maxima, costo_base_euros, emisiones_base_co2) VALUES
('RUTA-DOM-HOY', CURRENT_DATE, '28931', NULL, 15, 100, 120.00, 45.00),
('RUTA-P1-MANANA', CURRENT_DATE + INTERVAL '1 day', '28931', 'P1', 31, 100, 120.00, 45.00),
('RUTA-P3-PASADO', CURRENT_DATE + INTERVAL '2 days', '28931', 'P3', 51, 100, 120.00, 45.00),
('RUTA-P1-D3', CURRENT_DATE + INTERVAL '3 days', '28931', 'P1', 25, 100, 120.00, 45.00),
('RUTA-P1-D5', CURRENT_DATE + INTERVAL '5 days', '28931', 'P1', 40, 100, 120.00, 45.00),
('RUTA-P2-D2', CURRENT_DATE + INTERVAL '2 days', '28931', 'P2', 10, 100, 120.00, 45.00),
('RUTA-P2-D4', CURRENT_DATE + INTERVAL '4 days', '28931', 'P2', 35, 100, 120.00, 45.00),
('RUTA-P2-D6', CURRENT_DATE + INTERVAL '6 days', '28931', 'P2', 15, 100, 120.00, 45.00),
('RUTA-P3-D4', CURRENT_DATE + INTERVAL '4 days', '28931', 'P3', 20, 100, 120.00, 45.00),
('RUTA-P3-D6', CURRENT_DATE + INTERVAL '6 days', '28931', 'P3', 60, 100, 120.00, 45.00);

-- Insertar un pedido de prueba asociado a Carlos M. y a la ruta de hoy a domicilio
INSERT INTO pedidos (id_pedido, id_usuario, id_ruta, peso_kg, es_envio_eco, puntos_otorgados) VALUES
('ENV-1234567890', '11111111-1111-1111-1111-111111111111', 'RUTA-DOM-HOY', 2.3, FALSE, 0);