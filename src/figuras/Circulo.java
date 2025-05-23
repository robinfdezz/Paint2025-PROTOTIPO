package figuras;

import java.awt.*;

public class Circulo extends Figura {

    /**
     * @param centro El punto central inicial del círculo.
     */
    public Circulo(Point centro) {
        super(centro, new Point(centro));
    }

    public Color getColorDePrimerPlano() {
        return colorDePrimerPlano;
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
        Graphics2D g2d = (Graphics2D) g;

        int radio = (int) getPunto(0).distance(getPunto(1));
        int diametro = radio * 2;

        int x = getPunto(0).x - radio;
        int y = getPunto(0).y - radio;

        g2d.setStroke(new BasicStroke(this.grosor));

        if (relleno) {
            if (colorDeRelleno != null) {
                g2d.setColor(colorDeRelleno);
            }
            g2d.fillOval(x, y, diametro, diametro);

            if (colorDeRelleno != colorDePrimerPlano) {
                g2d.setColor(colorDePrimerPlano);
                g2d.drawOval(x, y, diametro, diametro);
            }
        } else {
            g2d.setColor(colorDePrimerPlano);
            g2d.drawOval(x, y, diametro, diametro);
        }
    }

    /**
     * @return Un objeto FiguraData con los datos del círculo.
     */
    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Círculo");
        data.setPuntoInicial(this.getPunto(0));
        data.setPuntoFinal(this.getPunto(1));
        data.setCentro(this.getPunto(0));
        data.setColorDePrimerPlano(this.colorDePrimerPlano);
        data.setColorDeRelleno(this.colorDeRelleno);
        data.setEstaRelleno(this.relleno);
        data.setGrosor(this.grosor);

        return data;
    }

    /**
     * @param p El punto a verificar.
     * @return true si el punto está dentro del círculo, false en caso contrario.
     */
    @Override
    public boolean contains(Point p) {
        if (getPunto(0) == null || getPunto(1) == null || p == null) {
            return false;
        }
        int radio = (int) getPunto(0).distance(getPunto(1));
        return getPunto(0).distance(p) <= radio;
    }

    /**
     * @return Un objeto Rectangle que representa el área delimitador del círculo.
     */
    @Override
    public Rectangle getBounds() {
        if (puntos.size() < 2)
            return null;

        int radio = (int) getPunto(0).distance(getPunto(1));
        int x = getPunto(0).x - radio;
        int y = getPunto(0).y - radio;
        int diametro = radio * 2;
        return new Rectangle(x, y, diametro, diametro);
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