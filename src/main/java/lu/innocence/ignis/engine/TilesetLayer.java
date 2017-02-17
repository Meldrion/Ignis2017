package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetLayer extends Layer<TileCell> {


    private static final Logger LOGGER = LogManager.getLogger(TilesetLayer.class);

    /**
     *
     */
    TilesetLayer() {
        super();
        LOGGER.info("Created TileLayer");
    }

    /**
     *
     * @param width
     * @param height
     */
    void setDimension(int width, int height) {

        if (this.getWidth() != width|| this.getHeight() != height) {
            this.setWidth(width);
            this.setHeight(height);
            this.buildMatrix();
        }

    }

/*    *//**
     *
     *//*
    public void buildMatrix() {

        List<List<TileCell>> old = this.matrix;

        this.matrix = new ArrayList<>();
        for (int i = 0; i < this.width; i++) {

            List<TileCell> innerMatrix = new ArrayList<>();
            for (int j = 0; j < this.height; j++) {
                if (old != null && isInRange(i,j,old.size(),old.get(0).size())) {
                    innerMatrix.add(old.get(i).get(j));
                } else {
                    innerMatrix.add(null);
                }
            }
            this.matrix.add(innerMatrix);
        }

    }*/


    /**
     * @param g
     * @param tileset
     */
    public void render(GraphicsContext g, Tileset tileset) {

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                TileCell cell = this.getMatrix().get(i).get(j);
                if (cell != null) {
                    cellDrawing(g, tileset, cell, i, j);
                }
            }
        }
    }

    /**
     * @param g
     * @param tileset
     * @param cell
     * @param x
     * @param y
     */
    private void cellDrawing(GraphicsContext g, Tileset tileset, TileCell cell,
                             int x, int y) {
        if (Tileset.isTilesetCell(cell.tsY)) {
            // Tileset
            tileset.drawTileTo(g, x, y, cell.tsX, cell.tsY - 1);
        } else {
            // Terrain
            tileset.getTerrain(cell.tsX).draw(g, x, y,
                    this.calculateScore(this.buildNeightbourMatrix(x,y)));
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private boolean[][] buildNeightbourMatrix(int x, int y) {
        TileCell cellToTest = this.getMatrix().get(x).get(y);

        // Create the Matrix the will be analysed later
        boolean[][] matrix = new boolean[3][3];
        // Get the Point Zero
        int nX = x - 1;
        int nY = y - 1;

        // Loop through the neightbours
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                // If the tile is not in range, the result will
                // always be true
                if (this.isInRange(nX + i, nY + j)) {
                    TileCell cTile = this.getMatrix().get(nX + i).get(nY + j);
                    matrix[i][j] = cTile != null && cTile.sameTileAs(cellToTest);
                } else {
                    matrix[i][j] = true;
                }

            }
        }

        // Return the Matrix
        return matrix;
    }

    // Get the Score Value depending on the 9x9 Matrix
    private int calculateScore(boolean[][] neightbours) {
        int totalScore = 0;

        totalScore += neightbours[1][0] ? 1 : 0;
        totalScore += neightbours[0][1] ? 2 : 0;
        totalScore += neightbours[2][1] ? 4 : 0;
        totalScore += neightbours[1][2] ? 8 : 0;

        return totalScore;
    }




    /**
     * @param g
     * @param x
     * @param y
     * @param tileset
     */
    public void renderPartial(GraphicsContext g, int x, int y, Tileset tileset) {
        if (-1 < x && -1 < y && x < this.getWidth() && y < this.getHeight()) {
            TileCell cell = this.getMatrix().get(x).get(y);
            if (cell != null) {
                if (Tileset.isTilesetCell(cell.tsY)) {
                    // Tileset
                    tileset.drawTileTo(g, x, y, cell.tsX, cell.tsY - 1);
                } else {
                    // Terrain
                    cellDrawing(g, tileset, cell, x, y);
                }
            }
        }
    }

    /**
     * @param x
     * @param y
     */
    public TileCell removeCell(int x, int y) {
        TileCell cell = this.getMatrix().get(x).get(y);
        this.getMatrix().get(x).set(y, null);
        return cell;
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONArray saveLayer() {
        JSONArray layer = new JSONArray();
        for (int i = 0; i < this.getMatrix().size(); i++) {
            for (int j = 0; j < this.getMatrix().get(0).size(); j++) {
                TileCell cell = this.getMatrix().get(i).get(j);
                if (cell != null) {
                    layer.add(cell.save());
                }
            }
        }
        return layer;
    }



}
