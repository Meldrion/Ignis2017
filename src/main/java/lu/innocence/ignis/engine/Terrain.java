package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

/**
 * @author Fabien Steines
 */
public class Terrain {

    public static final int IS_SAME = 0x0;
    public static final int IS_DIFFERENT = 0x1;
    public static final int IS_UNSET = 0x2;

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

    public void draw(GraphicsContext g, int x, int y,Integer[][] sameMatrix) {


        if (sameMatrix[1][0] == IS_SAME && sameMatrix[0][1] == IS_SAME
                && sameMatrix[2][1] == IS_SAME  && sameMatrix[1][2] == IS_SAME) {
            // Middle
            draw(g,x,y,1,2);
        } else {
            if (sameMatrix[0][0] == IS_SAME && sameMatrix[0][1] == IS_SAME &&
                    sameMatrix[1][2] == IS_SAME) {
                // Middle Right
                draw(g,x,y,2,2);
            } else {
                if (sameMatrix[0][1] == IS_SAME && sameMatrix[1][2] == IS_SAME
                        &&  sameMatrix[2][1] == IS_DIFFERENT) {
                    // Top right
                    draw(g,x,y,2,1);
                } else {
                    // We are nothing
                    draw(g,x,y,0,0);
                }

            }

        }
    }

}
