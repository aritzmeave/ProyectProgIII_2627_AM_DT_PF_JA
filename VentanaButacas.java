//IAG (herramienta: Gemini)
//ADAPTADO (integración de DialogoPago manteniendo intacto el diseño de IconoButaca, VIP, leyenda y plazas PMR)
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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import domain.Butaca;
import domain.DatosParaCines;
import domain.EstadoButaca;
import domain.Sesion;

public class VentanaButacas extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_LIBRE = new Color(102, 204, 102);
    private static final Color COLOR_SELECCIONADA = new Color(51, 153, 255);
    private static final Color COLOR_OCUPADA = new Color(160, 160, 160);
    private static final Color COLOR_VIP = new Color(230, 170, 30);

    private static final Border BORDE_NORMAL = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    private static final Border BORDE_RESALTADO = BorderFactory.createLineBorder(Color.BLACK, 2);

    private Sesion sesion;
    private int filas;
    private int columnas;
    private int pasillo;

    private ArrayList<Butaca> butacas = new ArrayList<>();
    private ArrayList<JLabel> etiquetas = new ArrayList<>();

    private JProgressBar barra;
    private JLabel lblTotal;
    private JButton btnConfirmar;

    private HiloTemporizador hilo;
    private DialogoPago dialogoPago;

    public VentanaButacas(Sesion sesion) {
        this.sesion = sesion;
        this.filas = sesion.getSala().getFilas();
        this.columnas = sesion.getSala().getColumnas();
        this.pasillo = columnas / 2;

        setTitle(sesion.toString());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 920);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblPantalla = new JLabel("PANTALLA", SwingConstants.CENTER);
        lblPantalla.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPantalla.setOpaque(true);
        lblPantalla.setBackground(new Color(220, 220, 220));
        lblPantalla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(crearLeyenda(), BorderLayout.NORTH);
        panelArriba.add(lblPantalla, BorderLayout.CENTER);
        add(panelArriba, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(filas + 1, columnas + 1, 3, 3));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(100, 20, 10, 20));

        int primeraPlaza = pasillo - Sesion.NUM_PLAZAS_MOVILIDAD / 2;
        int plaza = 0;
        for (int c = 0; c < columnas; c++) {
            if (c == pasillo) {
                panelCentro.add(new JLabel());
            }
            if (c >= primeraPlaza && plaza < Sesion.NUM_PLAZAS_MOVILIDAD) {
                Butaca b = sesion.getPlazaMovilidad(plaza);
                panelCentro.add(crearEtiqueta(b, "PMR " + (plaza + 1),
                        "Plaza para personas con movilidad reducida"));
                plaza++;
            } else {
                panelCentro.add(new JLabel());
            }
        }

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (c == pasillo) {
                    panelCentro.add(new JLabel());
                }
                Butaca b = sesion.getButaca(f, c);
                String ayuda = b.toString();
                if (b.isVip()) {
                    ayuda = ayuda + " (VIP)";
                }
                panelCentro.add(crearEtiqueta(b, (f + 1) + "-" + (c + 1), ayuda));
            }
        }
        add(panelCentro, BorderLayout.CENTER);

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (hilo != null && hilo.isAlive()) {
                    hilo.interrupt();
                }
                liberarButacasSeleccionadas();
            }
        });

        pintarButacas();
    }

    private JPanel crearLeyenda() {
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 8));

        leyenda.add(crearElementoLeyenda("Seleccionada", new IconoButaca(COLOR_SELECCIONADA, false, false, 0.7)));
        leyenda.add(crearElementoLeyenda("No disponible", new IconoButaca(COLOR_OCUPADA, false, false, 0.7)));
        leyenda.add(crearElementoLeyenda("VIP", new IconoButaca(COLOR_VIP, false, true, 0.7)));

        return leyenda;
    }

    private JLabel crearElementoLeyenda(String texto, IconoButaca icono) {
        JLabel elemento = new JLabel(texto, icono, SwingConstants.LEFT);
        elemento.setForeground(Color.BLACK);
        elemento.setFont(new Font("SansSerif", Font.PLAIN, 13));
        elemento.setIconTextGap(8);
        return elemento;
    }

    private JLabel crearEtiqueta(Butaca butaca, String texto, String ayuda) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 10));
        etiqueta.setVerticalTextPosition(SwingConstants.BOTTOM);
        etiqueta.setHorizontalTextPosition(SwingConstants.CENTER);
        etiqueta.setToolTipText(ayuda);
        etiqueta.setBorder(BORDE_NORMAL);

        etiqueta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clicEnButaca(butaca);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (butaca.getEstado() != EstadoButaca.OCUPADA) {
                    etiqueta.setBorder(BORDE_RESALTADO);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                etiqueta.setBorder(BORDE_NORMAL);
            }
        });

        butacas.add(butaca);
        etiquetas.add(etiqueta);
        return etiqueta;
    }

    public void pintarButacas() {
        for (int i = 0; i < butacas.size(); i++) {
            Butaca butaca = butacas.get(i);
            JLabel etiqueta = etiquetas.get(i);
            boolean movilidad = butaca.getFila() == Sesion.FILA_MOVILIDAD;

            Color color;
            if (butaca.getEstado() == EstadoButaca.LIBRE && butaca.isVip()) {
                color = COLOR_VIP;
                etiqueta.setForeground(Color.BLACK);
            } else if (butaca.getEstado() == EstadoButaca.LIBRE) {
                color = COLOR_LIBRE;
                etiqueta.setForeground(Color.BLACK);
            } else if (butaca.getEstado() == EstadoButaca.SELECCIONADA) {
                color = COLOR_SELECCIONADA;
                etiqueta.setForeground(Color.BLACK);
            } else {
                color = COLOR_OCUPADA;
                etiqueta.setForeground(Color.GRAY);
            }

            etiqueta.setIcon(new IconoButaca(color, movilidad, butaca.isVip()));
        }
    }

    private void clicEnButaca(Butaca butaca) {
        EstadoButaca estado = butaca.getEstado();

        if (estado == EstadoButaca.OCUPADA) {
            return;
        } else if (estado == EstadoButaca.LIBRE) {
            butaca.setEstado(EstadoButaca.SELECCIONADA);
            if (hilo == null || !hilo.isAlive()) {
                hilo = new HiloTemporizador(this, 120);
                hilo.start();
            }
        } else if (estado == EstadoButaca.SELECCIONADA) {
            butaca.setEstado(EstadoButaca.LIBRE);
        }

        pintarButacas();
        actualizarTotal();
    }

    private void actualizarTotal() {
        int seleccionadas = contarSeleccionadas();
        double total = seleccionadas * sesion.getPrecio();
        lblTotal.setText(String.format("Total: %.2f €", total));
    }

    private int contarSeleccionadas() {
        int contador = 0;
        for (Butaca butaca : butacas) {
            if (butaca.getEstado() == EstadoButaca.SELECCIONADA) {
                contador++;
            }
        }
        return contador;
    }

    private void liberarButacasSeleccionadas() {
        for (Butaca butaca : butacas) {
            if (butaca.getEstado() == EstadoButaca.SELECCIONADA) {
                butaca.setEstado(EstadoButaca.LIBRE);
            }
        }
    }

    public void actualizarBarra(int s) {
        barra.setValue(s);
        barra.setString(s + " s");
    }

    public void tiempoAgotado() {
        // Si estaba abierta la pasarela de pago cuando se acabó el tiempo, la cerramos automáticamente
        if (dialogoPago != null && dialogoPago.isVisible()) {
            dialogoPago.dispose();
        }
        liberarButacasSeleccionadas();
        pintarButacas();
        actualizarTotal();
        barra.setValue(120);
        barra.setString("120 s");
        JOptionPane.showMessageDialog(this, "El tiempo de reserva ha expirado", "Tiempo agotado",
                JOptionPane.WARNING_MESSAGE);
    }

    private void confirmarCompra() {
        int seleccionadas = contarSeleccionadas();

        if (seleccionadas == 0) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar al menos una butaca.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = seleccionadas * sesion.getPrecio();

        // 1. Abrimos la ventana modal de pago antes de confirmar la compra
        dialogoPago = new DialogoPago(this, seleccionadas, total);
        dialogoPago.setVisible(true);

        // 2. Si el usuario cerró/canceló la ventana de pago (o expiró el tiempo), no ocupamos las butacas
        if (!dialogoPago.isPagoRealizado()) {
            return;
        }

        // 3. Si el pago se completó con éxito, pasamos las butacas a OCUPADA
        for (Butaca butaca : butacas) {
            if (butaca.getEstado() == EstadoButaca.SELECCIONADA) {
                butaca.setEstado(EstadoButaca.OCUPADA);
            }
        }

        if (hilo != null && hilo.isAlive()) {
            hilo.interrupt();
        }

        pintarButacas();

        String mensaje = String.format("Pago completado con éxito.\n%d %s: %.2f €",
                seleccionadas,
                (seleccionadas == 1 ? "entrada" : "entradas"),
                total);

        JOptionPane.showMessageDialog(this, mensaje, "Compra confirmada", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatosParaCines datos = new DatosParaCines();
            new VentanaButacas(datos.getSesiones().get(0)).setVisible(true);
        });
    }
}