package figuras;

import java.awt.*;
import java.util.ArrayList;

public class Octagono extends Figura {
    private int numeroLados = 8;

    /**
     * @param centro El punto central inicial del octágono.
     */
    public Octagono(Point centro) {
        super(centro, new Point(centro));
    }

    /**
     * @param nuevoPunto El nuevo punto de referencia.
     */
    @Override
    public void actualizar(Point nuevoPunto) {
        setPunto(1, nuevoPunto);
    }

    /**
     * @param g El objeto Graphics sobre el que dibujar.
     */
    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colorDePrimerPlano);
        g2.setStroke(new BasicStroke(this.grosor));

        int radio = (int) getPunto(0).distance(getPunto(1));
        int[] puntosX = new int[numeroLados];
        int[] puntosY = new int[numeroLados];

        for (int i = 0; i < numeroLados; i++) {
            double angulo = Math.toRadians(-90 + i * (360.0 / numeroLados));
            puntosX[i] = getPunto(0).x + (int) (radio * Math.cos(angulo));
            puntosY[i] = getPunto(0).y + (int) (radio * Math.sin(angulo));
        }

        Polygon octagonoForma = new Polygon(puntosX, puntosY, numeroLados);

        if (relleno) {
            if (colorDeRelleno != null) {
                g2.setColor(colorDeRelleno);
                g2.fillPolygon(octagonoForma);
            }

            if (colorDeRelleno != colorDePrimerPlano) {
                g2.setColor(colorDePrimerPlano);
                g2.drawPolygon(octagonoForma);
            }
        } else {
            g2.drawPolygon(octagonoForma);
        }
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Octagono");
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
        if (getPunto(0) == null || getPunto(1) == null) {
            return false;
        }

        int radio = (int) getPunto(0).distance(getPunto(1));
        ArrayList<Point> vertices = new ArrayList<>();
        for (int i = 0; i < numeroLados; i++) {
            double angle = Math.toRadians(-90 + i * (360.0 / numeroLados));
            vertices.add(new Point(getPunto(0).x + (int) (radio * Math.cos(angle)),
                    getPunto(0).y + (int) (radio * Math.sin(angle))));
        }

        boolean inside = false;
        for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
            if ((vertices.get(i).y > p.y) != (vertices.get(j).y > p.y) &&
                    (p.x < (vertices.get(j).x - vertices.get(i).x) * (p.y - vertices.get(i).y)
                            / (vertices.get(j).y - vertices.get(i).y) + vertices.get(i).x)) {
                inside = !inside;
            }
        }
        return inside;
    }

    @Override
    public java.awt.Rectangle getBounds() {
        if (puntos.size() < 2)
            return null;

        int radio = (int) getPunto(0).distance(getPunto(1));
        return new java.awt.Rectangle(getPunto(0).x - radio, getPunto(0).y - radio, radio * 2, radio * 2);
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