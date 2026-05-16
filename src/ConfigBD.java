import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigBD {
    private static String url = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10827128";
    private static String usuario = "sql10827128";
    private static String password = "RMQ8AH4qdu";
    private static Connection conexion = null;

    public static void establecerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, password);
        } catch (SQLException ex) {
            System.out.println("Error de conexion: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver no encontrado: " + ex.getMessage());
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