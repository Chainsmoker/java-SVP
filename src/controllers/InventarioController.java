package controllers;
import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class InventarioController {
    private List<Producto> catalogo = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();
    private List<Proveedor> proveedores = new ArrayList<>();
    private List<MovimientoStock> historial = new ArrayList<>();

    private void validarCamposObligatorios(String codigo, String nombre) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new RuntimeException("Debe completar todos los campos obligatorios");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("Debe completar todos los campos obligatorios");
        }
    }

    // HU-01: Registro de Productos
    public void registrarProducto(String codigo, String nombre, double costo, double venta, int stock, int stockMin, Categoria cat) {
        validarCamposObligatorios(codigo, nombre);
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
        if (nuevoStockMinimo < 0) {
            throw new RuntimeException("El límite mínimo no puede ser menor a cero");
        }
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

    // HU-04 CA-04: Reactivación de Productos
    public void reactivarProducto(String codigo) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setActivo(true);
        }
    }

    // HU-05: Registro de Entrada de Stock - Compra
    public void registrarEntrada(String codigo, int cantidad, String username) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        Producto p = buscarPorCodigo(codigo);
        if (p == null) {
            throw new RuntimeException("Producto no encontrado");
        }
        if (!p.isActivo()) {
            throw new RuntimeException("No se puede registrar entrada. El repuesto está desactivado");
        }
        p.setStock(p.getStock() + cantidad);
        historial.add(new MovimientoStock("ENTRADA", p, cantidad, username, "Compra a proveedor"));
    }

    // HU-06: Registro de Salida de Stock - Venta
    public void registrarVenta(String codigo, int cantidad, String username) {
        if (cantidad <= 0) {
            throw new RuntimeException("Debe ingresar una cantidad válida mayor a cero");
        }
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

    // HU-07 CA-04: Exportar lista de compra
    public List<Producto> exportarListaCompra() {
        return obtenerAlertasStock();
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

    // HU-08 CA-03, CA-04: Eliminar Categoría
    public void eliminarCategoria(String nombre) {
        int productosVinculados = 0;
        for (Producto p : catalogo) {
            if (p.getCategoria().getNombre().equalsIgnoreCase(nombre)) {
                productosVinculados++;
            }
        }
        if (productosVinculados > 0) {
            throw new RuntimeException("No se puede eliminar. Hay " + productosVinculados + " productos vinculados a esta categoría");
        }
        categorias.removeIf(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    // HU-09: Gestión de Proveedores
    public Proveedor registrarProveedor(String ruc, String razonSocial) {
        if (ruc == null || ruc.length() != 11) {
            throw new RuntimeException("RUC inválido. Debe tener 11 dígitos");
        }
        Proveedor nuevo = new Proveedor(ruc, razonSocial);
        proveedores.add(nuevo);
        return nuevo;
    }

    // HU-09 CA-04: Actualizar datos de proveedor
    public void actualizarProveedor(String ruc, String telefono, String email) {
        for (Proveedor prov : proveedores) {
            if (prov.getRuc().equals(ruc)) {
                prov.setTelefono(telefono);
                prov.setEmail(email);
                return;
            }
        }
        throw new RuntimeException("Proveedor no encontrado");
    }

    // HU-09 CA-03: Listar productos por proveedor
    public List<Producto> listarProductosPorProveedor(String ruc) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.getProveedor() != null && p.getProveedor().getRuc().equals(ruc)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    // Asignar proveedor a producto
    public void asignarProveedor(String codigoProducto, Proveedor proveedor) {
        Producto p = buscarPorCodigo(codigoProducto);
        if (p != null) {
            p.setProveedor(proveedor);
        }
    }

    // HU-10: Reporte de Inventario Actual
    public void generarReporteInventario(boolean soloBajoMinimo, String nombreCategoria) {
        double valorTotal = 0;
        List<Producto> lista = catalogo;
        
        if (nombreCategoria != null && !nombreCategoria.equals("TODAS")) {
            lista = lista.stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria))
                .collect(Collectors.toList());
        }
        
        if (soloBajoMinimo) {
            lista = lista.stream()
                .filter(p -> p.isActivo() && p.getStock() <= p.getStockMinimo())
                .collect(Collectors.toList());
        }
        
        for (Producto p : lista) {
            if (!soloBajoMinimo || p.isActivo()) {
                double subtotal = p.getStock() * p.getPrecioCosto();
                valorTotal += subtotal;
                System.out.println(p.getCodigo() + " | " + p.getNombre() + " | Stock: " + p.getStock() + " | Valor: S/." + subtotal);
            }
        }
        System.out.println("VALOR TOTAL CONSOLIDADO: S/." + valorTotal);
    }

    public void generarReporteInventario(boolean soloBajoMinimo) {
        generarReporteInventario(soloBajoMinimo, "TODAS");
    }

    // HU-11: Reporte de Movimientos
    public List<MovimientoStock> obtenerHistorial(String filtroTipo, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            if (fechaInicio.isAfter(fechaFin)) {
                throw new RuntimeException("Rango de fechas inválido");
            }
        }
        
        List<MovimientoStock> filtrado = new ArrayList<>();
        for (MovimientoStock m : historial) {
            boolean cumpleTipo = filtroTipo.equals("TODOS") || m.getTipo().equals(filtroTipo);
            boolean cumpleFecha = true;
            if (fechaInicio != null && fechaFin != null) {
                cumpleFecha = !m.getFecha().isBefore(fechaInicio) && !m.getFecha().isAfter(fechaFin);
            }
            if (cumpleTipo && cumpleFecha) {
                filtrado.add(m);
            }
        }
        return filtrado;
    }

    public List<MovimientoStock> obtenerHistorial(String filtroTipo) {
        return obtenerHistorial(filtroTipo, null, null);
    }

    // HU-14: Ajuste de Inventario
    public void ajustarInventario(String codigo, int nuevaCantidad, String motivo, String username) {
        if (motivo == null || motivo.length() < 10) {
            throw new RuntimeException("El motivo del ajuste debe tener al menos 10 caracteres");
        }
        if (nuevaCantidad < 0) {
            throw new RuntimeException("El ajuste no puede resultar en stock negativo");
        }
        
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setStock(nuevaCantidad);
            historial.add(new MovimientoStock("AJUSTE", p, nuevaCantidad, username, motivo));
        }
    }

    // HU-15: Productos Más Vendidos
    public void mostrarProductosMasVendidos(int anio, int mes) {
        Map<String, Integer> ventas = new java.util.HashMap<>();
        
        for (MovimientoStock m : historial) {
            if (m.getTipo().equals("SALIDA")) {
                LocalDateTime fecha = m.getFecha();
                boolean matchAnio = (anio == 0 || fecha.getYear() == anio);
                boolean matchMes = (mes == 0 || fecha.getMonthValue() == mes);
                if (matchAnio && matchMes) {
                    String codigo = m.getProducto().getCodigo();
                    ventas.put(codigo, ventas.getOrDefault(codigo, 0) + m.getCantidad());
                }
            }
        }
        
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas en el período seleccionado");
            return;
        }
        
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>(ventas.entrySet());
        ranking.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue());
            if (cmp == 0) {
                Producto prodA = buscarPorCodigo(a.getKey());
                Producto prodB = buscarPorCodigo(b.getKey());
                if (prodA != null && prodB != null) {
                    return prodA.getNombre().compareTo(prodB.getNombre());
                }
            }
            return cmp;
        });
        
        System.out.println("=== TOP REPUESTOS MÁS VENDIDOS ===");
        int limit = Math.min(10, ranking.size());
        for (int i = 0; i < limit; i++) {
            Producto p = buscarPorCodigo(ranking.get(i).getKey());
            if (p != null) {
                System.out.println((i + 1) + ". " + p.getNombre() + " | " + ranking.get(i).getValue() + " unidades");
            }
        }
    }

    public void mostrarProductosMasVendidos() {
        mostrarProductosMasVendidos(0, 0);
    }
}