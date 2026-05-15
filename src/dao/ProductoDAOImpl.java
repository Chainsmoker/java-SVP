package dao;

import models.Producto;
import models.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private Connection conexion;

    public ProductoDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String query = "SELECT p.codigo, p.nombre, p.precio_costo, p.precio_venta, p.stock, " +
                       "p.stock_minimo, p.id_categoria, p.ruc_proveedor, p.activo, c.nombre as cat_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria cat = null;
                int idCat = rs.getInt("id_categoria");
                if (idCat > 0) {
                    cat = new Categoria(idCat, rs.getString("cat_nombre"));
                }

                Producto p = new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio_costo"),
                    rs.getDouble("precio_venta"),
                    rs.getInt("stock"),
                    rs.getInt("stock_minimo"),
                    cat
                );
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar productos: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> listarActivos() {
        List<Producto> lista = new ArrayList<>();
        String query = "SELECT p.codigo, p.nombre, p.precio_costo, p.precio_venta, p.stock, " +
                       "p.stock_minimo, p.id_categoria, p.activo, c.nombre as cat_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                       "WHERE p.activo = TRUE";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria cat = null;
                int idCat = rs.getInt("id_categoria");
                if (idCat > 0) {
                    cat = new Categoria(idCat, rs.getString("cat_nombre"));
                }

                Producto p = new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio_costo"),
                    rs.getDouble("precio_venta"),
                    rs.getInt("stock"),
                    rs.getInt("stock_minimo"),
                    cat
                );
                p.setActivo(true);
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar productos activos: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        String query = "SELECT p.codigo, p.nombre, p.precio_costo, p.precio_venta, p.stock, " +
                       "p.stock_minimo, p.id_categoria, p.activo, c.nombre as cat_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                       "WHERE p.codigo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = null;
                    int idCat = rs.getInt("id_categoria");
                    if (idCat > 0) {
                        cat = new Categoria(idCat, rs.getString("cat_nombre"));
                    }

                    Producto p = new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precio_costo"),
                        rs.getDouble("precio_venta"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        cat
                    );
                    p.setActivo(rs.getBoolean("activo"));
                    return p;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar producto: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> lista = new ArrayList<>();
        String query = "SELECT p.codigo, p.nombre, p.precio_costo, p.precio_venta, p.stock, " +
                       "p.stock_minimo, p.id_categoria, p.activo, c.nombre as cat_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                       "WHERE p.nombre LIKE ? AND p.activo = TRUE";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria cat = null;
                    int idCat = rs.getInt("id_categoria");
                    if (idCat > 0) {
                        cat = new Categoria(idCat, rs.getString("cat_nombre"));
                    }

                    Producto p = new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precio_costo"),
                        rs.getDouble("precio_venta"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        cat
                    );
                    p.setActivo(true);
                    lista.add(p);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar productos por nombre: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> buscarPorCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();
        String query = "SELECT p.codigo, p.nombre, p.precio_costo, p.precio_venta, p.stock, " +
                       "p.stock_minimo, p.id_categoria, p.activo, c.nombre as cat_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                       "WHERE p.id_categoria = ? AND p.activo = TRUE";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria cat = new Categoria(idCategoria, rs.getString("cat_nombre"));
                    Producto p = new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precio_costo"),
                        rs.getDouble("precio_venta"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        cat
                    );
                    p.setActivo(true);
                    lista.add(p);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar productos por categoria: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> productosBajoStock() {
        List<Producto> lista = new ArrayList<>();
        String query = "SELECT p.codigo, p.nombre, p.precio_costo, p.precio_venta, p.stock, " +
                       "p.stock_minimo, p.id_categoria, c.nombre as cat_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                       "WHERE p.stock <= p.stock_minimo AND p.activo = TRUE";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria cat = null;
                int idCat = rs.getInt("id_categoria");
                if (idCat > 0) {
                    cat = new Categoria(idCat, rs.getString("cat_nombre"));
                }

                Producto p = new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio_costo"),
                    rs.getDouble("precio_venta"),
                    rs.getInt("stock"),
                    rs.getInt("stock_minimo"),
                    cat
                );
                p.setActivo(true);
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar productos bajo stock: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(Producto producto) {
        if (buscarPorCodigo(producto.getCodigo()) != null) {
            throw new RuntimeException("El código del producto ya existe");
        }
        String query = "INSERT INTO productos(codigo, nombre, precio_costo, precio_venta, stock, " +
                       "stock_minimo, id_categoria, ruc_proveedor, activo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecioCosto());
            ps.setDouble(4, producto.getPrecioVenta());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getStockMinimo());
            if (producto.getCategoria() != null) {
                ps.setInt(7, producto.getCategoria().getId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            if (producto.getProveedor() != null) {
                ps.setString(8, producto.getProveedor().getRuc());
            } else {
                ps.setNull(8, Types.VARCHAR);
            }
            ps.setBoolean(9, producto.isActivo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al guardar producto: " + ex.getMessage());
        }
    }

    @Override
    public void actualizar(Producto producto) {
        String query = "UPDATE productos SET nombre = ?, precio_venta = ?, stock_minimo = ? WHERE codigo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecioVenta());
            ps.setInt(3, producto.getStockMinimo());
            ps.setString(4, producto.getCodigo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar producto: " + ex.getMessage());
        }
    }

    @Override
    public void desactivar(String codigo) {
        String query = "UPDATE productos SET activo = FALSE WHERE codigo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al desactivar producto: " + ex.getMessage());
        }
    }

    @Override
    public void reactivar(String codigo) {
        String query = "UPDATE productos SET activo = TRUE WHERE codigo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al reactivar producto: " + ex.getMessage());
        }
    }

    @Override
    public void asignarProveedor(String codigoProducto, String rucProveedor) {
        String query = "UPDATE productos SET ruc_proveedor = ? WHERE codigo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, rucProveedor);
            ps.setString(2, codigoProducto);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al asignar proveedor: " + ex.getMessage());
        }
    }
}