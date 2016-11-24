package lu.innocence.ignis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lu.innocence.ignis.engine.Tileset;

/**
 * @author Fabien Steines
 */
public class TilesetManagerCanvas extends Canvas {

    private final int cellSize = 32;
    private Tileset tileset;

    /**
     * @param tileset
     */
    public void setTileset(Tileset tileset) {

        if (tileset != null) {
            Image image = tileset.getTilesetImage();
            this.setWidth(image != null ? image.getWidth() : 0);
            this.setHeight(image != null ? image.getHeight() : 0);
        } else {
            if (this.tileset != null)
                this.setHeight(0);
        }

        this.tileset = tileset;
        this.render();
    }

    /**
     *
     */
    public void render() {

        if (this.getWidth() > 0 && this.getHeight() > 0) {
            GraphicsContext g = this.getGraphicsContext2D();
            ChessBGDrawer.drawChessBackground(g, (int) this.getWidth() / cellSize, (int) this.getHeight() / cellSize,
                    cellSize, cellSize);

            if (this.tileset != null && this.tileset.getTilesetImage() != null) {
                g.drawImage(this.tileset.getTilesetImage(), 0, 0);
            }
        }
    }

}
