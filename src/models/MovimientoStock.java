package models;

import java.time.LocalDateTime;

public class MovimientoStock {
    private LocalDateTime fecha;
    private String tipo;
    private Producto producto;
    private int cantidad;
    private String usuarioResponsable;
    private String motivo;

    public MovimientoStock(String tipo, Producto producto, int cantidad, String usuarioResponsable, String motivo) {
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.producto = producto;
        this.cantidad = cantidad;
        this.usuarioResponsable = usuarioResponsable;
        this.motivo = motivo;
    }
    
    public String getTipo() { 
        return tipo; 
    }

    public int getCantidad() { 
        return cantidad; 
    }

    public Producto getProducto() { 
        return producto; 
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public String getMotivo() {
        return motivo;
    }
}