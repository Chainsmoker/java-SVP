import javax.swing.SwingUtilities;
import views.LoginView;

public class Main {
    public static void main(String[] args) {
        ConfigBD.establecerConexion();

        if (ConfigBD.isConectado()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new LoginView(ConfigBD.getConexion());
                }
            });
        } else {
            System.out.println("No se pudo establecer conexion con la base de datos");
        }
    }
}