package figuras;

import java.awt.*;
import java.util.ArrayList;

public class Estrella extends Figura {
    private int puntas = 5;

    /**
     * @param centro El punto central inicial de la estrella.
     */
    public Estrella(Point centro) {
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
        int[] puntosX = new int[puntas * 2];
        int[] puntosY = new int[puntas * 2];

        for (int i = 0; i < puntas * 2; i++) {
            double angulo = Math.toRadians(-90 + i * (360.0 / (puntas * 2)));
            int radioActual = (i % 2 == 0) ? radio : (int) (radio * 0.5);
            puntosX[i] = getPunto(0).x + (int) (radioActual * Math.cos(angulo));
            puntosY[i] = getPunto(0).y + (int) (radioActual * Math.sin(angulo));
        }

        Polygon estrellaForma = new Polygon(puntosX, puntosY, puntas * 2);

        if (relleno) {
            if (colorDeRelleno != null) {
                g2.setColor(colorDeRelleno);
                g.fillPolygon(estrellaForma);
            }

            if (colorDeRelleno != colorDePrimerPlano) {
                g2.setColor(colorDePrimerPlano);
                g2.drawPolygon(estrellaForma);
            }
        } else {
            g2.drawPolygon(estrellaForma);
        }
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Estrella");
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
        for (int i = 0; i < puntas * 2; i++) {
            double angle = Math.toRadians(-90 + i * (360.0 / (puntas * 2)));
            int radioActual = (i % 2 == 0) ? radio : (int) (radio * 0.5);
            vertices.add(new Point(getPunto(0).x + (int) (radioActual * Math.cos(angle)),
                    getPunto(0).y + (int) (radioActual * Math.sin(angle))));
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
        if (puntos.size() < 2 || getPunto(0) == null || getPunto(1) == null)
            return null;

        int radio = (int) getPunto(0).distance(getPunto(1));
        int[] puntosX = new int[puntas * 2];
        int[] puntosY = new int[puntas * 2];

        for (int i = 0; i < puntas * 2; i++) {
            double angulo = Math.toRadians(-90 + i * (360.0 / (puntas * 2)));
            int radioActual = (i % 2 == 0) ? radio : (int) (radio * 0.5);
            puntosX[i] = getPunto(0).x + (int) (radioActual * Math.cos(angulo));
            puntosY[i] = getPunto(0).y + (int) (radioActual * Math.sin(angulo));
        }

        int minX = puntosX[0];
        int minY = puntosY[0];
        int maxX = puntosX[0];
        int maxY = puntosY[0];

        for (int i = 1; i < puntosX.length; i++) {
            minX = Math.min(minX, puntosX[i]);
            minY = Math.min(minY, puntosY[i]);
            maxX = Math.max(maxX, puntosX[i]);
            maxY = Math.max(maxY, puntosY[i]);
        }

        return new java.awt.Rectangle(minX, minY, maxX - minX, maxY - minY);
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