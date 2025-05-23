package figuras;

import java.awt.*;

public class Rectangulo extends Figura {
    private Point puntoInicial;
    private Point puntoFinal;

    /**
     * @param puntoInicial El punto inicial del rectángulo.
     */
    public Rectangulo(Point puntoInicial) {
        this.puntoInicial = puntoInicial;
        this.puntoFinal = puntoInicial;
    }

    /**
     * @param puntoFinal El punto actual (típicamente la ubicación mientras se
     *                   arrastra el ratón).
     */
    @Override
    public void actualizar(Point puntoFinal) {
        this.puntoFinal = puntoFinal;
    }

    /**
     * @param g El objeto Graphics sobre el que dibujar.
     */
    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(colorDePrimerPlano);
        g2d.setStroke(new BasicStroke(this.grosor));

        int x = Math.min(puntoInicial.x, puntoFinal.x);
        int y = Math.min(puntoInicial.y, puntoFinal.y);
        int width = Math.abs(puntoFinal.x - puntoInicial.x);
        int height = Math.abs(puntoFinal.y - puntoInicial.y);

        if (relleno) {
            g2d.setColor(colorDeRelleno);
            g2d.fillRect(x, y, width, height);

            if (colorDeRelleno != colorDePrimerPlano) {
                g2d.setColor(colorDePrimerPlano);
                g2d.drawRect(x, y, width, height);
            }
        } else {
            g2d.drawRect(x, y, width, height);
        }
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Rectangulo");
        data.setPuntoInicial(this.puntoInicial);
        data.setPuntoFinal(this.puntoFinal);
        data.setColorDePrimerPlano(this.colorDePrimerPlano);
        data.setColorDeRelleno(this.colorDeRelleno);
        data.setEstaRelleno(this.relleno);
        data.setGrosor(this.grosor);
        return data;
    }

    @Override
    public boolean contains(Point p) {
        int x = Math.min(puntoInicial.x, puntoFinal.x);
        int y = Math.min(puntoInicial.y, puntoFinal.y);
        int width = Math.abs(puntoFinal.x - puntoInicial.x);
        int height = Math.abs(puntoFinal.y - puntoInicial.y);
        return new java.awt.Rectangle(x, y, width, height).contains(p);
    }

    @Override
    public Rectangle getBounds() {
        int x = Math.min(puntoInicial.x, puntoFinal.x);
        int y = Math.min(puntoInicial.y, puntoFinal.y);
        int width = Math.abs(puntoFinal.x - puntoInicial.x);
        int height = Math.abs(puntoFinal.y - puntoInicial.y);
        return new java.awt.Rectangle(x, y, width, height);
    }

    @Override
    public void translate(Point offset) {
        if (puntoInicial != null) {
            puntoInicial.translate(offset.x, offset.y);
        }
        if (puntoFinal != null) {
            puntoFinal.translate(offset.x, offset.y);
        }
    }
}