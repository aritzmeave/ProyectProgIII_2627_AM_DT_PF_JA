package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class IconoButaca implements Icon {

    private Color color;
    private boolean movilidadReducida;
    private boolean vip;
    private double escala;

    public IconoButaca(Color color, boolean movilidadReducida, boolean vip) {
        this(color, movilidadReducida, vip, 1.3);
    }

    public IconoButaca(Color color, boolean movilidadReducida, boolean vip, double escala) {
        this.color = color;
        this.movilidadReducida = movilidadReducida;
        this.vip = vip;
        this.escala = escala;
    }

    @Override
    public int getIconWidth() {
        return (int) (32 * escala);
    }

    @Override
    public int getIconHeight() {
        return (int) (30 * escala);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);
        g2.scale(escala, escala);

        if (movilidadReducida) {
            dibujarSillaDeRuedas(g2, 0, 0);
        } else {
            dibujarAsiento(g2, 0, 0);
        }

        g2.dispose();
    }

    private void dibujarAsiento(Graphics2D g2, int x, int y) {
        g2.setColor(color);
        g2.fillRoundRect(x + 6, y + 1, 20, 18, 8, 8);
        g2.fillRoundRect(x + 4, y + 17, 24, 8, 6, 6);
        g2.fillRoundRect(x + 1, y + 11, 5, 16, 4, 4);
        g2.fillRoundRect(x + 26, y + 11, 5, 16, 4, 4);

        g2.setColor(color.darker());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x + 6, y + 1, 20, 18, 8, 8);
        g2.drawRoundRect(x + 4, y + 17, 24, 8, 6, 6);
        g2.drawRoundRect(x + 1, y + 11, 5, 16, 4, 4);
        g2.drawRoundRect(x + 26, y + 11, 5, 16, 4, 4);
        g2.drawLine(x + 10, y + 6, x + 22, y + 6);

        if (vip) {
            dibujarEstrella(g2, x, y);
        }
    }

    private void dibujarEstrella(Graphics2D g2, int x, int y) {
        int[] puntosX = {16, 18, 23, 19, 20, 16, 12, 13, 9, 14};
        int[] puntosY = {6, 10, 10, 13, 18, 15, 18, 13, 10, 10};
        for (int i = 0; i < puntosX.length; i++) {
            puntosX[i] += x;
            puntosY[i] += y;
        }
        g2.setColor(Color.WHITE);
        g2.fillPolygon(puntosX, puntosY, puntosX.length);
        g2.setColor(new Color(120, 80, 0));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawPolygon(puntosX, puntosY, puntosX.length);
    }

    private void dibujarSillaDeRuedas(Graphics2D g2, int x, int y) {
        g2.setColor(color);
        g2.fillRoundRect(x, y, 32, 30, 8, 8);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.fillOval(x + 12, y + 3, 6, 6);
        g2.drawLine(x + 14, y + 10, x + 14, y + 18);
        g2.drawLine(x + 14, y + 13, x + 20, y + 13);
        g2.drawLine(x + 14, y + 18, x + 21, y + 18);
        g2.drawLine(x + 21, y + 18, x + 24, y + 25);
        g2.drawArc(x + 6, y + 13, 14, 14, 110, 250);
    }
}