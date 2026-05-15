package dao;

import models.Usuario;
import java.util.List;

public interface UsuarioDAO {
    List<Usuario> listarTodos();
    Usuario buscarPorUsername(String username);
    void guardar(Usuario usuario);
    void desactivar(String username);
    void reactivar(String username);
}