package lu.innocence.ignis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Fabien Steines
 */
public class ChessBGDrawer {

    public static void drawChessBackground(GraphicsContext g, int width, int height, int cellWidth, int cellHeight) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                drawChessBackgroundSingle(g, i, j, cellWidth, cellHeight);
            }
        }
    }

    public static void drawChessBackgroundSingle(GraphicsContext g, double x, double y, int tileWidth, int tileHeight) {

        int tileWidthDiv = tileWidth / 2;
        int tileHeightDiv = tileHeight / 2;

        int coordX = (int) x * tileWidth;
        int coordY = (int) y * tileHeight;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {

                if ((i + j) % 2 == 0) {
                    g.setFill(Color.WHITE);
                } else {
                    g.setFill(Color.LIGHTGRAY);
                }

                g.fillRect(coordX + (i * tileWidthDiv), coordY + (j * tileHeightDiv), tileWidthDiv, tileHeightDiv);
            }
        }

    }

}
