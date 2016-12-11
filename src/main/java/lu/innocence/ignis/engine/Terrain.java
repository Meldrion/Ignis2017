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
        this.terrainImage = new Image(String.format("file:%s", imagePath));
        ;
    }

    public String getImageName() {
        return this.terrainImageName;
    }

    public void draw(GraphicsContext g, int x, int y, int tsX, int tsY) {
        g.drawImage(this.terrainImage, tsX * cellSize, tsY * cellSize, cellSize, cellSize,
                x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public void draw(GraphicsContext g, int x, int y, Integer[][] sameMatrix) {

        int top_left = sameMatrix[0][0];
        int top = sameMatrix[1][0];
        int top_right = sameMatrix[2][0];
        int middle_left = sameMatrix[0][1];
        int middle = IS_UNSET;
        int middle_right = sameMatrix[2][1];
        int bottom_left = sameMatrix[0][2];
        int bottom = sameMatrix[1][2];
        int bottom_right = sameMatrix[2][2];

        // Middle
        if (top == IS_SAME && bottom == IS_SAME && middle_left == IS_SAME
                && middle_right == IS_SAME) {
            draw(g, x, y, 1, 2);
        } else {
            // Middle Right
            if (top == IS_SAME && bottom == IS_SAME && middle_left == IS_SAME
                    && middle_right == IS_DIFFERENT) {
                draw(g, x, y, 2, 2);
            } else {
                // Middle Left
                if (top == IS_SAME && bottom == IS_SAME && middle_left == IS_DIFFERENT
                        && middle_right == IS_SAME) {
                    draw(g, x, y, 0, 2);
                } else {
                    // Top right
                    if (middle_left == IS_SAME && bottom == IS_SAME
                            && top == IS_DIFFERENT && middle_right == IS_DIFFERENT) {
                        draw(g, x, y, 2, 1);
                    } else {
                        // Top left
                        if (middle_right == IS_SAME && bottom == IS_SAME
                                && top == IS_DIFFERENT && middle_left == IS_DIFFERENT) {
                            draw(g, x, y, 0, 1);
                        } else {

                            // Bottom left
                            if (middle_right == IS_SAME && bottom == IS_DIFFERENT
                                    && top == IS_SAME && middle_left == IS_DIFFERENT) {
                                draw(g, x, y, 0, 3);
                            } else {
                                // Bottom right
                                if (middle_left == IS_SAME && bottom == IS_DIFFERENT
                                        && top == IS_SAME && middle_right == IS_DIFFERENT) {
                                    draw(g, x, y, 2, 3);
                                } else {
                                    // TOP
                                    if (middle_left == IS_SAME && middle_right == IS_SAME
                                            && bottom == IS_SAME && top == IS_DIFFERENT) {
                                        draw(g, x, y, 1, 1);
                                    } else {
                                        // BOTTOM
                                        if (middle_left == IS_SAME && middle_right == IS_SAME
                                                && bottom == IS_DIFFERENT && top == IS_SAME) {
                                            draw(g, x, y, 1, 3);
                                        } else {
                                            // We are nothing
                                            draw(g, x, y, 0, 0);
                                        }

                                    }
                                }
                            }

                        }
                    }

                }
            }
        }
    }
}
