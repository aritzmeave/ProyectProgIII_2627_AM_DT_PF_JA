package gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import domain.Sesion;

public class ModeloTablaSesiones extends AbstractTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnas = {
		    "Película", "Género", "Duración", "Clasificación",
		    "Sala", "Hora", "Precio", "Ocupación"
		};

    private List<Sesion> sesiones;

    public ModeloTablaSesiones(List<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    @Override
    public int getRowCount() {
        return sesiones.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Sesion sesion = sesiones.get(rowIndex);

        switch (columnIndex) {

            case 0:
                return sesion.getPelicula().getTitulo();

            case 1:
                return sesion.getPelicula().getGenero();

            case 2:
                return sesion.getPelicula().getDuracion();

            case 3:
                return sesion.getPelicula().getClasificacion();

            case 4:
                return sesion.getSala().getNumero();

            case 5:
                return sesion.getHora();

            case 6:
                return sesion.getPrecio();

            case 7:
                return String.format("%.1f%%", sesion.getPorcentajeOcupacion());

            default:
                return null;
        }
    }

    public Sesion getSesion(int fila) {
        return sesiones.get(fila);
    }

    public void actualizarTabla() {
        fireTableDataChanged();
    }
}