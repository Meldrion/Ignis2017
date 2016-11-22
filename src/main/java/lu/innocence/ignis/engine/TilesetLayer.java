package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetLayer {

    private final int cellSize = 32;
    private List<List<TilesetCell>> matrix;
    private int width;
    private int height;

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

            List<TilesetCell> innerMatrix = new ArrayList<>();
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
        this.matrix.get(x).set(y, new TilesetCell(x, y, tsX, tsY));
    }

    /**
     * @param g
     * @param tileset
     */
    public void render(GraphicsContext g, Tileset tileset) {

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                TilesetCell cell = this.matrix.get(i).get(j);
                if (cell != null) {
                    tileset.drawTileTo(g, i, j, cell.tsX, cell.tsY);
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
        TilesetCell cell = this.matrix.get(x).get(y);
        if (cell != null) {
            tileset.drawTileTo(g, x, y, cell.tsX, cell.tsY);
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
                TilesetCell cell = this.matrix.get(i).get(j);
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
