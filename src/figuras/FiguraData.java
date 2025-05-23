package figuras;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class FiguraData implements Serializable {
    private static final long serialVersionUID = 2L;

    private String tipoFigura;
    private Point puntoInicial;
    private Point puntoFinal;
    private Point centro;
    private ArrayList<Point> puntosTrazo;
    private Color colorDePrimerPlano;
    private Color colorDeRelleno;
    private boolean estaRelleno;
    private int tamanoBorrador;
    private int grosor;

    /**
     * @param tipoFigura El nombre del tipo de figura (e.g., "Rectangulo").
     */
    public FiguraData(String tipoFigura) {
        this.tipoFigura = tipoFigura;
        this.puntosTrazo = new ArrayList<>();
    }

    public String getTipoFigura() {
        return tipoFigura;
    }

    public Point getPuntoInicial() {
        return puntoInicial;
    }

    public Point getPuntoFinal() {
        return puntoFinal;
    }

    public Point getCentro() {
        return centro;
    }

    public ArrayList<Point> getPuntosTrazo() {
        return puntosTrazo;
    }

    public Color getColorDePrimerPlano() {
        return colorDePrimerPlano;
    }

    public Color getColorDeRelleno() {
        return colorDeRelleno;
    }

    public boolean isEstaRelleno() {
        return estaRelleno;
    }

    public int getTamanoBorrador() {
        return tamanoBorrador;
    }

    public void setPuntoInicial(Point puntoInicial) {
        this.puntoInicial = puntoInicial;
    }

    public void setPuntoFinal(Point puntoFinal) {
        this.puntoFinal = puntoFinal;
    }

    public void setCentro(Point centro) {
        this.centro = centro;
    }

    public void setPuntosTrazo(ArrayList<Point> puntosTrazo) {
        this.puntosTrazo = new ArrayList<>(puntosTrazo);
    }

    public void setColorDePrimerPlano(Color colorDePrimerPlano) {
        this.colorDePrimerPlano = colorDePrimerPlano;
    }

    public void setColorDeRelleno(Color colorDeRelleno) {
        this.colorDeRelleno = colorDeRelleno;
    }

    public void setEstaRelleno(boolean estaRelleno) {
        this.estaRelleno = estaRelleno;
    }

    public void setTamanoBorrador(int tamanoBorrador) {
        this.tamanoBorrador = tamanoBorrador;
    }

    @Override
    public String toString() {
        return "FiguraData{" +
                "tipo='" + tipoFigura + '\'' +
                ", pInicial=" + puntoInicial +
                ", pFinal=" + puntoFinal +
                ", centro=" + centro +
                ", colorBorde=" + colorDePrimerPlano +
                ", colorRelleno=" + colorDeRelleno +
                ", relleno=" + estaRelleno +
                ", puntosTrazoSize=" + (puntosTrazo != null ? puntosTrazo.size() : 0) +
                '}';
    }

    public int getGrosor() {
        return grosor;
    }

    public void setGrosor(int grosor) {
        this.grosor = grosor;
    }
}