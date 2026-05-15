package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import models.Proveedor;
import controllers.InventarioController;

public class ProveedoresView extends JPanel {
    private InventarioController controller;
    private JTable tablaProveedores;
    private DefaultTableModel modeloTabla;
    private JTextField txtRuc, txtRazonSocial, txtTelefono, txtEmail;
    private JLabel lblEstado;

    public ProveedoresView(InventarioController controller) {
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

        JLabel lblTitulo = new JLabel("Gestión de Proveedores");
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

        String[] columnas = {"RUC", "Razón Social", "Teléfono", "Email"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaProveedores = new JTable(modeloTabla);
        tablaProveedores.setFont(new Font("Calibri", Font.PLAIN, 12));
        tablaProveedores.setRowHeight(24);
        tablaProveedores.setGridColor(Color.LIGHT_GRAY);
        tablaProveedores.setSelectionBackground(new Color(220, 230, 240));
        tablaProveedores.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tablaProveedores.getTableHeader();
        header.setFont(new Font("Calibri", Font.BOLD, 12));
        header.setBackground(new Color(240, 245, 250));
        header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            tablaProveedores.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tablaProveedores.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaProveedores.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaProveedores.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaProveedores.getColumnModel().getColumn(3).setPreferredWidth(180);

        tablaProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaProveedores.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosFila(fila);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProveedores);
        scrollPane.setPreferredSize(new Dimension(0, 180));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Datos del Proveedor",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Calibri", Font.BOLD, 13),
            new Color(6, 90, 130)));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRuc = new JTextField();
        txtRuc.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtRuc.setPreferredSize(new Dimension(120, 28));

        txtRazonSocial = new JTextField();
        txtRazonSocial.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtRazonSocial.setPreferredSize(new Dimension(220, 28));

        txtTelefono = new JTextField();
        txtTelefono.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtTelefono.setPreferredSize(new Dimension(140, 28));

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtEmail.setPreferredSize(new Dimension(180, 28));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("RUC (11 dígitos):"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtRuc, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Razón Social:"), gbc);
        gbc.gridx = 3;
        panelForm.add(txtRazonSocial, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtTelefono, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        panelForm.add(txtEmail, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Calibri", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(6, 90, 130));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(100, 32));
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarProveedor();
            }
        });

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnActualizar.setBackground(new Color(60, 140, 80));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setPreferredSize(new Dimension(100, 32));
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarProveedor();
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

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnLimpiar);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Proveedor p : controller.getProveedores()) {
            Object[] fila = new Object[4];
            fila[0] = p.getRuc();
            fila[1] = p.getRazonSocial();
            fila[2] = p.getTelefono() != null ? p.getTelefono() : "-";
            fila[3] = p.getEmail() != null ? p.getEmail() : "-";
            modeloTabla.addRow(fila);
        }
    }

    private void cargarDatosFila(int fila) {
        String ruc = (String) modeloTabla.getValueAt(fila, 0);
        txtRuc.setText(ruc);
        txtRuc.setEditable(false);
        for (Proveedor p : controller.getProveedores()) {
            if (p.getRuc().equals(ruc)) {
                txtRazonSocial.setText(p.getRazonSocial());
                txtTelefono.setText(p.getTelefono() != null ? p.getTelefono() : "");
                txtEmail.setText(p.getEmail() != null ? p.getEmail() : "");
                break;
            }
        }
    }

    private void registrarProveedor() {
        lblEstado.setText("");
        String ruc = txtRuc.getText().trim();
        String razonSocial = txtRazonSocial.getText().trim();

        if (ruc.isEmpty() || razonSocial.isEmpty()) {
            lblEstado.setText("Complete RUC y Razón Social");
            return;
        }

        try {
            Proveedor nuevo = controller.registrarProveedor(ruc, razonSocial);
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            if (!telefono.isEmpty() || !email.isEmpty()) {
                controller.actualizarProveedor(ruc, telefono, email);
            }
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Proveedor registrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void actualizarProveedor() {
        lblEstado.setText("");
        String ruc = txtRuc.getText().trim();
        if (ruc.isEmpty()) {
            lblEstado.setText("Seleccione un proveedor de la tabla");
            return;
        }

        try {
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            controller.actualizarProveedor(ruc, telefono, email);
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Proveedor actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtRuc.setText("");
        txtRuc.setEditable(true);
        txtRazonSocial.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        lblEstado.setText("");
    }

    public void inicializar() {
        cargarTabla();
    }
}
