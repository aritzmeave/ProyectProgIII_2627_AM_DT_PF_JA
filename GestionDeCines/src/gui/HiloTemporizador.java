package gui;

import javax.swing.SwingUtilities;

public class HiloTemporizador extends Thread {

  private VentanaButacas ventana;
  private int segundos;

  public HiloTemporizador(VentanaButacas ventana, int segundos) {
      this.ventana = ventana;
      this.segundos = segundos;
  }

  @Override
  public void run() {
      for (int i = segundos; i >= 0; i--) {
          final int restantes = i;
          SwingUtilities.invokeLater(() -> ventana.actualizarBarra(restantes));
          try {
              Thread.sleep(1000);
          } catch (InterruptedException e) {
              return; // Nos han parado porque el cliente ya ha comprado o ha cerrado la ventana
          }
      }
      SwingUtilities.invokeLater(() -> ventana.tiempoAgotado());
  }
}
