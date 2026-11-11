
package gui;

// IAG (herramienta: ChatGPT)
// ADAPTADO (al proyecto GestionDeCines)

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import domain.Clasificacion;
import domain.DatosParaCines;
import domain.Genero;
import domain.Pelicula;
import domain.Sala;
import domain.Sesion;

public class DialogoPelicula extends JDialog {

    private static final long serialVersionUID = 1L;

    private DatosParaCines datos;

    private JTextField txtTitulo;
    private JTextField txtDirector;
    private JTextField txtFecha;

    private JSpinner spDuracion;

    private JComboBox<Genero> cbGenero;
    private JComboBox<Clasificacion> cbClasificacion;
    private JComboBox<Sala> cbSala;
    private JComboBox<String> cbHora;

    public DialogoPelicula(JFrame padre, DatosParaCines datos) {

        super(padre, "Nueva película", true);

        this.datos = datos;

        setSize(450, 380);
        setLocationRelativeTo(padre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 10, 10));

        txtTitulo = new JTextField();
        txtDirector = new JTextField();
        txtFecha = new JTextField("10/11/2026");

        spDuracion = new JSpinner(
                new SpinnerNumberModel(120, 1, 300, 1));

        cbGenero = new JComboBox<>(Genero.values());
        cbClasificacion = new JComboBox<>(Clasificacion.values());

        cbSala = new JComboBox<>();

        for (Sala sala : datos.getSalas()) {
            cbSala.addItem(sala);
        }

        cbHora = new JComboBox<>(
                new String[] {"16:00", "19:00", "22:00"});

        panelCampos.add(new JLabel("Título:"));
        panelCampos.add(txtTitulo);

        panelCampos.add(new JLabel("Director:"));
        panelCampos.add(txtDirector);

        panelCampos.add(new JLabel("Duración:"));
        panelCampos.add(spDuracion);

        panelCampos.add(new JLabel("Género:"));
        panelCampos.add(cbGenero);

        panelCampos.add(new JLabel("Clasificación:"));
        panelCampos.add(cbClasificacion);

        panelCampos.add(new JLabel("Sala:"));
        panelCampos.add(cbSala);

        panelCampos.add(new JLabel("Hora:"));
        panelCampos.add(cbHora);

        panelCampos.add(new JLabel("Fecha:"));
        panelCampos.add(txtFecha);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());

        btnCancelar.addActionListener(e -> dispose());

        // Eventos de teclado

        KeyAdapter teclado = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    guardar();

                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        };

        txtTitulo.addKeyListener(teclado);
        txtDirector.addKeyListener(teclado);
        txtFecha.addKeyListener(teclado);
    }

    private void guardar() {

        try {

            String titulo = txtTitulo.getText().trim();
            String director = txtDirector.getText().trim();

            int duracion = (Integer) spDuracion.getValue();

            Genero genero = (Genero) cbGenero.getSelectedItem();

            Clasificacion clasificacion =
                    (Clasificacion) cbClasificacion.getSelectedItem();

            Sala sala = (Sala) cbSala.getSelectedItem();

            String hora = (String) cbHora.getSelectedItem();

            String fecha = txtFecha.getText().trim();

            if (fecha.isEmpty()) {
                throw new IllegalArgumentException(
                        "La fecha no puede estar vacía.");
            }

            int idPelicula = datos.getPeliculas().size() + 1;
            int idSesion = datos.getSesiones().size() + 1;

            Pelicula pelicula = new Pelicula(
                    idPelicula,
                    titulo,
                    director,
                    duracion,
                    genero,
                    clasificacion);

            double precio = 7.50;

            if (hora.equals("22:00")) {
                precio = 9.00;
            }

            Sesion sesion = new Sesion(
                    idSesion,
                    pelicula,
                    sala,
                    fecha,
                    hora,
                    precio);

            datos.getPeliculas().add(pelicula);
            datos.getSesiones().add(sesion);

            JOptionPane.showMessageDialog(
                    this,
                    "Película guardada correctamente");

            dispose();

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Datos incorrectos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
