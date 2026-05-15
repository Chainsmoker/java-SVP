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
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import models.MovimientoStock;
import models.Producto;
import controllers.InventarioController;

public class MovimientosView extends JPanel {
    private InventarioController controller;
    private String usuarioLogueado;
    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigoProducto, txtCantidad;
    private JComboBox<String> cbTipo;
    private JTextArea txtMotivo;
    private JLabel lblEstado;

    public MovimientosView(InventarioController controller, String usuarioLogueado) {
        this.controller = controller;
        this.usuarioLogueado = usuarioLogueado;
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(crearPanelTitulo(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelFormulario(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 50));

        JLabel lblTitulo = new JLabel("Movimientos de Stock");
        lblTitulo.setFont(new Font("Calibri", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(6, 90, 130));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFiltro.setBackground(Color.WHITE);

        JLabel lblFiltrar = new JLabel("Filtrar por tipo:");
        lblFiltrar.setFont(new Font("Calibri", Font.PLAIN, 12));

        JComboBox<String> cbFiltro = new JComboBox<>(new String[]{"TODOS", "ENTRADA", "SALIDA", "AJUSTE"});
        cbFiltro.setFont(new Font("Calibri", Font.PLAIN, 12));
        cbFiltro.setPreferredSize(new Dimension(100, 28));
        cbFiltro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTabla((String) cbFiltro.getSelectedItem());
            }
        });

        lblEstado = new JLabel("");
        lblEstado.setFont(new Font("Calibri", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(180, 60, 60));

        panelFiltro.add(lblFiltrar);
        panelFiltro.add(cbFiltro);

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelFiltro, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnas = {"Fecha", "Tipo", "Producto", "Cantidad", "Usuario", "Motivo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaMovimientos = new JTable(modeloTabla);
        tablaMovimientos.setFont(new Font("Calibri", Font.PLAIN, 12));
        tablaMovimientos.setRowHeight(24);
        tablaMovimientos.setGridColor(Color.LIGHT_GRAY);
        tablaMovimientos.setSelectionBackground(new Color(220, 230, 240));
        tablaMovimientos.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tablaMovimientos.getTableHeader();
        header.setFont(new Font("Calibri", Font.BOLD, 12));
        header.setBackground(new Color(240, 245, 250));
        header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            tablaMovimientos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tablaMovimientos.getColumnModel().getColumn(0).setPreferredWidth(140);
        tablaMovimientos.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaMovimientos.getColumnModel().getColumn(2).setPreferredWidth(180);
        tablaMovimientos.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaMovimientos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaMovimientos.getColumnModel().getColumn(5).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tablaMovimientos);
        scrollPane.setPreferredSize(new Dimension(0, 180));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Registrar Movimiento",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Calibri", Font.BOLD, 13),
            new Color(6, 90, 130)));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigoProducto = new JTextField();
        txtCodigoProducto.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtCodigoProducto.setPreferredSize(new Dimension(120, 28));

        txtCantidad = new JTextField();
        txtCantidad.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtCantidad.setPreferredSize(new Dimension(80, 28));

        cbTipo = new JComboBox<>(new String[]{"ENTRADA", "SALIDA", "AJUSTE"});
        cbTipo.setFont(new Font("Calibri", Font.PLAIN, 12));
        cbTipo.setPreferredSize(new Dimension(100, 28));

        txtMotivo = new JTextArea(3, 30);
        txtMotivo.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        scrollMotivo.setPreferredSize(new Dimension(250, 60));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Código Producto:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtCodigoProducto, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Tipo Movimiento:"), gbc);
        gbc.gridx = 3;
        panelForm.add(cbTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtCantidad, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Motivo (mín. 10 chars):"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        panelForm.add(scrollMotivo, gbc);

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
                registrarMovimiento();
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
        panelBotones.add(btnLimpiar);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarTabla(String filtroTipo) {
        modeloTabla.setRowCount(0);
        List<MovimientoStock> movimientos = controller.obtenerHistorial(filtroTipo);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (MovimientoStock m : movimientos) {
            Object[] fila = new Object[6];
            fila[0] = sdf.format(java.sql.Timestamp.valueOf(m.getFecha()));
            fila[1] = m.getTipo();
            fila[2] = m.getProducto().getNombre();
            fila[3] = m.getCantidad();
            fila[4] = m.getUsuarioResponsable();
            fila[5] = m.getMotivo();
            modeloTabla.addRow(fila);
        }
    }

    private void registrarMovimiento() {
        lblEstado.setText("");
        String codigo = txtCodigoProducto.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        String tipo = (String) cbTipo.getSelectedItem();
        String motivo = txtMotivo.getText().trim();

        if (codigo.isEmpty() || cantidadStr.isEmpty()) {
            lblEstado.setText("Complete código y cantidad");
            return;
        }

        if (tipo.equals("AJUSTE") && motivo.length() < 10) {
            lblEstado.setText("El motivo debe tener al menos 10 caracteres");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            Producto producto = controller.buscarPorCodigo(codigo);

            if (producto == null) {
                lblEstado.setText("Producto no encontrado");
                return;
            }

            if (tipo.equals("ENTRADA")) {
                controller.registrarEntrada(codigo, cantidad, usuarioLogueado);
                JOptionPane.showMessageDialog(this, "Entrada registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else if (tipo.equals("SALIDA")) {
                controller.registrarVenta(codigo, cantidad, usuarioLogueado);
                JOptionPane.showMessageDialog(this, "Salida registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else if (tipo.equals("AJUSTE")) {
                controller.ajustarInventario(codigo, cantidad, motivo, usuarioLogueado);
                JOptionPane.showMessageDialog(this, "Ajuste registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            cargarTabla("TODOS");
            limpiarFormulario();

        } catch (NumberFormatException ex) {
            lblEstado.setText("Cantidad debe ser un número entero");
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtCodigoProducto.setText("");
        txtCantidad.setText("");
        txtMotivo.setText("");
        lblEstado.setText("");
    }

    public void inicializar() {
        cargarTabla("TODOS");
    }
}
