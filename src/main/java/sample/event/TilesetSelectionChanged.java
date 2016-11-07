package sample.event;

/**
 * @author Fabien Steines
 */
public interface TilesetSelectionChanged {
    void tilesetSelectionChanged(int startX,int startY,int width,int height);
}
