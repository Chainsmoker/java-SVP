package models;

import java.time.LocalDateTime;

// ============================================================
// MODELO: MOVIMIENTO STOCK
// Sistema de Inventario de Repuestos (SVP)
// ============================================================
// Registra movimientos de stock: ENTRADA, SALIDA, AJUSTE.
// HU-11 CA-03: Historial es de solo lectura.
// ============================================================

public class MovimientoStock {
    private LocalDateTime fecha;      // HU-11 CA-01: Fecha y hora del movimiento
    private String tipo;             // ENTRADA, SALIDA, AJUSTE - HU-11 CA-02
    private Producto producto;        // Producto afectado
    private int cantidad;             // Cantidad del movimiento
    private String usuarioResponsable; // Usuario que realizó la acción
    private String motivo;           // HU-14 CA-02: Motivo del ajuste (min 10 chars)

    public MovimientoStock(String tipo, Producto producto, int cantidad, 
                          String usuarioResponsable, String motivo) {
        this.fecha = LocalDateTime.now();  // Timestamp automático
        this.tipo = tipo;
        this.producto = producto;
        this.cantidad = cantidad;
        this.usuarioResponsable = usuarioResponsable;
        this.motivo = motivo;              // HU-14 CA-02: Validado en controller
    }
    
    // HU-10 CA-01: Mostrar tipo de movimiento en reportes
    public String getTipo() { 
        return tipo; 
    }

    // HU-15: Contar unidades vendidas por producto
    public int getCantidad() { 
        return cantidad; 
    }

    public Producto getProducto() { 
        return producto; 
    }

    // HU-11 CA-01: Filtro por rango de fechas
    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    // HU-14 CA-01: Motivo del ajuste registrado
    public String getMotivo() {
        return motivo;
    }
}