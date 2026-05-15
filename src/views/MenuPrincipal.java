package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import controllers.InventarioController;
import controllers.UsuarioController;
import models.Usuario;

public class MenuPrincipal extends JFrame {
    private Usuario usuarioLogueado;
    private JPanel panelContenido;
    private String seccionActual = "inicio";
    private JLabel lblSeccion;
    private JLabel lblBienvenida;
    private JLabel lblSubBienvenida;
    private InventarioController inventarioController;
    private ProductosView productosView;
    private CategoriasView categoriasView;
    private ProveedoresView proveedoresView;
    private MovimientosView movimientosView;
    private ReportesView reportesView;
    private UsuariosView usuariosView;
    private UsuarioController usuarioController;
    private JPanel dashboardView;
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
        dashboardView = null;
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
        JButton btnDashboard = crearBotonSidebar("Dashboard", "dashboard");
        JButton btnProductos = crearBotonSidebar("Productos", "productos");
        JButton btnCategorias = crearBotonSidebar("Categorías", "categorias");
        JButton btnProveedores = crearBotonSidebar("Proveedores", "proveedores");
        JButton btnMovimientos = crearBotonSidebar("Movimientos", "movimientos");
        JButton btnReportes = crearBotonSidebar("Reportes", "reportes");
        JButton btnUsuarios = crearBotonSidebar("Usuarios", "usuarios");

        panelMenu.add(btnInicio);
        panelMenu.add(btnDashboard);
        panelMenu.add(btnProductos);
        panelMenu.add(btnCategorias);
        panelMenu.add(btnProveedores);
        panelMenu.add(btnMovimientos);
        panelMenu.add(btnReportes);

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

        lblBienvenida = new JLabel("Bienvenido al Sistema");
        lblBienvenida.setFont(new Font("Calibri", Font.BOLD, 24));
        lblBienvenida.setForeground(new Color(80, 80, 80));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);

        lblSubBienvenida = new JLabel("Seleccione una opción del menú lateral");
        lblSubBienvenida.setFont(new Font("Calibri", Font.PLAIN, 14));
        lblSubBienvenida.setForeground(Color.GRAY);
        lblSubBienvenida.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelTexto = new JPanel(new BorderLayout());
        panelTexto.setBackground(Color.WHITE);
        panelTexto.add(lblBienvenida, BorderLayout.NORTH);
        panelTexto.add(lblSubBienvenida, BorderLayout.CENTER);

        panelCentro.add(panelTexto, BorderLayout.NORTH);
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
        } else if (seccion.equals("dashboard")) {
            if (dashboardView == null) {
                dashboardView = crearPanelDashboard();
            }
            vistaActual = dashboardView;
            panelContenido.add(dashboardView, BorderLayout.CENTER);
            lblSeccion.setText("Dashboard");
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
        } else if (seccion.equals("movimientos")) {
            if (movimientosView == null) {
                movimientosView = new MovimientosView(inventarioController, usuarioLogueado.getUsername());
                movimientosView.inicializar();
            }
            vistaActual = movimientosView;
            panelContenido.add(movimientosView, BorderLayout.CENTER);
            lblSeccion.setText("Movimientos");
        } else if (seccion.equals("reportes")) {
            if (reportesView == null) {
                reportesView = new ReportesView(inventarioController);
                reportesView.inicializar();
            }
            vistaActual = reportesView;
            panelContenido.add(reportesView, BorderLayout.CENTER);
            lblSeccion.setText("Reportes");
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

    private JPanel crearPanelDashboard() {
        JPanel panelDashboard = new JPanel(new BorderLayout(10, 10));
        panelDashboard.setBackground(Color.WHITE);
        panelDashboard.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelTarjetas = crearPanelTarjetas();
        JPanel panelTabla = crearPanelTablaStock();
        JPanel panelGrafica = crearPanelGraficaVentas();

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelTarjetas, BorderLayout.NORTH);
        panelSuperior.add(panelTabla, BorderLayout.CENTER);

        panelDashboard.add(panelSuperior, BorderLayout.NORTH);
        panelDashboard.add(panelGrafica, BorderLayout.CENTER);

        return panelDashboard;
    }

    private JPanel crearPanelTarjetas() {
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 4, 12, 0));
        panelTarjetas.setBackground(Color.WHITE);
        panelTarjetas.setPreferredSize(new Dimension(0, 100));

        int totalProductos = inventarioController.getCatalogo().size();
        int bajoStock = inventarioController.obtenerAlertasStock().size();
        int movimientosMes = inventarioController.obtenerHistorial("TODOS").size();

        java.util.List<models.Producto> topVendidos = inventarioController.getTopVendidos(0, 0);
        String topProducto = "Sin datos";
        if (!topVendidos.isEmpty()) {
            topProducto = topVendidos.get(0).getNombre();
        }

        panelTarjetas.add(crearTarjeta("Total Productos", String.valueOf(totalProductos), new Color(6, 90, 130)));
        panelTarjetas.add(crearTarjeta("Bajo Stock", String.valueOf(bajoStock), new Color(200, 80, 60)));
        panelTarjetas.add(crearTarjeta("Movimientos Mes", String.valueOf(movimientosMes), new Color(60, 140, 80)));
        panelTarjetas.add(crearTarjeta("Top Producto", topProducto, new Color(100, 80, 160)));

        return panelTarjetas;
    }

    private JPanel crearTarjeta(String titulo, String valor, Color colorAccento) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Calibri", Font.PLAIN, 12));
        lblTitulo.setForeground(Color.GRAY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Calibri", Font.BOLD, 22));
        lblValor.setForeground(colorAccento);

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblValor);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelInferior.setBackground(colorAccento);
        panelInferior.setPreferredSize(new Dimension(0, 3));

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(panelCentro, BorderLayout.CENTER);
        tarjeta.add(panelInferior, BorderLayout.SOUTH);

        return tarjeta;
    }

    private JPanel crearPanelTablaStock() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Productos con Bajo Stock",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Calibri", Font.BOLD, 13),
            new Color(180, 60, 60)));

        String[] columnas = {"Código", "Producto", "Stock", "Stock Mín.", "Categoría"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        java.util.List<models.Producto> bajoStock = inventarioController.obtenerAlertasStock();
        for (models.Producto p : bajoStock) {
            modelo.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getStock(),
                p.getStockMinimo(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "-"
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Calibri", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setSelectionBackground(new Color(220, 230, 240));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Calibri", Font.BOLD, 12));
        header.setBackground(new Color(240, 245, 250));
        header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        TableColumnModel columnModel = tabla.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(180);
        columnModel.getColumn(2).setPreferredWidth(60);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(0, 120));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelGraficaVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Top 5 Productos Más Vendidos",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Calibri", Font.BOLD, 13),
            new Color(6, 90, 130)));

        java.util.List<models.Producto> topVendidos = inventarioController.getTopVendidos(0, 0);
        java.util.Map<String, Integer> ventasMap = inventarioController.getVentasPorProducto(0, 0);

        int count = Math.min(5, topVendidos.size());
        String[] productos = new String[count];
        int[] ventas = new int[count];
        int maxVentas = 1;

        for (int i = 0; i < count; i++) {
            models.Producto p = topVendidos.get(i);
            productos[i] = p.getNombre();
            int cant = ventasMap.get(p.getCodigo()) != null ? ventasMap.get(p.getCodigo()) : 0;
            ventas[i] = cant;
            if (cant > maxVentas) maxVentas = cant;
        }
        if (maxVentas == 0) maxVentas = 1;

        GraficaBarras grafica = new GraficaBarras(productos, ventas, maxVentas);
        panel.add(grafica, BorderLayout.CENTER);

        return panel;
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

    class GraficaBarras extends JPanel {
        private String[] productos;
        private int[] ventas;
        private int maxVentas;

        public GraficaBarras(String[] productos, int[] ventas, int maxVentas) {
            this.productos = productos;
            this.ventas = ventas;
            this.maxVentas = maxVentas;
            setPreferredSize(new Dimension(0, 180));
            setBackground(Color.WHITE);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            int anchoTotal = getWidth() - 80;
            int altoBarra = 24;
            int espacio = 12;

            int inicioX = 100;
            int inicioY = 20;

            g.setFont(new Font("Calibri", Font.PLAIN, 11));

            for (int i = 0; i < productos.length; i++) {
                int y = inicioY + i * (altoBarra + espacio);

                g.setColor(Color.DARK_GRAY);
                g.drawString(productos[i], 5, y + altoBarra - 5);

                int anchoBarra = (int) ((ventas[i] * 1.0 / maxVentas) * anchoTotal);
                if (anchoBarra < 20) anchoBarra = 20;

                Color colorBarra = new Color(6, 90, 130);
                g.setColor(colorBarra);
                g.fillRect(inicioX, y, anchoBarra, altoBarra);

                g.setColor(Color.DARK_GRAY);
                g.drawRect(inicioX, y, anchoBarra, altoBarra);

                g.drawString(String.valueOf(ventas[i]), inicioX + anchoBarra + 8, y + altoBarra - 5);
            }
        }
    }
}
