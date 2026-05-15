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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import models.Producto;
import controllers.InventarioController;

public class ProductosView extends JPanel {
    private InventarioController controller;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigo, txtNombre, txtPrecioCosto, txtPrecioVenta, txtStock, txtStockMin;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbBuscarPor;
    private JTextField txtBuscar;
    private JButton btnRegistrar, btnActualizar, btnDesactivar, btnLimpiar, btnReactivar;
    private JLabel lblEstado;

    public ProductosView(InventarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelFormulario(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 50));

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBuscar.setBackground(Color.WHITE);

        cbBuscarPor = new JComboBox<>(new String[]{"Código", "Nombre"});
        cbBuscarPor.setFont(new Font("Calibri", Font.PLAIN, 12));
        cbBuscarPor.setPreferredSize(new Dimension(90, 28));

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtBuscar.setPreferredSize(new Dimension(180, 28));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnBuscar.setBackground(new Color(6, 90, 130));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setPreferredSize(new Dimension(80, 28));
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });

        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnMostrarTodos.setBackground(new Color(100, 100, 100));
        btnMostrarTodos.setForeground(Color.WHITE);
        btnMostrarTodos.setFocusPainted(false);
        btnMostrarTodos.setPreferredSize(new Dimension(110, 28));
        btnMostrarTodos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTablaProductos();
            }
        });

        panelBuscar.add(new JLabel("Buscar por:"));
        panelBuscar.add(cbBuscarPor);
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnMostrarTodos);

        lblEstado = new JLabel("");
        lblEstado.setFont(new Font("Calibri", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(180, 60, 60));

        panel.add(panelBuscar, BorderLayout.WEST);
        panel.add(lblEstado, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnas = {"Código", "Nombre", "P. Costo", "P. Venta", "Stock", "Stock Mín", "Categoría", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("Calibri", Font.PLAIN, 12));
        tablaProductos.setRowHeight(24);
        tablaProductos.setGridColor(Color.LIGHT_GRAY);
        tablaProductos.setSelectionBackground(new Color(220, 230, 240));
        tablaProductos.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tablaProductos.getTableHeader();
        header.setFont(new Font("Calibri", Font.BOLD, 12));
        header.setBackground(new Color(240, 245, 250));
        header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            tablaProductos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(60);
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(70);
        tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(100);
        tablaProductos.getColumnModel().getColumn(7).setPreferredWidth(70);

        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaProductos.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosFila(fila);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Datos del Producto",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Calibri", Font.BOLD, 13),
            new Color(6, 90, 130)));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField();
        txtCodigo.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtCodigo.setPreferredSize(new Dimension(150, 28));

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtNombre.setPreferredSize(new Dimension(200, 28));

        txtPrecioCosto = new JTextField();
        txtPrecioCosto.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtPrecioCosto.setPreferredSize(new Dimension(100, 28));

        txtPrecioVenta = new JTextField();
        txtPrecioVenta.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtPrecioVenta.setPreferredSize(new Dimension(100, 28));

        txtStock = new JTextField();
        txtStock.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtStock.setPreferredSize(new Dimension(80, 28));

        txtStockMin = new JTextField();
        txtStockMin.setFont(new Font("Calibri", Font.PLAIN, 12));
        txtStockMin.setPreferredSize(new Dimension(80, 28));

        cbCategoria = new JComboBox<>();
        cbCategoria.setFont(new Font("Calibri", Font.PLAIN, 12));
        cbCategoria.setPreferredSize(new Dimension(140, 28));
        cargarCategorias();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtCodigo, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3;
        panelForm.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Precio Costo:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtPrecioCosto, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Precio Venta:"), gbc);
        gbc.gridx = 3;
        panelForm.add(txtPrecioVenta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtStock, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Stock Mínimo:"), gbc);
        gbc.gridx = 3;
        panelForm.add(txtStockMin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelForm.add(cbCategoria, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(Color.WHITE);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Calibri", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(6, 90, 130));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(100, 32));
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarProducto();
            }
        });

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnActualizar.setBackground(new Color(60, 140, 80));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setPreferredSize(new Dimension(100, 32));
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarProducto();
            }
        });

        btnDesactivar = new JButton("Desactivar");
        btnDesactivar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnDesactivar.setBackground(new Color(200, 80, 60));
        btnDesactivar.setForeground(Color.WHITE);
        btnDesactivar.setFocusPainted(false);
        btnDesactivar.setPreferredSize(new Dimension(100, 32));
        btnDesactivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                desactivarProducto();
            }
        });

        btnReactivar = new JButton("Reactivar");
        btnReactivar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnReactivar.setBackground(new Color(100, 80, 160));
        btnReactivar.setForeground(Color.WHITE);
        btnReactivar.setFocusPainted(false);
        btnReactivar.setPreferredSize(new Dimension(100, 32));
        btnReactivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reactivarProducto();
            }
        });

        btnLimpiar = new JButton("Limpiar");
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
        panelBotones.add(btnDesactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnLimpiar);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        List<Categoria> categorias = controller.getCategorias();
        if (categorias.isEmpty()) {
            cbCategoria.addItem("Sin categorías");
        } else {
            for (Categoria c : categorias) {
                cbCategoria.addItem(c.getNombre());
            }
        }
    }

    private void cargarTablaProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = controller.getCatalogo();
        for (Producto p : productos) {
            Object[] fila = new Object[8];
            fila[0] = p.getCodigo();
            fila[1] = p.getNombre();
            fila[2] = String.format("S/. %.2f", p.getPrecioCosto());
            fila[3] = String.format("S/. %.2f", p.getPrecioVenta());
            fila[4] = p.getStock();
            fila[5] = p.getStockMinimo();
            fila[6] = p.getCategoria() != null ? p.getCategoria().getNombre() : "-";
            fila[7] = p.isActivo() ? "Activo" : "Inactivo";
            modeloTabla.addRow(fila);
        }
    }

    private void cargarDatosFila(int fila) {
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        Producto p = controller.buscarPorCodigo(codigo);
        if (p != null) {
            txtCodigo.setText(p.getCodigo());
            txtNombre.setText(p.getNombre());
            txtPrecioCosto.setText(String.valueOf(p.getPrecioCosto()));
            txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
            txtStock.setText(String.valueOf(p.getStock()));
            txtStockMin.setText(String.valueOf(p.getStockMinimo()));
            if (p.getCategoria() != null) {
                cbCategoria.setSelectedItem(p.getCategoria().getNombre());
            }
            txtCodigo.setEditable(false);
        }
    }

    private void registrarProducto() {
        lblEstado.setText("");
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String precioCostoStr = txtPrecioCosto.getText().trim();
            String precioVentaStr = txtPrecioVenta.getText().trim();
            String stockStr = txtStock.getText().trim();
            String stockMinStr = txtStockMin.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || precioCostoStr.isEmpty() ||
                precioVentaStr.isEmpty() || stockStr.isEmpty() || stockMinStr.isEmpty()) {
                lblEstado.setText("Complete todos los campos");
                return;
            }

            double precioCosto = Double.parseDouble(precioCostoStr);
            double precioVenta = Double.parseDouble(precioVentaStr);
            int stock = Integer.parseInt(stockStr);
            int stockMin = Integer.parseInt(stockMinStr);

            Categoria catSeleccionada = null;
            String catNombre = (String) cbCategoria.getSelectedItem();
            if (catNombre != null && !catNombre.equals("Sin categorías")) {
                for (Categoria c : controller.getCategorias()) {
                    if (c.getNombre().equals(catNombre)) {
                        catSeleccionada = c;
                        break;
                    }
                }
                if (catSeleccionada == null) {
                    catSeleccionada = controller.crearCategoria(catNombre);
                }
            }

            controller.registrarProducto(codigo, nombre, precioCosto, precioVenta, stock, stockMin, catSeleccionada);
            cargarTablaProductos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Producto registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            lblEstado.setText("Verifique el formato numérico");
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void actualizarProducto() {
        lblEstado.setText("");
        try {
            String codigo = txtCodigo.getText().trim();
            String precioVentaStr = txtPrecioVenta.getText().trim();
            String stockMinStr = txtStockMin.getText().trim();

            if (codigo.isEmpty()) {
                lblEstado.setText("Seleccione un producto de la tabla");
                return;
            }

            double precioVenta = Double.parseDouble(precioVentaStr);
            int stockMin = Integer.parseInt(stockMinStr);

            controller.actualizarProducto(codigo, precioVenta, stockMin);
            cargarTablaProductos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            lblEstado.setText("Verifique el formato numérico");
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
        }
    }

    private void desactivarProducto() {
        lblEstado.setText("");
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            lblEstado.setText("Seleccione un producto de la tabla");
            return;
        }
        controller.desactivarProducto(codigo);
        cargarTablaProductos();
        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Producto desactivado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void reactivarProducto() {
        lblEstado.setText("");
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            lblEstado.setText("Seleccione un producto de la tabla");
            return;
        }
        controller.reactivarProducto(codigo);
        cargarTablaProductos();
        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Producto reactivado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarProducto() {
        lblEstado.setText("");
        String criterio = txtBuscar.getText().trim();
        if (criterio.isEmpty()) {
            lblEstado.setText("Ingrese texto para buscar");
            return;
        }

        modeloTabla.setRowCount(0);
        try {
            List<Producto> resultados;
            if (cbBuscarPor.getSelectedItem().equals("Código")) {
                Producto p = controller.buscarPorCodigo(criterio);
                if (p != null) {
                    resultados = new ArrayList<>();
                    resultados.add(p);
                } else {
                    resultados = new ArrayList<>();
                }
            } else {
                resultados = controller.buscarPorNombre(criterio);
            }

            for (Producto p : resultados) {
                Object[] fila = new Object[8];
                fila[0] = p.getCodigo();
                fila[1] = p.getNombre();
                fila[2] = String.format("S/. %.2f", p.getPrecioCosto());
                fila[3] = String.format("S/. %.2f", p.getPrecioVenta());
                fila[4] = p.getStock();
                fila[5] = p.getStockMinimo();
                fila[6] = p.getCategoria() != null ? p.getCategoria().getNombre() : "-";
                fila[7] = p.isActivo() ? "Activo" : "Inactivo";
                modeloTabla.addRow(fila);
            }
        } catch (RuntimeException ex) {
            lblEstado.setText(ex.getMessage());
            cargarTablaProductos();
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecioCosto.setText("");
        txtPrecioVenta.setText("");
        txtStock.setText("");
        txtStockMin.setText("");
        txtBuscar.setText("");
        txtCodigo.setEditable(true);
        lblEstado.setText("");
    }

    public void inicializar() {
        cargarCategorias();
        cargarTablaProductos();
    }
}
