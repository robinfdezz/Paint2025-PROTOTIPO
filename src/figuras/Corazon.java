package figuras;

import java.awt.*;
import java.awt.geom.Path2D;

public class Corazon extends Figura {

    /**
     * @param puntoInicial El punto donde se inicia el dibujo del corazón.
     */
    public Corazon(Point puntoInicial) {
        super(puntoInicial, new Point(puntoInicial));
    }

    /**
     * @param puntoActual El punto actual mientras se arrastra el ratón.
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
        g2.setStroke(new BasicStroke(this.grosor));

        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);

        if (width <= 0 || height <= 0) {
            return;
        }

        int hendiduraX = x + width / 2;
        int hendiduraY = y;

        int puntaX = x + width / 2;
        int puntaY = y + height;

        int controlLobeXOffset = width / 2;
        int controlLobeYOffset = height / 3;

        int vBaseY = y + height * 2 / 3;
        int leftVBaseX = x + width / 6;
        int rightVBaseX = x + width * 5 / 6;

        Path2D corazon = new Path2D.Double();

        corazon.moveTo(hendiduraX, hendiduraY);
        corazon.curveTo(hendiduraX - controlLobeXOffset * 0.8, hendiduraY - controlLobeYOffset * 0.5, x, y + height / 3,
                leftVBaseX, vBaseY);
        corazon.lineTo(puntaX, puntaY);
        corazon.lineTo(rightVBaseX, vBaseY);
        corazon.curveTo(x + width, y + height / 3, hendiduraX + controlLobeXOffset * 0.8,
                hendiduraY - controlLobeYOffset * 0.5, hendiduraX, hendiduraY);
        corazon.closePath();

        if (relleno) {
            if (colorDeRelleno != null) {
                g2.setColor(colorDeRelleno);
                g2.fill(corazon);
            }

            if (colorDeRelleno != colorDePrimerPlano) {
                g2.setColor(colorDePrimerPlano);
                g2.draw(corazon);
            }

        } else {
            g2.setColor(colorDePrimerPlano);
            g2.draw(corazon);
        }
    }

    /**
     * @return Un objeto FiguraData con los datos del corazón.
     */
    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Corazón");
        data.setPuntoInicial(this.getPunto(0));
        data.setPuntoFinal(this.getPunto(1));
        data.setColorDePrimerPlano(this.colorDePrimerPlano);
        data.setColorDeRelleno(this.colorDeRelleno);
        data.setEstaRelleno(this.relleno);
        data.setGrosor(this.grosor);
        return data;
    }

    /**
     * @param p El punto a verificar.
     * @return true si el punto está dentro del corazón, false en caso contrario.
     */
    @Override
    public boolean contains(Point p) {
        if (getPunto(0) == null || getPunto(1) == null || p == null) {
            return false;
        }
        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);

        if (width <= 0 || height <= 0) {
            return false;
        }

        int hendiduraX = x + width / 2;
        int hendiduraY = y;

        int puntaX = x + width / 2;
        int puntaY = y + height;

        int controlLobeXOffset = width / 2;
        int controlLobeYOffset = height / 3;

        int vBaseY = y + height * 2 / 3;
        int leftVBaseX = x + width / 6;
        int rightVBaseX = x + width * 5 / 6;

        Path2D corazonForma = new Path2D.Double();
        corazonForma.moveTo(hendiduraX, hendiduraY);
        corazonForma.curveTo(hendiduraX - controlLobeXOffset * 0.8, hendiduraY - controlLobeYOffset * 0.5,
                x, y + height / 3,
                leftVBaseX, vBaseY);
        corazonForma.lineTo(puntaX, puntaY);
        corazonForma.lineTo(rightVBaseX, vBaseY);
        corazonForma.curveTo(x + width, y + height / 3,
                hendiduraX + controlLobeXOffset * 0.8, hendiduraY - controlLobeYOffset * 0.5,
                hendiduraX, hendiduraY);
        corazonForma.closePath();

        return corazonForma.contains(p);
    }

    /**
     * @return Un objeto Rectangle que representa el área delimitador del corazón.
     */
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