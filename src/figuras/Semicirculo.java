package figuras;

import java.awt.*;
import java.awt.geom.Arc2D;

public class Semicirculo extends Figura {

    /**
     * @param puntoInicial El punto donde se inicia el dibujo del semicírculo.
     */
    public Semicirculo(Point puntoInicial) {
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
        g2.setColor(colorDePrimerPlano);
        g2.setStroke(new BasicStroke(this.grosor));

        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);

        if (width <= 0 || height <= 0) {
            return;
        }

        int startAngle = (getPunto(1).y < getPunto(0).y) ? 180 : 0;
        int extentAngle = 180;
        int arcType = Arc2D.PIE;

        Arc2D.Double semicirculoForma = new Arc2D.Double(x, y, width, height, startAngle, extentAngle, arcType);

        if (relleno) {
            if (colorDeRelleno != null) {
                g2.setColor(colorDeRelleno);
                g2.fill(semicirculoForma);
            }

            if (colorDeRelleno != colorDePrimerPlano && colorDeRelleno != null) {
                g2.setColor(colorDePrimerPlano);
                g2.draw(semicirculoForma);
            }

        } else {
            g2.setColor(colorDePrimerPlano);
            g2.draw(semicirculoForma);
        }
    }

    /**
     * @return Un objeto FiguraData con los datos del semicírculo.
     */
    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Semicirculo");
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
     * @return true si el punto está dentro del semicírculo, false en caso
     *         contrario.
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

        int startAngle = (getPunto(1).y < getPunto(0).y) ? 180 : 0;
        int extentAngle = 180;
        int arcType = Arc2D.PIE;

        Arc2D.Double semicirculoForma = new Arc2D.Double(x, y, width, height, startAngle, extentAngle, arcType);

        return semicirculoForma.contains(p);
    }

    /**
     * @return Un objeto Rectangle que representa el área delimitador del
     *         semicírculo.
     */
    @Override
    public Rectangle getBounds() {
        if (getPunto(0) == null || getPunto(1) == null)
            return null;

        int x = Math.min(getPunto(0).x, getPunto(1).x);
        int y = Math.min(getPunto(0).y, getPunto(1).y);
        int width = Math.abs(getPunto(1).x - getPunto(0).x);
        int height = Math.abs(getPunto(1).y - getPunto(0).y);

        return new Rectangle(x, y, width, height);
    }

    /**
     * @param offset El desplazamiento (dx, dy) a aplicar.
     */
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