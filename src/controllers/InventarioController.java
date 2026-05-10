package controllers;
import models.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioController {
    private List<Producto> catalogo = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();
    private List<Proveedor> proveedores = new ArrayList<>();
    private List<MovimientoStock> historial = new ArrayList<>();

    // HU-01: Registro de Productos
    public void registrarProducto(String codigo, String nombre, double costo, double venta, int stock, int stockMin, Categoria cat) {
        if (buscarPorCodigo(codigo) != null) {
            throw new RuntimeException("El código ya está registrado.");
        }
        if (venta <= costo) {
            throw new RuntimeException("El precio de venta debe ser mayor al precio de costo.");
        }
        Producto nuevo = new Producto(codigo, nombre, costo, venta, stock, stockMin, cat);
        catalogo.add(nuevo);
    }

    // HU-02: Búsqueda de Productos
    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : catalogo) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null; 
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultados = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.getNombre().toLowerCase().contains(texto.toLowerCase()) && p.isActivo()) {
                resultados.add(p);
            }
        }
        
        if (resultados.isEmpty()) {
            throw new RuntimeException("No se encontraron productos con ese criterio");
        }
        return resultados;
    }

    // HU-03: Actualización de Productos
    public void actualizarProducto(String codigo, double nuevoPrecioVenta, int nuevoStockMinimo) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setPrecioVenta(nuevoPrecioVenta);
            p.setStockMinimo(nuevoStockMinimo);
        }
    }

    // HU-04: Desactivación de Productos
    public void desactivarProducto(String codigo) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setActivo(false);
        }
    }

    // HU-05: Registro de Entrada de Stock - Compra
    public void registrarEntrada(String codigo, int cantidad, String username) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setStock(p.getStock() + cantidad);
            historial.add(new MovimientoStock("ENTRADA", p, cantidad, username, "Compra a proveedor"));
        }
    }

    // HU-06: Registro de Salida de Stock - Venta
    public void registrarVenta(String codigo, int cantidad, String username) {
        Producto p = buscarPorCodigo(codigo);
        if (p == null || !p.isActivo()) {
            throw new RuntimeException("Producto no disponible");
        }
        if (p.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + p.getStock() + " unidades");
        }
        
        p.setStock(p.getStock() - cantidad);
        historial.add(new MovimientoStock("SALIDA", p, cantidad, username, "Venta al cliente"));
        
        if (p.getStock() <= p.getStockMinimo()) {
            System.out.println("ALERTA AUTOMÁTICA: El producto " + p.getNombre() + " requiere reposición.");
        }
    }

    // HU-07: Alerta de Stock Mínimo
    public List<Producto> obtenerAlertasStock() {
        List<Producto> alertas = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.isActivo() && p.getStock() <= p.getStockMinimo()) {
                alertas.add(p);
            }
        }
        return alertas;
    }

    // HU-08: Gestión de Categorías
    public Categoria crearCategoria(String nombre) {
        for (Categoria c : categorias) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                throw new RuntimeException("La categoría ya existe");
            }
        }
        Categoria c = new Categoria(nombre);
        categorias.add(c);
        return c;
    }

    // HU-09: Gestión de Proveedores
    public void registrarProveedor(String ruc, String razonSocial) {
        if (ruc == null || ruc.length() != 11) {
            throw new RuntimeException("RUC inválido. Debe tener 11 dígitos");
        }
        proveedores.add(new Proveedor(ruc, razonSocial));
    }

    // HU-10: Reporte de Inventario Actual
    public void generarReporteInventario(boolean soloBajoMinimo) {
        double valorTotal = 0;
        List<Producto> lista = soloBajoMinimo ? obtenerAlertasStock() : catalogo;
        
        for (Producto p : lista) {
            double subtotal = p.getStock() * p.getPrecioCosto();
            valorTotal += subtotal;
            System.out.println(p.getCodigo() + " | " + p.getNombre() + " | Stock: " + p.getStock() + " | Valor: S/." + subtotal);
        }
        System.out.println("VALOR TOTAL CONSOLIDADO: S/." + valorTotal);
    }

    // HU-11: Reporte de Movimientos
    public List<MovimientoStock> obtenerHistorial(String filtroTipo) {
        if (filtroTipo.equals("TODOS")) {
            return historial;
        }
        
        List<MovimientoStock> filtrado = new ArrayList<>();
        for (MovimientoStock m : historial) {
            if (m.getTipo().equals(filtroTipo)) {
                filtrado.add(m);
            }
        }
        return filtrado;
    }

    // HU-14: Ajuste de Inventario
    public void ajustarInventario(String codigo, int nuevaCantidad, String motivo, String username) {
        if (motivo == null || motivo.length() < 10) {
            throw new RuntimeException("El motivo del ajuste debe tener al menos 10 caracteres");
        }
        
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setStock(nuevaCantidad);
            historial.add(new MovimientoStock("AJUSTE", p, nuevaCantidad, username, motivo));
        }
    }

    // HU-15: Productos Más Vendidos
    public void mostrarProductosMasVendidos() {
        System.out.println("--- VENTAS POR PRODUCTO ---");
        for (Producto p : catalogo) {
            int cantidadVendida = 0;
            for (MovimientoStock m : historial) {
                if (m.getTipo().equals("SALIDA") && m.getProducto().getCodigo().equals(p.getCodigo())) {
                    cantidadVendida += m.getCantidad();
                }
            }
            if (cantidadVendida > 0) {
                System.out.println("Producto: " + p.getNombre() + " | Unidades vendidas: " + cantidadVendida);
            }
        }
    }
}