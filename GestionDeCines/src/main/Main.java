
package main;

import javax.swing.JOptionPane;

import domain.DatosParaCines;
import gui.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        try {
        	DatosParaCines datos = new DatosParaCines();
            VentanaPrincipal ventana = new VentanaPrincipal(datos);
            ventana.setVisible(true);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}