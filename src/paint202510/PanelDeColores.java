package paint202510;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Clase PanelDeColores - Proporciona controles para seleccionar colores y
 * opciones de relleno,
 * así como botones para mostrar información del proyecto y cargar una imagen.
 */
public class PanelDeColores extends JPanel {

    private final JButton botonColorBorde;
    private Color colorBordeActual = Color.BLACK;
    private JButton botonColorRelleno;
    private JButton botonInformacion;
    private Color colorRellenoActual = Color.WHITE;
    private final JCheckBox checkRellenar;
    protected JToggleButton botonCargar;
    private PanelDeDibujo panelDeDibujo = null;

    private BarraDeHerramientas barraDeHerramientas = null;

    private JSpinner grosorSpinner;
    private int grosorActual = 1;

    public PanelDeColores() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        botonColorBorde = new JButton("Color Borde");
        botonColorRelleno = new JButton("Color Relleno");
        botonInformacion = new JButton("Informacion");
        botonCargar = new JToggleButton("Cargar Imagen");

        checkRellenar = new JCheckBox("Rellenar");
        checkRellenar.setSelected(false);

        botonColorBorde.addActionListener((ActionEvent e) -> {
            Color nuevoColor = JColorChooser.showDialog(
                    PanelDeColores.this,
                    "Seleccionar Color del Borde",
                    colorBordeActual);

            if (nuevoColor != null) {
                colorBordeActual = nuevoColor;
                if (panelDeDibujo != null) {
                    panelDeDibujo.deseleccionarArea(); // Usar deseleccionarArea()
                }
                if (barraDeHerramientas != null) {
                    barraDeHerramientas.setSeleccionarButtonState(false);
                }
            }
        });

        botonColorRelleno.addActionListener((ActionEvent e) -> {
            Color nuevoColor = JColorChooser.showDialog(
                    PanelDeColores.this,
                    "Seleccionar Color de Relleno",
                    colorRellenoActual);

            if (nuevoColor != null) {
                colorRellenoActual = nuevoColor;
                if (panelDeDibujo != null) {
                    panelDeDibujo.deseleccionarArea();
                }
                if (barraDeHerramientas != null) {
                    barraDeHerramientas.setSeleccionarButtonState(false);
                }
            }
        });

        checkRellenar.addActionListener((ActionEvent e) -> {
            if (panelDeDibujo != null) {
                panelDeDibujo.deseleccionarArea();
            }
            if (barraDeHerramientas != null) {
                barraDeHerramientas.setSeleccionarButtonState(false);
            }
        });

        botonInformacion.addActionListener((ActionEvent e) -> {
            mostrarInformacionProyecto();
            if (panelDeDibujo != null) {
                panelDeDibujo.deseleccionarArea();
            }
            if (barraDeHerramientas != null) {
                barraDeHerramientas.setSeleccionarButtonState(false);
            }
        });

        botonCargar.addActionListener((ActionEvent e) -> {
            if (panelDeDibujo != null) {
                panelDeDibujo.deseleccionarArea();
            }
            if (barraDeHerramientas != null) {
                barraDeHerramientas.setSeleccionarButtonState(false);
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Cargar Imagen");
            int userSelection = fileChooser.showOpenDialog(PanelDeColores.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                try {
                    BufferedImage loadedImage = ImageIO.read(fileToLoad);
                    if (loadedImage != null) {
                        if (panelDeDibujo != null) {
                            panelDeDibujo.setImagenDeFondo(loadedImage);
                            JOptionPane.showMessageDialog(PanelDeColores.this,
                                    "Imagen cargada correctamente.",
                                    "Carga Exitosa",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(PanelDeColores.this,
                                    "Error interno: No se pudo obtener la referencia al Panel de Dibujo.",
                                    "Error de Carga",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(PanelDeColores.this,
                                "No se pudo leer la imagen. Asegúrate de que es un formato válido.",
                                "Error al Cargar",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PanelDeColores.this,
                            "Error al leer el archivo de imagen:\n" + ex.getMessage(),
                            "Error de E/S al Cargar",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PanelDeColores.this,
                            "Ocurrió un error inesperado al cargar la imagen:\n" + ex.getMessage(),
                            "Error General al Cargar",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        grosorSpinner = new JSpinner(new SpinnerNumberModel(grosorActual, 1, 20, 1));
        grosorSpinner.setPreferredSize(new Dimension(50, 30));

        grosorSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                grosorActual = (Integer) grosorSpinner.getValue();
                if (panelDeDibujo != null) {
                    panelDeDibujo.setGrosor(grosorActual);
                    panelDeDibujo.deseleccionarArea();
                }
            }
        });

        add(grosorSpinner);

        formatearYAgregar(botonColorBorde, "borde.png", "Color de borde", false);
        formatearYAgregar(botonColorRelleno, "relleno.png", "Color de relleno", false);
        add(checkRellenar);
        formatearYAgregar(botonInformacion, "informacion.png", "Infotmacion", false);
        formatearYAgregar(botonCargar, "subirImagen.png", "Cargar Imagen", false);
    }

    private void formatearYAgregar(AbstractButton boton, String nombreIcono, String tooltip, boolean esBotonFigura) {
        boton.setFocusable(false);
        boton.setToolTipText("Seleccione: " + tooltip);

        boton.putClientProperty("esBotonFigura", esBotonFigura);

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

    public void setPanelDeDibujo(PanelDeDibujo panelDeDibujo) {
        this.panelDeDibujo = panelDeDibujo;
    }

    public PanelDeDibujo getPanelDeDibujo() {
        return panelDeDibujo;
    }

    public void setBarraDeHerramientas(BarraDeHerramientas barraDeHerramientas) {
        this.barraDeHerramientas = barraDeHerramientas;
    }

    private void mostrarInformacionProyecto() {
        String mensaje = "Descripción del Proyecto:\n" +
                "Este proyecto es una aplicación de dibujo llamada \"Paint 2025-10\", que permite\n" +
                "a los usuarios crear y editar gráficos utilizando diversas herramientas.\n" +
                "Los usuarios pueden seleccionar diferentes formas, colores y opciones de relleno para\n" +
                "personalizar sus creaciones. La interfaz es intuitiva, con una barra de herramientas que facilita\n" +
                "el acceso a las funciones de dibujo, y un panel de colores para elegir los tonos deseados.\n" +
                "La aplicación también permite guardar las imágenes creadas en formato PNG.\n\n" +
                "Integrantes del Proyecto:\n" +
                "- José Ariel Pereyra Francisco (Profesor)\n" +
                "- Gustavo Junior Bonifacio Peña (Gerente del proyecto)\n" +
                "- Carolina De Jesús Reinoso\n" +
                "- Robinzon Michel Gabino Fernández\n" +
                "- Marcos Miguel Gómez Camilo\n" +
                "- Jon Luis Jones Esteban\n" +
                "- Frailyn José Martinez Santos\n" +
                "- Ebenezer Peña Hernandez\n" +
                "- Bryan José Ureña Castillo";

        JOptionPane.showMessageDialog(this, mensaje, "Acerca del Proyecto", JOptionPane.INFORMATION_MESSAGE);
    }

    public Color getColorBordeActual() {
        return colorBordeActual;
    }

    public Color getColorRellenoActual() {
        return colorRellenoActual;
    }

    public boolean isRellenar() {
        return checkRellenar.isSelected();
    }

    public int getGrosorActual() {
        return grosorActual;
    }
}