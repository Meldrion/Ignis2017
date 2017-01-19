package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import lu.innocence.ignis.event.RenderTerrainTileInterface;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetLayer {

    private final int cellSize = 32;
    private List<List<TileCell>> matrix;
    private int width;
    private int height;
    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(TilesetLayer.class);

    public TilesetLayer() {
        this.width = 0;
        this.height = 0;
    }

    /**
     * @param x
     * @param y
     */
    public void setDimension(int x, int y) {

        if (this.width != x || this.height != y) {
            this.width = x;
            this.height = y;
            this.buildMatrix();
        }

    }

    /**
     *
     */
    public void buildMatrix() {

        this.matrix = new ArrayList<>();
        for (int i = 0; i < this.width; i++) {

            List<TileCell> innerMatrix = new ArrayList<>();
            for (int j = 0; j < this.height; j++) {
                innerMatrix.add(null);
            }

            this.matrix.add(innerMatrix);

        }

    }

    /**
     * @param x
     * @param y
     * @param tsX
     * @param tsY
     */
    public TileCell addCell(int x, int y, int tsX, int tsY) {
        if (this.isInRange(x,y)) {
            TileCell tsCell = new TileCell(x, y, tsX, tsY);
            return this.matrix.get(x).set(y, tsCell);
        } else {
            return null;
        }
    }

    /**
     * @param g
     * @param tileset
     */
    public void render(GraphicsContext g, Tileset tileset) {

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                TileCell cell = this.matrix.get(i).get(j);
                if (cell != null) {
                    cellDrawing(g, tileset, cell, i, j, true);
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
                             int x, int y, boolean ignoreRenderOthersArround) {
        if (Tileset.isTilesetCell(cell.tsY)) {
            // Tileset
            tileset.drawTileTo(g, x, y, cell.tsX, cell.tsY - 1);
        } else {
            // Terrain
            tileset.getTerrain(cell.tsX).draw(g, x, y,
                    this.calculateScore(this.buildNeightbourMatrix(x,y)));
        }
    }

    private boolean[][] buildNeightbourMatrix(int x, int y) {
        TileCell cellToTest = this.matrix.get(x).get(y);

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
                    TileCell cTile = this.matrix.get(nX + i).get(nY + j);
                    matrix[i][j] = cTile.sameTileAs(cellToTest);
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

    private boolean isInRange(int x,int y) {
        return 0 <= x && x < this.width && 0 <= y && y < this.height;
    }


    /**
     * @param g
     * @param x
     * @param y
     * @param tileset
     * @param ignoreRenderOthersArround
     */
    public void renderPartial(GraphicsContext g, int x, int y, Tileset tileset, boolean ignoreRenderOthersArround) {
        if (-1 < x && -1 < y && x < this.width && y < this.height) {
            TileCell cell = this.matrix.get(x).get(y);
            if (cell != null) {
                if (tileset.isTilesetCell(cell.tsY)) {
                    // Tileset
                    tileset.drawTileTo(g, x, y, cell.tsX, cell.tsY - 1);
                } else {
                    // Terrain
                    cellDrawing(g, tileset, cell, x, y, ignoreRenderOthersArround);
                }
            }
        }
    }

    /**
     * @param x
     * @param y
     */
    public TileCell removeCell(int x, int y) {
        TileCell cell = this.matrix.get(x).get(y);
        this.matrix.get(x).set(y, null);
        return cell;
    }

    /**
     * @return
     */
    public JSONArray saveLayer() {
        JSONArray layer = new JSONArray();
        for (int i = 0; i < this.matrix.size(); i++) {
            for (int j = 0; j < this.matrix.get(0).size(); j++) {
                TileCell cell = this.matrix.get(i).get(j);
                if (cell != null) {
                    layer.add(cell.save());
                }
            }
        }
        return layer;
    }

    /**
     *
     */
    public void clearLayer() {
        this.matrix.clear();
    }

}
