package paint202510;

// Importar la nueva clase
import figuras.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template.
 */

/**
 *
 * @author Gustavo j. Bonifacio
 */
public class Ventana extends JFrame {

    /**
     * El panel de dibujo principal donde se dibujan las figuras.
     */
    private PanelDeDibujo lienzo;
    /**
     * La barra de herramientas que contiene botones de formas y acciones.
     */
    private BarraDeHerramientas barraDeHerramientas;
    /**
     * El panel para seleccionar colores y opciones de relleno.
     */
    private PanelDeColores panelDeColores;
    /**
     * Panel con scroll para la barra de herramientas, permitiendo desplazamiento vertical si es necesario.
     */
    private JScrollPane scrollPaneBarra;

    // Variable para almacenar temporalmente la figura copiada (usando FiguraData y Transferible)
    // Aunque no se usa directamente aquí, TransferibleFigura encapsula FiguraData para el portapapeles.
    // private FiguraData figuraCopiadaData = null; // Esta variable local no es necesaria para la implementación actual de copiar/pegar con el portapapeles.
    
    // Nuevo: Variable para almacenar temporalmente la imagen copiada
    private BufferedImage imagenCopiada = null;

    /**
     * Constructor de la ventana principal de la aplicación.
     * Inicializa y organiza los componentes principales: barra de herramientas, panel de colores y panel de dibujo.
     * Configura los listeners de acciones para los botones de la barra de herramientas y los elementos del menú.
     */
    public Ventana() {
        initComponents(); // Inicializa componentes generados por el diseñador de formularios (si hay alguno).

        // Configurar el título de la ventana
        setTitle("Paint202510");

        // Crear instancias de los paneles principales en el orden correcto
        barraDeHerramientas = new BarraDeHerramientas();
        panelDeColores = new PanelDeColores();
        lienzo = new PanelDeDibujo(barraDeHerramientas, panelDeColores);

        // Ahora qué lienzo está creado, pasárselo a panelDeColores
        panelDeColores.setPanelDeDibujo(lienzo);

        // *** ESTABLECER LAS REFERENCIAS CRUZADAS (FRAGMENTO CLAVE) ***
        // Esto permite que PanelDeColores y BarraDeHerramientas se comuniquen
        panelDeColores.setBarraDeHerramientas(barraDeHerramientas);
        barraDeHerramientas.setPanelDeColores(panelDeColores);
        // ***********************************************************


        // Añadir listeners de acciones para los botones de acción en la barra de herramientas
        JButton btnGuardar = barraDeHerramientas.getBtnGuardar();
        btnGuardar.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic en el botón Guardar.
             * Abre un selector de archivos y guarda el contenido del lienzo como una imagen PNG.
             * @param e El ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Funcionalidad de guardar (ya presente y documentada)
                if (lienzo.getWidth() <= 0 || lienzo.getHeight() <= 0) {
                    JOptionPane.showMessageDialog(Ventana.this,
                            "El área de dibujo no tiene tamaño para guardar.",
                            "Error al Guardar",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BufferedImage imagen = new BufferedImage(lienzo.getWidth(), lienzo.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = imagen.createGraphics();
                lienzo.printAll(g2d);
                g2d.dispose();

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar imagen del lienzo");

                int userSelection = fileChooser.showSaveDialog(Ventana.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    String filePath = fileToSave.getAbsolutePath();
                    String lowerCaseFilePath = filePath.toLowerCase();

                    if (!lowerCaseFilePath.endsWith(".png") &&
                            !lowerCaseFilePath.endsWith(".jpg") &&
                            !lowerCaseFilePath.endsWith(".jpeg") &&
                            !lowerCaseFilePath.endsWith(".gif") &&
                            !lowerCaseFilePath.endsWith(".bmp")) {
                        fileToSave = new File(filePath + ".png");
                    }

                    try {
                        String fileExtension = "png";
                        int dotIndex = fileToSave.getName().lastIndexOf('.');
                        if (dotIndex > 0 && dotIndex < fileToSave.getName().length() - 1) {
                            fileExtension = fileToSave.getName().substring(dotIndex + 1).toLowerCase();
                        }

                        boolean success = ImageIO.write(imagen, fileExtension, fileToSave);

                        if (success) {
                            System.out.println("Imagen guardada correctamente en: " + fileToSave.getAbsolutePath());
                            JOptionPane.showMessageDialog(Ventana.this,
                                    "Imagen guardada correctamente como ." + fileExtension.toUpperCase(),
                                    "Guardado Exitoso",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Ventana.this,
                                    "Formato de archivo no soportado para guardar: ." + fileExtension + "\nIntente .png o .jpg",
                                    "Error de Formato al Guardar",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Ventana.this,
                                "Error al guardar la imagen:\n" + ex.getMessage(),
                                "Error de E/S al Guardar",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Ventana.this,
                                "Ocurrió un error inesperado:\n" + ex.getMessage(),
                                "Error General al Guardar",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("Guardado cancelado por el usuario.");
                }
            }
        });

        // Añadir ActionListener para el botón Deshacer
        JButton btnDeshacer = barraDeHerramientas.getBtnDeshacer();
        btnDeshacer.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic en el botón Deshacer.
             * Llama al método undo() del panel de dibujo para deshacer la última acción.
             * @param e El ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                lienzo.undo(); // Llamar al método undo en PanelDeDibujo
            }
        });

        // Añadir ActionListener para el botón Rehacer
        JButton btnRehacer = barraDeHerramientas.getBtnRehacer();
        btnRehacer.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic en el botón Rehacer.
             * Llama al método redo() del panel de dibujo para rehacer la última acción deshecha.
             * @param e El ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                lienzo.redo(); // Llamar al método redo en PanelDeDibujo
            }
        });

        // Añadir ActionListener para el botón Limpiar
        JButton btnLimpiar = barraDeHerramientas.getBtnLimpiar();
        btnLimpiar.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic en el botón Limpiar.
             * Llama al método clearCanvas() del panel de dibujo para borrar todo el contenido.
             * @param e El ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                lienzo.clearCanvas(); // Llamar al método clearCanvas en PanelDeDibujo
            }
        });


        // --- IMPLEMENTACIÓN DE COPIAR Y PEGAR ---

jMenuItem2.addActionListener(new ActionListener() { // Copiar
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Intentando copiar imagen...");
                BufferedImage selectedImage = lienzo.getImagenSeleccionada(); // Obtener la imagen seleccionada

                if (selectedImage != null) {
                    System.out.println("Imagen seleccionada (dimensiones): " + selectedImage.getWidth() + "x" + selectedImage.getHeight());
                    // Almacenar en la variable de la Ventana (opcional, para depuración o si se quiere mantener una copia local)
                    imagenCopiada = selectedImage;

                    // Crear un objeto Transferible con la imagen
                    Transferable transferable = new TransferibleImagen(selectedImage);

                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                    try {
                        clipboard.setContents(transferable, null);
                        System.out.println("Imagen copiada al portapapeles exitosamente.");
                        JOptionPane.showMessageDialog(Ventana.this, "Imagen copiada al portapapeles.", "Copiar", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalStateException ex) {
                        System.err.println("Error: El portapapeles no está disponible. " + ex.getMessage());
                        JOptionPane.showMessageDialog(Ventana.this, "Error: El portapapeles no está disponible.", "Error al Copiar", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        System.err.println("Error inesperado al copiar la imagen: " + ex.getMessage());
                        JOptionPane.showMessageDialog(Ventana.this, "Error inesperado al copiar la imagen.", "Error al Copiar", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Ninguna imagen seleccionada para copiar.");
                    JOptionPane.showMessageDialog(Ventana.this, "Ninguna imagen seleccionada para copiar. Primero use la herramienta 'Seleccionar Figura'.", "Copiar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        jMenuItem3.addActionListener(new ActionListener() { // Pegar
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Intentando pegar imagen...");
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable content = clipboard.getContents(null);

                if (content != null) {
                    System.out.println("Contenido encontrado en el portapapeles.");
                    try {
                        if (content.isDataFlavorSupported(TransferibleImagen.BUFFERED_IMAGE_FLAVOR)) {
                            BufferedImage pastedImage = (BufferedImage) content.getTransferData(TransferibleImagen.BUFFERED_IMAGE_FLAVOR);
                            System.out.println("BufferedImage recuperada del portapapeles.");

                            // Obtener la posición actual del ratón para pegar la imagen allí
                            Point mouseLoc = lienzo.getMousePosition(); // Obtener posición del ratón en el lienzo
                            int pasteX = (mouseLoc != null) ? mouseLoc.x : (lienzo.getWidth() / 2 - pastedImage.getWidth() / 2);
                            int pasteY = (mouseLoc != null) ? mouseLoc.y : (lienzo.getHeight() / 2 - pastedImage.getHeight() / 2);

                            // Ajustar la posición para que el centro de la imagen pegada esté en el ratón, o la esquina superior izquierda si lo prefieres
                            // pasteX = (mouseLoc != null) ? mouseLoc.x - pastedImage.getWidth() / 2 : pasteX;
                            // pasteY = (mouseLoc != null) ? mouseLoc.y - pastedImage.getHeight() / 2 : pasteY;


                            lienzo.pegarImagen(pastedImage, pasteX, pasteY); // Nuevo método en PanelDeDibujo
                            JOptionPane.showMessageDialog(Ventana.this, "Imagen pegada en el lienzo.", "Pegar", JOptionPane.INFORMATION_MESSAGE);

                        } else if (content.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                            // Soporte para imágenes estándar del sistema si no es nuestro DataFlavor personalizado
                            java.awt.Image img = (java.awt.Image) content.getTransferData(DataFlavor.imageFlavor);
                            BufferedImage pastedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g2d = pastedImage.createGraphics();
                            g2d.drawImage(img, 0, 0, null);
                            g2d.dispose();

                            Point mouseLoc = lienzo.getMousePosition();
                            int pasteX = (mouseLoc != null) ? mouseLoc.x : (lienzo.getWidth() / 2 - pastedImage.getWidth() / 2);
                            int pasteY = (mouseLoc != null) ? mouseLoc.y : (lienzo.getHeight() / 2 - pastedImage.getHeight() / 2);

                            lienzo.pegarImagen(pastedImage, pasteX, pasteY);
                            JOptionPane.showMessageDialog(Ventana.this, "Imagen pegada en el lienzo (desde portapapeles estándar).", "Pegar", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            System.out.println("El portapapeles no contiene datos de imagen válidos.");
                            JOptionPane.showMessageDialog(Ventana.this, "El portapapeles no contiene datos de imagen válidos.", "Pegar", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (UnsupportedFlavorException ex) {
                        System.err.println("Error al pegar: DataFlavor no soportado inesperadamente. " + ex.getMessage());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Ventana.this, "Error al pegar: Formato no soportado.", "Error al Pegar", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        System.err.println("Error de E/S al obtener datos del portapapeles: " + ex.getMessage());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Ventana.this, "Error de E/S al pegar la imagen.", "Error al Pegar", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        System.err.println("Error inesperado al pegar la imagen: " + ex.getMessage());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Ventana.this, "Ocurrió un error inesperado al pegar la imagen.", "Error General al Pegar", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("El portapapeles está vacío.");
                    JOptionPane.showMessageDialog(Ventana.this, "El portapapeles está vacío.", "Pegar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // --- FIN IMPLEMENTACIÓN DE COPIAR Y PEGAR ---



        // --- INICIO Bloque de código añadido/reinsertado para el layout ---
        scrollPaneBarra = new JScrollPane(barraDeHerramientas);
        scrollPaneBarra.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneBarra.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneBarra.setPreferredSize(new Dimension(80, (int) barraDeHerramientas.getPreferredSize().getHeight()));

        // Establecer el layout del content pane a BorderLayout antes de añadir los componentes
        getContentPane().setLayout(new BorderLayout()); // Añadir esta línea

        getContentPane().add(panelDeColores, BorderLayout.NORTH);
        getContentPane().add(scrollPaneBarra, BorderLayout.EAST);
        getContentPane().add(lienzo, BorderLayout.CENTER);

        // Configurar el tamaño y la ubicación de la ventana
        setSize(800, 600);
        setLocationRelativeTo(null);
        // --- FIN Bloque de código añadido/reinsertado ---

    }

    // --- MÉTODOS AUXILIARES PARA PEGAR ---

    /**
     * Crea una nueva instancia de Figura a partir de los datos contenidos en un objeto FiguraData.
     * Este método necesita lógica para instanciar la clase de figura correcta basada en figuraData.getTipoFigura().
     * DEBES IMPLEMENTAR LA CREACIÓN DE CADA TIPO DE FIGURA AQUÍ.
     * @param figuraData Los datos de la figura.
     * @return Una nueva instancia de Figura con los datos aplicados, o null si el tipo de figura es desconocido.
     */
    private Figura crearFiguraDesdeData(FiguraData figuraData) {
        if (figuraData == null || figuraData.getTipoFigura() == null) {
            return null;
        }

        Figura nuevaFigura = null;
        Point pInicial = figuraData.getPuntoInicial();
        Point pFinal = figuraData.getPuntoFinal();
        Point centro = figuraData.getCentro(); // Usado por algunas figuras (Circulo, Poligonos, Estrella, Rombo)
        java.util.ArrayList<Point> puntosTrazo = figuraData.getPuntosTrazo(); // Usado por DibujoLibre/Lapiz

        // --- IMPLEMENTACIÓN NECESARIA: Crear la figura correcta según el tipo ---
        // Debes añadir un 'case' para cada tipo de figura que soportes
        // Asegúrate de usar los puntos (pInicial, pFinal, centro, puntosTrazo) que correspondan
        // a cómo cada constructor y método 'actualizar' de la figura funciona.
        switch (figuraData.getTipoFigura()) {
            case "Linea":
                if (pInicial != null && pFinal != null) {
                    // Asumiendo que Linea puede ser construida con dos puntos o un punto y luego actualizar
                    nuevaFigura = new Linea(pInicial);
                    nuevaFigura.actualizar(pFinal); // Establecer el punto final
                }
                break;
            case "Rectangulo":
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Rectangulo(pInicial);
                    nuevaFigura.actualizar(pFinal); // Rectangulo usa actualizar para definir el segundo punto y tamaño
                }
                break;
            case "Ovalo":
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Ovalo(pInicial);
                    nuevaFigura.actualizar(pFinal); // Ovalo usa actualizar para definir el segundo punto y tamaño
                }
                break;
            case "Círculo":
                // Tu Circulo usa puntoInicial como centro y puntoFinal para el radio.
                // FiguraData tiene 'centro' y 'puntoFinal'. Si guardaste el centro en 'centro' de FiguraData
                // y un punto en la circunferencia en 'puntoFinal' de FiguraData, usa eso.
                if (centro != null && pFinal != null) { // Usando centro (de FiguraData) y puntoFinal (de FiguraData)
                    nuevaFigura = new Circulo(centro);
                    nuevaFigura.actualizar(pFinal); // El puntoFinal aquí actúa como referencia para el radio
                } else if (pInicial != null && pFinal != null) { // Alternativa si solo guardaste puntoInicial (como centro) y puntoFinal (para radio) en FiguraData
                    nuevaFigura = new Circulo(pInicial);
                    nuevaFigura.actualizar(pFinal);
                }
                break;
            case "Cuadrado":
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Cuadrado(pInicial);
                    nuevaFigura.actualizar(pFinal); // Cuadrado usa actualizar para definir el segundo punto
                }
                break;
            case "Triangulo": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Triangulo(pInicial);
                    nuevaFigura.actualizar(pFinal); // Triangulo usa actualizar para definir el segundo punto
                }
                break;
            case "Pentagono": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                // Tu Pentagono usa 'centro' como centro geométrico y 'puntoActual' (que corresponde a puntoFinal en FiguraData)
                // como referencia para tamaño/orientación.
                if (centro != null && pFinal != null) {
                    nuevaFigura = new Pentagono(centro);
                    nuevaFigura.actualizar(pFinal); // El puntoFinal aquí actúa como referencia para tamaño/orientación
                }
                break;
            case "Rombo": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                // Tu Rombo usa 'centro' y 'puntoActual' (que corresponde a puntoFinal en FiguraData).
                if (centro != null && pFinal != null) {
                    nuevaFigura = new Rombo(centro);
                    nuevaFigura.actualizar(pFinal); // El puntoFinal aquí actúa como referencia para tamaño/forma
                }
                break;
            case "Heptagono": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                // Tu Heptagono usa 'centroGeometrico' y 'verticeReferencia' (que corresponde a puntoFinal en FiguraData).
                if (centro != null && pFinal != null) {
                    nuevaFigura = new Heptagono(centro); // Asumiendo que Heptagono tiene un constructor con Point para el centro
                    nuevaFigura.actualizar(pFinal); // El puntoFinal aquí actúa como referencia
                }
                break;
            case "Octagono": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                // Tu Octagono usa 'centroGeometrico' y 'verticeReferencia' (que corresponde a puntoFinal en FiguraData).
                if (centro != null && pFinal != null) {
                    nuevaFigura = new Octagono(centro); // Asumiendo que Octagono tiene un constructor con Point para el centro
                    nuevaFigura.actualizar(pFinal); // El puntoFinal aquí actúa como referencia
                }
                break;
            case "Estrella": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                // Tu Estrella usa 'centroGeometrico' y 'verticeReferencia' (que corresponde a puntoFinal en FiguraData).
                if (centro != null && pFinal != null) {
                    nuevaFigura = new Estrella(centro); // Asumiendo que Estrella tiene un constructor con Point para el centro
                    nuevaFigura.actualizar(pFinal); // El puntoFinal aquí actúa como referencia
                }
                break;
            case "Flecha": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Flecha(pInicial);
                    nuevaFigura.actualizar(pFinal); // Flecha usa actualizar para definir el punto final
                }
                break;
            case "Corazón": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Corazon(pInicial);
                    nuevaFigura.actualizar(pFinal); // Corazon usa actualizar para definir el segundo punto
                }
                break;
            case "Trapecio": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Trapecio(pInicial);
                    nuevaFigura.actualizar(pFinal); // Trapecio usa actualizar para definir el segundo punto
                }
                break;
            case "Semicirculo": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                if (pInicial != null && pFinal != null) {
                    nuevaFigura = new Semicirculo(pInicial);
                    nuevaFigura.actualizar(pFinal); // Semicirculo usa actualizar para definir el segundo punto
                }
                break;
            case "Ring": // Asegúrate de que el nombre coincida con el guardado en getFiguraData()
                // Tu Ring usa 'puntoInicial' como centro y 'puntoFinal' como punto en el radio.
                // FiguraData tiene 'centro' y 'puntoFinal'. Si guardaste el centro en 'puntoInicial' de FiguraData,
                // o en 'centro' de FiguraData, úsalo. Asumiremos que el centro se guarda en 'puntoInicial' o 'centro'.
                Point ringCenter = (pInicial != null) ? pInicial : centro;
                if (ringCenter != null && pFinal != null) {
                    nuevaFigura = new Ring(ringCenter); // Asumiendo constructor con punto central
                    nuevaFigura.actualizar(pFinal); // El puntoFinal aquí actúa como referencia para el radio exterior
                }
                break;
            case "DibujoLibre": // Asegúrate de que el nombre coincida exactamente con lo guardado en FiguraData
            //case "Lapiz": // Asumiendo que Lapiz y DibujoLibre son similares o uno es el otro
                if (puntosTrazo != null && !puntosTrazo.isEmpty()) {
                    // Para DibujoLibre/Lapiz, necesitas un constructor que acepte la lista de puntos
                    // O añadir los puntos uno por uno después de crear la instancia.
                    // Si DibujoLibre tiene un constructor(Point), puedes crearlo y luego añadir los puntos.
                    // Si Lapiz tiene un constructor(Color, int), necesitas adaptar.
                    // Asumiremos que DibujoLibre puede ser recreado a partir de su lista de puntos.
                    // Asumiendo que el constructor de DibujoLibre puede tomar el primer punto:
                    DibujoLibre dl = new DibujoLibre(puntosTrazo.get(0)); // Crear con el primer punto
                    for (int i = 1; i < puntosTrazo.size(); i++) {
                        dl.actualizar(puntosTrazo.get(i)); // Añadir el resto de puntos
                    }
                    // Asegúrate de guardar el color de primer plano original en FiguraData para DibujoLibre/Lapiz
                    // y establecerlo aquí si usan el campo heredado:
                    // dl.setColorDePrimerPlano(figuraData.getColorDePrimerPlano());
                    // Si Lapiz tiene su propio campo 'color', asegúrate de guardarlo en FiguraData y establecerlo en Lapiz.
                    nuevaFigura = dl;
                }
                break;
            // Añadir 'case' para otras figuras aquí (Pentagono, Rombo, etc.)


            default:
                System.err.println("Tipo de figura desconocido o no implementado para pegar: " + figuraData.getTipoFigura());
                break;
        }
        // --- FIN IMPLEMENTACIÓN NECESARIA ---

        // Si se creó la figura y NO es Borrador (que no se copia/pega normalmente)
        // establecer sus propiedades comunes (colores, relleno).
        // Algunas figuras como DibujoLibre/Lapiz podrían manejar sus colores de manera diferente.
        if (nuevaFigura != null && !(nuevaFigura instanceof Borrador)) {
            // Solo establecer colorDePrimerPlano si no es DibujoLibre/Lapiz
            // si estas figuras manejan su color de forma independiente del campo heredado.
            // Si DibujoLibre/Lapiz usan colorDePrimerPlano heredado, esta línea está bien.
            nuevaFigura.setColorDePrimerPlano(figuraData.getColorDePrimerPlano()); // Usar el getter público


            // Solo establecer colorDeRelleno y estado de relleno si la figura soporta relleno
            // (la mayoría de las figuras cerradas, pero no Línea, Borrador, DibujoLibre/Lapiz).
            // Puedes añadir un método 'soportaRelleno()' a Figura si la lógica es compleja,
            // o simplemente verificar si la figura no es de un tipo que no soporta relleno.
            if (!(nuevaFigura instanceof Linea) && !(nuevaFigura instanceof DibujoLibre)) {
                nuevaFigura.setColorDeRelleno(figuraData.getColorDeRelleno()); // Podría ser null
                nuevaFigura.setRelleno(figuraData.isEstaRelleno());
            }

            // Si decides copiar el tamaño del borrador, necesitarías un setter en Borrador y manejarlo aquí
            // if (nuevaFigura instanceof Borrador) { ((Borrador) nuevaFigura).setTamano(figuraData.getTamanoBorrador()); }
            nuevaFigura.setGrosor(figuraData.getGrosor()); // Establecer el grosor
        }
        return nuevaFigura;
    }

    /**
     * Ajusta la posición de una figura desplazando sus puntos por un offset dado.
     * Llama al método translate de la figura, asumiendo que cada figura
     * sobrescribe 'translate' para mover todos sus puntos relevantes.
     * @param figura La figura a ajustar.
     * @param offset El desplazamiento a aplicar.
     */
    private void ajustarPosicionFigura(Figura figura, Point offset) {
        if (figura == null || offset == null) {
            return;
        }

        // Llama al método translate implementado en la clase Figura
        // o sobrescrito en las subclases de figura.
        figura.translate(offset);

        // NOTA IMPORTANTE: Asegúrate de que el método 'translate(Point offset)'
        // en CADA UNA DE TUS CLASES DE FIGURA (Rectangulo, Circulo, etc., incluyendo DibujoLibre)
        // esté implementado correctamente para mover TODOS los puntos que definen la posición de esa figura.
        // La implementación por defecto en Figura.java solo mueve puntoInicial y puntoFinal.
        // Si tu figura usa un campo 'centro' o una lista de 'puntos', DEBES sobrescribir 'translate'
        // en esa clase para mover esos puntos también.

        // Los métodos helper getPuntoInicialDeFigura y getPuntoFinalDeFigura que usaban reflexión
        // han sido eliminados y reemplazados por los getters públicos en la clase Figura.
    }

    // Los métodos getPuntoInicialDeFigura y getPuntoFinalDeFigura privados han sido eliminados.


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new JMenuBar();
        jMenu3 = new JMenu();
        jMenu4 = new JMenu();
        jMenuBar3 = new JMenuBar();
        jMenu5 = new JMenu();
        jMenu6 = new JMenu();
        jMenuBar4 = new JMenuBar();
        jMenu7 = new JMenu();
        jMenu8 = new JMenu();
        jMenuBar5 = new JMenuBar();
        jMenu9 = new JMenu();
        jMenu10 = new JMenu();
        jMenuBar6 = new JMenuBar();
        jMenu11 = new JMenu();
        jMenu12 = new JMenu();
        jButton1 = new JButton();
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenuItem1 = new JMenuItem();
        jMenuItem2 = new JMenuItem();
        jMenuItem3 = new JMenuItem();
        jMenu2 = new JMenu();

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("File");
        jMenuBar3.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar3.add(jMenu6);

        jMenu7.setText("File");
        jMenuBar4.add(jMenu7);

        jMenu8.setText("Edit");
        jMenuBar4.add(jMenu8);

        jMenu9.setText("File");
        jMenuBar5.add(jMenu9);

        jMenu10.setText("Edit");
        jMenuBar5.add(jMenu10);

        jMenu11.setText("File");
        jMenuBar6.add(jMenu11);

        jMenu12.setText("Edit");
        jMenuBar6.add(jMenu12);

        jButton1.setText("jButton1");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("Archivo");

        jMenuItem1.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setIcon(new ImageIcon(getClass().getResource("/iconos/salida.png"))); // NOI18N
        jMenuItem1.setText("Salir");
        jMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setIcon(new ImageIcon(getClass().getResource("/iconos/copiar.png"))); // NOI18N
        jMenuItem2.setText("Copiar");
        jMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setIcon(new ImageIcon(getClass().getResource("/iconos/pegar.png"))); // NOI18N
        jMenuItem3.setText("Pegar");
        jMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 277, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Comando para copiar - IMPLEMENTADO ARRIBA
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Comando para pegar - IMPLEMENTADO ARRIBA
    }//GEN-LAST:event_jMenuItem3ActionPerformed


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {


        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JMenu jMenu1;
    private JMenu jMenu10;
    private JMenu jMenu11;
    private JMenu jMenu12;
    private JMenu jMenu2;
    private JMenu jMenu3;
    private JMenu jMenu4;
    private JMenu jMenu5;
    private JMenu jMenu6;
    private JMenu jMenu7;
    private JMenu jMenu8;
    private JMenu jMenu9;
    private JMenuBar jMenuBar1;
    private JMenuBar jMenuBar2;
    private JMenuBar jMenuBar3;
    private JMenuBar jMenuBar4;
    private JMenuBar jMenuBar5;
    private JMenuBar jMenuBar6;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem3;
    // End of variables declaration//GEN-END:variables
}