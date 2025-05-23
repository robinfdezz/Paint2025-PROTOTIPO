package paint202510;

import java.util.EventListener;
public interface HerramientaSeleccionadaListener extends EventListener {
    void herramientaSeleccionadaCambio(String nuevaHerramienta);
}