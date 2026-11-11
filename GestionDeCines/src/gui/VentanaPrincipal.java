package gui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import domain.DatosParaCines;

public class VentanaPrincipal<DatosParaCiness> extends JFrame {
    private static final long serialVersionUID = 1L;

    private DatosParaCines datos;
    private JPanel panelCentral;
    private JLabel lblResumen;
    private JLabel lblReloj;

    public VentanaPrincipal(DatosParaCines datos) {
        this.datos = datos;

        setTitle("Gestion de Cines");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar barraMenu = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        menuArchivo.add(itemSalir);

        JMenu menuPeliculas = new JMenu("Peliculas");
        JMenuItem itemNuevaPelicula = new JMenuItem("Nueva pelicula");
        menuPeliculas.add(itemNuevaPelicula);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
        menuAyuda.add(itemAcercaDe);

        barraMenu.add(menuArchivo);
        barraMenu.add(menuPeliculas);
        barraMenu.add(menuAyuda);
        setJMenuBar(barraMenu);

        panelCentral = new JPanel(new BorderLayout());
        JLabel lblAviso = new JLabel("Aqui ira la cartelera (JTable de la Persona 2)", SwingConstants.CENTER);
        panelCentral.add(lblAviso, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        lblResumen = new JLabel();
        lblReloj = new JLabel();
        panelInferior.add(lblResumen, BorderLayout.WEST);
        panelInferior.add(lblReloj, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        itemSalir.addActionListener(e -> System.exit(0));
        itemNuevaPelicula.addActionListener(e -> nuevaPelicula());
        itemAcercaDe.addActionListener(e -> acercaDe());

        actualizarResumen();

        HiloReloj hiloReloj = new HiloReloj(lblReloj);
        hiloReloj.start();
    }

    public void setCartelera(JComponent componente) {
        panelCentral.removeAll();
        panelCentral.add(componente, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    public void actualizarResumen() {
        lblResumen.setText("Peliculas: " + datos.getPeliculas().size()
                + "  |  Sesiones: " + datos.getSesiones().size()
                + "  |  Salas: " + datos.getSalas().size());
    }

    public DatosParaCines getDatos() {
        return datos;
    }

    private void nuevaPelicula() {
        JOptionPane.showMessageDialog(this, "Esta opción la esta haciendo la Persona 3.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void acercaDe() {
        JOptionPane.showMessageDialog(this, "Gestion de Cines\nProyecto de Programación III",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
}