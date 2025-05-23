package figuras;

import java.awt.*;

public class Ring extends Figura {

    /**
     * @param centro El punto central inicial del anillo.
     */
    public Ring(Point centro) {
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(this.grosor));

        int radioExterior = (int) getPunto(0).distance(getPunto(1));
        int radioInterior = (int) (radioExterior * 0.6);

        int xExt = getPunto(0).x - radioExterior;
        int yExt = getPunto(0).y - radioExterior;
        int diamExt = radioExterior * 2;

        int xInt = getPunto(0).x - radioInterior;
        int yInt = getPunto(0).y - radioInterior;
        int diamInt = radioInterior * 2;

        if (relleno) {
            if (colorDeRelleno != null) {
                g2d.setColor(colorDeRelleno);
            }
            g2d.fillOval(xExt, yExt, diamExt, diamExt);

            if (getParent() != null) {
                Color colorFondoPanel = getParent().getBackground();
                if (colorFondoPanel != null) {
                    g2d.setColor(colorFondoPanel);
                    g2d.fillOval(xInt, yInt, diamInt, diamInt);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(xInt, yInt, diamInt, diamInt);
                }
            } else {
                g2d.setColor(Color.WHITE);
                g2d.fillOval(xInt, yInt, diamInt, diamInt);
            }

            if (colorDeRelleno != colorDePrimerPlano) {
                g2d.setColor(colorDePrimerPlano);
                g2d.drawOval(xExt, yExt, diamExt, diamExt);
            }

            g2d.setColor(colorDePrimerPlano);
            g2d.drawOval(xInt, yInt, diamInt, diamInt);

        } else {
            g2d.setColor(colorDePrimerPlano);
            g2d.drawOval(xExt, yExt, diamExt, diamExt);
            g2d.drawOval(xInt, yInt, diamInt, diamInt);
        }
    }

    private Graphics2D getParent() {
        return null;
    }

    /**
     * @return Un objeto FiguraData con los datos del anillo.
     */
    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Ring");
        data.setPuntoInicial(this.getPunto(0));
        data.setPuntoFinal(this.getPunto(1));
        data.setColorDePrimerPlano(this.colorDePrimerPlano);
        data.setColorDeRelleno(this.colorDeRelleno);
        data.setEstaRelleno(this.relleno);
        data.setCentro(this.getPunto(0));
        data.setGrosor(this.grosor);
        return data;
    }

    /**
     * @param p El punto a verificar.
     * @return true si el punto está dentro del anillo, false en caso contrario.
     */
    @Override
    public boolean contains(Point p) {
        if (getPunto(0) == null || getPunto(1) == null || p == null) {
            return false;
        }

        int radioExterior = (int) getPunto(0).distance(getPunto(1));
        int radioInterior = (int) (radioExterior * 0.6);
        double distanciaAlCentro = getPunto(0).distance(p);

        return distanciaAlCentro <= radioExterior && distanciaAlCentro >= radioInterior;
    }

    /**
     * @return Un objeto Rectangle que representa el área delimitador del anillo.
     */
    @Override
    public Rectangle getBounds() {
        if (puntos.size() < 2)
            return null;

        int radioExterior = (int) getPunto(0).distance(getPunto(1));
        int x = getPunto(0).x - radioExterior;
        int y = getPunto(0).y - radioExterior;
        int diametro = radioExterior * 2;
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