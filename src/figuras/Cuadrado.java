package figuras;

import java.awt.*;

public class Cuadrado extends Figura {
    private Point puntoInicial;
    private Point puntoFinal;

    /**
     * @param puntoInicial El punto inicial del cuadrado.
     */
    public Cuadrado(Point puntoInicial) {
        this.puntoInicial = puntoInicial;
        this.puntoFinal = puntoInicial;
    }

    /**
     * @param puntoActual El punto actual (típicamente la ubicación mientras se
     *                    arrastra el ratón).
     */
    @Override
    public void actualizar(Point puntoActual) {
        this.puntoFinal = puntoActual;
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
        int lado = Math.max(Math.abs(puntoFinal.x - puntoInicial.x), Math.abs(puntoFinal.y - puntoInicial.y));

        if (relleno) {
            if (colorDeRelleno != null) {
                g.setColor(colorDeRelleno);
            }
            g.fillRect(x, y, lado, lado);

            if (colorDeRelleno != colorDePrimerPlano && colorDeRelleno != null) {
                g.setColor(colorDePrimerPlano);
                g.drawRect(x, y, lado, lado);
            }
        } else {
            g.drawRect(x, y, lado, lado);
        }
    }

    @Override
    public FiguraData getFiguraData() {
        FiguraData data = new FiguraData("Cuadrado");
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
        int lado = Math.max(Math.abs(puntoFinal.x - puntoInicial.x), Math.abs(puntoFinal.y - puntoInicial.y));
        return new java.awt.Rectangle(x, y, lado, lado).contains(p);
    }

    @Override
    public Rectangle getBounds() {
        int x = Math.min(puntoInicial.x, puntoFinal.x);
        int y = Math.min(puntoInicial.y, puntoFinal.y);
        int lado = Math.max(Math.abs(puntoFinal.x - puntoInicial.x), Math.abs(puntoFinal.y - puntoInicial.y));
        return new java.awt.Rectangle(x, y, lado, lado);
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