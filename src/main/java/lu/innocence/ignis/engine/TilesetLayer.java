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

    /**
     * @param x
     * @param y
     */
    public void setDimension(int x, int y) {
        this.width = x;
        this.height = y;
        this.buildMatrix();
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
        TileCell tsCell = new TileCell(x, y, tsX, tsY);
        return this.matrix.get(x).set(y, tsCell);
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
    private void cellDrawing(GraphicsContext g, Tileset tileset, TileCell cell, int x, int y, boolean ignoreRenderOthersArround) {
        if (Tileset.isTilesetCell(cell.tsY)) {
            // Tileset
            tileset.drawTileTo(g, x, y, cell.tsX, cell.tsY - 1);
        } else {

            boolean hasLeft = x != 0;
            boolean hasTop = y != 0;
            boolean hasRight = x != (this.width - 1);
            boolean hasBottom = y != (this.height - 1);

            Integer[][] sameMatrix = new Integer[3][3];
            sameMatrix[0][0] = (hasLeft && hasTop) ? cell.sameTileAs(this.matrix.get(x - 1).get(y - 1))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            sameMatrix[0][1] = hasLeft ? cell.sameTileAs(this.matrix.get(x - 1).get(y))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            sameMatrix[0][2] = (hasLeft && hasBottom) ? cell.sameTileAs(this.matrix.get(x - 1).get(y + 1))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            sameMatrix[1][0] = hasTop ? cell.sameTileAs(this.matrix.get(x).get(y - 1))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            sameMatrix[1][1] = null;
            sameMatrix[1][2] = hasBottom ? cell.sameTileAs(this.matrix.get(x).get(y + 1))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            sameMatrix[2][0] = (hasRight && hasTop) ? cell.sameTileAs(this.matrix.get(x + 1).get(y - 1))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            sameMatrix[2][1] = hasRight ? cell.sameTileAs(this.matrix.get(x + 1).get(y))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            sameMatrix[2][2] = (hasRight && hasBottom) ? cell.sameTileAs(this.matrix.get(x + 1).get(y + 1))
                    ? Terrain.IS_SAME : Terrain.IS_DIFFERENT : Terrain.IS_UNSET;
            // Terrain
            tileset.getTerrain(cell.tsX).draw(g, x, y, sameMatrix);
        }
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
