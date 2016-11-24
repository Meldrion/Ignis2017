package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

/**
 * @author Fabien Steines
 */
public class Tileset {

    private Image tilesetImage;
    private int cellSize = 32;
    private int index;
    private String name;
    private String imageName;

    public Tileset() {
        this.name = "";
        this.imageName = "";
    }

    public void loadImage(String imagePath) {
        this.imageName = (new File(imagePath)).getName();
        this.tilesetImage = new Image(String.format("file:%s", imagePath));
    }

    public void drawTileTo(GraphicsContext g, int x, int y, int tsX, int tsY) {
        g.drawImage(this.tilesetImage, tsX * cellSize, tsY * cellSize, cellSize, cellSize,
                x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public Image getTilesetImage() {
        return this.tilesetImage;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }
}
