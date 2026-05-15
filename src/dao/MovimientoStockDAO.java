package dao;

import models.MovimientoStock;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoStockDAO {
    List<MovimientoStock> listarTodos();
    List<MovimientoStock> listarPorTipo(String tipo);
    List<MovimientoStock> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);
    List<MovimientoStock> listarPorProducto(String codigoProducto);
    void guardar(MovimientoStock movimiento);
}