package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import controllers.UsuarioController;
import models.Usuario;

public class LoginView extends JFrame {
    private JTextField txtUsuario = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    private JButton btnIngresar = new JButton("Ingresar");
    private JButton btnCancelar = new JButton("Cancelar");
    private JLabel lblLogo = new JLabel();
    private JLabel lblError = new JLabel("");
    private UsuarioController usuarioController;
    private Connection conexion;

    public LoginView(Connection conexion) {
        this.conexion = conexion;
        this.usuarioController = new UsuarioController(conexion);

        setTitle("Sistema de Inventario - Login");
        setSize(400, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        lblError.setForeground(new Color(180, 50, 50));
        lblError.setFont(new Font("Calibri", Font.PLAIN, 12));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        lblLogo.setText("SVR");
        lblLogo.setFont(new Font("Calibri", Font.BOLD, 32));
        lblLogo.setForeground(new Color(6, 90, 130));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(80, 80));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panelLogo.add(lblLogo);
        panelLogo.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Sistema de Repuestos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Calibri", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(6, 90, 130));
        lblTitulo.setBackground(new Color(240, 240, 245));
        lblTitulo.setOpaque(true);
        lblTitulo.setPreferredSize(new Dimension(0, 40));

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        panelTitulo.setBackground(new Color(240, 240, 245));

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Calibri", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panelCentral.add(lblUser, gbc);

        txtUsuario.setFont(new Font("Calibri", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        panelCentral.add(txtUsuario, gbc);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Calibri", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panelCentral.add(lblPass, gbc);

        txtPassword.setFont(new Font("Calibri", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        panelCentral.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 5, 10);
        panelCentral.add(lblError, gbc);

        btnIngresar.setBackground(new Color(6, 90, 130));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setFont(new Font("Calibri", Font.BOLD, 13));
        btnIngresar.setPreferredSize(new Dimension(110, 34));

        btnCancelar.setBackground(new Color(120, 120, 130));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Calibri", Font.PLAIN, 13));
        btnCancelar.setPreferredSize(new Dimension(110, 34));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnIngresar);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(Color.WHITE);
        panelSur.add(panelBotones, BorderLayout.CENTER);
        panelSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        setLayout(new BorderLayout(0, 0));
        add(panelLogo, BorderLayout.NORTH);
        add(panelTitulo, BorderLayout.PAGE_START);
        add(panelCentral, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
        getContentPane().setBackground(Color.WHITE);

        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtUsuario.setText("");
                txtPassword.setText("");
                lblError.setText("");
                txtUsuario.requestFocus();
            }
        });

        txtPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });

        setVisible(true);
    }

    private void validarLogin() {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        lblError.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Complete todos los campos");
            return;
        }

        try {
            Usuario usuario = usuarioController.iniciarSesion(username, password);
            JOptionPane.showMessageDialog(this,
                "Bienvenido " + usuario.getUsername() + "\nRol: " + usuario.getRol(),
                "Login exitoso", JOptionPane.INFORMATION_MESSAGE);

            txtUsuario.setText("");
            txtPassword.setText("");
            dispose();
            new views.MenuPrincipal(usuario, conexion);

        } catch (RuntimeException ex) {
            lblError.setText(ex.getMessage());
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}
