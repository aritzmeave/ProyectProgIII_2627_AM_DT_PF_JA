package gui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class HiloReloj extends Thread {
    private JLabel etiqueta;

    public HiloReloj(JLabel etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public void run() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("hh:mm:ss");

        while (true) {
            String hora = LocalTime.now().format(formato);

            SwingUtilities.invokeLater(() -> etiqueta.setText("Hora: " + hora));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Se ha detenido el hilo del reloj.");
                return;
            }
        }
    }
}
