package gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import domain.Sesion;

//IAG (herramienta: ChatGPT)
//SIN CAMBIOS
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

        JScrollPane scroll = new JScrollPane(tablaSesiones);

        add(scroll, BorderLayout.CENTER);
    }
}