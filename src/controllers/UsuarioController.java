package controllers;

import dao.UsuarioDAO;
import dao.UsuarioDAOImpl;
import models.Usuario;
import java.sql.Connection;
import java.util.List;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;

    public UsuarioController(Connection conexion) {
        this.usuarioDAO = new UsuarioDAOImpl(conexion);
    }

    public Usuario iniciarSesion(String username, String password) {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                if (!u.isActivo()) {
                    throw new RuntimeException("Cuenta desactivada");
                }
                return u;
            }
        }
        throw new RuntimeException("Credenciales incorrectas");
    }

    public void crearUsuario(Usuario adminActual, String username, String password, String rol) {
        if (!adminActual.getRol().equals("ADMIN")) {
            throw new RuntimeException("Acceso denegado. Se requiere rol Administrador");
        }

        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                throw new RuntimeException("Nombre de usuario duplicado");
            }
        }
        Usuario nuevo = new Usuario(username, password, rol);
        nuevo.setActivo(true);
        usuarioDAO.guardar(nuevo);
    }

    public void desactivarUsuario(Usuario adminActual, String username) {
        if (!adminActual.getRol().equals("ADMIN")) {
            throw new RuntimeException("Acceso denegado.");
        }

        List<Usuario> usuarios = usuarioDAO.listarTodos();
        long adminsActivos = 0;
        for (Usuario u : usuarios) {
            if (u.getRol().equals("ADMIN") && u.isActivo()) {
                adminsActivos++;
            }
        }

        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                if (u.getRol().equals("ADMIN") && adminsActivos == 1) {
                    throw new RuntimeException("No se puede desactivar el unico administrador activo");
                }
                usuarioDAO.desactivar(username);
                return;
            }
        }
    }

    public void reactivarUsuario(String username) {
        usuarioDAO.reactivar(username);
    }

    public List<Usuario> getUsuarios() {
        return usuarioDAO.listarTodos();
    }
}