package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * @author Fabien Steines
 */
public class Tileset {

    private Image tilesetImage;
    private int cellSize = 32;

    public void loadImage(String imagePath) {
        this.tilesetImage = new Image(String.format("file:%s",imagePath));
    }

    public void drawTileTo(GraphicsContext g,int x, int y, int tsX, int tsY) {
        g.drawImage(this.tilesetImage,tsX * cellSize,tsY * cellSize,cellSize,cellSize,
                   x * cellSize,y * cellSize,cellSize,cellSize);
    }

    public Image getTilesetImage() {
        return this.tilesetImage;
    }

}
