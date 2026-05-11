package models;

// ============================================================
// MODELO: USUARIO
// Sistema de Inventario de Repuestos (SVP)
// ============================================================
// Representa un usuario del sistema con rol y estado.
// Roles: ADMIN (administrador), VENDEDOR
// ============================================================

public class Usuario {
    private String username;
    private String password;
    private String rol;           // ADMIN o VENDEDOR - HU-12 CA-01, HU-12 CA-03
    private boolean activo;      // HU-12 CA-04, HU-13 CA-02

    public Usuario(String username, String password, String rol) {
        this.username = username;  // HU-13 CA-04: Username debe ser único
        this.password = password;
        this.rol = rol;
        this.activo = true;       // Usuario activo por defecto
    }
    
    // HU-12 CA-01: Getter para inicio de sesión
    public String getUsername() { 
        return username; 
    }
    
    public String getPassword() { 
        return password; 
    }

    // HU-12 CA-01: Determina acceso según rol
    // HU-12 CA-03: Verifica rol ADMIN para gestión de usuarios
    public String getRol() { 
        return rol; 
    }

    // HU-12 CA-04: Verificar si cuenta está activa
    public boolean isActivo() { 
        return activo; 
    }

    // HU-13 CA-02: Desactivar usuario
    // HU-13 CA-03: Reactivar usuario
    public void setActivo(boolean activo) { 
        this.activo = activo; 
    }
}