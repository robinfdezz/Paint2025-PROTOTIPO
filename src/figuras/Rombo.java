package figuras;

import java.awt.*;

public class Rombo extends Figura {

    /**
     * @param centro El punto central inicial del rombo.
     */
    public Rombo(Point centro) {
        super(centro, new Point(centro));
    }

    /**
     * @param puntoActual El punto actual.
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

        if (relleno) {
            if (colorDeRelleno != null) {
                g2.setColor(colorDeRelleno);
            }
            int dx = getPunto(1).x - getPunto(0).x;
            int dy = getPunto(1).y - getPunto(0).y;

            int[] xPoints = {
                    getPunto(0).x,
                    getPunto(0).x + dx,
                    getPunto(0).x,
                    getPunto(0).x - dx
            };

            int[] yPoints = {
                    getPunto(0).y - dy,
                    getPunto(0).y,
                    getPunto(0).y + dy,
                    getPunto(0).y
            };

            Polygon rombo = new Polygon(xPoints, yPoints, 4);
            g2.fillPolygon(rombo);

            if (colorDeRelleno != colorDePrimerPlano) {
                g2.setColor(colorDePrimerPlano);
                g2.drawPolygon(rombo);
            }
        } else {
            int dx = getPunto(1).x - getPunto(0).x;
            int dy = getPunto(1).y - getPunto(0).y;

            int[] xPoints = {
                    getPunto(0).x,
                    getPunto(0).x + dx,
                    getPunto(0).x,
                    getPunto(0).x - dx
            };

            int[] yPoints = {
                    getPunto(0).y - dy,
                    getPunto(0).y,
                    getPunto(0).y + dy,
                    getPunto(0).y
            };

            Polygon rombo = new Polygon(xPoints, yPoints, 4);
            g2.drawPolygon(rombo);
        }
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Rombo");
        data.setPuntoInicial(this.getPunto(0));
        data.setPuntoFinal(this.getPunto(1));
        data.setColorDePrimerPlano(this.colorDePrimerPlano);
        data.setColorDeRelleno(this.colorDeRelleno);
        data.setEstaRelleno(this.relleno);
        data.setCentro(this.getPunto(0));
        data.setGrosor(this.grosor);
        return data;
    }

    @Override
    public boolean contains(Point p) {
        if (getPunto(0) == null || getPunto(1) == null || p == null) {
            return false;
        }

        int dx = getPunto(1).x - getPunto(0).x;
        int dy = getPunto(1).y - getPunto(0).y;

        if (dx == 0 && dy == 0) {
            return getPunto(0).distance(p) < 5;
        }

        double relX = (p.x - getPunto(0).x) / (double) dx;
        double relY = (p.y - getPunto(0).y) / (double) dy;

        return Math.abs(relX) + Math.abs(relY) <= 1;
    }

    @Override
    public java.awt.Rectangle getBounds() {
        if (puntos.size() < 2)
            return null;

        int dx = Math.abs(getPunto(1).x - getPunto(0).x);
        int dy = Math.abs(getPunto(1).y - getPunto(0).y);
        return new java.awt.Rectangle(getPunto(0).x - dx, getPunto(0).y - dy, dx * 2, dy * 2);
    }

    @Override
    public void translate(Point offset) {
        if (getPunto(0) != null) {
            getPunto(0).translate(offset.x, offset.y);
        }
        if (getPunto(1) != null) {
            getPunto(1).translate(offset.x, offset.y);
        }
    }
}