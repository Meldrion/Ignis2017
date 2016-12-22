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
    private static final int cellSize = 32;

    public Terrain() {
        this.terrainImageName = "";
    }

    public void loadImage(String imagePath) {
        this.terrainImageName = (new File(imagePath)).getName();
        this.terrainImage = new Image(String.format("file:%s", imagePath));
    }

    public String getImageName() {
        return this.terrainImageName;
    }

    public void draw(GraphicsContext g, int x, int y, int tileId) {

    }

    public void draw(GraphicsContext g, int x, int y, int tsX, int tsY) {
        //draw(g, x, y, tsX, tsY, false);
    }

    public void draw(GraphicsContext g, int x, int y, Boolean[][] sameMatrix) {

    }

}
