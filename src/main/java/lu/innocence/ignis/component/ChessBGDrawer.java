package lu.innocence.ignis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Fabien on 12.11.2016.
 */
public class ChessBGDrawer {

    public static void drawChessBackground(GraphicsContext g,double width,double height,int cellWidth,int cellHeight) {
        for (int i = 0; i < width / cellWidth; i++) {
            for (int j = 0; j < height / cellHeight; j++) {
                drawChessBackgroundSingle(g,i,j,cellWidth,cellHeight);
            }}
    }

    public static void drawChessBackgroundSingle(GraphicsContext g,double x,double y,int tileWidth,int tileHeight) {

        int tileWidthDiv = tileWidth / 2;
        int tileHeightDiv = tileHeight / 2;

        for (int i = 0; i < tileWidth / 2; i++) {
            for (int j = 0; j < tileHeight / 2; j++) {

                if ((i + j) % 2 == 0) {
                    g.setFill(Color.WHITE);
                } else {
                    g.setFill(Color.LIGHTGRAY);
                }

                g.fillRect(i * 16, j * 16, 16, 16);
            }
        }

    }

}
