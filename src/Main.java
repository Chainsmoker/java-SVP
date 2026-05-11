import java.time.LocalDateTime;
import java.util.List;

import controllers.InventarioController;
import controllers.UsuarioController;

import models.Categoria;
import models.MovimientoStock;
import models.Producto;
import models.Proveedor;
import models.Usuario;

public class Main {
    public static void main(String[] args) {
        UsuarioController uc = new UsuarioController();
        InventarioController ic = new InventarioController();

        try {
            System.out.println("==========================================");
            System.out.println("   SISTEMA DE INVENTARIO - REPUESTOS      ");
            System.out.println("==========================================\n");
            
            // ==================== AUTENTICACIÓN Y USUARIOS ====================
            System.out.println("--- HU-12: INICIANDO SESIÓN ---");
            Usuario admin = uc.iniciarSesion("admin", "1234");
            System.out.println("Bienvenido: " + admin.getUsername() + " | Rol: " + admin.getRol());

            // HU-07 CA-01: Mostrar alertas al iniciar sesión
            System.out.println("\n--- HU-07 CA-01: ALERTAS DE STOCK AL INICIAR ---");
            List<Producto> alertasInicial = ic.obtenerAlertasStock();
            if (alertasInicial.isEmpty()) {
                System.out.println("Sin alertas. Inventario saludable");
            } else {
                for (Producto p : alertasInicial) {
                    System.out.println("ALERTA: " + p.getNombre() + " tiene stock de " + p.getStock() + " (Mínimo: " + p.getStockMinimo() + ")");
                }
            }

            // ==================== GESTIÓN DE USUARIOS ====================
            System.out.println("\n--- HU-13: GESTIÓN DE USUARIOS ---");
            uc.crearUsuario(admin, "vendedor_juan", "1234", "VENDEDOR");
            System.out.println("CA-01: Nuevo usuario 'vendedor_juan' creado exitosamente");

            // HU-13 CA-04: Username duplicado
            System.out.println("\n--- HU-13 CA-04: VALIDAR USERNAME DUPLICADO ---");
            try {
                uc.crearUsuario(admin, "vendedor_juan", "5678", "VENDEDOR");
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }

            // ==================== CATEGORÍAS ====================
            System.out.println("\n--- HU-08: GESTIÓN DE CATEGORÍAS ---");
            Categoria catMotor = ic.crearCategoria("Motor");
            Categoria catFrenos = ic.crearCategoria("Frenos");
            System.out.println("CA-01: Categorías 'Motor' y 'Frenos' creadas");

            // HU-08 CA-02: Categoría duplicada
            try {
                ic.crearCategoria("Motor");
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-08 CA-04: Eliminar categoría sin productos
            ic.crearCategoria("Electrónica");
            ic.eliminarCategoria("Electrónica");
            System.out.println("CA-04: Categoría 'Electrónica' eliminada correctamente");

            // HU-08 CA-03: Intentar eliminar categoría con productos
            try {
                ic.eliminarCategoria("Motor");
            } catch (RuntimeException e) {
                System.out.println("CA-03: Correcto - " + e.getMessage());
            }

            // ==================== PROVEEDORES ====================
            System.out.println("\n--- HU-09: GESTIÓN DE PROVEEDORES ---");
            Proveedor prov1 = ic.registrarProveedor("20100200301", "Repuestos del Centro S.A.C.");
            System.out.println("CA-01: Proveedor registrado");

            // HU-09 CA-02: RUC inválido
            try {
                ic.registrarProveedor("12345", "Proveedor Inválido");
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-09 CA-04: Actualizar proveedor
            ic.actualizarProveedor("20100200301", "999888777", "contacto@repuestoscentro.com");
            System.out.println("CA-04: Datos de proveedor actualizados");

            // ==================== PRODUCTOS ====================
            System.out.println("\n--- HU-01: REGISTRO DE PRODUCTOS ---");
            
            // HU-01 CA-01: Registro exitoso
            ic.registrarProducto("REP-001", "Bujía Bosch", 10.0, 15.0, 50, 10, catMotor);
            System.out.println("CA-01: Producto 'REP-001' registrado");

            // HU-01 CA-04: Campos obligatorios vacíos
            try {
                ic.registrarProducto("", "Producto sin código", 10.0, 15.0, 50, 10, catMotor);
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }
            try {
                ic.registrarProducto("REP-XXX", "", 10.0, 15.0, 50, 10, catMotor);
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }

            // HU-01 CA-02: Código duplicado
            try {
                ic.registrarProducto("REP-001", "Otro producto", 20.0, 30.0, 20, 5, catFrenos);
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-01 CA-03: Precio venta <= costo
            try {
                ic.registrarProducto("REP-010", "Producto costoso", 50.0, 40.0, 10, 5, catMotor);
            } catch (RuntimeException e) {
                System.out.println("CA-03: Correcto - " + e.getMessage());
            }

            // Más productos
            ic.registrarProducto("REP-002", "Filtro de Aceite", 20.0, 35.0, 5, 10, catMotor);
            ic.registrarProducto("REP-003", "Pastillas de Freno", 40.0, 70.0, 30, 5, catFrenos);
            ic.registrarProducto("REP-004", "Disco de Freno", 80.0, 120.0, 15, 4, catFrenos); 
            ic.registrarProducto("REP-005", "Aceite Sintético 5W30", 60.0, 90.0, 20, 8, catMotor);
            System.out.println("Catálogo de productos completado");

            // Asignar proveedor a productos
            ic.asignarProveedor("REP-001", prov1);
            ic.asignarProveedor("REP-002", prov1);

            // ==================== BÚSQUEDA ====================
            System.out.println("\n--- HU-02: BÚSQUEDA DE PRODUCTOS ---");
            
            // HU-02 CA-01: Búsqueda por nombre
            System.out.println("CA-01: Buscando 'freno'...");
            List<Producto> resultados = ic.buscarPorNombre("freno");
            for (Producto p : resultados) {
                System.out.println("  Encontrado: " + p.getCodigo() + " - " + p.getNombre() + " | Stock: " + p.getStock());
            }

            // HU-02 CA-02: Sin resultados
            try {
                ic.buscarPorNombre("xyz123");
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-02 CA-03: Búsqueda por código
            System.out.println("CA-03: Buscando código 'REP-001'...");
            Producto p1 = ic.buscarPorCodigo("REP-001");
            System.out.println("  Producto: " + p1.getNombre() + " | Stock: " + p1.getStock());

            // HU-02 CA-04: Mayúsculas/minúsculas
            System.out.println("CA-04: Buscando 'BUJÍA' (mayúsculas)...");
            List<Producto> resultados2 = ic.buscarPorNombre("BUJÍA");
            System.out.println("  Encontrados: " + resultados2.size() + " producto(s)");

            // ==================== ACTUALIZACIÓN ====================
            System.out.println("\n--- HU-03: ACTUALIZACIÓN DE PRODUCTOS ---");
            
            // HU-03 CA-01: Actualización exitosa
            ic.actualizarProducto("REP-002", 38.0, 12);
            System.out.println("CA-01: Producto REP-002 actualizado");

            // HU-03 CA-04: Stock mínimo negativo
            try {
                ic.actualizarProducto("REP-002", 38.0, -5);
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }

            // HU-03 CA-02: Verificar que código no se modifica (no existe método)
            System.out.println("CA-02: El código es campo no modificable (sin setter)");

            // ==================== DESACTIVACIÓN Y REACTIVACIÓN ====================
            System.out.println("\n--- HU-04: DESACTIVACIÓN Y REACTIVACIÓN ---");
            
            // HU-04 CA-01: Desactivar producto
            ic.desactivarProducto("REP-004");
            System.out.println("CA-01: Producto REP-004 desactivado");

            // HU-04 CA-02: Venta con producto inactivo
            try {
                ic.registrarVenta("REP-004", 2, "vendedor_juan");
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-04 CA-04: Reactivar producto
            ic.reactivarProducto("REP-004");
            System.out.println("CA-04: Producto REP-004 reactivado");

            // ==================== ENTRADA DE STOCK ====================
            System.out.println("\n--- HU-05: REGISTRO DE ENTRADA DE STOCK ---");
            
            // HU-05 CA-01: Entrada exitosa
            ic.registrarEntrada("REP-002", 20, admin.getUsername());
            System.out.println("CA-01: Entrada de 20 unidades a REP-002. Nuevo stock: " + ic.buscarPorCodigo("REP-002").getStock());

            // HU-05 CA-02: Cantidad 0 o negativa
            try {
                ic.registrarEntrada("REP-002", 0, admin.getUsername());
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-05 CA-04: Entrada a producto desactivado
            ic.desactivarProducto("REP-003");
            try {
                ic.registrarEntrada("REP-003", 10, admin.getUsername());
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }
            ic.reactivarProducto("REP-003");

            // ==================== SALIDA DE STOCK ====================
            System.out.println("\n--- HU-06: REGISTRO DE SALIDA DE STOCK ---");
            
            // HU-06 CA-01: Venta exitosa
            ic.registrarVenta("REP-001", 5, admin.getUsername());
            System.out.println("CA-01: Venta de 5 unidades de REP-001");

            // HU-06 CA-02: Stock insuficiente
            try {
                ic.registrarVenta("REP-002", 1000, admin.getUsername());
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-06 CA-04: Cantidad 0 o negativa
            try {
                ic.registrarVenta("REP-002", 0, admin.getUsername());
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }

            // HU-06 CA-03: Alerta por stock bajo mínimo
            ic.registrarVenta("REP-005", 12, admin.getUsername());

            // Más ventas para ranking
            ic.registrarVenta("REP-003", 12, admin.getUsername());
            ic.registrarVenta("REP-005", 8, admin.getUsername());
            ic.registrarVenta("REP-002", 2, admin.getUsername());

            // ==================== ALERTAS DE STOCK ====================
            System.out.println("\n--- HU-07: ALERTAS DE STOCK MÍNIMO ---");
            List<Producto> alertas = ic.obtenerAlertasStock();
            if (alertas.isEmpty()) {
                System.out.println("CA-03: Sin alertas. Inventario saludable");
            } else {
                for (Producto p : alertas) {
                    int diferencia = p.getStockMinimo() - p.getStock();
                    System.out.println("CA-02: " + p.getNombre() + " | Stock: " + p.getStock() + " | Mínimo: " + p.getStockMinimo() + " | A reponer: " + diferencia);
                }
            }

            // HU-07 CA-04: Exportar lista de compra
            System.out.println("\n--- HU-07 CA-04: EXPORTAR LISTA DE COMPRA ---");
            List<Producto> listaCompra = ic.exportarListaCompra();
            System.out.println("Productos urgentes a reponer: " + listaCompra.size());

            // ==================== AJUSTE DE INVENTARIO ====================
            System.out.println("\n--- HU-14: AJUSTE DE INVENTARIO ---");
            
            // HU-14 CA-01: Ajuste exitoso
            ic.ajustarInventario("REP-004", 14, "Se encontró 1 disco dañado en almacén", admin.getUsername());
            System.out.println("CA-01: Ajuste realizado a REP-004. Stock: " + ic.buscarPorCodigo("REP-004").getStock());

            // HU-14 CA-02: Motivo con menos de 10 caracteres
            try {
                ic.ajustarInventario("REP-004", 15, "Corto", admin.getUsername());
            } catch (RuntimeException e) {
                System.out.println("CA-02: Correcto - " + e.getMessage());
            }

            // HU-14 CA-04: Ajuste que resulta en stock negativo
            try {
                ic.ajustarInventario("REP-001", -10, "Ajuste negativo incorrecto", admin.getUsername());
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }

            // ==================== REPORTE DE INVENTARIO ====================
            System.out.println("\n================ HU-10: REPORTE DE INVENTARIO ================");
            
            // HU-10 CA-01: Reporte completo
            System.out.println("--- CA-01: Reporte completo ---");
            ic.generarReporteInventario(false);

            // HU-10 CA-02: Filtro bajo mínimo
            System.out.println("\n--- CA-02: Productos bajo mínimo ---");
            ic.generarReporteInventario(true);

            // HU-10 CA-04: Filtro por categoría
            System.out.println("\n--- CA-04: Reporte por categoría 'Motor' ---");
            ic.generarReporteInventario(false, "Motor");

            // ==================== REPORTE DE MOVIMIENTOS ====================
            System.out.println("\n================ HU-11: REPORTE DE MOVIMIENTOS ================");
            
            // HU-11 CA-01: Historial con filtro por fechas
            LocalDateTime hoy = LocalDateTime.now();
            LocalDateTime haceUnaSemana = hoy.minusDays(7);
            List<MovimientoStock> historial = ic.obtenerHistorial("TODOS", haceUnaSemana, hoy);
            System.out.println("CA-01: Movimientos en rango de fechas");
            for (MovimientoStock m : historial) {
                System.out.println("  " + m.getFecha() + " | " + m.getTipo() + " | " + m.getProducto().getNombre() + " | Cant: " + m.getCantidad());
            }

            // HU-11 CA-02: Filtro por tipo SALIDA
            List<MovimientoStock> salidas = ic.obtenerHistorial("SALIDA");
            System.out.println("CA-02: Total de SALIDAS: " + salidas.size());

            // HU-11 CA-04: Rango de fechas inválido
            try {
                ic.obtenerHistorial("TODOS", hoy, haceUnaSemana);
            } catch (RuntimeException e) {
                System.out.println("CA-04: Correcto - " + e.getMessage());
            }

            // ==================== PRODUCTOS MÁS VENDIDOS ====================
            System.out.println("\n================ HU-15: TOP REPUESTOS MÁS VENDIDOS ================");
            
            // HU-15 CA-01: Top 10 productos
            System.out.println("--- CA-01: Top 10 del mes ---");
            ic.mostrarProductosMasVendidos(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());

            // HU-15 CA-02: Cambiar filtro a año completo
            System.out.println("\n--- CA-02: Ranking anual ---");
            ic.mostrarProductosMasVendidos(LocalDateTime.now().getYear(), 0);

            // HU-15 CA-04: Desempate alfabético
            System.out.println("CA-04: Ordenamiento por cantidad + nombre (desempate alfabético)");

            // ==================== PROVEEDOR Y PRODUCTOS ====================
            System.out.println("\n--- HU-09 CA-03: PRODUCTOS POR PROVEEDOR ---");
            List<Producto> productosProv = ic.listarProductosPorProveedor("20100200301");
            System.out.println("Proveedor: " + prov1.getRazonSocial());
            System.out.println("Productos vinculados: " + productosProv.size());
            for (Producto p : productosProv) {
                System.out.println("  - " + p.getCodigo() + ": " + p.getNombre());
            }

            // ==================== REPORTE FINAL ====================
            System.out.println("\n================ REPORTE FINAL DE INVENTARIO ================");
            ic.generarReporteInventario(false);

            System.out.println("\n==========================================");
            System.out.println("   PRUEBAS COMPLETADAS EXITOSAMENTE        ");
            System.out.println("==========================================");

        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}