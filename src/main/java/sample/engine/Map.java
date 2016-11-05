package sample.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class Map {

    private List<TilesetLayer> layers;
    private int width;
    private int height;
    private Tileset tileset;
    private int activeLayerIndex;

    public Map() {
        this.activeLayerIndex = 0;
        this.layers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            this.layers.add(new TilesetLayer());
        }
    }

    public void setDimension(int x, int y) {
        this.width = x;
        this.height = y;
        for (TilesetLayer layer : this.layers) {
            layer.setDimension(x, y);
        }
    }

    private void simulateLayerRender(GraphicsContext g, int index) {

        if (this.activeLayerIndex == index && index != 0) {
            g.setGlobalAlpha(0.5);
            g.setFill(Color.BLACK);
            g.fillRect(0, 0, width * 32, height * 32);
            g.setGlobalAlpha(1.0);
        } else {
            if (this.activeLayerIndex < index) {
                g.setGlobalAlpha(0.5);

            }
        }

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {


                switch (index) {
                    case 0:
                        this.tileset.drawTileTo(g, i, j, 0, 0);
                        break;
                    case 1:
                        this.tileset.drawTileTo(g, i, j, 0, 2);
                        break;
                    case 2:
                        this.tileset.drawTileTo(g, i, j, 0, 3);
                        break;
                }

            }
        }

        g.setGlobalAlpha(1.0);
    }

    public void renderMap(GraphicsContext g) {
        simulateLayerRender(g, 0);
        simulateLayerRender(g, 1);
        simulateLayerRender(g, 2);
    }

    public void renderPartialMap(GraphicsContext g, int x, int y) {
        g.clearRect(x * 32, y * 32, 32, 32);
        this.tileset.drawTileTo(g, x, y, 1, 1);
    }

    public void addCell(int layerIndex, int x, int y, int tsX, int tsY) {
        this.layers.get(layerIndex).addCell(x, y, tsX, tsY);
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }

    public Tileset getTileset() {
        return this.tileset;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setActiveLayerIndex(int activeLayerIndex) {
        this.activeLayerIndex = activeLayerIndex;
    }
}
