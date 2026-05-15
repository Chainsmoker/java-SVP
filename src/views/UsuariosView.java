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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import models.Usuario;
import controllers.UsuarioController;

public class UsuariosView extends JPanel {
    private UsuarioController controller;
    private Usuario adminActual;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRol;
    private JLabel lblEstado;

    public UsuariosView(UsuarioController controller, Usuario adminActual) {
        this.controller = controller;
        this.adminActual = adminActual;
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

        JLabel lblTitulo = new JLabel("Gestión de Usuarios");
        lblTitulo.setFont(new Font("Calibri", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(6, 90, 130));

        lblEstado = new JLabel("");
        lblEstado.setFont(new Font("Calibri", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(180, 60, 60));

        panel.add(lblTitulo);
        panel.add(Box.createHorizontalStrut(400));
        panel.add(lblEstado);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnas = {"Username", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Calibri", Font.PLAIN, 12));
        tablaUsuarios.setRowHeight(24);
        tablaUsuarios.setGridColor(Color.LIGHT_GRAY);
        tablaUsuarios.setSelectionBackground(new Color(220, 230, 240));
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tablaUsuarios.getTableHeader();
        header.setFont(new Font("Calibri", Font.BOLD, 12));
        header.setBackground(new Color(240, 245, 250));
        header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            tablaUsuarios.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(100);

        tablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaUsuarios.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosFila(fila);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setPreferredSize(new Dimension(0, 180));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Datos del Usuario",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Calibri", Font.BOLD, 13),
            new Color(6, 90, 130)));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtUsername.setPreferredSize(new Dimension(150, 28));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtPassword.setPreferredSize(new Dimension(150, 28));

        cbRol = new JComboBox<>(new String[]{"ADMIN", "VENDEDOR"});
        cbRol.setFont(new Font("Calibri", Font.PLAIN, 12));
        cbRol.setPreferredSize(new Dimension(120, 28));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Password:"), gbc);
        gbc.gridx = 3;
        panelForm.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        panelForm.add(cbRol, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCrear = new JButton("Crear Usuario");
        btnCrear.setFont(new Font("Calibri", Font.BOLD, 12));
        btnCrear.setBackground(new Color(6, 90, 130));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setPreferredSize(new Dimension(120, 32));
        btnCrear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crearUsuario();
            }
        });

        JButton btnDesactivar = new JButton("Desactivar");
        btnDesactivar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnDesactivar.setBackground(new Color(200, 80, 60));
        btnDesactivar.setForeground(Color.WHITE);
        btnDesactivar.setFocusPainted(false);
        btnDesactivar.setPreferredSize(new Dimension(120, 32));
        btnDesactivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                desactivarUsuario();
            }
        });

        JButton btnReactivar = new JButton("Reactivar");
        btnReactivar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnReactivar.setBackground(new Color(100, 80, 160));
        btnReactivar.setForeground(Color.WHITE);
        btnReactivar.setFocusPainted(false);
        btnReactivar.setPreferredSize(new Dimension(120, 32));
        btnReactivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reactivarUsuario();
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

        panelBotones.add(btnCrear);
        panelBotones.add(btnDesactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnLimpiar);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Usuario u : controller.getUsuarios()) {
            Object[] fila = new Object[3];
            fila[0] = u.getUsername();
            fila[1] = u.getRol();
            fila[2] = u.isActivo() ? "Activo" : "Inactivo";
            modeloTabla.addRow(fila);
        }
    }

    private void cargarDatosFila(int fila) {
        String username = (String) modeloTabla.getValueAt(fila, 0);
        txtUsername.setText(username);
        txtUsername.setEditable(false);
        txtPassword.setText("");
    }

    private void crearUsuario() {
        lblEstado.setText("");
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cbRol.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            lblEstado.setText("Complete username y password");
            return;
        }

        try {
            controller.crearUsuario(adminActual, username, password, rol);
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void desactivarUsuario() {
        lblEstado.setText("");
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            lblEstado.setText("Seleccione un usuario de la tabla");
            return;
        }

        try {
            controller.desactivarUsuario(adminActual, username);
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Usuario desactivado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void reactivarUsuario() {
        lblEstado.setText("");
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            lblEstado.setText("Seleccione un usuario de la tabla");
            return;
        }

        try {
            controller.reactivarUsuario(username);
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Usuario reactivado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtUsername.setText("");
        txtUsername.setEditable(true);
        txtPassword.setText("");
        lblEstado.setText("");
    }

    public void inicializar() {
        cargarTabla();
    }
}
