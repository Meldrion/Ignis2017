package lu.innocence.ignis.event;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by fabien on 13/12/16.
 */
public interface RenderTerrainTileInterface {
    void forceRenderTile(GraphicsContext g,int x, int y);
}
