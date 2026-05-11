package models;

// ============================================================
// MODELO: PROVEEDOR
// Sistema de Inventario de Repuestos (SVP)
// ============================================================
// Representa un proveedor de repuestos.
// RUC: Identificador único de 11 dígitos.
// ============================================================

public class Proveedor {
    private String ruc;           // HU-09 CA-01/02: RUC único de 11 dígitos
    private String razonSocial;
    private String telefono;      // HU-09 CA-04: Datos de contacto actualizables
    private String email;         // HU-09 CA-04: Datos de contacto actualizables
    
    public Proveedor(String ruc, String razonSocial) {
        this.ruc = ruc;           // HU-09 CA-02: Validación de 11 dígitos
        this.razonSocial = razonSocial;
        this.telefono = "";
        this.email = "";
    }
    
    // HU-09 CA-01: Getter de RUC (no tiene setter - HU-09 CA-04)
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
    
    // HU-09 CA-04: Actualizar datos de contacto (no RUC)
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // HU-09 CA-04: NO EXISTE setRuc() - El RUC es inmutable
}