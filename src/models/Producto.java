package models;

public class Producto {
    private String codigo;
    private String nombre;
    private double precioCosto;
    private double precioVenta;
    private int stock;
    private int stockMinimo;
    private Categoria categoria;
    private boolean activo;

    public Producto(String codigo, String nombre, double precioCosto, double precioVenta, int stockInicial, int stockMinimo, Categoria categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.stock = stockInicial;
        this.stockMinimo = stockMinimo;
        this.categoria = categoria;
        this.activo = true;
    }
    
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

    public int getStock() { 
        return stock; 
    }

    public int getStockMinimo() { 
        return stockMinimo; 
    }

    public Categoria getCategoria() { 
        return categoria; 
    }

    public boolean isActivo() { 
        return activo; 
    }
    
    public void setPrecioVenta(double precioVenta) { 
        this.precioVenta = precioVenta; 
    }
    
    public void setStockMinimo(int stockMinimo) { 
        this.stockMinimo = stockMinimo; 
    }

    public void setStock(int stock) { 
        this.stock = stock; 
    }

    public void setActivo(boolean activo) { 
        this.activo = activo;
    }
}