package figuras;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Figura implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Color colorDePrimerPlano = Color.BLACK;
    protected boolean relleno = false;
    protected Color colorDeRelleno; // CAMBIO: Visibilidad de public a protected
    protected List<Point> puntos = new ArrayList<>();
    protected int grosor;

    public Figura() {
        this.grosor = 1;
    }

    public Figura(Point... puntosIniciales) {
        this.grosor = 1;
        puntos.addAll(Arrays.asList(puntosIniciales));
    }

    public Point getPunto(int index) {
        if (index >= 0 && index < puntos.size()) {
            return puntos.get(index);
        }
        return null;
    }

    public void setPunto(int index, Point p) {
        if (index >= 0 && index < puntos.size()) {
            puntos.set(index, p);
        }
    }

    public abstract void dibujar(Graphics g);

    public abstract void actualizar(Point puntoActual);

    public Rectangle getBounds() {
        if (puntos.isEmpty()) {
            return null;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : puntos) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public void translate(Point offset) {
        for (Point p : puntos) {
            p.translate(offset.x, offset.y);
        }
    }

    public abstract FiguraData getFiguraData();

    public abstract boolean contains(Point p); // CAMBIO: Declarado como abstract

    public void setColorDePrimerPlano(Color colorBordeActual) {
        this.colorDePrimerPlano = colorBordeActual;
    }

    public boolean isRelleno() {
        return this.relleno;
    }

    public void setColorDeRelleno(Color colorRellenoActual) {
        this.colorDeRelleno = colorRellenoActual;
    }

    public Color getColorDeRelleno() {
        return this.colorDeRelleno;
    }

    public void setRelleno(boolean relleno) {
        this.relleno = relleno;
    }

    public void setGrosor(int grosor) {
        this.grosor = grosor;
    }

    public int getGrosor() {
        return this.grosor;
    }

}