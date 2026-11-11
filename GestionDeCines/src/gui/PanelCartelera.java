package gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import domain.Sesion;


public class PanelCartelera extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tablaSesiones;
    private ModeloTablaSesiones modelo;

    public PanelCartelera(List<Sesion> sesiones) {

        setLayout(new BorderLayout());

        modelo = new ModeloTablaSesiones(sesiones);

        tablaSesiones = new JTable(modelo);
        
        tablaSesiones.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int fila = tablaSesiones.getSelectedRow();

                    if (fila != -1) {

                        int filaModelo = tablaSesiones.convertRowIndexToModel(fila);

                        Sesion sesion = modelo.getSesion(filaModelo);

                        System.out.println("Sesión seleccionada: " + sesion);

                    }
                }
            }
        });
        RendererCartelera renderer = new RendererCartelera(modelo);

        tablaSesiones.setDefaultRenderer(Object.class, renderer);
        tablaSesiones.setDefaultRenderer(Number.class, renderer);

        JPanel panelBuscador = new JPanel();

        JLabel lblBuscar = new JLabel("Buscar película:");

        JTextField txtBuscar = new JTextField(20);

        panelBuscador.add(lblBuscar);
        panelBuscador.add(txtBuscar);

        add(panelBuscador, BorderLayout.NORTH);
        
        TableRowSorter<ModeloTablaSesiones> sorter = new TableRowSorter<>(modelo);

        tablaSesiones.setRowSorter(sorter);

        txtBuscar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                String texto = txtBuscar.getText();

                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(
                        "(?i)" + java.util.regex.Pattern.quote(texto), 0));
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(tablaSesiones);

        add(scroll, BorderLayout.CENTER);
    }
}

    public JTable getTabla() {
        return tablaSesiones;
    }

    public Sesion getSesionSeleccionada() {

        int fila = tablaSesiones.getSelectedRow();

        if (fila == -1) {
            return null;
        }

        int filaModelo = tablaSesiones.convertRowIndexToModel(fila);

        return modelo.getSesion(filaModelo);
    }

    public void actualizarTabla() {
        modelo.fireTableDataChanged();
    }
