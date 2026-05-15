package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HomeView extends JPanel {
    private MenuPrincipal menuPadre;

    public HomeView(MenuPrincipal menuPadre) {
        this.menuPadre = menuPadre;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTitulo.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Panel de Control");
        lblTitulo.setFont(new Font("Calibri", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(6, 90, 130));
        panelTitulo.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Seleccione una opción para comenzar");
        lblSubtitulo.setFont(new Font("Calibri", Font.PLAIN, 13));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setPreferredSize(new Dimension(0, 25));

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 5));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(lblSubtitulo, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCards = new JPanel(new GridLayout(2, 3, 20, 20));
        panelCards.setBackground(Color.WHITE);

        panelCards.add(crearCard("Inventario", "Gestionar productos", new Color(6, 90, 130), "productos"));
        panelCards.add(crearCard("Stock", "Movimientos de inventario", new Color(60, 140, 80), "movimientos"));
        panelCards.add(crearCard("Reportes", "Ver reportes y estadísticas", new Color(100, 80, 160), "reportes"));
        panelCards.add(crearCard("Categorías", "Administrar categorías", new Color(200, 100, 50), "categorias"));
        panelCards.add(crearCard("Proveedores", "Gestionar proveedores", new Color(80, 120, 180), "proveedores"));
        panelCards.add(crearCard("Usuarios", "Administrar usuarios", new Color(160, 80, 140), "usuarios"));

        add(panelCards, BorderLayout.CENTER);
    }

    private JPanel crearCard(String titulo, String descripcion, Color colorAccento, String seccion) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        JPanel panelBarra = new JPanel();
        panelBarra.setBackground(colorAccento);
        panelBarra.setPreferredSize(new Dimension(0, 4));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Calibri", Font.BOLD, 16));
        lblTitulo.setForeground(colorAccento);

        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("Calibri", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblTitulo);
        panelCentro.add(lblDesc);

        card.add(panelBarra, BorderLayout.NORTH);
        card.add(panelCentro, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                menuPadre.cambiarSeccion(seccion);
            }
        });

        return card;
    }
}