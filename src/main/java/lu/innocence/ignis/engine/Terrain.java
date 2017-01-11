package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.File;

/**
 * @author Fabien Steines
 */
public class Terrain {

    private String terrainImageName;
    private Image terrainImage;
    private Image terrainTiles[];
    private static final int cellSize = 32;
    private static final int MAX = 4;
    private static final int[][] LOOK_UP_COORDS = {
        // 0
        {0,0,0,0},

        // Middle Part Coords
        {0,1,0,1},{1,1,1,1},{2,1,2,1},
        {0,2,0,2},{1,2,1,2},{2,2,2,2},
        {0,3,0,3},{1,3,1,3},{2,3,2,3},

        // Speical cases
        {0,1,0,3},{0,1,2,1},
        {2,1,2,3},{0,3,2,3},
        {1,1,1,3},{0,2,2,2},

        // For testing only
        {1,0,1,0}
    };

    private static final int[] TERRAIN_SETTINGS = {
        0, 12, 14, 10,
        13, 15, 11, 5,
        7, 3, 4, 8,
        2, 1, 6, 9
    };

    public Terrain() {
        this.terrainImageName = "";
    }

    public void loadImage(String imagePath) {
        this.terrainImageName = (new File(imagePath)).getName();
        this.terrainImage = new Image(String.format("file:%s", imagePath));
        this.terrainTiles = new Image[(int) Math.pow(MAX, 2)];
    }

    /**
     *
     * @param x
     * @param y
     * @param terrainImage
     * @param cellSize
     * @param MAX
     * @return
     */
    private Image createTile(int x, int y, Image terrainImage, int cellSize, int MAX) {
        int index = y * MAX + x;
        return this.buildTile(LOOK_UP_COORDS[index], cellSize, terrainImage, index % 2 == 0);
    }

    /**
     *
     * @param coordArray
     * @param cellSize
     * @param terrainImage
     * @param horizontal
     * @return
     */
    private Image buildTile(int[] coordArray, int cellSize, Image terrainImage, boolean horizontal) {

        int sX1 = coordArray[0];
        int sY1 = coordArray[1];
        int sX2 = coordArray[2];
        int sY2 = coordArray[3];

        int width = horizontal ? cellSize : cellSize / 2;
        int height = horizontal ? cellSize / 2 : cellSize;

        PixelReader px = terrainImage.getPixelReader();
        WritableImage terrainTile = new WritableImage(cellSize,cellSize);
        terrainTile.getPixelWriter().setPixels(0,0,width,height,px,
                sX1 * cellSize, sY1 * cellSize);

        terrainTile.getPixelWriter().setPixels(cellSize - width, cellSize - height,
                width,height,px,sX2 * cellSize + (cellSize - width),
                sY2 * cellSize + (cellSize - height));

        return terrainTile;

    }

    public Image getTileAtIndex(int index) {
        return this.terrainTiles[index];
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
