package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Usuario;

public class UsuarioController {
    private List<Usuario> usuarios = new ArrayList<>();

    public UsuarioController() {
        usuarios.add(new Usuario("admin", "1234", "ADMIN"));
    }

    // HU-12: Autenticación de Usuarios
    public Usuario iniciarSesion(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                if (!u.isActivo()) throw new RuntimeException("Cuenta desactivada");
                return u;
            }
        }
        throw new RuntimeException("Credenciales incorrectas"); 
    }

    // HU-13: Gestión de Usuarios - Crear
    public void crearUsuario(Usuario adminActual, String username, String password, String rol) {
        if (!adminActual.getRol().equals("ADMIN")) throw new RuntimeException("Acceso denegado. Se requiere rol Administrador");
        
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                throw new RuntimeException("Nombre de usuario duplicado");
            }
        }
        usuarios.add(new Usuario(username, password, rol));
    }

    // HU-13: Gestión de Usuarios - Desactivar
    public void desactivarUsuario(Usuario adminActual, String username) {
        if (!adminActual.getRol().equals("ADMIN")) throw new RuntimeException("Acceso denegado.");
        
        long adminsActivos = usuarios.stream().filter(u -> u.getRol().equals("ADMIN") && u.isActivo()).count();
        
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                if (u.getRol().equals("ADMIN") && adminsActivos == 1) {
                    throw new RuntimeException("No se puede desactivar el único administrador activo"); // CA-03
                }
                u.setActivo(false);
                return;
            }
        }
    }
}