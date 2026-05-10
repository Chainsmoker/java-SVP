import java.util.List;

import controllers.InventarioController;
import controllers.UsuarioController;

import models.Categoria;
import models.MovimientoStock;
import models.Producto;
import models.Usuario;

public class Main {
    public static void main(String[] args) {
        UsuarioController uc = new UsuarioController();
        InventarioController ic = new InventarioController();

        try {
            System.out.println("==========================================");
            System.out.println("   SISTEMA DE INVENTARIO - REPUESTOS      ");
            System.out.println("==========================================\n");
            
            // HU-12: Autenticación de Usuarios
            System.out.println("--- INICIANDO SESIÓN ---");
            Usuario logueado = uc.iniciarSesion("admin", "1234");
            System.out.println("Bienvenido: " + logueado.getUsername() + " | Rol: " + logueado.getRol());

            // HU-13: Gestión de Usuarios
            System.out.println("\n--- GESTIÓN DE USUARIOS ---");
            uc.crearUsuario(logueado, "vendedor_juan", "1234", "VENDEDOR");
            System.out.println("Nuevo usuario 'vendedor_juan' creado exitosamente");

            // HU-08: Gestión de Categorías
            System.out.println("\n--- CONFIGURANDO CATEGORÍAS ---");
            Categoria catMotor = ic.crearCategoria("Motor");
            Categoria catFrenos = ic.crearCategoria("Frenos");
            System.out.println("Categorías 'Motor' y 'Frenos' creadas");

            // HU-09: Gestión de Proveedores
            System.out.println("\n--- GESTIÓN DE PROVEEDORES ---");
            ic.registrarProveedor("20100200301", "Repuestos del Centro S.A.C.");
            System.out.println("Proveedor 'Repuestos del Centro S.A.C.' registrado");

            // HU-01: Registro de Productos
            System.out.println("\n--- REGISTRANDO PRODUCTOS INICIALES ---");
            ic.registrarProducto("REP-001", "Bujía Bosch", 10.0, 15.0, 50, 10, catMotor);
            ic.registrarProducto("REP-002", "Filtro de Aceite", 20.0, 35.0, 5, 10, catMotor);
            ic.registrarProducto("REP-003", "Pastillas de Freno", 40.0, 70.0, 30, 5, catFrenos);
            ic.registrarProducto("REP-004", "Disco de Freno", 80.0, 120.0, 15, 4, catFrenos); 
            ic.registrarProducto("REP-005", "Aceite Sintético 5W30", 60.0, 90.0, 20, 8, catMotor);
            System.out.println("Catálogo de 5 productos registrado con éxito");

            // HU-02: Búsqueda de Productos
            System.out.println("\n--- BÚSQUEDA DE PRODUCTOS ---");
            List<Producto> resultados = ic.buscarPorNombre("freno");
            for (Producto p : resultados) {
                System.out.println("Encontrado: " + p.getCodigo() + " - " + p.getNombre() + " | Stock: " + p.getStock());
            }

            // HU-03: Actualización de Productos
            System.out.println("\n--- ACTUALIZANDO PRECIOS ---");
            ic.actualizarProducto("REP-002", 38.0, 12);
            Producto actualizado = ic.buscarPorCodigo("REP-002");
            System.out.println("Producto actualizado: " + actualizado.getNombre() + " | Nuevo Precio Venta: S/." + actualizado.getPrecioVenta());

            // HU-06: Registro de Salida de Stock (Venta)
            System.out.println("\n--- REGISTRANDO VENTAS DEL DÍA ---");
            ic.registrarVenta("REP-001", 5, logueado.getUsername()); 
            ic.registrarVenta("REP-003", 12, logueado.getUsername()); 
            ic.registrarVenta("REP-005", 8, logueado.getUsername()); 
            ic.registrarVenta("REP-002", 2, logueado.getUsername()); 
            System.out.println("Ventas procesadas correctamente");

            // HU-07: Alerta de Stock Mínimo
            System.out.println("\n--- REVISANDO ALERTAS DE STOCK ---");
            List<Producto> productosEnAlerta = ic.obtenerAlertasStock();
            for (Producto p : productosEnAlerta) {
                System.out.println("ALERTA: " + p.getNombre() + " tiene stock de " + p.getStock() + " (Mínimo requerido: " + p.getStockMinimo() + ")");
            }

            // HU-05: Registro de Entrada de Stock (Compra a Proveedor)
            System.out.println("\n--- COMPRANDO MERCADERÍA AL PROVEEDOR ---");
            ic.registrarEntrada("REP-002", 20, logueado.getUsername());
            System.out.println("Ingreso de 20 unidades de 'Filtro de Aceite'. Nuevo stock: " + ic.buscarPorCodigo("REP-002").getStock());

            // HU-14: Ajuste de Inventario
            System.out.println("\n--- AJUSTE DE INVENTARIO MANUAL ---");
            ic.ajustarInventario("REP-004", 14, "Se encontró 1 disco dañado en almacén", logueado.getUsername());
            System.out.println("Ajuste realizado. Stock de 'Disco de Freno' actualizado a: " + ic.buscarPorCodigo("REP-004").getStock());

            // HU-04: Desactivación de Productos
            System.out.println("\n--- DESACTIVANDO PRODUCTOS OBSOLETOS ---");
            ic.desactivarProducto("REP-004");
            System.out.println("El producto 'Disco de Freno' ha sido desactivado del catálogo");

            // HU-10: Reporte de Inventario Actual
            System.out.println("\n================ REPORTE DE INVENTARIO ================");
            ic.generarReporteInventario(false);

            // HU-15: Productos Más Vendidos
            System.out.println("\n================ TOP REPUESTOS MÁS VENDIDOS ================");
            ic.mostrarProductosMasVendidos();

            // HU-11: Reporte de Movimientos
            System.out.println("\n================ HISTORIAL COMPLETO DE MOVIMIENTOS ================");
            List<MovimientoStock> historial = ic.obtenerHistorial("TODOS");
            for (MovimientoStock m : historial) {
                System.out.println(m.getTipo() + " | Producto: " + m.getProducto().getNombre() + " | Cant: " + m.getCantidad() + " | Usuario: " + m.getUsuarioResponsable() + " | Nota: " + m.getMotivo());
            }

        } catch (Exception e) {
            System.err.println("Error en el sistema: " + e.getMessage());
        }
    }
}