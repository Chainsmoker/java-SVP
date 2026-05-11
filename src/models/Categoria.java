package models;

// ============================================================
// MODELO: CATEGORÍA
// Sistema de Inventario de Repuestos (SVP)
// ============================================================
// Representa una categoría de productos.
// HU-08 CA-03: Verifica productos vinculados antes de eliminar.
// ============================================================

public class Categoria {
    private String nombre;  // HU-08 CA-02: Nombre único por categoría
    
    public Categoria(String nombre) { 
        this.nombre = nombre; 
    }
    
    // HU-08 CA-01: Getter de nombre
    public String getNombre() { 
        return nombre; 
    }
}