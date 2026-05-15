package dao;

import models.Proveedor;
import java.util.List;

public interface ProveedorDAO {
    List<Proveedor> listarTodos();
    Proveedor buscarPorRuc(String ruc);
    void guardar(Proveedor proveedor);
    void actualizar(Proveedor proveedor);
}