package figuras;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TransferibleImagen implements Transferable {

    // Define un DataFlavor personalizado para BufferedImage
    public static final DataFlavor BUFFERED_IMAGE_FLAVOR = new DataFlavor(BufferedImage.class, "BufferedImage");

    private static final DataFlavor[] SUPPORTED_FLAVORS = {
        BUFFERED_IMAGE_FLAVOR,
        DataFlavor.imageFlavor // También soportamos el DataFlavor.imageFlavor estándar
    };

    private final BufferedImage imagen;

    /**
     * @param imagen La instancia de BufferedImage a transferir.
     */
    public TransferibleImagen(BufferedImage imagen) {
        this.imagen = imagen;
    }

    /**
     * @return Un array de DataFlavor soportados.
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVORS;
    }

    /**
     * @param flavor El DataFlavor a verificar.
     * @return true si el flavor es soportado, false en caso contrario.
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return BUFFERED_IMAGE_FLAVOR.equals(flavor) || DataFlavor.imageFlavor.equals(flavor);
    }

    /**
     * @param flavor El DataFlavor solicitado.
     * @return Los datos en el formato solicitado (un objeto BufferedImage en este caso).
     * @throws UnsupportedFlavorException Si el flavor no es soportado.
     * @throws IOException Si ocurre un error de E/S al obtener los datos (poco probable aquí).
     */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (BUFFERED_IMAGE_FLAVOR.equals(flavor) || DataFlavor.imageFlavor.equals(flavor)) {
            return imagen;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}