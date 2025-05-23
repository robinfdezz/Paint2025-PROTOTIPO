package figuras;

import java.awt.*;

public class Ovalo extends Figura {

    /**
     * @param puntoInicial El punto inicial del óvalo.
     */
    public Ovalo(Point puntoInicial) {
        super(puntoInicial, new Point(puntoInicial));
    }

    /**
     * @param puntoActual El punto actual (típicamente la ubicación mientras se arrastra el ratón).
     */
    @Override
    public void actualizar(Point puntoActual) {
        setPunto(1, puntoActual);
    }

    /**
     * @param g El objeto Graphics sobre el que dibujar.
     */
    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colorDePrimerPlano);
        g2.setStroke(new BasicStroke(this.grosor));
        
        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);

        if (relleno) {
            if (colorDeRelleno != null) {
                g2.setColor(colorDeRelleno);
            }
            g2.fillOval(x, y, width, height);

            if (colorDeRelleno != colorDePrimerPlano && colorDeRelleno != null) {
                g2.setColor(colorDePrimerPlano);
                g2.drawOval(x, y, width, height);
            }
        } else {
            g2.drawOval(x, y, width, height);
        }
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Ovalo");
        data.setPuntoInicial(this.getPunto(0));
        data.setPuntoFinal(this.getPunto(1));
        data.setColorDePrimerPlano(this.colorDePrimerPlano);
        data.setColorDeRelleno(this.colorDeRelleno);
        data.setEstaRelleno(this.relleno);
        data.setGrosor(this.grosor);
        return data;
    }

    @Override
    public boolean contains(Point p) {
        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);
        return new java.awt.Rectangle(x, y, width, height).contains(p);
    }

    @Override
    public Rectangle getBounds() {
        if (puntos.size() < 2) return null;

        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);
        return new Rectangle(x, y, width, height);
    }
}