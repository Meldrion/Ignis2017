package lu.innocence.ignis.view.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.Map;
import lu.innocence.ignis.engine.Terrain;
import lu.innocence.ignis.engine.Tileset;
import lu.innocence.ignis.event.ActiveMapListener;
import lu.innocence.ignis.event.MapPropertiesUpdated;
import lu.innocence.ignis.event.TilesetSelectionChanged;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetCanvas extends Canvas implements ActiveMapListener , MapPropertiesUpdated {

    private final int cellSize;
    private Tileset linkedTileset;
    private int lastX;
    private int lastY;

    private int mouseStartX;
    private int mouseStartY;
    private int mouseEndX;
    private int mouseEndY;

    private int containerWidth;
    private int containerHeight;

    private List<TilesetSelectionChanged> tilesetSelectionListener;

    /**
     *
     */
    public TilesetCanvas() {

        this.containerWidth = 0;
        this.containerHeight = 0;
        this.lastX = -1;
        this.lastY = -1;
        this.cellSize = 32;

        this.tilesetSelectionListener = new ArrayList<>();

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, t -> {

            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;

            this.mouseStartX = x;
            this.mouseStartY = y;
            this.mouseEndX = x;
            this.mouseEndY = y;
            lastX = x;
            lastY = y;

            render();
        });

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, t -> {

            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;

            if (x != lastX || y != lastY) {
                this.render();

                this.mouseEndX = x;
                this.mouseEndY = y;
                lastX = x;
                lastY = y;

                render();
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_RELEASED, t -> fireUpdate());
    }

    /**
     *
     */
    public void render() {
        GraphicsContext g = this.getGraphicsContext2D();

        if (this.linkedTileset != null) {

            ChessBGDrawer.drawChessBackground(g, (int) this.getWidth() / 32,
                    (int) this.getHeight() / 32, 32, 32);

            for (int i=0;i<8;i++) {
                Terrain cTerrain = this.linkedTileset.getTerrain(i);
                if (cTerrain != null)
                    cTerrain.draw(g,i,0,0);
            }

            g.drawImage(this.linkedTileset.getTilesetImage(), 0, cellSize);

            int[] coords = IgnisGlobals.fixCoords(this.mouseStartX, this.mouseStartY, this.mouseEndX, this.mouseEndY);

            g.setFill(Color.RED);
            g.setGlobalAlpha(0.5);
            g.fillRect(coords[0] * this.cellSize, coords[1] * this.cellSize,
                    (coords[2] - coords[0]) * this.cellSize, (coords[3] - coords[1]) * this.cellSize);
            g.setGlobalAlpha(1);

        } else {
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    /**
     *
     * @param tileset
     */
    public void setTileset(Tileset tileset) {
        if (tileset != null && tileset.getTilesetImage() != null) {
            this.setWidth(tileset.getTilesetImage().getWidth());
            this.setHeight(tileset.getTilesetImage().getHeight());
            this.linkedTileset = tileset;
            this.fitToContainer(this.containerWidth, this.containerHeight + cellSize);
        } else {
            this.linkedTileset = null;
            this.setWidth(0);
            this.setHeight(0);
            this.render();
        }
    }

    /**
     *
     * @param listener
     */
    public void addSelecitonListener(TilesetSelectionChanged listener) {
        if (!this.tilesetSelectionListener.contains(listener)) {
            this.tilesetSelectionListener.add(listener);
        }
    }

    /**
     *
     */
    private void fireUpdate() {

        int[] coords = IgnisGlobals.fixCoords(this.mouseStartX, this.mouseStartY, this.mouseEndX, this.mouseEndY);
        int w = coords[2] - coords[0];
        int h = coords[3] - coords[1];

        for (TilesetSelectionChanged listener : this.tilesetSelectionListener) {
            listener.tilesetSelectionChanged(coords[0], coords[1], w, h);
        }
    }

    /**
     *
     * @param width
     * @param height
     */
    private void fitToContainer(int width, int height) {
        if (this.linkedTileset != null) {

            if (this.linkedTileset.getTilesetImage().getHeight() < height) {
                this.setHeight(height);
            } else {
                this.setHeight(this.linkedTileset.getTilesetImage().getHeight());
            }

            if (this.linkedTileset.getTilesetImage().getWidth() < width) {
                this.setWidth(width);
            } else {
                this.setWidth(this.linkedTileset.getTilesetImage().getWidth());
            }
        }

        this.render();
    }

    /**
     *
     * @param width
     * @param height
     */
    public void containerSizeChanged(int width, int height) {
        this.containerWidth = width;
        this.containerHeight = height;
        this.fitToContainer(width, height);
    }

    /**
     * @param map the current map
     *            Can be null
     */
    @Override
    public void activeMapChanged(Map map) {

        if (map != null) {
            map.addMapPropertiesListener(this);
            this.setTileset(map.getTileset());
        } else {
            this.setTileset(null);
        }

    }

    /**
     *
     * @param map the current map
     *            Can be null
     */
    @Override
    public void mapPropertiesUpdated(Map map) {
        activeMapChanged(map);
    }
}
