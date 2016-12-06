package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

/**
 * @author Fabien Steines
 */
public class Terrain {

    private String terrainImageName;
    private Image terrainImage;
    private int cellSize = 32;

    public Terrain() {
        this.terrainImageName = "";
    }

    public void loadImage(String imagePath) {
        this.terrainImageName = (new File(imagePath)).getName();
        this.terrainImage = new Image(String.format("file:%s", imagePath));;
    }

    public String getImageName() {
        return this.terrainImageName;
    }

    public void draw(GraphicsContext g, int x, int y, int tsX, int tsY) {
        g.drawImage(this.terrainImage, tsX * cellSize, tsY * cellSize, cellSize, cellSize,
                x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public void draw(GraphicsContext g, int x, int y) {

    }

}
