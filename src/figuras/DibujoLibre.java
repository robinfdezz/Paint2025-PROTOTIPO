package figuras;

import java.awt.*;
import java.util.ArrayList;

public class DibujoLibre extends Figura {

    /**
     * @param puntoInicial El punto inicial del dibujo a mano alzada.
     */
    public DibujoLibre(Point puntoInicial) {
        super(puntoInicial);
    }

    /**
     * @param g El objeto Graphics sobre el que dibujar.
     */
    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(colorDePrimerPlano);
        g2d.setStroke(new BasicStroke(this.grosor));

        if (puntos.size() < 2) {
            if (puntos.size() == 1 && getPunto(0) != null) {
                g2d.fillRect(getPunto(0).x, getPunto(0).y, 1, 1);
            }
            return;
        }

        for (int i = 1; i < puntos.size(); i++) {
            Point p1 = getPunto(i - 1);
            Point p2 = getPunto(i);
            if (p1 != null && p2 != null) {
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    /**
     * @param puntoActual El punto actual a añadir.
     */
    @Override
    public void actualizar(Point puntoActual) {
        puntos.add(puntoActual);
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("DibujoLibre");
        data.setPuntoInicial(this.getPunto(0));
        data.setPuntoFinal(this.getPunto(puntos.isEmpty() ? 0 : puntos.size() - 1));
        data.setColorDePrimerPlano(this.colorDePrimerPlano);
        data.setColorDeRelleno(this.colorDeRelleno);
        data.setEstaRelleno(this.relleno);
        data.setPuntosTrazo(new ArrayList<>(this.puntos));
        data.setGrosor(this.grosor);
        return data;
    }

    @Override
    public boolean contains(Point p) {
        if (puntos == null || puntos.isEmpty()) {
            return false;
        }

        final int TOLERANCIA = 5;

        for (Point punto : puntos) {
            if (punto != null && punto.distance(p) <= TOLERANCIA) {
                return true;
            }
        }

        for (int i = 1; i < puntos.size(); i++) {
            Point p1 = getPunto(i - 1);
            Point p2 = getPunto(i);
            if (p1 != null && p2 != null && distanciaPuntoALinea(p, p1, p2) <= TOLERANCIA) {
                return true;
            }
        }

        return false;
    }

    private double distanciaPuntoALinea(Point p, Point p1, Point p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;

        if (dx == 0 && dy == 0) {
            return p1.distance(p);
        }

        double t = ((p.x - p1.x) * dx + (p.y - p1.y) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        Point closestPoint = new Point((int) (p1.x + t * dx), (int) (p1.y + t * dy));
        return p.distance(closestPoint);
    }

    @Override
    public Rectangle getBounds() {
        if (puntos == null || puntos.isEmpty()) {
            return null;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : puntos) {
            if (p != null) {
                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
                maxX = Math.max(maxX, p.x);
                maxY = Math.max(maxY, p.y);
            }
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public void translate(Point offset) {
        if (puntos != null) {
            for (Point p : puntos) {
                if (p != null) {
                    p.translate(offset.x, offset.y);
                }
            }
        }
    }
}