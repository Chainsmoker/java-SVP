package dao;

import models.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {
    private Connection conexion;

    public CategoriaDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();
        String query = "SELECT id, nombre FROM categorias ORDER BY nombre";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria cat = new Categoria(rs.getInt("id"), rs.getString("nombre"));
                lista.add(cat);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar categorias: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Categoria buscarPorNombre(String nombre) {
        String query = "SELECT id, nombre FROM categorias WHERE nombre = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar categoria: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void guardar(Categoria categoria) {
        if (buscarPorNombre(categoria.getNombre()) != null) {
            throw new RuntimeException("La categoría ya existe");
        }
        String query = "INSERT INTO categorias(nombre) VALUES(?)";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, categoria.getNombre());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al guardar categoria: " + ex.getMessage());
        }
    }

    @Override
    public void eliminar(String nombre) {
        String query = "DELETE FROM categorias WHERE nombre = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar categoria: " + ex.getMessage());
        }
    }
}