package controllers;
import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.LocalDate;

// ============================================================
// CONTROLADOR DE INVENTARIO - SISTEMA DE REPUESTOS (SVP)
// ============================================================
// Este controlador implementa las historias de usuario del
// sistema de inventario con sus criterios de aceptación.
// ============================================================

public class InventarioController {
    private List<Producto> catalogo = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();
    private List<Proveedor> proveedores = new ArrayList<>();
    private List<MovimientoStock> historial = new ArrayList<>();

    // ============================================================
    // HU-01 CA-04: Validación de campos obligatorios
    // Verifica que código y nombre no estén vacíos antes de registrar
    // ============================================================
    private void validarCamposObligatorios(String codigo, String nombre) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new RuntimeException("Debe completar todos los campos obligatorios");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("Debe completar todos los campos obligatorios");
        }
    }

    // ============================================================
    // HU-01: REGISTRO DE PRODUCTOS
    // CA-01: Producto guardado con código único
    // CA-02: Validación de código duplicado
    // CA-03: Precio venta debe ser mayor al costo
    // CA-04: Validación de campos obligatorios
    // ============================================================
    public void registrarProducto(String codigo, String nombre, double costo, double venta, int stock, int stockMin, Categoria cat) {
        validarCamposObligatorios(codigo, nombre);  // HU-01 CA-04
        if (buscarPorCodigo(codigo) != null) {       // HU-01 CA-02
            throw new RuntimeException("El código ya está registrado.");
        }
        if (venta <= costo) {                        // HU-01 CA-03
            throw new RuntimeException("El precio de venta debe ser mayor al precio de costo.");
        }
        Producto nuevo = new Producto(codigo, nombre, costo, venta, stock, stockMin, cat);  // HU-01 CA-01
        catalogo.add(nuevo);
    }

    // ============================================================
    // HU-02: BÚSQUEDA DE PRODUCTOS
    // CA-03: Búsqueda exacta por código único
    // ============================================================
    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : catalogo) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null; 
    }

    // ============================================================
    // HU-02: BÚSQUEDA DE PRODUCTOS
    // CA-01: Búsqueda por nombre devuelve productos con stock
    // CA-02: Lanza excepción si no hay resultados
    // CA-04: Ignora mayúsculas/minúsculas (case-insensitive)
    // ============================================================
    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultados = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.getNombre().toLowerCase().contains(texto.toLowerCase()) && p.isActivo()) {  // HU-02 CA-04
                resultados.add(p);
            }
        }
        
        if (resultados.isEmpty()) {                  // HU-02 CA-02
            throw new RuntimeException("No se encontraron productos con ese criterio");
        }
        return resultados;                            // HU-02 CA-01
    }

    // ============================================================
    // HU-03: ACTUALIZACIÓN DE PRODUCTOS
    // CA-01: Actualiza precio y stock mínimo
    // CA-02: Código es campo no modificable (sin setter en Producto.java)
    // CA-04: Stock mínimo no puede ser negativo
    // ============================================================
    public void actualizarProducto(String codigo, double nuevoPrecioVenta, int nuevoStockMinimo) {
        if (nuevoStockMinimo < 0) {                   // HU-03 CA-04
            throw new RuntimeException("El límite mínimo no puede ser menor a cero");
        }
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setPrecioVenta(nuevoPrecioVenta);
            p.setStockMinimo(nuevoStockMinimo);       // HU-03 CA-01
        }
    }

    // ============================================================
    // HU-04: DESACTIVACIÓN DE PRODUCTOS
    // CA-01: Producto cambia a estado inactivo
    // ============================================================
    public void desactivarProducto(String codigo) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setActivo(false);                       // HU-04 CA-01
        }
    }

    // ============================================================
    // HU-04: DESACTIVACIÓN DE PRODUCTOS
    // CA-04: Reactivar producto inactivo
    // ============================================================
    public void reactivarProducto(String codigo) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setActivo(true);                        // HU-04 CA-04
        }
    }

    public List<Producto> getCatalogo() {
        return catalogo;
    }

    // ============================================================
    // HU-05: REGISTRO DE ENTRADA DE STOCK
    // CA-01: Stock aumenta y se registra movimiento ENTRADA
    // CA-02: Cantidad debe ser mayor a cero
    // CA-03: Nuevo stock refleja la suma
    // CA-04: No permite entrada a producto desactivado
    // ============================================================
    public void registrarEntrada(String codigo, int cantidad, String username) {
        if (cantidad <= 0) {                          // HU-05 CA-02
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        Producto p = buscarPorCodigo(codigo);
        if (p == null) {
            throw new RuntimeException("Producto no encontrado");
        }
        if (!p.isActivo()) {                          // HU-05 CA-04
            throw new RuntimeException("No se puede registrar entrada. El repuesto está desactivado");
        }
        p.setStock(p.getStock() + cantidad);         // HU-05 CA-01, CA-03
        historial.add(new MovimientoStock("ENTRADA", p, cantidad, username, "Compra a proveedor"));  // HU-05 CA-01
    }

    // ============================================================
    // HU-06: REGISTRO DE SALIDA DE STOCK (VENTA)
    // CA-01: Stock disminuye y se genera movimiento SALIDA
    // CA-02: Valida stock disponible
    // CA-03: Alerta automática si stock <= mínimo
    // CA-04: Cantidad debe ser mayor a cero
    // ============================================================
    public void registrarVenta(String codigo, int cantidad, String username) {
        if (cantidad <= 0) {                          // HU-06 CA-04
            throw new RuntimeException("Debe ingresar una cantidad válida mayor a cero");
        }
        Producto p = buscarPorCodigo(codigo);
        if (p == null || !p.isActivo()) {             // HU-04 CA-02
            throw new RuntimeException("Producto no disponible");
        }
        if (p.getStock() < cantidad) {                 // HU-06 CA-02
            throw new RuntimeException("Stock insuficiente. Disponible: " + p.getStock() + " unidades");
        }
        
        p.setStock(p.getStock() - cantidad);          // HU-06 CA-01
        historial.add(new MovimientoStock("SALIDA", p, cantidad, username, "Venta al cliente"));   // HU-06 CA-01
        
        if (p.getStock() <= p.getStockMinimo()) {     // HU-06 CA-03
            System.out.println("ALERTA AUTOMÁTICA: El producto " + p.getNombre() + " requiere reposición.");
        }
    }

    // ============================================================
    // HU-07: ALERTA DE STOCK MÍNIMO
    // CA-02: Lista productos con stock <= mínimo y diferencia
    // CA-03: Retorna lista vacía si no hay alertas
    // ============================================================
    public List<Producto> obtenerAlertasStock() {
        List<Producto> alertas = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.isActivo() && p.getStock() <= p.getStockMinimo()) {  // HU-07 CA-02
                alertas.add(p);
            }
        }
        return alertas;                               // HU-07 CA-03
    }

    // ============================================================
    // HU-07: ALERTA DE STOCK MÍNIMO
    // CA-04: Exportar lista de productos urgentes a reponer
    // ============================================================
    public List<Producto> exportarListaCompra() {
        return obtenerAlertasStock();                  // HU-07 CA-04
    }

    // ============================================================
    // HU-08: GESTIÓN DE CATEGORÍAS
    // CA-01: Crea categoría nueva
    // CA-02: Evita categoría con nombre duplicado
    // ============================================================
    public Categoria crearCategoria(String nombre) {
        for (Categoria c : categorias) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {  // HU-08 CA-02
                throw new RuntimeException("La categoría ya existe");
            }
        }
        Categoria c = new Categoria(nombre);
        categorias.add(c);                             // HU-08 CA-01
        return c;
    }

    // ============================================================
    // HU-08: GESTIÓN DE CATEGORÍAS
    // CA-03: Bloquea eliminación si hay productos vinculados
    // CA-04: Permite eliminar categoría sin productos
    // ============================================================
    public void eliminarCategoria(String nombre) {
        int productosVinculados = 0;
        for (Producto p : catalogo) {
            if (p.getCategoria().getNombre().equalsIgnoreCase(nombre)) {  // HU-08 CA-03
                productosVinculados++;
            }
        }
        if (productosVinculados > 0) {                // HU-08 CA-03
            throw new RuntimeException("No se puede eliminar. Hay " + productosVinculados + " productos vinculados a esta categoría");
        }
        categorias.removeIf(c -> c.getNombre().equalsIgnoreCase(nombre));  // HU-08 CA-04
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    // ============================================================
    // HU-09: GESTIÓN DE PROVEEDORES
    // CA-01: Registra proveedor con RUC válido de 11 dígitos
    // CA-02: Valida RUC debe tener exactamente 11 dígitos
    // ============================================================
    public Proveedor registrarProveedor(String ruc, String razonSocial) {
        if (ruc == null || ruc.length() != 11) {      // HU-09 CA-02
            throw new RuntimeException("RUC inválido. Debe tener 11 dígitos");
        }
        Proveedor nuevo = new Proveedor(ruc, razonSocial);  // HU-09 CA-01
        proveedores.add(nuevo);
        return nuevo;
    }

    // ============================================================
    // HU-09: GESTIÓN DE PROVEEDORES
    // CA-04: Actualiza teléfono y email manteniendo RUC original
    // ============================================================
    public void actualizarProveedor(String ruc, String telefono, String email) {
        for (Proveedor prov : proveedores) {
            if (prov.getRuc().equals(ruc)) {
                prov.setTelefono(telefono);
                prov.setEmail(email);                 // HU-09 CA-04
                return;
            }
        }
        throw new RuntimeException("Proveedor no encontrado");
    }

    // ============================================================
    // HU-09: GESTIÓN DE PROVEEDORES
    // CA-03: Lista productos suministrado por un proveedor
    // ============================================================
    public List<Producto> listarProductosPorProveedor(String ruc) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.getProveedor() != null && p.getProveedor().getRuc().equals(ruc)) {  // HU-09 CA-03
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    // HU-09: Vincular producto con proveedor
    public void asignarProveedor(String codigoProducto, Proveedor proveedor) {
        Producto p = buscarPorCodigo(codigoProducto);
        if (p != null) {
            p.setProveedor(proveedor);
        }
    }

    // ============================================================
    // HU-10: REPORTE DE INVENTARIO ACTUAL
    // CA-01: Muestra código, nombre, stock, precio y valor total
    // CA-02: Filtro de productos bajo mínimo
    // CA-03: Valor total consolidado al final
    // CA-04: Filtro por categoría específica
    // ============================================================
    public void generarReporteInventario(boolean soloBajoMinimo, String nombreCategoria) {
        double valorTotal = 0;
        List<Producto> lista = catalogo;
        
        // HU-10 CA-04: Filtrar por categoría
        if (nombreCategoria != null && !nombreCategoria.equals("TODAS")) {
            lista = lista.stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria))
                .collect(Collectors.toList());
        }
        
        // HU-10 CA-02: Filtrar productos bajo mínimo
        if (soloBajoMinimo) {
            lista = lista.stream()
                .filter(p -> p.isActivo() && p.getStock() <= p.getStockMinimo())
                .collect(Collectors.toList());
        }
        
        // HU-10 CA-01: Listar productos con valores
        for (Producto p : lista) {
            if (!soloBajoMinimo || p.isActivo()) {
                double subtotal = p.getStock() * p.getPrecioCosto();
                valorTotal += subtotal;
                System.out.println(p.getCodigo() + " | " + p.getNombre() + " | Stock: " + p.getStock() + " | Valor: S/." + subtotal);
            }
        }
        // HU-10 CA-03: Valor total consolidado
        System.out.println("VALOR TOTAL CONSOLIDADO: S/." + valorTotal);
    }

    public void generarReporteInventario(boolean soloBajoMinimo) {
        generarReporteInventario(soloBajoMinimo, "TODAS");
    }

    // ============================================================
    // HU-11: REPORTE DE MOVIMIENTOS
    // CA-01: Filtro por rango de fechas
    // CA-02: Filtro por tipo (SALIDA, ENTRADA, AJUSTE)
    // CA-03: Historial es solo lectura (no hay método eliminar)
    // CA-04: Valida fecha inicio menor a fecha fin
    // ============================================================
    public List<MovimientoStock> obtenerHistorial(String filtroTipo, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // HU-11 CA-04: Validar rango de fechas
        if (fechaInicio != null && fechaFin != null) {
            if (fechaInicio.isAfter(fechaFin)) {
                throw new RuntimeException("Rango de fechas inválido");
            }
        }
        
        List<MovimientoStock> filtrado = new ArrayList<>();
        for (MovimientoStock m : historial) {
            boolean cumpleTipo = filtroTipo.equals("TODOS") || m.getTipo().equals(filtroTipo);  // HU-11 CA-02
            boolean cumpleFecha = true;
            if (fechaInicio != null && fechaFin != null) {  // HU-11 CA-01
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

    // ============================================================
    // HU-14: AJUSTE DE INVENTARIO
    // CA-01: Actualiza stock y registra movimiento tipo AJUSTE
    // CA-02: Motivo debe tener al menos 10 caracteres
    // CA-03: Historial muestra el ajuste con todos sus datos
    // CA-04: No permite ajuste que resulte en stock negativo
    // ============================================================
    public void ajustarInventario(String codigo, int nuevaCantidad, String motivo, String username) {
        if (motivo == null || motivo.length() < 10) {  // HU-14 CA-02
            throw new RuntimeException("El motivo del ajuste debe tener al menos 10 caracteres");
        }
        if (nuevaCantidad < 0) {                       // HU-14 CA-04
            throw new RuntimeException("El ajuste no puede resultar en stock negativo");
        }
        
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setStock(nuevaCantidad);                // HU-14 CA-01, CA-03
            historial.add(new MovimientoStock("AJUSTE", p, nuevaCantidad, username, motivo));  // HU-14 CA-03
        }
    }

    // ============================================================
    // HU-15: PRODUCTOS MÁS VENDIDOS
    // CA-01: Top 10 productos ordenados por cantidad vendida
    // CA-02: Filtro por año y mes
    // CA-03: Mensaje si no hay ventas en el período
    // CA-04: Desempate alfabético por nombre
    // ============================================================
    public void mostrarProductosMasVendidos(int anio, int mes) {
        Map<String, Integer> ventas = new java.util.HashMap<>();
        
        // HU-15 CA-02: Filtrar por año y mes
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
        
        // HU-15 CA-03: Sin ventas registradas
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas en el período seleccionado");
            return;
        }
        
        // HU-15 CA-04: Ordenar por cantidad y desempate alfabético
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>(ventas.entrySet());
        ranking.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue());
            if (cmp == 0) {
                Producto prodA = buscarPorCodigo(a.getKey());
                Producto prodB = buscarPorCodigo(b.getKey());
                if (prodA != null && prodB != null) {
                    return prodA.getNombre().compareTo(prodB.getNombre());  // HU-15 CA-04
                }
            }
            return cmp;
        });
        
        // HU-15 CA-01: Mostrar top 10
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