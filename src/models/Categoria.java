package models;

public class Categoria {
    private int id;
    private String nombre;  // HU-08 CA-02: Nombre único por categoría

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // HU-08 CA-01: Getter de nombre
    public String getNombre() {
        return nombre;
    }
}