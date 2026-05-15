package dao;

import models.Categoria;
import java.util.List;

public interface CategoriaDAO {
    List<Categoria> listarTodos();
    Categoria buscarPorNombre(String nombre);
    void guardar(Categoria categoria);
    void eliminar(String nombre);
}