package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import domain.Sesion;


public class RendererCartelera extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private ModeloTablaSesiones modelo;

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

        // Mostrar la duración en horas y minutos
        if (column == 2 && value instanceof Number) {
            int minutos = ((Number) value).intValue();
            setText(minutos / 60 + "h " + minutos % 60 + "min");
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

      if (column == 3 && !isSelected) {

          String clasificacion = value.toString();

          if (clasificacion.equals("TP") || clasificacion.equals("+7")) {
              setBackground(Color.GREEN);

          } else if (clasificacion.equals("+12") || clasificacion.equals("+16")) {
              setBackground(Color.ORANGE);

          } else if (clasificacion.equals("+18")) {
              setBackground(Color.RED);
          }
      }
        
        return this;
        
        
    }
    
    
}