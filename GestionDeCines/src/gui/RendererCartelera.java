package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import domain.Clasificacion;
import domain.Genero;
import domain.Pelicula;
import domain.Sesion;


public class RendererCartelera extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private ModeloTablaSesiones modelo;
    public static final int ANCHO_CARTEL = 60;
    public static final int ALTO_CARTEL = 90;
    private static final String CARPETA_CARTELES = "/images/carteles/";
    public static final int TAMANO_EDAD = 50;
    private static final String CARPETA_EDADES = "/images/clasificacion/";

    private HashMap<String, ImageIcon> imagenes = new HashMap<>();

    public RendererCartelera(ModeloTablaSesiones modelo) {
        this.modelo = modelo;
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        int filaModelo = table.convertRowIndexToModel(row);
        Sesion sesion = modelo.getSesion(filaModelo);

        setText(value == null ? "" : value.toString());
        setIcon(null);
        setToolTipText(null);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.TRAILING);

        if (column == 0) {
            setIcon(obtenerCartel(sesion.getPelicula()));
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setHorizontalTextPosition(SwingConstants.CENTER);
        }

        // Mostrar la duración en horas y minutos
        if (column == 2 && value instanceof Number) {
            int minutos = ((Number) value).intValue();
            setText(minutos / 60 + "h " + minutos % 60 + "min");
        }

        if (column == 5) {
            setText(value + "h");
        }

        if (column == 6) {
            setText(value + "\u20AC");
        }

        // Colores de las filas
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else if (sesion.getPorcentajeOcupacion() > 90) {
            setBackground(new Color(255, 180, 180));
            setForeground(Color.BLACK);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }

        if (column == 1) {
            setForeground(colorGenero(sesion.getPelicula().getGenero()));
        }

        if (column == 3) {
            Clasificacion clasificacion = sesion.getPelicula().getClasificacion();
            ImageIcon icono = obtenerIconoEdad(clasificacion);
            if (icono != null) {
                setIcon(icono);
                setText("");
            }
            setToolTipText(textoEdad(clasificacion));
        }
        
        return this;
    }

    private ImageIcon obtenerCartel(Pelicula pelicula) {
        String archivo = pelicula.getCartel();

        if (archivo == null) {
            return null;
        }

        return cargarImagen(CARPETA_CARTELES + archivo, ANCHO_CARTEL, ALTO_CARTEL);
    }
    private ImageIcon obtenerIconoEdad(Clasificacion clasificacion) {
        String archivo = clasificacion.name().toLowerCase() + ".png";
        return cargarImagen(CARPETA_EDADES + archivo, TAMANO_EDAD, TAMANO_EDAD);
    }

    private String textoEdad(Clasificacion clasificacion) {
        switch (clasificacion) {
            case TP:
                return "Apta para todos los públicos";
            case MAS_7:
                return "No recomendada para menores de 7 años";
            case MAS_12:
                return "No recomendada para menores de 12 años";
            case MAS_16:
                return "No recomendada para menores de 16 años";
            case MAS_18:
                return "No recomendada para menores de 18 años";
            default:
                return null;
        }
    }

    private ImageIcon cargarImagen(String ruta, int ancho, int alto) {
        if (imagenes.containsKey(ruta)) {
            return imagenes.get(ruta);
        }

        ImageIcon original = null;
        URL url = RendererCartelera.class.getResource(ruta);
        File fichero = new File("resources" + ruta);

        if (url != null) {
            original = new ImageIcon(url);
        } else if (fichero.exists()) {
            original = new ImageIcon(fichero.getPath());
        } else {
            System.out.println("No se encuentra la imagen: " + ruta);
        }

        ImageIcon icono = null;
        if (original != null) {
            Image pequena = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            icono = new ImageIcon(pequena);
        }

        imagenes.put(ruta, icono);
        return icono;
    }

    private Color colorGenero(Genero genero) {
        switch (genero) {
            case ACCION:
                return new Color(160, 0, 0);
            case COMEDIA:
                return new Color(130, 60, 0);
            case DRAMA:
                return new Color(25, 45, 130);
            case TERROR:
                return new Color(80, 0, 100);
            case CIENCIA_FICCION:
                return new Color(0, 80, 95);
            case ANIMACION:
                return new Color(0, 90, 25);
            case ROMANCE:
                return new Color(150, 0, 75);
            default:
                return Color.BLACK;
        }
    }
}
