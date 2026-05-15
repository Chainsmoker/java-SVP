package dao;

import models.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAOImpl implements ProveedorDAO {
    private Connection conexion;

    public ProveedorDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Proveedor> listarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        String query = "SELECT ruc, razon_social, telefono, email FROM proveedores";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedor p = new Proveedor(
                    rs.getString("ruc"),
                    rs.getString("razon_social")
                );
                p.setTelefono(rs.getString("telefono"));
                p.setEmail(rs.getString("email"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar proveedores: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Proveedor buscarPorRuc(String ruc) {
        String query = "SELECT ruc, razon_social, telefono, email FROM proveedores WHERE ruc = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, ruc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proveedor p = new Proveedor(
                        rs.getString("ruc"),
                        rs.getString("razon_social")
                    );
                    p.setTelefono(rs.getString("telefono"));
                    p.setEmail(rs.getString("email"));
                    return p;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar proveedor: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void guardar(Proveedor proveedor) {
        if (buscarPorRuc(proveedor.getRuc()) != null) {
            throw new RuntimeException("El RUC ya está registrado");
        }
        String query = "INSERT INTO proveedores(ruc, razon_social, telefono, email) VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, proveedor.getRuc());
            ps.setString(2, proveedor.getRazonSocial());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al guardar proveedor: " + ex.getMessage());
        }
    }

    @Override
    public void actualizar(Proveedor proveedor) {
        String query = "UPDATE proveedores SET razon_social = ?, telefono = ?, email = ? WHERE ruc = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, proveedor.getRazonSocial());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getEmail());
            ps.setString(4, proveedor.getRuc());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar proveedor: " + ex.getMessage());
        }
    }
}