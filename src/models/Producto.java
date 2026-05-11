package models;

// ============================================================
// MODELO: PRODUCTO
// Sistema de Inventario de Repuestos (SVP)
// ============================================================
// Representa un repuesto en el catálogo del almacén.
// Campos: código (inmutable), nombre, precios, stock.
// Relación: pertenece a una Categoría y puede tener un Proveedor.
// ============================================================

public class Producto {
    private String codigo;            // HU-03 CA-02: Campo no modificable (sin setter)
    private String nombre;
    private double precioCosto;
    private double precioVenta;
    private int stock;
    private int stockMinimo;          // HU-03 CA-04: Límite mínimo no puede ser negativo
    private Categoria categoria;
    private Proveedor proveedor;      // HU-09 CA-03: Relación con proveedor
    private boolean activo;           // HU-04 CA-01/04: Estado activo/inactivo

    public Producto(String codigo, String nombre, double precioCosto, double precioVenta, 
                    int stockInicial, int stockMinimo, Categoria categoria) {
        this.codigo = codigo;         // HU-01 CA-01: Código único para cada producto
        this.nombre = nombre;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.stock = stockInicial;
        this.stockMinimo = stockMinimo;
        this.categoria = categoria;
        this.proveedor = null;
        this.activo = true;            // HU-04 CA-01: Nuevo producto activo por defecto
    }
    
    // HU-01 CA-01: Getter de código único
    public String getCodigo() { 
        return codigo; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public double getPrecioCosto() { 
        return precioCosto; 
    }
    
    public double getPrecioVenta() { 
        return precioVenta; 
    }

    // HU-06 CA-01: Getter de stock actual (usado en búsqueda HU-02 CA-01)
    public int getStock() { 
        return stock; 
    }

    public int getStockMinimo() { 
        return stockMinimo; 
    }

    public Categoria getCategoria() { 
        return categoria; 
    }

    // HU-09 CA-03: Proveedor que suministra este producto
    public Proveedor getProveedor() {
        return proveedor;
    }

    // HU-04 CA-01/02: Estado activo para ventas y alertas
    public boolean isActivo() { 
        return activo; 
    }
    
    public void setPrecioVenta(double precioVenta) { 
        this.precioVenta = precioVenta; 
    }
    
    public void setStockMinimo(int stockMinimo) { 
        this.stockMinimo = stockMinimo; 
    }

    // HU-05 CA-01/03, HU-06 CA-01, HU-14 CA-01: Modificar stock
    public void setStock(int stock) { 
        this.stock = stock; 
    }

    // HU-04 CA-01: Desactivar producto
    // HU-04 CA-04: Reactivar producto
    public void setActivo(boolean activo) { 
        this.activo = activo;
    }

    // HU-09 CA-03: Asignar proveedor al producto
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    // HU-03 CA-02: NO EXISTE setCodigo() - El código es inmutable
}