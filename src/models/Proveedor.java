package models;

public class Proveedor {
    private String ruc;
    private String razonSocial;
    private String telefono;
    private String email;
    
    public Proveedor(String ruc, String razonSocial) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.telefono = "";
        this.email = "";
    }
    
    public String getRuc() { 
        return ruc; 
    }
    
    public String getRazonSocial() { 
        return razonSocial; 
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}