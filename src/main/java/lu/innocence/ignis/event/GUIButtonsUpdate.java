package lu.innocence.ignis.event;

/**
 * @author Fabien Steines
 */
public interface GUIButtonsUpdate {
    void activeLayerChanged(int layerIndex);
    void activeToolChanged(int toolIndex);
}
