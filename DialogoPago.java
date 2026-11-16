package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DialogoPago extends JDialog {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> comboMetodo;
    private JTextField txtTitular;
    private JTextField txtNumero;
    private JTextField txtCaducidad;
    private JPasswordField txtCvv;

    private JLabel lblNumero;
    private JLabel lblCaducidad;
    private JLabel lblCvv;

    private boolean pagoRealizado = false;

    public DialogoPago(JFrame padre, int numEntradas, double total) {
        super(padre, "Pasarela de Pago", true); // true = modal
        setSize(420, 330);
        setLocationRelativeTo(padre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. ARRIBA: Resumen del importe a pagar
        String textoResumen = String.format("Total a pagar (%d %s): %.2f €",
                numEntradas, (numEntradas == 1 ? "entrada" : "entradas"), total);
        JLabel lblResumen = new JLabel(textoResumen, SwingConstants.CENTER);
        lblResumen.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblResumen.setOpaque(true);
        lblResumen.setBackground(new Color(230, 240, 255));
        lblResumen.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        add(lblResumen, BorderLayout.NORTH);

        // 2. CENTRO: Formulario con GridLayout
        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 8, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelCampos.add(new JLabel("Método de pago:"));
        comboMetodo = new JComboBox<>(new String[]{"Tarjeta Bancaria", "Bizum"});
        panelCampos.add(comboMetodo);

        panelCampos.add(new JLabel("Nombre del titular:"));
        txtTitular = new JTextField();
        panelCampos.add(txtTitular);

        lblNumero = new JLabel("Nº Tarjeta (16 dígitos):");
        panelCampos.add(lblNumero);
        txtNumero = new JTextField();
        panelCampos.add(txtNumero);

        lblCaducidad = new JLabel("Caducidad (MM/AA):");
        panelCampos.add(lblCaducidad);
        txtCaducidad = new JTextField("12/28");
        panelCampos.add(txtCaducidad);

        lblCvv = new JLabel("CVV (3 dígitos):");
        panelCampos.add(lblCvv);
        txtCvv = new JPasswordField();
        panelCampos.add(txtCvv);

        add(panelCampos, BorderLayout.CENTER);

        // Cambiar campos activos según si elige Tarjeta o Bizum
        comboMetodo.addActionListener(e -> actualizarCamposPorMetodo());

        // Evento de teclado: Enter para pagar, Escape para cancelar
        KeyAdapter listenerTeclado = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    procesarPago();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        };
        txtTitular.addKeyListener(listenerTeclado);
        txtNumero.addKeyListener(listenerTeclado);
        txtCaducidad.addKeyListener(listenerTeclado);
        txtCvv.addKeyListener(listenerTeclado);

        // 3. ABAJO: Botones Pagar y Cancelar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnPagar = new JButton("Pagar ahora");
        btnPagar.setBackground(new Color(46, 139, 87));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnCancelar.addActionListener(e -> dispose());
        btnPagar.addActionListener(e -> procesarPago());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnPagar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarCamposPorMetodo() {
        boolean esTarjeta = comboMetodo.getSelectedIndex() == 0;
        if (esTarjeta) {
            lblNumero.setText("Nº Tarjeta (16 dígitos):");
            lblCaducidad.setEnabled(true);
            txtCaducidad.setEnabled(true);
            lblCvv.setEnabled(true);
            txtCvv.setEnabled(true);
        } else {
            lblNumero.setText("Teléfono Bizum (9 dígitos):");
            lblCaducidad.setEnabled(false);
            txtCaducidad.setEnabled(false);
            lblCvv.setEnabled(false);
            txtCvv.setEnabled(false);
        }
    }

    private void procesarPago() {
        String titular = txtTitular.getText().trim();
        String numero = txtNumero.getText().replaceAll("\\s+", ""); // Quita espacios

        if (titular.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce el nombre del titular.", "Datos incompletos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (comboMetodo.getSelectedIndex() == 0) { // Validación de Tarjeta
            String caducidad = txtCaducidad.getText().trim();
            String cvv = new String(txtCvv.getPassword()).trim();

            if (!numero.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this, "El número de tarjeta debe tener exactamente 16 dígitos numéricos.", "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!caducidad.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                JOptionPane.showMessageDialog(this, "La fecha de caducidad debe tener el formato MM/AA (ej: 08/27).", "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!cvv.matches("\\d{3}")) {
                JOptionPane.showMessageDialog(this, "El código CVV debe tener 3 dígitos numéricos.", "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else { // Validación de Bizum
            if (!numero.matches("\\d{9}")) {
                JOptionPane.showMessageDialog(this, "El número de teléfono para Bizum debe tener 9 dígitos.", "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        pagoRealizado = true;
        dispose();
    }

    public boolean isPagoRealizado() {
        return pagoRealizado;
    }
}
