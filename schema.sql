USE db_repuestos;

-- usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    rol ENUM('ADMIN', 'VENDEDOR') NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

INSERT IGNORE INTO usuarios (username, password, rol, activo) VALUES ('admin', '1234', 'ADMIN', TRUE);

-- categorias
CREATE TABLE IF NOT EXISTS categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

-- proveedores
CREATE TABLE IF NOT EXISTS proveedores (
    ruc VARCHAR(11) PRIMARY KEY,
    razon_social VARCHAR(200) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100)
);

-- productos
CREATE TABLE IF NOT EXISTS productos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    precio_costo DECIMAL(10, 2) NOT NULL,
    precio_venta DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 0,
    id_categoria INT,
    ruc_proveedor VARCHAR(11),
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id),
    FOREIGN KEY (ruc_proveedor) REFERENCES proveedores(ruc)
);

-- movimientos_stock
CREATE TABLE IF NOT EXISTS movimientos_stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT NULL,
    tipo ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    codigo_producto VARCHAR(20) NOT NULL,
    cantidad INT NOT NULL,
    usuario_responsable VARCHAR(50) NOT NULL,
    motivo VARCHAR(500),
    FOREIGN KEY (codigo_producto) REFERENCES productos(codigo),
    FOREIGN KEY (usuario_responsable) REFERENCES usuarios(username)
);

-- DATOS DE PRUEBA
INSERT IGNORE INTO categorias (nombre) VALUES ('Motor'), ('Frenos'), ('Suspension'), ('Filtros'), ('Encendido');

-- Proveedores
INSERT IGNORE INTO proveedores (ruc, razon_social, telefono, email) VALUES
('20100200301', 'Repuestos del Centro S.A.C.', '999888777', 'contacto@repuestos.com'),
('20123456789', 'AutoPartes Lima S.A.', '888777666', 'ventas@autopartes.pe');

-- Productos
INSERT IGNORE INTO productos (codigo, nombre, precio_costo, precio_venta, stock, stock_minimo, id_categoria, ruc_proveedor, activo) VALUES
('REP-001', 'Bujia Bosch', 10.00, 15.00, 50, 10, 5, '20100200301', TRUE),
('REP-002', 'Filtro de Aceite', 20.00, 35.00, 5, 10, 4, '20100200301', TRUE),
('REP-003', 'Pastillas de Freno', 40.00, 70.00, 30, 5, 2, '20123456789', TRUE),
('REP-004', 'Disco de Freno', 80.00, 120.00, 15, 4, 2, '20123456789', TRUE),
('REP-005', 'Aceite Sintetico 5W30', 60.00, 90.00, 20, 8, 1, '20100200301', TRUE);

-- Movimientos 
INSERT IGNORE INTO movimientos_stock (fecha, tipo, codigo_producto, cantidad, usuario_responsable, motivo) VALUES
(NOW(), 'ENTRADA', 'REP-001', 50, 'admin', 'Compra inicial'),
(NOW(), 'ENTRADA', 'REP-002', 20, 'admin', 'Compra a proveedor'),
(NOW(), 'SALIDA', 'REP-001', 5, 'admin', 'Venta al cliente');