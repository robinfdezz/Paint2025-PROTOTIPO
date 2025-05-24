package paint202510;

import figuras.*;

import java.awt.*;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;
import javax.imageio.ImageIO;

public class PanelDeDibujo extends JPanel implements HerramientaSeleccionadaListener {
    private Figura figuraActual;
    private Stack<BufferedImage> undoStack = new Stack<>();
    private Stack<BufferedImage> redoStack = new Stack<>();

    PanelDeColores panelDeColores;
    BarraDeHerramientas barraDeHerramientas;

    private BufferedImage lienzoImagen;
    private Graphics2D lienzoGraphics;

    private BufferedImage imagenDeFondo;

    private Cursor cursorDibujoGeneral;
    private Cursor cursorBorrador;
    private Cursor cursorLataPintura;
    private Cursor cursorSeleccionar;
    private Point puntoInicialArrastre = null;
    private int grosor = 1;

    private Rectangle areaSeleccionada = null;
    private Point inicioArrastreSeleccion = null;
    private Point finArrastreSeleccion = null;
    private BufferedImage imagenSeleccionada = null;
    private Point offsetMovimiento = null;
    private boolean isDraggingSelectedArea = false;
    private BufferedImage imagenFondoRecorte = null;

    /**
     * Constructor del panel de dibujo.
     *
     * @param barraDeHerramientas La instancia de BarraDeHerramientas.
     * @param panelDeColores      La instancia de PanelDeColores.
     */
    public PanelDeDibujo(BarraDeHerramientas barraDeHerramientas, PanelDeColores panelDeColores) {
        this.barraDeHerramientas = barraDeHerramientas;
        this.panelDeColores = panelDeColores;
        setBackground(Color.WHITE);
        setDoubleBuffered(false);

        setPreferredSize(new Dimension(800, 600));

        try {
            BufferedImage dibujoGeneralImage = ImageIO.read(getClass().getResource("/iconos/cruz.png"));
            BufferedImage borradorImage = ImageIO.read(getClass().getResource("/iconos/borrador2.png"));
            BufferedImage lataPinturaImage = ImageIO.read(getClass().getResource("/iconos/seleccion4.png"));
            BufferedImage seleccionarImage = ImageIO.read(getClass().getResource("/iconos/seleccion4.png"));

            cursorDibujoGeneral = Toolkit.getDefaultToolkit().createCustomCursor(dibujoGeneralImage, new Point(16, 16),
                    "cursorDibujoGeneral");
            cursorBorrador = Toolkit.getDefaultToolkit().createCustomCursor(borradorImage, new Point(8, 18),
                    "cursorBorrador");
            cursorLataPintura = Toolkit.getDefaultToolkit().createCustomCursor(lataPinturaImage, new Point(16, 16),
                    "cursorLataPintura");
            cursorSeleccionar = Toolkit.getDefaultToolkit().createCustomCursor(seleccionarImage, new Point(16, 16),
                    "cursorSeleccionar");

        } catch (IOException e) {
            System.err.println("Error al cargar las imágenes de los cursores: " + e.getMessage());
            cursorDibujoGeneral = Cursor.getDefaultCursor();
            cursorBorrador = Cursor.getDefaultCursor();
            cursorLataPintura = Cursor.getDefaultCursor();
            cursorSeleccionar = Cursor.getDefaultCursor();
        }
        barraDeHerramientas.setHerramientaSeleccionadaListener(this);
        configurarEventosRaton();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (lienzoImagen == null || lienzoImagen.getWidth() != getWidth() || lienzoImagen.getHeight() != getHeight()) {
            crearLienzoImagen();
        }
    }

    private void crearLienzoImagen() {
        int width = getWidth() > 0 ? getWidth() : getPreferredSize().width;
        int height = getHeight() > 0 ? getHeight() : getPreferredSize().height;

        if (width <= 0 || height <= 0) {
            width = 800;
            height = 600;
        }

        lienzoImagen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        lienzoGraphics = lienzoImagen.createGraphics();

        lienzoGraphics.setComposite(AlphaComposite.Clear);
        lienzoGraphics.fillRect(0, 0, width, height);
        lienzoGraphics.setComposite(AlphaComposite.SrcOver);

        lienzoGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        lienzoGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (imagenDeFondo != null) {
            lienzoGraphics.drawImage(imagenDeFondo, 0, 0, this);
        }

        if (undoStack.isEmpty()) {
            undoStack.push(copyImage(lienzoImagen));
        } else {
            undoStack.push(copyImage(lienzoImagen));
        }
        redoStack.clear();
    }

    @Override
    public void herramientaSeleccionadaCambio(String nuevaHerramienta) {
        if (!"Seleccionar Figura".equals(nuevaHerramienta)) {
            deseleccionarArea();
        }
        actualizarCursor(nuevaHerramienta);
    }

    private void actualizarCursor(String herramienta) {
        switch (herramienta) {
            case "Color de relleno":
                setCursor(cursorLataPintura);
                break;
            case "Seleccionar Figura":
                setCursor(cursorSeleccionar);
                break;
            case "Borrador":
                setCursor(cursorBorrador);
                break;
            case "Línea":
            case "Rectángulo":
            case "Óvalo":
            case "Círculo":
            case "Cuadrado":
            case "Triángulo":
            case "Pentágono":
            case "Rombo":
            case "Heptagono":
            case "Octagono":
            case "Estrella":
            case "Flecha":
            case "Corazón":
            case "Trapecio":
            case "Semicirculo":
            case "Ring":
            case "Lapiz":
            case "Dibujo Libre":
                setCursor(cursorDibujoGeneral);
                break;
            default:
                setCursor(Cursor.getDefaultCursor());
                break;
        }
    }

    private void configurarEventosRaton() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String herramienta = barraDeHerramientas.getHerramientaSeleccionada();

                if ("Seleccionar Figura".equals(herramienta)) {
                    undoStack.push(copyImage(lienzoImagen));
                    redoStack.clear();

                    if (areaSeleccionada != null && areaSeleccionada.contains(e.getPoint())) {
                        isDraggingSelectedArea = true;
                        offsetMovimiento = new Point(e.getX() - areaSeleccionada.x, e.getY() - areaSeleccionada.y);
                    } else {
                        deseleccionarArea();
                        inicioArrastreSeleccion = e.getPoint();
                        finArrastreSeleccion = e.getPoint();
                        areaSeleccionada = null;
                        imagenSeleccionada = null;
                        imagenFondoRecorte = null;
                    }
                    repaint();
                    return;
                }

                if ("Color de relleno".equals(herramienta)) {
                    if (lienzoImagen != null && lienzoGraphics != null) {
                        int clickedX = e.getX();
                        int clickedY = e.getY();
                        if (clickedX >= 0 && clickedX < lienzoImagen.getWidth() &&
                                clickedY >= 0 && clickedY < lienzoImagen.getHeight()) {

                            Color targetColor = new Color(lienzoImagen.getRGB(clickedX, clickedY), true);
                            Color replacementColor = panelDeColores.getColorRellenoActual();

                            if (!targetColor.equals(replacementColor)) {
                                undoStack.push(copyImage(lienzoImagen));
                                redoStack.clear();
                                floodFill(lienzoImagen, clickedX, clickedY, targetColor, replacementColor);
                                repaint();
                            }
                        }
                    }
                    figuraActual = null;
                    return;
                }
                undoStack.push(copyImage(lienzoImagen));
                redoStack.clear();

                if ("Borrador".equals(herramienta)) {
                    figuraActual = new Borrador(e.getPoint());
                    ((Borrador) figuraActual).setTamano(panelDeColores.getGrosorActual());
                    return;
                }

                figuraActual = obtenerFiguraADibujar(e.getPoint());

                if (figuraActual != null) {
                    figuraActual.setColorDePrimerPlano(panelDeColores.getColorBordeActual());
                    figuraActual.setColorDeRelleno(panelDeColores.getColorRellenoActual());
                    figuraActual.setRelleno(panelDeColores.isRellenar());
                    figuraActual.setGrosor(grosor);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                String herramienta = barraDeHerramientas.getHerramientaSeleccionada();

                if ("Seleccionar Figura".equals(herramienta)) {
                    if (isDraggingSelectedArea) {
                        isDraggingSelectedArea = false;
                        if (imagenSeleccionada != null && areaSeleccionada != null) {
                            Graphics2D g2dClear = lienzoImagen.createGraphics();
                            g2dClear.setComposite(AlphaComposite.Clear);
                            g2dClear.fillRect(areaSeleccionada.x, areaSeleccionada.y, areaSeleccionada.width,
                                    areaSeleccionada.height);
                            g2dClear.dispose();

                            int newX = e.getX() - offsetMovimiento.x;
                            int newY = e.getY() - offsetMovimiento.y;

                            lienzoGraphics.drawImage(imagenSeleccionada, newX, newY, null);

                            areaSeleccionada.setLocation(newX, newY);
                        }
                    } else {
                        if (inicioArrastreSeleccion != null && finArrastreSeleccion != null) {
                            int x = Math.min(inicioArrastreSeleccion.x, finArrastreSeleccion.x);
                            int y = Math.min(inicioArrastreSeleccion.y, finArrastreSeleccion.y);
                            int width = Math.abs(finArrastreSeleccion.x - inicioArrastreSeleccion.x);
                            int height = Math.abs(finArrastreSeleccion.y - inicioArrastreSeleccion.y);

                            if (width > 0 && height > 0) {
                                areaSeleccionada = new Rectangle(x, y, width, height);
                                try {
                                    imagenSeleccionada = lienzoImagen.getSubimage(x, y, width, height);
                                } catch (java.awt.image.RasterFormatException ex) {
                                    System.err.println(
                                            "Error al recortar imagen (área fuera de límites?): " + ex.getMessage());
                                    areaSeleccionada = null;
                                    imagenSeleccionada = null;
                                    imagenFondoRecorte = null;
                                    if (undoStack.size() > 1) {
                                        undoStack.pop();
                                    }
                                }
                            } else {
                                deseleccionarArea();
                            }
                        }
                    }
                    inicioArrastreSeleccion = null;
                    finArrastreSeleccion = null;
                    repaint();
                    return;
                }

                if (figuraActual != null && !(figuraActual instanceof Borrador)
                        && !"Color de relleno".equals(herramienta)) {
                    if (lienzoGraphics != null) {
                        figuraActual.setColorDePrimerPlano(panelDeColores.getColorBordeActual());
                        figuraActual.setColorDeRelleno(panelDeColores.getColorRellenoActual());
                        figuraActual.setRelleno(panelDeColores.isRellenar());
                        figuraActual.setGrosor(grosor);
                        figuraActual.dibujar(lienzoGraphics);
                        repaint();
                    }
                    figuraActual = null;
                } else if (figuraActual instanceof Borrador) {
                    undoStack.push(copyImage(lienzoImagen));
                    redoStack.clear();
                    figuraActual = null;
                    repaint();
                }
                puntoInicialArrastre = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                String herramienta = barraDeHerramientas.getHerramientaSeleccionada();

                if ("Seleccionar Figura".equals(herramienta)) {
                    if (isDraggingSelectedArea) {
                        if (!undoStack.isEmpty()) {
                            lienzoImagen = copyImage(undoStack.peek());
                            lienzoGraphics = lienzoImagen.createGraphics();
                            lienzoGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        }
                        repaint();
                    } else {
                        finArrastreSeleccion = e.getPoint();
                        repaint();
                    }
                    return;
                }

                if ("Dibujo Libre".equals(herramienta) || "Lapiz".equals(herramienta)
                        || "Borrador".equals(herramienta)) {
                    if (figuraActual == null) {
                        figuraActual = obtenerFiguraADibujar(e.getPoint());
                        if (figuraActual != null) {
                            figuraActual.setColorDePrimerPlano(panelDeColores.getColorBordeActual());
                            figuraActual.setGrosor(grosor);
                            if (figuraActual instanceof Borrador) {
                                ((Borrador) figuraActual).setTamano(panelDeColores.getGrosorActual());
                            }
                        }
                    } else {
                        figuraActual.actualizar(e.getPoint());
                    }

                    if (lienzoGraphics != null && figuraActual instanceof Borrador) {
                        Borrador borrador = (Borrador) figuraActual;
                        lienzoGraphics.setComposite(AlphaComposite.Clear);
                        lienzoGraphics.fillOval(e.getX() - borrador.getTamano() / 2,
                                e.getY() - borrador.getTamano() / 2,
                                borrador.getTamano(), borrador.getTamano());
                        lienzoGraphics.setComposite(AlphaComposite.SrcOver);
                    }
                } else if (figuraActual != null) {
                    figuraActual.actualizar(e.getPoint());
                }
                repaint();
            }
        });
    }

    private void floodFill(BufferedImage image, int x, int y, Color targetColor, Color replacementColor) {
        if (targetColor.equals(replacementColor)) {
            return;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        LinkedList<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        if (x < 0 || x >= width || y < 0 || y >= height || image.getRGB(x, y) != targetColor.getRGB()) {
            return;
        }

        Color originalColor = lienzoGraphics.getColor();
        lienzoGraphics.setColor(replacementColor);

        while (!queue.isEmpty()) {
            Point current = queue.removeFirst();
            int currentX = current.x;
            int currentY = current.y;

            if (currentX < 0 || currentX >= width || currentY < 0 || currentY >= height
                    || image.getRGB(currentX, currentY) != targetColor.getRGB()) {
                continue;
            }

            lienzoGraphics.fillRect(currentX, currentY, 1, 1);

            queue.add(new Point(currentX + 1, currentY));
            queue.add(new Point(currentX - 1, currentY));
            queue.add(new Point(currentX, currentY + 1));
            queue.add(new Point(currentX, currentY - 1));
        }
        lienzoGraphics.setColor(originalColor);
    }

    private Figura obtenerFiguraADibujar(Point punto) {
        String herramienta = barraDeHerramientas.getHerramientaSeleccionada();

        switch (herramienta) {
            case "Línea":
                return new Linea(punto);
            case "Rectángulo":
                return new Rectangulo(punto);
            case "Borrador":
                return new Borrador(punto);
            case "Óvalo":
                return new Ovalo(punto);
            case "Círculo":
                return new Circulo(punto);
            case "Cuadrado":
                return new Cuadrado(punto);
            case "Triángulo":
                return new Triangulo(punto);
            case "Pentágono":
                return new Pentagono(punto);
            case "Rombo":
                return new Rombo(punto);
            case "Heptagono":
                return new Heptagono(punto);
            case "Octagono":
                return new Octagono(punto);
            case "Estrella":
                return new Estrella(punto);
            case "Flecha":
                return new Flecha(punto);
            case "Corazón":
                return new Corazon(punto);
            case "Trapecio":
                return new Trapecio(punto);
            case "Semicirculo":
                return new Semicirculo(punto);
            case "Ring":
                return new Ring(punto);
            case "Dibujo Libre":
            case "Lapiz":
            default:
                return new DibujoLibre(punto);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (lienzoImagen != null) {
            g.drawImage(lienzoImagen, 0, 0, this);
        }

        if (barraDeHerramientas.getHerramientaSeleccionada().equals("Seleccionar Figura")
                && inicioArrastreSeleccion != null && finArrastreSeleccion != null) {
            int x = Math.min(inicioArrastreSeleccion.x, finArrastreSeleccion.x);
            int y = Math.min(inicioArrastreSeleccion.y, finArrastreSeleccion.y);
            int width = Math.abs(finArrastreSeleccion.x - inicioArrastreSeleccion.x);
            int height = Math.abs(finArrastreSeleccion.y - inicioArrastreSeleccion.y);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 255, 128));
            g2d.fillRect(x, y, width, height);
            g2d.setColor(Color.BLUE);
            g2d.drawRect(x, y, width, height);
        }

        if (imagenSeleccionada != null && isDraggingSelectedArea) {
            Point currentMousePos = getMousePosition();
            if (currentMousePos != null && offsetMovimiento != null) {
                int newX = currentMousePos.x - offsetMovimiento.x;
                int newY = currentMousePos.y - offsetMovimiento.y;
                g.drawImage(imagenSeleccionada, newX, newY, null);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
                        new float[] { 5.0f }, 0.0f));
                g2d.drawRect(newX, newY, imagenSeleccionada.getWidth(), imagenSeleccionada.getHeight());
                g2d.setStroke(new BasicStroke(1));
            }
        } else if (imagenSeleccionada != null && areaSeleccionada != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 5.0f },
                    0.0f));
            g2d.drawRect(areaSeleccionada.x, areaSeleccionada.y, areaSeleccionada.width, areaSeleccionada.height);
            g2d.setStroke(new BasicStroke(1));
        }

        if (figuraActual != null && !(figuraActual instanceof Borrador)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(grosor));
            g2d.setColor(panelDeColores.getColorBordeActual());

            boolean tempRelleno = figuraActual.isRelleno();
            Color tempColorRelleno = figuraActual.getColorDeRelleno();

            if (panelDeColores.isRellenar()) {
                figuraActual.setRelleno(true);
                figuraActual.setColorDeRelleno(panelDeColores.getColorRellenoActual());
            } else {
                figuraActual.setRelleno(false);
            }

            figuraActual.dibujar(g2d);

            figuraActual.setRelleno(tempRelleno);
            figuraActual.setColorDeRelleno(tempColorRelleno);
        }
    }

    public void setImagenDeFondo(BufferedImage imagen) {
        this.imagenDeFondo = imagen;
        if (lienzoGraphics != null) {
            lienzoGraphics.setComposite(AlphaComposite.Clear);
            lienzoGraphics.fillRect(0, 0, lienzoImagen.getWidth(), lienzoImagen.getHeight());
            lienzoGraphics.setComposite(AlphaComposite.SrcOver);

            lienzoGraphics.drawImage(imagenDeFondo, 0, 0, this);
            undoStack.push(copyImage(lienzoImagen));
            redoStack.clear();
        } else {
            crearLienzoImagen();
        }
        repaint();
    }

    public void undo() {
        if (undoStack.size() > 1) {
            redoStack.push(copyImage(lienzoImagen));
            lienzoImagen = undoStack.pop();
            lienzoGraphics = lienzoImagen.createGraphics();
            lienzoGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            deseleccionarArea();
            repaint();
        } else {
            System.out.println("No hay más acciones para deshacer.");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(copyImage(lienzoImagen));
            lienzoImagen = redoStack.pop();
            lienzoGraphics = lienzoImagen.createGraphics();
            lienzoGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            deseleccionarArea();
            repaint();
        } else {
            System.out.println("No hay más acciones para rehacer.");
        }
    }

    public void clearCanvas() {
        if (lienzoGraphics != null) {
            undoStack.push(copyImage(lienzoImagen));
            redoStack.clear();
            lienzoGraphics.setComposite(AlphaComposite.Clear);
            lienzoGraphics.fillRect(0, 0, lienzoImagen.getWidth(), lienzoImagen.getHeight());
            lienzoGraphics.setComposite(AlphaComposite.SrcOver);
            imagenDeFondo = null;
        }
        deseleccionarArea();
        repaint();
    }

    private BufferedImage copyImage(BufferedImage source) {
        if (source == null)
            return null;
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public Figura getFiguraSeleccionada() {
        return null;
    }

    public void addFigura(Figura figura) {
    }

    public void deseleccionarFigura() {
        deseleccionarArea();
    }

    public void deseleccionarArea() {
        if (areaSeleccionada != null || imagenSeleccionada != null) {
            areaSeleccionada = null;
            imagenSeleccionada = null;
            imagenFondoRecorte = null;
            inicioArrastreSeleccion = null;
            finArrastreSeleccion = null;
            isDraggingSelectedArea = false;
            repaint();
        }
    }

    /**
     * Obtiene la imagen del área seleccionada.
     * 
     * @return El BufferedImage del área seleccionada, o null si no hay selección.
     */
    public BufferedImage getImagenSeleccionada() {
        return imagenSeleccionada;
    }

    /**
     * Obtiene el rectángulo que define el área seleccionada.
     * 
     * @return El Rectangle del área seleccionada, o null si no hay selección.
     */
    public Rectangle getAreaSeleccionada() {
        return areaSeleccionada;
    }

    /**
     * Pega una imagen en el lienzo en una posición específica.
     * 
     * @param image La imagen a pegar.
     * @param x     La coordenada X donde se pegará la esquina superior izquierda.
     * @param y     La coordenada Y donde se pegará la esquina superior izquierda.
     */
    public void pegarImagen(BufferedImage image, int x, int y) {
        if (lienzoGraphics != null && image != null) {
            undoStack.push(copyImage(lienzoImagen));
            redoStack.clear();

            lienzoGraphics.drawImage(image, x, y, null);
            deseleccionarArea();
            repaint();
        }
    }

    public void setGrosor(int grosor) {
        this.grosor = grosor;
    }
}