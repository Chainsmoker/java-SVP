import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigBD {
    private static String url = "";
    private static String usuario = "";
    private static String password = "";
    private static Connection conexion = null;

    public static void establecerConexion() {
        try {
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexion OK: " + conexion.getCatalog());
        } catch (SQLException ex) {
            System.out.println("Error de conexion: " + ex.getMessage());
        }
    }

    public static Connection getConexion() {
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexion cerrada");
            } catch (SQLException ex) {
                System.out.println("Error al cerrar: " + ex.getMessage());
            }
        }
    }

    public static boolean isConectado() {
        return conexion != null;
    }
}