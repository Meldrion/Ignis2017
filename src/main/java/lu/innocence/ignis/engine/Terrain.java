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

        int top_left = sameMatrix[0][0];
        int top = sameMatrix[1][0];
        int top_right = sameMatrix[2][0];
        int middle_left = sameMatrix[0][1];
        int middle = IS_UNSET;
        int middle_right = sameMatrix[2][1];
        int bottom_left = sameMatrix[0][2];
        int bottom = sameMatrix[1][2];
        int bottom_right = sameMatrix[2][2];

        if (top == IS_SAME && bottom == IS_SAME && middle_left == IS_SAME && middle_right == IS_SAME) {
            // Middle
            draw(g,x,y,1,2);
        } else {
            if (top == IS_SAME && bottom == IS_SAME && middle_left == IS_SAME && middle_right == IS_DIFFERENT) {
                // Middle Right
                draw(g,x,y,2,2);
            } else {
                if (middle_left == IS_SAME && bottom == IS_SAME
                        && top == IS_DIFFERENT && middle_right == IS_DIFFERENT) {
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
