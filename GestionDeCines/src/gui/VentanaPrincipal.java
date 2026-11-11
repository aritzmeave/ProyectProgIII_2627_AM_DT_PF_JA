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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import domain.DatosParaCines;

public class VentanaPrincipal<DatosParaCiness> extends JFrame {
    private static final long serialVersionUID = 1L;

    private DatosParaCines datos;
    private JPanel panelCentral;
    private PanelCartelera cartelera;
    private JLabel lblResumen;
    private JLabel lblReloj;
 
    public VentanaPrincipal(DatosParaCines datos) {
        this.datos = datos;

        setTitle("Gestion de Cines");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	    addWindowListener(new WindowAdapter() {
	
	        @Override
	        public void windowClosing(WindowEvent e) {
	            confirmarSalida();
	        }
	    });
	    
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
        cartelera = new PanelCartelera(datos.getSesiones());
        panelCentral.add(cartelera, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        lblResumen = new JLabel();
        lblReloj = new JLabel();
        panelInferior.add(lblResumen, BorderLayout.WEST);
        panelInferior.add(lblReloj, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        itemSalir.addActionListener(e -> confirmarSalida());
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

      DialogoPelicula dialogo = new DialogoPelicula(this, datos);

      dialogo.setVisible(true);

      actualizarResumen();

      if (cartelera != null) {
          cartelera.actualizarTabla();
      }
  }
    

    private void acercaDe() {
        JOptionPane.showMessageDialog(this, "Gestion de Cines\nProyecto de Programación III",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
    
  private void confirmarSalida() {

      int respuesta = JOptionPane.showConfirmDialog(
              this,
              "¿Seguro que quieres salir?",
              "Confirmar salida",
              JOptionPane.YES_NO_OPTION
      );

      if (respuesta == JOptionPane.YES_OPTION) {
          System.exit(0);
      }
  }
}