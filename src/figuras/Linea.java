package figuras;

import java.awt.*;

public class Linea extends Figura {

    /**
     * @param puntoInicial El punto de inicio de la línea.
     * @param puntoFinal   El punto final de la línea.
     */
    public Linea(Point puntoInicial, Point puntoFinal) {
        super(puntoInicial, puntoFinal);
    }

    /**
     * @param puntoInicial El punto inicial de la línea.
     */
    public Linea(Point puntoInicial) {
        this(puntoInicial, puntoInicial);
    }

    /**
     * @param g El objeto Graphics sobre el que dibujar.
     */
    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(colorDePrimerPlano);
        g2d.setStroke(new BasicStroke(this.grosor));
        g2d.drawLine(getPunto(0).x, getPunto(0).y, getPunto(1).x, getPunto(1).y);
    }

    /**
     * @param puntoActual El punto final actual.
     */
    @Override
    public void actualizar(Point puntoActual) {
        setPunto(1, puntoActual);
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Linea");
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
        final int TOLERANCIA = 5;
        if (getPunto(0) != null && p.distance(getPunto(0)) <= TOLERANCIA) {
            return true;
        }
        if (getPunto(1) != null && p.distance(getPunto(1)) <= TOLERANCIA) {
            return true;
        }
        return false;
    }

    @Override
    public Rectangle getBounds() {
        if (puntos.size() < 2)
            return null;
        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);
        return new Rectangle(x, y, width, height);
    }
}