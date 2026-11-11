package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.Butaca;
import domain.DatosParaCines;
import domain.EstadoButaca;
import domain.Sesion;

public class VentanaButacas extends JFrame {

    private static final long serialVersionUID = 1L;

    private Sesion sesion;
    private int filas;
    private int columnas;

    private JLabel[][] etiquetas;
    private JProgressBar barra;
    private JLabel lblTotal;
    private JButton btnConfirmar;

    private HiloTemporizador hilo;

    // Colores definidos para los estados de las butacas
    private static final Color COLOR_LIBRE = new Color(102, 204, 102);       // Verde
    private static final Color COLOR_SELECCIONADA = new Color(51, 153, 255); // Azul
    private static final Color COLOR_OCUPADA = new Color(160, 160, 160);     // Gris

    public VentanaButacas(Sesion sesion) {
        this.sesion = sesion;
        this.filas = sesion.getSala().getFilas();
        this.columnas = sesion.getSala().getColumnas();

        // Configuración básica de la ventana
        setTitle(sesion.toString());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No usar EXIT_ON_CLOSE
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. ARRIBA: Etiqueta "PANTALLA" centrada
        JLabel lblPantalla = new JLabel("PANTALLA", SwingConstants.CENTER);
        lblPantalla.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPantalla.setOpaque(true);
        lblPantalla.setBackground(new Color(220, 220, 220));
        lblPantalla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblPantalla, BorderLayout.NORTH);

        // 2. CENTRO: Matriz de etiquetas JLabel[][] en un GridLayout
        JPanel panelCentro = new JPanel(new GridLayout(filas, columnas, 3, 3));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        etiquetas = new JLabel[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                JLabel etiqueta = new JLabel((f + 1) + "-" + (c + 1), SwingConstants.CENTER);
                etiqueta.setOpaque(true); // Imprescindible para que se vea el color de fondo
                etiqueta.setFont(new Font("SansSerif", Font.BOLD, 11));
                etiqueta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

                // Tooltip informativo usando el toString() de Butaca ("Fila X - Asiento Y")
                Butaca b = sesion.getButaca(f, c);
                if (b != null) {
                    etiqueta.setToolTipText(b.toString());
                }

                final int fila = f;     // Copia en variable final para usar dentro del MouseAdapter
                final int columna = c;

                etiqueta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        clicEnButaca(fila, columna);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // (Opcional) Resaltar la butaca bajo el ratón si no está ocupada
                        if (sesion.getButaca(fila, columna).getEstado() != EstadoButaca.OCUPADA) {
                            etiqueta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        etiqueta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                    }
                });

                etiquetas[f][c] = etiqueta;
                panelCentro.add(etiqueta);
            }
        }
        add(panelCentro, BorderLayout.CENTER);

        // 3. ABAJO: JProgressBar (0 a 120 s), JLabel con el total en euros y botón "Confirmar compra"
        JPanel panelAbajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        barra = new JProgressBar(0, 120);
        barra.setValue(120);
        barra.setString("120 s");
        barra.setStringPainted(true);

        lblTotal = new JLabel("Total: 0,00 €");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnConfirmar = new JButton("Confirmar compra");
        btnConfirmar.addActionListener(e -> confirmarCompra());

        panelAbajo.add(barra);
        panelAbajo.add(lblTotal);
        panelAbajo.add(btnConfirmar);
        add(panelAbajo, BorderLayout.SOUTH);

        // Si el usuario cierra la ventana con la 'X', paramos el hilo y liberamos las seleccionadas
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (hilo != null && hilo.isAlive()) {
                    hilo.interrupt();
                }
                liberarButacasSeleccionadas();
            }
        });

        // Pintar el estado inicial de las butacas
        pintarButacas();
    }

    /**
     * Recorre todas las butacas de la sesión y asigna el color según su estado.
     */
    public void pintarButacas() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                EstadoButaca estado = sesion.getButaca(f, c).getEstado();
                if (estado == EstadoButaca.LIBRE) {
                    etiquetas[f][c].setBackground(COLOR_LIBRE);
                    etiquetas[f][c].setForeground(Color.BLACK);
                } else if (estado == EstadoButaca.SELECCIONADA) {
                    etiquetas[f][c].setBackground(COLOR_SELECCIONADA);
                    etiquetas[f][c].setForeground(Color.WHITE);
                } else if (estado == EstadoButaca.OCUPADA) {
                    etiquetas[f][c].setBackground(COLOR_OCUPADA);
                    etiquetas[f][c].setForeground(Color.DARK_GRAY);
                }
            }
        }
    }

    /**
     * Gestiona el clic en una butaca concreta según su estado.
     */
    private void clicEnButaca(int fila, int columna) {
        Butaca butaca = sesion.getButaca(fila, columna);
        EstadoButaca estado = butaca.getEstado();

        if (estado == EstadoButaca.OCUPADA) {
            return; // Si está OCUPADA: no hace nada
        } else if (estado == EstadoButaca.LIBRE) {
            butaca.setEstado(EstadoButaca.SELECCIONADA);
            // Si es la primera vez que selecciona una (el hilo no está corriendo), arranca el temporizador
            if (hilo == null || !hilo.isAlive()) {
                hilo = new HiloTemporizador(this, 120);
                hilo.start(); // Arrancar siempre con start(), nunca con run()
            }
        } else if (estado == EstadoButaca.SELECCIONADA) {
            butaca.setEstado(EstadoButaca.LIBRE);
        }

        pintarButacas();
        actualizarTotal();
    }

    /**
     * Calcula cuántas butacas están SELECCIONADAS y actualiza la etiqueta del precio total.
     */
    private void actualizarTotal() {
        int seleccionadas = contarSeleccionadas();
        double total = seleccionadas * sesion.getPrecio();
        lblTotal.setText(String.format("Total: %.2f €", total));
    }

    /**
     * Cuenta el número de butacas actualmente en estado SELECCIONADA.
     */
    private int contarSeleccionadas() {
        int contador = 0;
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (sesion.getButaca(f, c).getEstado() == EstadoButaca.SELECCIONADA) {
                    contador++;
                }
            }
        }
        return contador;
    }

    /**
     * Pasa todas las butacas SELECCIONADAS de vuelta al estado LIBRE.
     */
    private void liberarButacasSeleccionadas() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Butaca b = sesion.getButaca(f, c);
                if (b.getEstado() == EstadoButaca.SELECCIONADA) {
                    b.setEstado(EstadoButaca.LIBRE);
                }
            }
        }
    }

    /**
     * Método llamado desde HiloTemporizador mediante SwingUtilities.invokeLater.
     */
    public void actualizarBarra(int s) {
        barra.setValue(s);
        barra.setString(s + " s");
    }

    /**
     * Método llamado desde HiloTemporizador cuando la cuenta atrás llega a 0.
     */
    public void tiempoAgotado() {
        liberarButacasSeleccionadas();
        pintarButacas();
        actualizarTotal();
        barra.setValue(120);
        barra.setString("120 s");
        JOptionPane.showMessageDialog(this, "El tiempo de reserva ha expirado", "Tiempo agotado", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Lógica del botón "Confirmar compra".
     */
    private void confirmarCompra() {
        int seleccionadas = contarSeleccionadas();

        if (seleccionadas == 0) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar al menos una butaca.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pasar las SELECCIONADAS a OCUPADAS
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Butaca b = sesion.getButaca(f, c);
                if (b.getEstado() == EstadoButaca.SELECCIONADA) {
                    b.setEstado(EstadoButaca.OCUPADA);
                }
            }
        }

        // Parar la cuenta atrás del hilo
        if (hilo != null && hilo.isAlive()) {
            hilo.interrupt();
        }

        pintarButacas();

        // Mostrar resumen ("3 entradas: 22,50 €") y cerrar la ventana
        double total = seleccionadas * sesion.getPrecio();
        String mensaje = String.format("%d %s: %.2f €", 
                seleccionadas, 
                (seleccionadas == 1 ? "entrada" : "entradas"), 
                total);

        JOptionPane.showMessageDialog(this, mensaje, "Compra confirmada", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // Main de prueba adaptado a DatosParaCines
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatosParaCines datos = new DatosParaCines();
            new VentanaButacas(datos.getSesiones().get(0)).setVisible(true);
        });
    }
}