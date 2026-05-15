package dao;

import models.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private Connection conexion;

    public UsuarioDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT username, password, rol, activo FROM usuarios";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("rol")
                );
                u.setActivo(rs.getBoolean("activo"));
                lista.add(u);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar usuarios: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        String query = "SELECT username, password, rol, activo FROM usuarios WHERE username = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                    );
                    u.setActivo(rs.getBoolean("activo"));
                    return u;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar usuario: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void guardar(Usuario usuario) {
        if (buscarPorUsername(usuario.getUsername()) != null) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        String query = "INSERT INTO usuarios(username, password, rol, activo) VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRol());
            ps.setBoolean(4, usuario.isActivo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al guardar usuario: " + ex.getMessage());
        }
    }

    @Override
    public void desactivar(String username) {
        String query = "UPDATE usuarios SET activo = FALSE WHERE username = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al desactivar usuario: " + ex.getMessage());
        }
    }

    @Override
    public void reactivar(String username) {
        String query = "UPDATE usuarios SET activo = TRUE WHERE username = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al reactivar usuario: " + ex.getMessage());
        }
    }
}