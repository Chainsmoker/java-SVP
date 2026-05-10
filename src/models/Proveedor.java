package models;

public class Proveedor {
    private String ruc;
    private String razonSocial;
    
    public Proveedor(String ruc, String razonSocial) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
    }
    
    public String getRuc() { 
        return ruc; 
    }
    
    public String getRazonSocial() { 
        return razonSocial; 
    }
}