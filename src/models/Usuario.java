package models;

public class Usuario {
    private String username;
    private String password;
    private String rol;
    private boolean activo;

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = true;
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public String getPassword() { 
        return password; 
    }

    public String getRol() { 
        return rol; 
    }

    public boolean isActivo() { 
        return activo; 
    }

    public void setActivo(boolean activo) { 
        this.activo = activo; 
    }
}