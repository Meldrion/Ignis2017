package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
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
    public void addCell(int x, int y, int tsX, int tsY) {
        this.matrix.get(x).set(y, new TileCell(x, y, tsX, tsY));
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
                    if ((tileset.isTilesetCell(cell.tsY)) ) {
                        // Tileset
                        tileset.drawTileTo(g, i, j, cell.tsX, cell.tsY - 1);
                    } else {

                        boolean hasLeft = i != 0;
                        boolean hasTop = j != 0;
                        boolean hasRight = i != (this.width - 1);
                        boolean hasBottom = j != (this.height - 1);

                        Boolean[][] sameMatrix = new Boolean[3][3];
                        sameMatrix[0][0] = (hasLeft && hasTop) ? cell.sameTileAs(this.matrix.get(i-1).get(j-1)) : null;
                        sameMatrix[0][1] = (hasTop) ? cell.sameTileAs(this.matrix.get(i).get(j-1)) : null;
                        sameMatrix[0][2] = (hasRight && hasTop) ? cell.sameTileAs(this.matrix.get(i+1).get(j-1)) : null;
                        sameMatrix[1][0] = (hasLeft) ? cell.sameTileAs(this.matrix.get(i-1).get(j)) : null;
                        sameMatrix[1][1] = null;
                        sameMatrix[1][2] = (hasRight) ? cell.sameTileAs(this.matrix.get(i+1).get(j)) : null;
                        sameMatrix[2][0] = (hasLeft && hasBottom) ? cell.sameTileAs(this.matrix.get(i-1).get(j+1)) : null;
                        sameMatrix[2][1] = (hasBottom) ? cell.sameTileAs(this.matrix.get(i).get(j+1)) : null;
                        sameMatrix[2][2] = (hasRight && hasBottom) ? cell.sameTileAs(this.matrix.get(i+1).get(j+1)) : null;

                        // Terrain
                        tileset.getTerrain(cell.tsX).draw(g,i,j,sameMatrix);
                    }
                }
            }
        }
    }

    /**
     * @param g
     * @param x
     * @param y
     * @param tileset
     */
    public void renderPartial(GraphicsContext g, int x, int y, Tileset tileset) {
        TileCell cell = this.matrix.get(x).get(y);
        if (cell != null) {
            if (tileset.isTilesetCell(cell.tsY)) {
                // Tileset
                tileset.drawTileTo(g, x, y, cell.tsX, cell.tsY - 1);
            } else {
                // Terrain
                tileset.getTerrain(cell.tsX).draw(g,x,y,0,0);
            }
        }
    }

    /**
     * @param x
     * @param y
     */
    public void removeCell(int x, int y) {
        this.matrix.get(x).set(y, null);
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
