package dao;

import models.Producto;
import java.util.List;

public interface ProductoDAO {
    List<Producto> listarTodos();
    List<Producto> listarActivos();
    Producto buscarPorCodigo(String codigo);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorCategoria(int idCategoria);
    List<Producto> productosBajoStock();
    void guardar(Producto producto);
    void actualizar(Producto producto);
    void desactivar(String codigo);
    void reactivar(String codigo);
    void asignarProveedor(String codigoProducto, String rucProveedor);
}