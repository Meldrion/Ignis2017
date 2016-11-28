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

    private boolean[][] collisionMatrix;
    private Terrain[] terrainCells;

    public Tileset() {
        this.name = "";
        this.imageName = "";
        terrainCells = new Terrain[8];
    }

    public void loadImage(String imagePath) {
        this.imageName = (new File(imagePath)).getName();
        Image newTsImage = new Image(String.format("file:%s", imagePath));
        initCollisionMatrix(newTsImage);
        this.tilesetImage = newTsImage;
    }

    public void drawTileTo(GraphicsContext g, int x, int y, int tsX, int tsY) {
        g.drawImage(this.tilesetImage, tsX * cellSize, tsY * cellSize, cellSize, cellSize,
                x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public boolean isTilesetCell(int y) {
        return 0 < y;
    }

    private void initCollisionMatrix(Image newImage) {
        boolean[][] tmpCollisionMatrix = new boolean[getCellWidth(newImage)][getCellHeight(newImage)];

        for (int x = 0;x < tmpCollisionMatrix.length;x++) {
            for (int y = 0;y < tmpCollisionMatrix[0].length;y++) {
                if (this.collisionMatrix != null && inRange(x,y)) {
                    tmpCollisionMatrix[x][y] = this.collisionMatrix[x][y];
                } else {
                    tmpCollisionMatrix[x][y] = false;
                }
            }
        }

        this.collisionMatrix = tmpCollisionMatrix;
    }

    public boolean collisionAt(int x,int y) {
        return this.collisionMatrix[x][y];
    }

    public void setCollisionAt(int x,int y,boolean collision) {
        this.collisionMatrix[x][y] = collision;
    }

    public boolean[][] getCollisionMatrix() {
        return this.collisionMatrix;
    }

    public boolean inRange(int x,int y) {
        return x < this.getCellWidth() && y < this.getCellHeight();
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

    public int getCellSize() {
        return this.cellSize;
    }

    public int getCellWidth() {
        return this.getCellWidth(this.tilesetImage);
    }

    private int getCellWidth(Image tsImage) {
        return tsImage != null ? (int) tsImage.getWidth() / this.cellSize : 0;
    }

    public int getCellHeight() {
        return this.getCellHeight(this.tilesetImage);
    }

    public int getCellHeight(Image tsImage) {
        return tsImage != null ? (int) tsImage.getHeight() / this.cellSize + 1: 0;
    }

    public void setTerrain(int index,Terrain terrain) {
        this.terrainCells[index] = terrain;
    }

    public Terrain getTerrain(int index) {
        return this.terrainCells[index];
    }
}
