package figuras;

import java.awt.*;

public class Borrador extends Figura {
    private int tamano = 20;

    /**
     * @param p El punto inicial del borrador.
     */
    public Borrador(Point p) {
        super(p);
        this.colorDePrimerPlano = Color.WHITE;
        this.relleno = false;
        this.colorDeRelleno = null;
    }

    /**
     * @param g El objeto Graphics sobre el que dibujar.
     */
    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(this.grosor));
        g2d.fillOval(getPunto(0).x - tamano / 2, getPunto(0).y - tamano / 2, tamano, tamano);
    }

    /**
     * @param p El nuevo punto central del borrador.
     */
    @Override
    public void actualizar(Point p) {
        setPunto(0, p);
    }

    /**
     * @param tamano El nuevo tamaño del borrador.
     */
    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    /**
     * @return El tamaño del borrador.
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * @return Un objeto FiguraData con los datos del borrador, o null si no es
     *         copiable.
     */
    @Override
    public FiguraData getFiguraData() {
        return null; // Por defecto, no se copia el borrador
    }

    /**
     * @param p El punto a verificar.
     * @return Siempre false.
     */
    @Override
    public boolean contains(Point p) {
        return false;
    }

    /**
     * @return Un objeto Rectangle que representa el área del borrador.
     */
    @Override
    public java.awt.Rectangle getBounds() {
        if (getPunto(0) != null) {
            int x = getPunto(0).x - tamano / 2;
            int y = getPunto(0).y - tamano / 2;
            return new java.awt.Rectangle(x, y, tamano, tamano);
        }
        return null;
    }

    /**
     * @param offset El desplazamiento (dx, dy) a aplicar.
     */
    @Override
    public void translate(Point offset) {
        if (getPunto(0) != null) {
            getPunto(0).translate(offset.x, offset.y);
        }
    }
}