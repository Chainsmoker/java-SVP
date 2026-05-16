package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controllers.InventarioController;
import controllers.UsuarioController;
import models.Usuario;

public class MenuPrincipal extends JFrame {
    private Usuario usuarioLogueado;
    private JPanel panelContenido;
    private String seccionActual = "inicio";
    private JLabel lblSeccion;
    private InventarioController inventarioController;
    private ProductosView productosView;
    private CategoriasView categoriasView;
    private ProveedoresView proveedoresView;
    private UsuariosView usuariosView;
    private UsuarioController usuarioController;
    private JPanel homeView;
    private JPanel vistaActual;
    private Connection conexion;

    public MenuPrincipal(Usuario usuario, Connection conexion) {
        this.usuarioLogueado = usuario;
        this.conexion = conexion;
        this.inventarioController = new InventarioController(conexion);
        this.usuarioController = new UsuarioController(conexion);

        setTitle("Sistema de Inventario");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelWest = crearSidebar();
        JPanel panelCentro = crearContenidoPrincipal();

        homeView = new HomeView(this);
        vistaActual = homeView;

        setLayout(new BorderLayout(0, 0));
        add(panelWest, BorderLayout.WEST);
        add(panelCentro, BorderLayout.CENTER);

        panelContenido.add(homeView, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel crearSidebar() {
        JPanel panelWest = new JPanel();
        panelWest.setLayout(new BorderLayout(0, 0));
        panelWest.setPreferredSize(new Dimension(220, 0));
        panelWest.setBackground(new Color(6, 90, 130));

        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelLogo.setBackground(new Color(6, 90, 130));
        panelLogo.setPreferredSize(new Dimension(220, 90));

        JLabel lblLogo = new JLabel("SVR");
        lblLogo.setFont(new Font("Calibri", Font.BOLD, 26));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        panelLogo.add(lblLogo);

        JPanel panelUser = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 8));
        panelUser.setBackground(new Color(6, 90, 130));
        panelUser.setPreferredSize(new Dimension(220, 50));

        JLabel lblUsuario = new JLabel(usuarioLogueado.getUsername());
        lblUsuario.setFont(new Font("Calibri", Font.BOLD, 13));
        lblUsuario.setForeground(Color.WHITE);
        panelUser.add(lblUsuario);

        JLabel lblRol = new JLabel("(" + usuarioLogueado.getRol() + ")");
        lblRol.setFont(new Font("Calibri", Font.PLAIN, 11));
        lblRol.setForeground(new Color(200, 220, 230));
        panelUser.add(lblRol);

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        panelMenu.setBackground(new Color(5, 75, 110));
        panelMenu.setPreferredSize(new Dimension(220, 350));

        JButton btnInicio = crearBotonSidebar("Inicio", "inicio");
        JButton btnProductos = crearBotonSidebar("Productos", "productos");
        JButton btnCategorias = crearBotonSidebar("Categorías", "categorias");
        JButton btnProveedores = crearBotonSidebar("Proveedores", "proveedores");
        JButton btnUsuarios = crearBotonSidebar("Usuarios", "usuarios");

        panelMenu.add(btnInicio);
        panelMenu.add(btnProductos);
        panelMenu.add(btnCategorias);
        panelMenu.add(btnProveedores);

        if (usuarioLogueado.getRol().equals("ADMIN")) {
            panelMenu.add(btnUsuarios);
        }

        JButton btnCerrar = crearBotonSidebar("Cerrar Sesión", "cerrar");
        btnCerrar.setBackground(new Color(160, 40, 40));

        JPanel panelCerrar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panelCerrar.setBackground(new Color(6, 90, 130));
        panelCerrar.setPreferredSize(new Dimension(220, 60));
        panelCerrar.add(btnCerrar);

        panelWest.add(panelLogo, BorderLayout.NORTH);
        panelWest.add(panelUser, BorderLayout.NORTH);
        panelWest.add(panelMenu, BorderLayout.CENTER);
        panelWest.add(panelCerrar, BorderLayout.SOUTH);

        return panelWest;
    }

    private JButton crearBotonSidebar(String texto, String accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Calibri", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(6, 90, 130));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 38));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (accion.equals("cerrar")) {
                    cerrarSesion();
                } else {
                    cambiarSeccion(accion);
                }
            }
        });

        return btn;
    }

    private JPanel crearContenidoPrincipal() {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 12));
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setPreferredSize(new Dimension(0, 55));
        panelTitulo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        lblSeccion = new JLabel("Inicio");
        lblSeccion.setFont(new Font("Calibri", Font.BOLD, 18));
        lblSeccion.setForeground(new Color(6, 90, 130));
        panelTitulo.add(lblSeccion);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);

        panelContenido.add(panelTitulo, BorderLayout.NORTH);
        panelContenido.add(panelCentro, BorderLayout.CENTER);

        return panelContenido;
    }

    public void cambiarSeccion(String seccion) {
        if (seccionActual.equals(seccion)) {
            return;
        }

        seccionActual = seccion;

        if (vistaActual != null) {
            panelContenido.remove(vistaActual);
        }

        if (seccion.equals("inicio")) {
            vistaActual = homeView;
            panelContenido.add(homeView, BorderLayout.CENTER);
            lblSeccion.setText("Inicio");
        } else if (seccion.equals("productos")) {
            if (productosView == null) {
                productosView = new ProductosView(inventarioController);
                productosView.inicializar();
            }
            vistaActual = productosView;
            panelContenido.add(productosView, BorderLayout.CENTER);
            lblSeccion.setText("Productos");
        } else if (seccion.equals("categorias")) {
            if (categoriasView == null) {
                categoriasView = new CategoriasView(inventarioController);
                categoriasView.inicializar();
            }
            vistaActual = categoriasView;
            panelContenido.add(categoriasView, BorderLayout.CENTER);
            lblSeccion.setText("Categorías");
        } else if (seccion.equals("proveedores")) {
            if (proveedoresView == null) {
                proveedoresView = new ProveedoresView(inventarioController);
                proveedoresView.inicializar();
            }
            vistaActual = proveedoresView;
            panelContenido.add(proveedoresView, BorderLayout.CENTER);
            lblSeccion.setText("Proveedores");
        } else if (seccion.equals("usuarios")) {
            if (usuariosView == null) {
                usuariosView = new UsuariosView(usuarioController, usuarioLogueado);
                usuariosView.inicializar();
            }
            vistaActual = usuariosView;
            panelContenido.add(usuariosView, BorderLayout.CENTER);
            lblSeccion.setText("Usuarios");
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void cerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            dispose();
            new LoginView(conexion);
        }
    }
}