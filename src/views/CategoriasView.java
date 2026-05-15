package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import models.Categoria;
import controllers.InventarioController;

public class CategoriasView extends JPanel {
    private InventarioController controller;
    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre;
    private JLabel lblEstado;

    public CategoriasView(InventarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(crearPanelTitulo(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelFormulario(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 50));

        JLabel lblTitulo = new JLabel("Gestión de Categorías");
        lblTitulo.setFont(new Font("Calibri", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(6, 90, 130));

        lblEstado = new JLabel("");
        lblEstado.setFont(new Font("Calibri", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(180, 60, 60));

        panel.add(lblTitulo);
        panel.add(Box.createHorizontalStrut(300));
        panel.add(lblEstado);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnas = {"Nombre de Categoría"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setFont(new Font("Calibri", Font.PLAIN, 12));
        tablaCategorias.setRowHeight(24);
        tablaCategorias.setGridColor(Color.LIGHT_GRAY);
        tablaCategorias.setSelectionBackground(new Color(220, 230, 240));
        tablaCategorias.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tablaCategorias.getTableHeader();
        header.setFont(new Font("Calibri", Font.BOLD, 12));
        header.setBackground(new Color(240, 245, 250));
        header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaCategorias.getColumnModel().getColumn(0).setCellRenderer(renderer);

        tablaCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaCategorias.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosFila(fila);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaCategorias);
        scrollPane.setPreferredSize(new Dimension(0, 180));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Datos de Categoría",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Calibri", Font.BOLD, 13),
            new Color(6, 90, 130)));

        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelForm.setBackground(Color.WHITE);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtNombre.setPreferredSize(new Dimension(200, 28));

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Calibri", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(6, 90, 130));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(100, 32));
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarCategoria();
            }
        });

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnEliminar.setBackground(new Color(200, 80, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setPreferredSize(new Dimension(100, 32));
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCategoria();
            }
        });

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnLimpiar.setBackground(new Color(120, 120, 130));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setPreferredSize(new Dimension(90, 32));
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(btnRegistrar);
        panelForm.add(btnEliminar);
        panelForm.add(btnLimpiar);

        panel.add(panelForm, BorderLayout.NORTH);

        return panel;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Categoria c : controller.getCategorias()) {
            modeloTabla.addRow(new Object[]{c.getNombre()});
        }
    }

    private void cargarDatosFila(int fila) {
        String nombre = (String) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText(nombre);
    }

    private void registrarCategoria() {
        lblEstado.setText("");
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            lblEstado.setText("Ingrese nombre de categoría");
            return;
        }
        try {
            controller.crearCategoria(nombre);
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Categoría registrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void eliminarCategoria() {
        lblEstado.setText("");
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            lblEstado.setText("Seleccione una categoría de la tabla");
            return;
        }
        try {
            controller.eliminarCategoria(nombre);
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Categoría eliminada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        lblEstado.setText("");
    }

    public void inicializar() {
        cargarTabla();
    }
}
