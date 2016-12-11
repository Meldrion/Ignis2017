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

    public void draw(GraphicsContext g, int x, int y, int tsX, int tsY, boolean specialCase) {

        if (specialCase) {

            if (tsX == 1 && tsY == 1) {
                g.drawImage(this.terrainImage, 0, cellSize, cellSize / 2, cellSize,
                        x * cellSize, y * cellSize, cellSize / 2, cellSize);

                g.drawImage(this.terrainImage, 2 * cellSize + cellSize / 2, cellSize, cellSize / 2, cellSize,
                        x * cellSize + cellSize / 2, y * cellSize, cellSize / 2, cellSize);
                return;
            }

            if (tsX == 1 && tsY == 3) {
                g.drawImage(this.terrainImage, 0, cellSize * 3, cellSize / 2, cellSize,
                        x * cellSize, y * cellSize, cellSize / 2, cellSize);

                g.drawImage(this.terrainImage, 2 * cellSize + cellSize / 2, cellSize * 3, cellSize / 2, cellSize,
                        x * cellSize + cellSize / 2, y * cellSize, cellSize / 2, cellSize);
                return;
            }

        } else {
            g.drawImage(this.terrainImage, tsX * cellSize, tsY * cellSize, cellSize, cellSize,
                    x * cellSize, y * cellSize, cellSize, cellSize);
        }

    }

    public void draw(GraphicsContext g, int x, int y, int tsX, int tsY) {
        draw(g, x, y, tsX, tsY, false);
    }

    public void draw(GraphicsContext g, int x, int y, Integer[][] sameMatrix) {

        int topLeft = sameMatrix[0][0];
        int top = sameMatrix[1][0];
        int topRight = sameMatrix[2][0];
        int middleLeft = sameMatrix[0][1];
        int middleRight = sameMatrix[2][1];
        int bottomLeft = sameMatrix[0][2];
        int bottom = sameMatrix[1][2];
        int bottomRight = sameMatrix[2][2];

        // Middle
        if (top == IS_SAME && bottom == IS_SAME && middleLeft == IS_SAME
                && middleRight == IS_SAME) {
            draw(g, x, y, 1, 2);
            return;
        }
        // Middle Right
        if (top == IS_SAME && bottom == IS_SAME && middleLeft == IS_SAME
                && middleRight == IS_DIFFERENT) {
            draw(g, x, y, 2, 2);
            return;
        }
        // Middle Left
        if (top == IS_SAME && bottom == IS_SAME && middleLeft == IS_DIFFERENT
                && middleRight == IS_SAME) {
            draw(g, x, y, 0, 2);
            return;
        }
        // Top right
        if (middleLeft == IS_SAME && bottom == IS_SAME
                && top == IS_DIFFERENT && middleRight == IS_DIFFERENT) {
            draw(g, x, y, 2, 1);
            return;
        }
        // Top left
        if (middleRight == IS_SAME && bottom == IS_SAME
                && top == IS_DIFFERENT && middleLeft == IS_DIFFERENT) {
            draw(g, x, y, 0, 1);
            return;
        }

        // Bottom left
        if (middleRight == IS_SAME && bottom == IS_DIFFERENT
                && top == IS_SAME && middleLeft == IS_DIFFERENT) {
            draw(g, x, y, 0, 3);
            return;
        }
        // Bottom right
        if (middleLeft == IS_SAME && bottom == IS_DIFFERENT
                && top == IS_SAME && middleRight == IS_DIFFERENT) {
            draw(g, x, y, 2, 3);
            return;
        }
        // TOP
        if (middleLeft == IS_SAME && middleRight == IS_SAME
                && bottom == IS_SAME && top == IS_DIFFERENT) {
            draw(g, x, y, 1, 1);
            return;
        }
        // BOTTOM
        if (middleLeft == IS_SAME && middleRight == IS_SAME
                && bottom == IS_DIFFERENT && top == IS_SAME) {
            draw(g, x, y, 1, 3);
            return;
        }

        // TOP w/o neighbours
        if (bottom == IS_SAME && middleLeft == IS_DIFFERENT
                && middleRight == IS_DIFFERENT) {
            draw(g, x, y, 1, 1, true);
            return;
        }

        // Bottom w/o neighbours
        if (top == IS_SAME && bottom == IS_DIFFERENT && middleLeft == IS_DIFFERENT
                && middleRight == IS_DIFFERENT) {
            draw(g, x, y, 1, 3, true);
            return;
        }

        // We are nothing
        draw(g, x, y, 0, 0);
    }

}
