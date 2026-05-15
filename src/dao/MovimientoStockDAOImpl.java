package dao;

import models.MovimientoStock;
import models.Producto;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoStockDAOImpl implements MovimientoStockDAO {
    private Connection conexion;

    public MovimientoStockDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<MovimientoStock> listarTodos() {
        List<MovimientoStock> lista = new ArrayList<>();
        String query = "SELECT m.id, m.fecha, m.tipo, m.codigo_producto, m.cantidad, " +
                       "m.usuario_responsable, m.motivo, p.nombre as producto_nombre " +
                       "FROM movimientos_stock m " +
                       "JOIN productos p ON m.codigo_producto = p.codigo " +
                       "ORDER BY m.fecha DESC";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto prod = new Producto();
                prod.setCodigo(rs.getString("codigo_producto"));
                prod.setNombre(rs.getString("producto_nombre"));

                MovimientoStock m = new MovimientoStock(
                    rs.getString("tipo"),
                    prod,
                    rs.getInt("cantidad"),
                    rs.getString("usuario_responsable"),
                    rs.getString("motivo")
                );
                m.setId(rs.getInt("id"));
                m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                lista.add(m);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar movimientos: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<MovimientoStock> listarPorTipo(String tipo) {
        List<MovimientoStock> lista = new ArrayList<>();
        String query = "SELECT m.id, m.fecha, m.tipo, m.codigo_producto, m.cantidad, " +
                       "m.usuario_responsable, m.motivo, p.nombre as producto_nombre " +
                       "FROM movimientos_stock m " +
                       "JOIN productos p ON m.codigo_producto = p.codigo " +
                       "WHERE m.tipo = ? ORDER BY m.fecha DESC";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto prod = new Producto();
                    prod.setCodigo(rs.getString("codigo_producto"));
                    prod.setNombre(rs.getString("producto_nombre"));

                    MovimientoStock m = new MovimientoStock(
                        rs.getString("tipo"),
                        prod,
                        rs.getInt("cantidad"),
                        rs.getString("usuario_responsable"),
                        rs.getString("motivo")
                    );
                    m.setId(rs.getInt("id"));
                    m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    lista.add(m);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar movimientos por tipo: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<MovimientoStock> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        List<MovimientoStock> lista = new ArrayList<>();
        String query = "SELECT m.id, m.fecha, m.tipo, m.codigo_producto, m.cantidad, " +
                       "m.usuario_responsable, m.motivo, p.nombre as producto_nombre " +
                       "FROM movimientos_stock m " +
                       "JOIN productos p ON m.codigo_producto = p.codigo " +
                       "WHERE m.fecha BETWEEN ? AND ? ORDER BY m.fecha DESC";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto prod = new Producto();
                    prod.setCodigo(rs.getString("codigo_producto"));
                    prod.setNombre(rs.getString("producto_nombre"));

                    MovimientoStock m = new MovimientoStock(
                        rs.getString("tipo"),
                        prod,
                        rs.getInt("cantidad"),
                        rs.getString("usuario_responsable"),
                        rs.getString("motivo")
                    );
                    m.setId(rs.getInt("id"));
                    m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    lista.add(m);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar movimientos por fechas: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<MovimientoStock> listarPorProducto(String codigoProducto) {
        List<MovimientoStock> lista = new ArrayList<>();
        String query = "SELECT m.id, m.fecha, m.tipo, m.codigo_producto, m.cantidad, " +
                       "m.usuario_responsable, m.motivo, p.nombre as producto_nombre " +
                       "FROM movimientos_stock m " +
                       "JOIN productos p ON m.codigo_producto = p.codigo " +
                       "WHERE m.codigo_producto = ? ORDER BY m.fecha DESC";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, codigoProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto prod = new Producto();
                    prod.setCodigo(rs.getString("codigo_producto"));
                    prod.setNombre(rs.getString("producto_nombre"));

                    MovimientoStock m = new MovimientoStock(
                        rs.getString("tipo"),
                        prod,
                        rs.getInt("cantidad"),
                        rs.getString("usuario_responsable"),
                        rs.getString("motivo")
                    );
                    m.setId(rs.getInt("id"));
                    m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    lista.add(m);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar movimientos por producto: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public void guardar(MovimientoStock movimiento) {
        String query = "INSERT INTO movimientos_stock(tipo, codigo_producto, cantidad, " +
                       "usuario_responsable, motivo) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, movimiento.getTipo());
            ps.setString(2, movimiento.getProducto().getCodigo());
            ps.setInt(3, movimiento.getCantidad());
            ps.setString(4, movimiento.getUsuarioResponsable());
            ps.setString(5, movimiento.getMotivo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al guardar movimiento: " + ex.getMessage());
        }
    }
}