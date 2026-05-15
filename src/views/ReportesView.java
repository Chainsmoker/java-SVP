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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
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

import models.MovimientoStock;
import models.Producto;
import controllers.InventarioController;

public class ReportesView extends JPanel {
    private InventarioController controller;
    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;
    private JLabel lblTotal;
    private String[] columnasActuales;

    public ReportesView(InventarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(crearPanelTitulo(), BorderLayout.NORTH);
        add(crearPanelBotones(), BorderLayout.CENTER);
        add(crearPanelTabla(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 50));

        JLabel lblTitulo = new JLabel("Reportes y Consultas");
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

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnInventario = new JButton("Inventario Valorizado");
        btnInventario.setFont(new Font("Calibri", Font.BOLD, 12));
        btnInventario.setBackground(new Color(6, 90, 130));
        btnInventario.setForeground(Color.WHITE);
        btnInventario.setFocusPainted(false);
        btnInventario.setPreferredSize(new Dimension(180, 40));
        btnInventario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarInventarioValorizado();
            }
        });

        JButton btnBajoStock = new JButton("Productos Bajo Stock");
        btnBajoStock.setFont(new Font("Calibri", Font.BOLD, 12));
        btnBajoStock.setBackground(new Color(200, 80, 60));
        btnBajoStock.setForeground(Color.WHITE);
        btnBajoStock.setFocusPainted(false);
        btnBajoStock.setPreferredSize(new Dimension(180, 40));
        btnBajoStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarReporteBajoStock();
            }
        });

        JButton btnTopVentas = new JButton("Top Productos Vendidos");
        btnTopVentas.setFont(new Font("Calibri", Font.BOLD, 12));
        btnTopVentas.setBackground(new Color(60, 140, 80));
        btnTopVentas.setForeground(Color.WHITE);
        btnTopVentas.setFocusPainted(false);
        btnTopVentas.setPreferredSize(new Dimension(180, 40));
        btnTopVentas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarTopVentas();
            }
        });

        JButton btnHistorial = new JButton("Historial Movimientos");
        btnHistorial.setFont(new Font("Calibri", Font.BOLD, 12));
        btnHistorial.setBackground(new Color(100, 80, 160));
        btnHistorial.setForeground(Color.WHITE);
        btnHistorial.setFocusPainted(false);
        btnHistorial.setPreferredSize(new Dimension(180, 40));
        btnHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarHistorialMovimientos();
            }
        });

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Calibri", Font.PLAIN, 12));
        btnLimpiar.setBackground(new Color(120, 120, 130));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setPreferredSize(new Dimension(120, 40));
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeloTabla.setRowCount(0);
                lblEstado.setText("");
                lblTotal.setText("Total valorizado: S/. 0.00");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btnInventario, gbc);
        gbc.gridx = 1;
        panel.add(btnBajoStock, gbc);
        gbc.gridx = 2;
        panel.add(btnTopVentas, gbc);
        gbc.gridx = 3;
        panel.add(btnHistorial, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        panel.add(btnLimpiar, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        columnasActuales = new String[]{"Código", "Producto", "Stock", "P. Unitario", "Valor Total", "Estado"};
        modeloTabla = new DefaultTableModel(columnasActuales, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaReporte = new JTable(modeloTabla);
        tablaReporte.setFont(new Font("Calibri", Font.PLAIN, 12));
        tablaReporte.setRowHeight(24);
        tablaReporte.setGridColor(Color.LIGHT_GRAY);
        tablaReporte.setSelectionBackground(new Color(220, 230, 240));
        tablaReporte.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tablaReporte.getTableHeader();
        header.setFont(new Font("Calibri", Font.BOLD, 12));
        header.setBackground(new Color(240, 245, 250));
        header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnasActuales.length; i++) {
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaReporte.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaReporte.getColumnModel().getColumn(2).setPreferredWidth(70);
        tablaReporte.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaReporte.getColumnModel().getColumn(4).setPreferredWidth(110);
        tablaReporte.getColumnModel().getColumn(5).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(tablaReporte);
        scrollPane.setPreferredSize(new Dimension(0, 280));

        lblTotal = new JLabel("Total valorizado: S/. 0.00");
        lblTotal.setFont(new Font("Calibri", Font.BOLD, 13));
        lblTotal.setForeground(new Color(6, 90, 130));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelTotal.setBackground(Color.WHITE);
        panelTotal.add(lblTotal);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelTotal, BorderLayout.SOUTH);

        return panel;
    }

    private void setColumnas(String[] columnas) {
        columnasActuales = columnas;
        modeloTabla.setColumnIdentifiers(columnas);
    }

    private void generarInventarioValorizado() {
        lblEstado.setText("");
        modeloTabla.setRowCount(0);
        setColumnas(new String[]{"Código", "Producto", "Stock", "P. Unitario", "Valor Total", "Estado"});
        double totalGeneral = 0;

        List<Producto> productos = controller.getCatalogo();
        for (Producto p : productos) {
            if (p.isActivo()) {
                Object[] fila = new Object[6];
                fila[0] = p.getCodigo();
                fila[1] = p.getNombre();
                fila[2] = p.getStock();
                fila[3] = String.format("S/. %.2f", p.getPrecioCosto());
                double valorTotal = p.getStock() * p.getPrecioCosto();
                fila[4] = String.format("S/. %.2f", valorTotal);
                fila[5] = p.getStock() <= p.getStockMinimo() ? "BAJO STOCK" : "Normal";
                modeloTabla.addRow(fila);
                totalGeneral += valorTotal;
            }
        }
        lblTotal.setText("Total valorizado: S/. " + String.format("%.2f", totalGeneral));
    }

    private void generarReporteBajoStock() {
        lblEstado.setText("");
        modeloTabla.setRowCount(0);
        setColumnas(new String[]{"Código", "Producto", "Stock Actual", "Stock Mínimo", "P. Unitario", "Estado"});
        double totalGeneral = 0;

        List<Producto> productos = controller.getCatalogo();
        for (Producto p : productos) {
            if (p.isActivo() && p.getStock() <= p.getStockMinimo()) {
                Object[] fila = new Object[6];
                fila[0] = p.getCodigo();
                fila[1] = p.getNombre();
                fila[2] = p.getStock();
                fila[3] = p.getStockMinimo();
                fila[4] = String.format("S/. %.2f", p.getPrecioCosto());
                fila[5] = "REQUIERE REPOSICIÓN";
                modeloTabla.addRow(fila);
                totalGeneral += p.getStock() * p.getPrecioCosto();
            }
        }
        lblTotal.setText("Total valorizado: S/. " + String.format("%.2f", totalGeneral));
    }

    private void generarTopVentas() {
        lblEstado.setText("");
        modeloTabla.setRowCount(0);
        setColumnas(new String[]{"Producto", "Cant. Vendida", "P. Venta", "Stock Actual"});

        List<Producto> topProductos = controller.getTopVendidos(0, 0);
        Map<String, Integer> ventas = controller.getVentasPorProducto(0, 0);

        if (topProductos.isEmpty()) {
            lblEstado.setText("No hay ventas registradas");
            lblTotal.setText("Total valorizado: S/. 0.00");
            return;
        }

        for (Producto p : topProductos) {
            Object[] fila = new Object[4];
            fila[0] = p.getNombre();
            fila[1] = ventas.get(p.getCodigo());
            fila[2] = String.format("S/. %.2f", p.getPrecioVenta());
            fila[3] = p.getStock();
            modeloTabla.addRow(fila);
        }
        lblTotal.setText("Total valorizado: S/. 0.00");
    }

    private void generarHistorialMovimientos() {
        lblEstado.setText("");
        modeloTabla.setRowCount(0);
        setColumnas(new String[]{"Fecha", "Tipo", "Producto", "Cantidad", "Usuario", "Motivo"});
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        List<MovimientoStock> movimientos = controller.obtenerHistorial("TODOS");
        for (MovimientoStock m : movimientos) {
            Object[] fila = new Object[6];
            fila[0] = sdf.format(java.sql.Timestamp.valueOf(m.getFecha()));
            fila[1] = m.getTipo();
            fila[2] = m.getProducto().getNombre();
            fila[3] = m.getCantidad();
            fila[4] = m.getUsuarioResponsable();
            fila[5] = m.getMotivo() != null ? m.getMotivo() : "-";
            modeloTabla.addRow(fila);
        }
        lblTotal.setText("Total valorizado: S/. 0.00");
    }

    public void inicializar() {
        modeloTabla.setRowCount(0);
        lblTotal.setText("Total valorizado: S/. 0.00");
    }
}