package controllers;

import dao.*;
import models.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class InventarioController {
    private Connection conexion;
    private CategoriaDAO categoriaDAO;
    private ProductoDAO productoDAO;
    private ProveedorDAO proveedorDAO;
    private MovimientoStockDAO movimientoDAO;

    public InventarioController(Connection conexion) {
        this.conexion = conexion;
        this.categoriaDAO = new CategoriaDAOImpl(conexion);
        this.productoDAO = new ProductoDAOImpl(conexion);
        this.proveedorDAO = new ProveedorDAOImpl(conexion);
        this.movimientoDAO = new MovimientoStockDAOImpl(conexion);
    }

    private void validarCamposObligatorios(String codigo, String nombre) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new RuntimeException("Debe completar todos los campos obligatorios");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("Debe completar todos los campos obligatorios");
        }
    }

    public void registrarProducto(String codigo, String nombre, double costo, double venta,
                                  int stock, int stockMin, Categoria cat) {
        validarCamposObligatorios(codigo, nombre);
        if (buscarPorCodigo(codigo) != null) {
            throw new RuntimeException("El código ya está registrado.");
        }
        if (venta <= costo) {
            throw new RuntimeException("El precio de venta debe ser mayor al precio de costo.");
        }
        Producto nuevo = new Producto(codigo, nombre, costo, venta, stock, stockMin, cat);
        nuevo.setActivo(true);
        productoDAO.guardar(nuevo);
    }

    public Producto buscarPorCodigo(String codigo) {
        return productoDAO.buscarPorCodigo(codigo);
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultados = productoDAO.buscarPorNombre(texto);
        if (resultados.isEmpty()) {
            throw new RuntimeException("No se encontraron productos con ese criterio");
        }
        return resultados;
    }

    public void actualizarProducto(String codigo, double nuevoPrecioVenta, int nuevoStockMinimo) {
        if (nuevoStockMinimo < 0) {
            throw new RuntimeException("El límite mínimo no puede ser menor a cero");
        }
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setPrecioVenta(nuevoPrecioVenta);
            p.setStockMinimo(nuevoStockMinimo);
            productoDAO.actualizar(p);
        }
    }

    public void desactivarProducto(String codigo) {
        productoDAO.desactivar(codigo);
    }

    public void reactivarProducto(String codigo) {
        productoDAO.reactivar(codigo);
    }

    public List<Producto> getCatalogo() {
        return productoDAO.listarTodos();
    }

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
        productoDAO.actualizar(p);
        MovimientoStock m = new MovimientoStock("ENTRADA", p, cantidad, username, "Compra a proveedor");
        movimientoDAO.guardar(m);
    }

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
        productoDAO.actualizar(p);
        MovimientoStock m = new MovimientoStock("SALIDA", p, cantidad, username, "Venta al cliente");
        movimientoDAO.guardar(m);

        if (p.getStock() <= p.getStockMinimo()) {
            System.out.println("ALERTA AUTOMATICA: El producto " + p.getNombre() + " requiere reposicion.");
        }
    }

    public List<Producto> obtenerAlertasStock() {
        return productoDAO.productosBajoStock();
    }

    public List<Producto> exportarListaCompra() {
        return obtenerAlertasStock();
    }

    public Categoria crearCategoria(String nombre) {
        Categoria existente = categoriaDAO.buscarPorNombre(nombre);
        if (existente != null) {
            throw new RuntimeException("La categoría ya existe");
        }
        Categoria c = new Categoria(nombre);
        categoriaDAO.guardar(c);
        return c;
    }

    public void eliminarCategoria(String nombre) {
        List<Producto> productos = productoDAO.listarTodos();
        int productosVinculados = 0;
        for (Producto p : productos) {
            if (p.getCategoria() != null && p.getCategoria().getNombre().equalsIgnoreCase(nombre)) {
                productosVinculados++;
            }
        }
        if (productosVinculados > 0) {
            throw new RuntimeException("No se puede eliminar. Hay " + productosVinculados + " productos vinculados a esta categoría");
        }
        categoriaDAO.eliminar(nombre);
    }

    public List<Categoria> getCategorias() {
        return categoriaDAO.listarTodos();
    }

    public Proveedor registrarProveedor(String ruc, String razonSocial) {
        if (ruc == null || ruc.length() != 11) {
            throw new RuntimeException("RUC inválido. Debe tener 11 dígitos");
        }
        Proveedor nuevo = new Proveedor(ruc, razonSocial);
        proveedorDAO.guardar(nuevo);
        return nuevo;
    }

    public void actualizarProveedor(String ruc, String telefono, String email) {
        Proveedor p = proveedorDAO.buscarPorRuc(ruc);
        if (p != null) {
            p.setTelefono(telefono);
            p.setEmail(email);
            proveedorDAO.actualizar(p);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }

    public List<Producto> listarProductosPorProveedor(String ruc) {
        List<Producto> resultado = productoDAO.listarTodos();
        List<Producto> filtrados = new java.util.ArrayList<>();
        for (Producto p : resultado) {
            if (p.getProveedor() != null && p.getProveedor().getRuc().equals(ruc)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public List<Proveedor> getProveedores() {
        return proveedorDAO.listarTodos();
    }

    public void asignarProveedor(String codigoProducto, Proveedor proveedor) {
        productoDAO.asignarProveedor(codigoProducto, proveedor.getRuc());
    }

    public void generarReporteInventario(boolean soloBajoMinimo) {
        generarReporteInventario(soloBajoMinimo, "TODAS");
    }

    public void generarReporteInventario(boolean soloBajoMinimo, String nombreCategoria) {
        List<Producto> lista = productoDAO.listarActivos();
        double valorTotal = 0;

        if (soloBajoMinimo) {
            lista = productoDAO.productosBajoStock();
        }

        for (Producto p : lista) {
            if (!soloBajoMinimo || p.isActivo()) {
                if (nombreCategoria != null && !nombreCategoria.equals("TODAS")) {
                    if (p.getCategoria() == null || !p.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria)) {
                        continue;
                    }
                }
                double subtotal = p.getStock() * p.getPrecioCosto();
                valorTotal += subtotal;
                System.out.println(p.getCodigo() + " | " + p.getNombre() + " | Stock: " + p.getStock() + " | Valor: S/." + subtotal);
            }
        }
        System.out.println("VALOR TOTAL CONSOLIDADO: S/." + valorTotal);
    }

    public List<MovimientoStock> obtenerHistorial(String filtroTipo) {
        return obtenerHistorial(filtroTipo, null, null);
    }

    public List<MovimientoStock> obtenerHistorial(String filtroTipo, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            if (fechaInicio.isAfter(fechaFin)) {
                throw new RuntimeException("Rango de fechas inválido");
            }
            return movimientoDAO.listarPorRangoFechas(fechaInicio, fechaFin);
        }
        if (filtroTipo.equals("TODOS")) {
            return movimientoDAO.listarTodos();
        }
        return movimientoDAO.listarPorTipo(filtroTipo);
    }

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
            productoDAO.actualizar(p);
            MovimientoStock m = new MovimientoStock("AJUSTE", p, nuevaCantidad, username, motivo);
            movimientoDAO.guardar(m);
        }
    }

    public java.util.List<Producto> getTopVendidos(int anio, int mes) {
        java.util.List<MovimientoStock> todos = movimientoDAO.listarTodos();
        java.util.Map<String, Integer> ventas = new java.util.HashMap<>();

        for (MovimientoStock m : todos) {
            if (m.getTipo().equals("SALIDA")) {
                int year = m.getFecha().getYear();
                int month = m.getFecha().getMonthValue();
                boolean matchAnio = (anio == 0 || year == anio);
                boolean matchMes = (mes == 0 || month == mes);

                if (matchAnio && matchMes) {
                    String codigo = m.getProducto().getCodigo();
                    int cantidadActual = ventas.getOrDefault(codigo, 0);
                    ventas.put(codigo, cantidadActual + m.getCantidad());
                }
            }
        }

        if (ventas.isEmpty()) {
            return new java.util.ArrayList<>();
        }

        java.util.List<java.util.Map.Entry<String, Integer>> ranking = new java.util.ArrayList<>(ventas.entrySet());

        for (int i = 0; i < ranking.size() - 1; i++) {
            for (int j = 0; j < ranking.size() - i - 1; j++) {
                int cmp = ranking.get(j + 1).getValue().compareTo(ranking.get(j).getValue());
                if (cmp > 0) {
                    java.util.Map.Entry<String, Integer> temp = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, temp);
                } else if (cmp == 0) {
                    Producto prodA = productoDAO.buscarPorCodigo(ranking.get(j).getKey());
                    Producto prodB = productoDAO.buscarPorCodigo(ranking.get(j + 1).getKey());
                    if (prodA != null && prodB != null) {
                        int cmpNombre = prodA.getNombre().compareTo(prodB.getNombre());
                        if (cmpNombre < 0) {
                            java.util.Map.Entry<String, Integer> temp = ranking.get(j);
                            ranking.set(j, ranking.get(j + 1));
                            ranking.set(j + 1, temp);
                        }
                    }
                }
            }
        }

        java.util.List<Producto> resultado = new java.util.ArrayList<>();
        int limit = Math.min(5, ranking.size());
        for (int i = 0; i < limit; i++) {
            Producto p = productoDAO.buscarPorCodigo(ranking.get(i).getKey());
            if (p != null) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public java.util.Map<String, Integer> getVentasPorProducto(int anio, int mes) {
        java.util.List<MovimientoStock> todos = movimientoDAO.listarTodos();
        java.util.Map<String, Integer> ventas = new java.util.HashMap<>();

        for (MovimientoStock m : todos) {
            if (m.getTipo().equals("SALIDA")) {
                int year = m.getFecha().getYear();
                int month = m.getFecha().getMonthValue();
                boolean matchAnio = (anio == 0 || year == anio);
                boolean matchMes = (mes == 0 || month == mes);

                if (matchAnio && matchMes) {
                    String codigo = m.getProducto().getCodigo();
                    int cantidadActual = ventas.getOrDefault(codigo, 0);
                    ventas.put(codigo, cantidadActual + m.getCantidad());
                }
            }
        }
        return ventas;
    }

    public void mostrarProductosMasVendidos(int anio, int mes) {
        java.util.List<Producto> top = getTopVendidos(anio, mes);
        java.util.Map<String, Integer> ventas = getVentasPorProducto(anio, mes);
        for (Producto p : top) {
            System.out.println(p.getNombre() + " | " + ventas.get(p.getCodigo()) + " unidades");
        }
    }

    public void mostrarProductosMasVendidos() {
        mostrarProductosMasVendidos(0, 0);
    }
}