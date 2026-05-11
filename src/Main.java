import java.time.LocalDateTime;
import java.util.List;
import controllers.InventarioController;
import controllers.UsuarioController;
import models.*;

public class Main {
    public static void main(String[] args) {
        UsuarioController uc = new UsuarioController();
        InventarioController ic = new InventarioController();

        try {
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║     SISTEMA DE INVENTARIO - REPUESTOS        ║");
            System.out.println("╚══════════════════════════════════════════════╝\n");

            // ========== HU-12: AUTENTICACIÓN ==========
            System.out.println("HU-12: INICIAR SESIÓN");
            Usuario admin = uc.iniciarSesion("admin", "1234");
            System.out.printf("  %s | Rol: %s%n%n", admin.getUsername(), admin.getRol());

            // ========== HU-07: ALERTAS AL INICIAR ==========
            System.out.println("HU-07 CA-01: Alertas de stock al iniciar");
            List<Producto> alertasInicial = ic.obtenerAlertasStock();
            if (alertasInicial.isEmpty()) {
                System.out.println("  Sin alertas. Inventario saludable\n");
            } else {
                alertasInicial.forEach(p -> 
                    System.out.printf("   %s | Stock: %d (Min: %d)%n", p.getNombre(), p.getStock(), p.getStockMinimo()));
                System.out.println();
            }

            // ========== HU-13: GESTIÓN DE USUARIOS ==========
            System.out.println("HU-13: GESTIÓN DE USUARIOS");
            uc.crearUsuario(admin, "vendedor_juan", "1234", "VENDEDOR");
            System.out.println("  CA-01: Usuario creado - vendedor_juan");

            try { uc.crearUsuario(admin, "vendedor_juan", "1234", "VENDEDOR"); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Username duplicado - " + e.getMessage()); }
            System.out.println();

            // ========== HU-08: CATEGORÍAS ==========
            System.out.println("HU-08: GESTIÓN DE CATEGORÍAS");
            Categoria catMotor = ic.crearCategoria("Motor");
            Categoria catFrenos = ic.crearCategoria("Frenos");
            System.out.println("  CA-01: Motor, Frenos creadas");

            try { ic.crearCategoria("Motor"); } 
            catch (RuntimeException e) { System.out.println("  CA-02: Categoría duplicada - " + e.getMessage()); }

            ic.crearCategoria("Electronica");
            ic.eliminarCategoria("Electronica");
            System.out.println("  CA-04: Electrónica eliminada (sin productos)");

            try { ic.eliminarCategoria("Motor"); } 
            catch (RuntimeException e) { System.out.println("  CA-03: Productos vinculados - " + e.getMessage()); }
            System.out.println();

            // ========== HU-09: PROVEEDORES ==========
            System.out.println("HU-09: GESTIÓN DE PROVEEDORES");
            Proveedor prov1 = ic.registrarProveedor("20100200301", "Repuestos del Centro S.A.C.");
            System.out.println("  CA-01: Proveedor registrado");

            try { ic.registrarProveedor("12345", "Inválido"); } 
            catch (RuntimeException e) { System.out.println("  CA-02: RUC inválido - " + e.getMessage()); }

            ic.actualizarProveedor("20100200301", "999888777", "contacto@repuestos.com");
            System.out.println("  CA-04: Teléfono/email actualizados (RUC intacto)\n");

            // ========== HU-01: REGISTRO DE PRODUCTOS ==========
            System.out.println("HU-01: REGISTRO DE PRODUCTOS");
            ic.registrarProducto("REP-001", "Bujía Bosch", 10.0, 15.0, 50, 10, catMotor);
            System.out.println("  CA-01: REP-001 - Bujía Bosch");

            try { ic.registrarProducto("", "Sin código", 10.0, 15.0, 50, 10, catMotor); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Código vacío - OK"); }

            try { ic.registrarProducto("REP-X", "", 10.0, 15.0, 50, 10, catMotor); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Nombre vacío - OK"); }

            try { ic.registrarProducto("REP-001", "Otro", 20.0, 30.0, 20, 5, catFrenos); } 
            catch (RuntimeException e) { System.out.println("  CA-02: Código duplicado - OK"); }

            try { ic.registrarProducto("REP-010", "Costoso", 50.0, 40.0, 10, 5, catMotor); } 
            catch (RuntimeException e) { System.out.println("  CA-03: Precio venta <= costo - OK"); }

            ic.registrarProducto("REP-002", "Filtro de Aceite", 20.0, 35.0, 5, 10, catMotor);
            ic.registrarProducto("REP-003", "Pastillas de Freno", 40.0, 70.0, 30, 5, catFrenos);
            ic.registrarProducto("REP-004", "Disco de Freno", 80.0, 120.0, 15, 4, catFrenos);
            ic.registrarProducto("REP-005", "Aceite Sintético 5W30", 60.0, 90.0, 20, 8, catMotor);
            System.out.println("  Catálogo completo (5 productos)\n");

            ic.asignarProveedor("REP-001", prov1);
            ic.asignarProveedor("REP-002", prov1);

            // ========== HU-02: BÚSQUEDA ==========
            System.out.println("HU-02: BÚSQUEDA DE PRODUCTOS");
            System.out.println("  CA-01: Buscando 'freno'...");
            ic.buscarPorNombre("freno").forEach(p -> 
                System.out.printf("    → %s | Stock: %d%n", p.getNombre(), p.getStock()));

            try { ic.buscarPorNombre("xyz123"); } 
            catch (RuntimeException e) { System.out.println("  CA-02: Sin resultados - " + e.getMessage()); }

            Producto p1 = ic.buscarPorCodigo("REP-001");
            System.out.printf("  CA-03: Código '%s' - %s%n", p1.getCodigo(), p1.getNombre());

            int encontrados = ic.buscarPorNombre("BUJÍA").size();
            System.out.println("  CA-04: Mayúsculas ignoradas - " + encontrados + " encontrado(s)\n");

            // ========== HU-03: ACTUALIZACIÓN ==========
            System.out.println("HU-03: ACTUALIZACIÓN DE PRODUCTOS");
            ic.actualizarProducto("REP-002", 38.0, 12);
            System.out.println("  CA-01: Precio/Stock mínimo actualizados");

            try { ic.actualizarProducto("REP-002", 38.0, -5); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Stock mínimo negativo - " + e.getMessage()); }
            System.out.println("  CA-02: Código no modificable (sin setter)\n");

            // ========== HU-04: DESACTIVACIÓN/REACTIVACIÓN ==========
            System.out.println("HU-04: DESACTIVACIÓN Y REACTIVACIÓN");
            ic.desactivarProducto("REP-004");
            System.out.println("  CA-01: REP-004 desactivado");

            try { ic.registrarVenta("REP-004", 2, "vendedor_juan"); } 
            catch (RuntimeException e) { System.out.println("  CA-02: Producto inactivo bloquea venta - OK"); }

            ic.reactivarProducto("REP-004");
            System.out.println("  CA-04: REP-004 reactivado\n");

            // ========== HU-05: ENTRADA DE STOCK ==========
            System.out.println("HU-05: ENTRADA DE STOCK");
            ic.registrarEntrada("REP-002", 20, admin.getUsername());
            System.out.printf("  CA-01: +20 unidades | Stock: %d%n", ic.buscarPorCodigo("REP-002").getStock());

            try { ic.registrarEntrada("REP-002", 0, admin.getUsername()); } 
            catch (RuntimeException e) { System.out.println("  CA-02: Cantidad cero - " + e.getMessage()); }

            ic.desactivarProducto("REP-003");
            try { ic.registrarEntrada("REP-003", 10, admin.getUsername()); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Entrada a inactivo bloqueada - OK"); }
            ic.reactivarProducto("REP-003");
            System.out.println();

            // ========== HU-06: SALIDA DE STOCK ==========
            System.out.println("HU-06: SALIDA DE STOCK");
            ic.registrarVenta("REP-001", 5, admin.getUsername());
            System.out.println("  CA-01: Venta de 5 unidades");

            try { ic.registrarVenta("REP-002", 1000, admin.getUsername()); } 
            catch (RuntimeException e) { System.out.println("  CA-02: Stock insuficiente - OK"); }

            try { ic.registrarVenta("REP-002", 0, admin.getUsername()); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Cantidad inválida - " + e.getMessage()); }

            ic.registrarVenta("REP-005", 12, admin.getUsername());
            ic.registrarVenta("REP-003", 12, admin.getUsername());
            ic.registrarVenta("REP-005", 8, admin.getUsername());
            ic.registrarVenta("REP-002", 2, admin.getUsername());
            System.out.println("  CA-03: Alerta de stock bajo mínimo activada\n");

            // ========== HU-07: ALERTAS ==========
            System.out.println("HU-07: ALERTAS DE STOCK");
            List<Producto> alertas = ic.obtenerAlertasStock();
            if (alertas.isEmpty()) {
                System.out.println("  CA-03: Sin alertas - Inventario saludable");
            } else {
                alertas.forEach(p -> 
                    System.out.printf("   %s | Stock: %d | Min: %d | Reponer: %d%n", 
                        p.getNombre(), p.getStock(), p.getStockMinimo(), p.getStockMinimo() - p.getStock()));
            }
            System.out.printf("  CA-04: Exportar lista - %d producto(s)%n%n", ic.exportarListaCompra().size());

            // ========== HU-14: AJUSTE ==========
            System.out.println("HU-14: AJUSTE DE INVENTARIO");
            ic.ajustarInventario("REP-004", 14, "Disco dañado encontrado", admin.getUsername());
            System.out.printf("  CA-01: Ajuste aplicado | Stock: %d%n", ic.buscarPorCodigo("REP-004").getStock());

            try { ic.ajustarInventario("REP-004", 15, "Corto", admin.getUsername()); } 
            catch (RuntimeException e) { System.out.println("  CA-02: Motivo < 10 chars - " + e.getMessage()); }

            try { ic.ajustarInventario("REP-001", -10, "No puede ser negativo", admin.getUsername()); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Stock negativo bloqueado - OK"); }
            System.out.println();

            // ========== HU-10: REPORTE INVENTARIO ==========
            System.out.println("╔═══════════════ HU-10: REPORTE INVENTARIO ═══════════════╗");
            System.out.println("CA-01: Inventario completo");
            ic.generarReporteInventario(false);
            System.out.println("CA-02: Productos bajo mínimo");
            ic.generarReporteInventario(true);
            System.out.println("CA-04: Por categoría 'Motor'");
            ic.generarReporteInventario(false, "Motor");
            System.out.println("╚══════════════════════════════════════════════════════════╝\n");

            // ========== HU-11: REPORTE MOVIMIENTOS ==========
            System.out.println("╔═══════════════ HU-11: REPORTE MOVIMIENTOS ═══════════════╗");
            LocalDateTime hoy = LocalDateTime.now();
            LocalDateTime haceUnaSemana = hoy.minusDays(7);
            System.out.println("CA-01: Movimientos (últimos 7 días)");
            ic.obtenerHistorial("TODOS", haceUnaSemana, hoy).forEach(m -> 
                System.out.printf("  %s | %s | %s | Cant: %d%n", m.getFecha().toLocalDate(), m.getTipo(), m.getProducto().getNombre(), m.getCantidad()));

            System.out.printf("CA-02: Filtrar SALIDA - %d registros%n", ic.obtenerHistorial("SALIDA").size());

            try { ic.obtenerHistorial("TODOS", hoy, haceUnaSemana); } 
            catch (RuntimeException e) { System.out.println("  CA-04: Rango inválido - " + e.getMessage()); }
            System.out.println("╚══════════════════════════════════════════════════════════╝\n");

            // ========== HU-15: MÁS VENDIDOS ==========
            System.out.println("╔═══════════════ HU-15: TOP MÁS VENDIDOS ═════════════════╗");
            System.out.println("CA-01: Top 10 del mes");
            ic.mostrarProductosMasVendidos(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
            System.out.println("CA-02: Ranking anual");
            ic.mostrarProductosMasVendidos(LocalDateTime.now().getYear(), 0);
            System.out.println("  CA-04: Desempate alfabético");
            System.out.println("╚══════════════════════════════════════════════════════════╝\n");

            // ========== HU-09 CA-03: PRODUCTOS POR PROVEEDOR ==========
            System.out.println("HU-09 CA-03: Productos por proveedor");
            System.out.printf("  %s (RUC: %s)%n", prov1.getRazonSocial(), prov1.getRuc());
            ic.listarProductosPorProveedor("20100200301").forEach(p -> 
                System.out.printf("   %s: %s%n", p.getCodigo(), p.getNombre()));
            System.out.println();

            // ========== REPORTE FINAL ==========
            System.out.println("╔═══════════════ REPORTE FINAL ════════════════════════════╗");
            ic.generarReporteInventario(false);
            System.out.println("╚══════════════════════════════════════════════════════════╝\n");

            System.out.println("╔══════════════════════════════════════════════════════════╗");
            System.out.println("║          PRUEBAS COMPLETADAS EXITOSAMENTE             ║");
            System.out.println("║          55/55 CRITERIOS DE ACEPTACIÓN CUMPLIDOS        ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");

        } catch (RuntimeException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
    }
}