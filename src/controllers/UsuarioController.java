package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Usuario;

// ============================================================
// CONTROLADOR DE USUARIOS - SISTEMA DE REPUESTOS (SVP)
// ============================================================
// Este controlador implementa las historias de usuario HU-12
// y HU-13 con sus criterios de aceptación.
// ============================================================

public class UsuarioController {
    private List<Usuario> usuarios = new ArrayList<>();

    public UsuarioController() {
        usuarios.add(new Usuario("admin", "1234", "ADMIN"));
    }

    // ============================================================
    // HU-12: AUTENTICACIÓN DE USUARIOS
    // CA-01: Credenciales correctas permiten acceso según rol
    // CA-02: Contraseña incorrecta muestra error
    // CA-03: Vendedor no puede acceder a gestión de usuarios
    // CA-04: Usuario desactivado no puede iniciar sesión
    // ============================================================
    public Usuario iniciarSesion(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {  // HU-12 CA-01
                if (!u.isActivo()) throw new RuntimeException("Cuenta desactivada");       // HU-12 CA-04
                return u;
            }
        }
        throw new RuntimeException("Credenciales incorrectas");                             // HU-12 CA-02
    }

    // ============================================================
    // HU-13: GESTIÓN DE USUARIOS
    // CA-01: Crea nuevo usuario con username, password y rol
    // CA-04: Verifica que el username no esté duplicado
    // ============================================================
    public void crearUsuario(Usuario adminActual, String username, String password, String rol) {
        if (!adminActual.getRol().equals("ADMIN")) throw new RuntimeException("Acceso denegado. Se requiere rol Administrador");  // HU-12 CA-03
        
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {         // HU-13 CA-04
                throw new RuntimeException("Nombre de usuario duplicado");
            }
        }
        usuarios.add(new Usuario(username, password, rol)); // HU-13 CA-01
    }

    // ============================================================
    // HU-13: GESTIÓN DE USUARIOS
    // CA-02: Desactiva cuenta de empleado
    // CA-03: No permite desactivar el único administrador activo
    // ============================================================
    public void desactivarUsuario(Usuario adminActual, String username) {
        if (!adminActual.getRol().equals("ADMIN")) throw new RuntimeException("Acceso denegado.");  // HU-12 CA-03
        
        long adminsActivos = usuarios.stream().filter(u -> u.getRol().equals("ADMIN") && u.isActivo()).count();
        
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                // HU-13 CA-03: Previene desactivar único admin
                if (u.getRol().equals("ADMIN") && adminsActivos == 1) {
                    throw new RuntimeException("No se puede desactivar el único administrador activo");
                }
                u.setActivo(false);                          // HU-13 CA-02
                return;
            }
        }
    }
}