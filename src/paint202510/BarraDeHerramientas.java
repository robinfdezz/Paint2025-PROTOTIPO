package paint202510;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.AbstractButton;

/**
 * Clase BarraDeHerramientas - Crea y gestiona la barra de herramientas con
 * botones de figuras y acciones.
 * Ahora incluye el botón de selección de figura.
 */
public class BarraDeHerramientas extends JToolBar {

    // JToggleButtons para las herramientas de dibujo y selección
    protected JToggleButton btnLataPintura;
    protected JToggleButton btnBorrador;
    protected JToggleButton btnLapiz;
    protected JToggleButton btnLinea;
    protected JToggleButton btnRectangulo;
    protected JToggleButton btnOvalo;
    protected JToggleButton btnCirculo;
    protected JToggleButton btnCuadrado;
    protected JToggleButton btnTriangulo;
    protected JToggleButton btnPentagono;
    protected JToggleButton btnRombo;
    protected JToggleButton btnHeptagono;
    protected JToggleButton btnOctagono;
    protected JToggleButton btnEstrella;
    protected JToggleButton btnFlecha;
    protected JToggleButton btnCorazon;
    protected JToggleButton btnTrapecio;
    protected JToggleButton btnSemicirculo;
    protected JToggleButton btnRing;
    protected JToggleButton btnSeleccionar;

    protected JButton btnGuardar;
    protected JButton btnDeshacer;
    protected JButton btnRehacer;
    protected JButton btnLimpiar;

    private ButtonGroup grupoBotones;
    private HerramientaSeleccionadaListener herramientaSeleccionadaListener;

    private PanelDeColores panelDeColores;

    public BarraDeHerramientas() {
        setOrientation(JToolBar.VERTICAL);

        btnLataPintura = new JToggleButton("Lata");

        btnLapiz = new JToggleButton("Lapiz");
        btnLinea = new JToggleButton("Linea");
        btnRectangulo = new JToggleButton("Rectangulo");
        btnOvalo = new JToggleButton("Óvalo");
        btnCirculo = new JToggleButton("Círculo");
        btnBorrador = new JToggleButton("Borrador");
        btnCuadrado = new JToggleButton("Cuadrado");
        btnTriangulo = new JToggleButton("Triángulo");
        btnPentagono = new JToggleButton("Pentágono");
        btnRombo = new JToggleButton("Rombo");
        btnHeptagono = new JToggleButton("Heptagono");
        btnOctagono = new JToggleButton("Octagono");
        btnEstrella = new JToggleButton("Estrella");
        btnFlecha = new JToggleButton("Flecha");
        btnCorazon = new JToggleButton("Corazon");
        btnTrapecio = new JToggleButton("Trapecio");
        btnSemicirculo = new JToggleButton("Semicirculo");
        btnRing = new JToggleButton("Ring");

        btnSeleccionar = new JToggleButton("Seleccionar Figura");

        btnGuardar = new JButton("Guardar");
        btnDeshacer = new JButton("Deshacer");
        btnRehacer = new JButton("Rehacer");
        btnLimpiar = new JButton("Limpiar");

        formatearYAgregar(btnGuardar, "guardar.png", "Guardar Imagen", false);
        formatearYAgregar(btnDeshacer, "deshacer.png", "Deshacer última acción", false);
        formatearYAgregar(btnRehacer, "rehacer.png", "Rehacer última acción deshecha", false);
        formatearYAgregar(btnLimpiar, "limpiar.png", "Limpiar todo el lienzo", false);
        formatearYAgregar(btnSeleccionar, "seleccion.png", "Seleccionar Figura", true);
        formatearYAgregar(btnBorrador, "borrador.png", "Borrador", true);

        formatearYAgregar(btnLataPintura, "lata_pintura.png", "Color de relleno", true);
        add(new Separator());

        formatearYAgregar(btnLapiz, "lapiz.png", "Dibujo Libre", true);
        formatearYAgregar(btnLinea, "linea.png", "Línea", true);
        formatearYAgregar(btnRectangulo, "rectangulo.png", "Rectángulo", true);
        formatearYAgregar(btnCuadrado, "cuadrado.png", "Cuadrado", true);
        formatearYAgregar(btnOvalo, "ovalo.png", "Óvalo", true);
        formatearYAgregar(btnCirculo, "circulo.png", "Círculo", true);
        formatearYAgregar(btnTriangulo, "triangulo.png", "Triángulo", true);
        formatearYAgregar(btnRombo, "rombo.png", "Rombo", true);
        formatearYAgregar(btnPentagono, "pentagono.png", "Pentágono", true);
        formatearYAgregar(btnHeptagono, "heptagono.png", "Heptagono", true);
        formatearYAgregar(btnOctagono, "octagono.png", "Octagono", true);
        formatearYAgregar(btnEstrella, "estrella.png", "Estrella", true);
        formatearYAgregar(btnFlecha, "flecha.png", "Flecha", true);
        formatearYAgregar(btnCorazon, "corazon.png", "Corazón", true);
        formatearYAgregar(btnTrapecio, "trapecio.png", "Trapecio", true);
        formatearYAgregar(btnSemicirculo, "semicirculo.png", "Semicirculo", true);
        formatearYAgregar(btnRing, "ring.png", "Ring", true);

        grupoBotones = new ButtonGroup();
        grupoBotones.add(btnSeleccionar);
        grupoBotones.add(btnLapiz);
        grupoBotones.add(btnLinea);
        grupoBotones.add(btnRectangulo);
        grupoBotones.add(btnCuadrado);
        grupoBotones.add(btnOvalo);
        grupoBotones.add(btnCirculo);
        grupoBotones.add(btnTriangulo);
        grupoBotones.add(btnPentagono);
        grupoBotones.add(btnRombo);
        grupoBotones.add(btnHeptagono);
        grupoBotones.add(btnOctagono);
        grupoBotones.add(btnEstrella);
        grupoBotones.add(btnFlecha);
        grupoBotones.add(btnCorazon);
        grupoBotones.add(btnTrapecio);
        grupoBotones.add(btnSemicirculo);
        grupoBotones.add(btnRing);
        grupoBotones.add(btnBorrador);
        grupoBotones.add(btnLataPintura);

        ActionListener herramientaButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JToggleButton sourceButton = (JToggleButton) e.getSource();
                if (sourceButton.isSelected()) {
                    notificarCambioDeHerramienta();
                    if (panelDeColores != null && panelDeColores.getPanelDeDibujo() != null) {
                        panelDeColores.getPanelDeDibujo().deseleccionarArea();
                        panelDeColores.getPanelDeDibujo().repaint();
                    }
                } else if (!sourceButton.isSelected() && sourceButton == btnSeleccionar) {
                    if (panelDeColores != null && panelDeColores.getPanelDeDibujo() != null) {
                        panelDeColores.getPanelDeDibujo().deseleccionarArea();
                        panelDeColores.getPanelDeDibujo().repaint();
                    }
                }
            }
        };

        btnSeleccionar.addActionListener(herramientaButtonListener);
        btnLapiz.addActionListener(herramientaButtonListener);
        btnLinea.addActionListener(herramientaButtonListener);
        btnRectangulo.addActionListener(herramientaButtonListener);
        btnOvalo.addActionListener(herramientaButtonListener);
        btnCirculo.addActionListener(herramientaButtonListener);
        btnCuadrado.addActionListener(herramientaButtonListener);
        btnTriangulo.addActionListener(herramientaButtonListener);
        btnPentagono.addActionListener(herramientaButtonListener);
        btnRombo.addActionListener(herramientaButtonListener);
        btnHeptagono.addActionListener(herramientaButtonListener);
        btnOctagono.addActionListener(herramientaButtonListener);
        btnEstrella.addActionListener(herramientaButtonListener);
        btnFlecha.addActionListener(herramientaButtonListener);
        btnCorazon.addActionListener(herramientaButtonListener);
        btnTrapecio.addActionListener(herramientaButtonListener);
        btnSemicirculo.addActionListener(herramientaButtonListener);
        btnRing.addActionListener(herramientaButtonListener);
        btnBorrador.addActionListener(herramientaButtonListener);
        btnLataPintura.addActionListener(herramientaButtonListener);

        ActionListener accionButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deseleccionarBotonesHerramienta();
                if (panelDeColores != null && panelDeColores.getPanelDeDibujo() != null) {
                    panelDeColores.getPanelDeDibujo().deseleccionarArea();
                    panelDeColores.getPanelDeDibujo().repaint();
                }
            }
        };

        btnDeshacer.addActionListener(accionButtonListener);
        btnRehacer.addActionListener(accionButtonListener);
        btnLimpiar.addActionListener(accionButtonListener);

    }

    public void setHerramientaSeleccionadaListener(HerramientaSeleccionadaListener listener) {
        this.herramientaSeleccionadaListener = listener;
    }

    private void notificarCambioDeHerramienta() {
        if (herramientaSeleccionadaListener != null) {
            herramientaSeleccionadaListener.herramientaSeleccionadaCambio(getHerramientaSeleccionada());
        }
    }

    private void formatearYAgregar(AbstractButton boton, String nombreIcono, String tooltip, boolean esHerramienta) {
        boton.setFocusable(false);
        boton.setToolTipText("Seleccione: " + tooltip);

        boton.putClientProperty("esHerramienta", esHerramienta);

        java.net.URL ruta = getClass().getResource("/iconos/" + nombreIcono);
        if (ruta != null) {
            boton.setIcon(new ImageIcon(ruta));
            boton.setText(null);
        } else {
            if (boton.getText() == null || boton.getText().isEmpty()) {
                boton.setText(tooltip);
            }

            boton.setFont(new Font("Arial", Font.BOLD, 10));
            boton.setHorizontalTextPosition(SwingConstants.CENTER);
            boton.setVerticalTextPosition(SwingConstants.BOTTOM);

            System.err.println("Icono no encontrado: /iconos/" + nombreIcono + " (Usando texto)");
        }

        add(boton);
    }

    public String getHerramientaSeleccionada() {
        for (Component boton : this.getComponents()) {
            if (boton instanceof JToggleButton) {
                JToggleButton toggleButton = (JToggleButton) boton;
                if (toggleButton.isSelected()) {
                    return toggleButton.getToolTipText().replace("Seleccione: ", "");
                }
            }
        }
        return "Ninguna";
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnDeshacer() {
        return btnDeshacer;
    }

    public JButton getBtnRehacer() {
        return btnRehacer;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public void deseleccionarBotonesHerramienta() {
        if (grupoBotones != null) {
            grupoBotones.clearSelection();
        }
    }

    public void setPanelDeColores(PanelDeColores panelDeColores) {
        this.panelDeColores = panelDeColores;
    }

    public void setSeleccionarButtonState(boolean selected) {
        btnSeleccionar.setSelected(selected);
        if (!selected && panelDeColores != null && panelDeColores.getPanelDeDibujo() != null) {
            panelDeColores.getPanelDeDibujo().deseleccionarArea();
            panelDeColores.getPanelDeDibujo().repaint();
        } else if (selected && panelDeColores != null && panelDeColores.getPanelDeDibujo() != null) {
            panelDeColores.getPanelDeDibujo().deseleccionarArea();
            panelDeColores.getPanelDeDibujo().repaint();
        }

        if (selected) {
            deseleccionarOtrosBotonesHerramienta(btnSeleccionar);
        }
    }

    private void deseleccionarOtrosBotonesHerramienta(JToggleButton botonExcluido) {
        for (Component boton : this.getComponents()) {
            if (boton instanceof JToggleButton && boton != botonExcluido) {
                ((JToggleButton) boton).setSelected(false);
            }
        }
    }
}