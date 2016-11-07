package sample.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.IgnisGlobals;
import sample.engine.Map;
import sample.engine.Tileset;
import sample.event.ActiveMapListener;
import sample.event.TilesetSelectionChanged;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetCanvas extends Canvas implements ActiveMapListener {

    private final int cellSize;
    private Tileset linkedTileset;
    private int lastX;
    private int lastY;

    private int mouseStartX;
    private int mouseStartY;
    private int mouseEndX;
    private int mouseEndY;

    private List<TilesetSelectionChanged> tilesetSelectionListener;



    public TilesetCanvas() {

        this.lastX = -1;
        this.lastY = -1;
        this.cellSize = 32;

        this.tilesetSelectionListener = new ArrayList<>();

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, t -> {

            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;

            this.mouseStartX = x;
            this.mouseStartY = y;
            this.mouseEndX = x;
            this.mouseEndY = y;
            lastX = x;
            lastY = y;

            render();
        });

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, t -> {

            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;

            if (x != lastX || y != lastY) {
                this.render();

                this.mouseEndX = x;
                this.mouseEndY = y;
                lastX = x;
                lastY = y;

                render();
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_RELEASED,t -> {
            fireUpdate();
        });
    }

    public void render() {
        GraphicsContext g = this.getGraphicsContext2D();
        g.clearRect(0,0,this.getWidth(),this.getHeight());
        if (this.linkedTileset != null) {

            this.drawChessBackground(g);

            g.drawImage(this.linkedTileset.getTilesetImage(),0,0);

            int[] coords = IgnisGlobals.fixCoords(this.mouseStartX,this.mouseStartY,this.mouseEndX,this.mouseEndY);

            g.setFill(Color.RED);
            g.setGlobalAlpha(0.5);
            g.fillRect(coords[0] * this.cellSize,coords[1]  * this.cellSize,
                    (coords[2] - coords[0])  * this.cellSize,(coords[3] - coords[1]) * this.cellSize);
            g.setGlobalAlpha(1);

        }
    }

    private void drawChessBackground(GraphicsContext g) {
        for (int i = 0; i < this.getWidth() / 16; i++) {
            for (int j = 0; j < this.getHeight() / 16; j++) {

                if ((i + j) % 2 == 0) {
                    g.setFill(Color.WHITE);
                } else {
                    g.setFill(Color.LIGHTGRAY);
                }

                g.fillRect(i * 16, j * 16, 16, 16);
            }
        }
    }

    public void setTileset(Tileset tileset) {
        if (tileset != null) {
            this.setWidth(tileset.getTilesetImage().getWidth());
            this.setHeight(tileset.getTilesetImage().getHeight());
            this.linkedTileset =  tileset;
        } else {
            this.linkedTileset = null;
            this.setWidth(0);
            this.setHeight(0);
        }
        this.render();
    }

    public void addSelecitonListener(TilesetSelectionChanged listener) {
        if (!this.tilesetSelectionListener.contains(listener)) {
            this.tilesetSelectionListener.add(listener);
        }
    }

    private void fireUpdate() {

        int[] coords = IgnisGlobals.fixCoords(this.mouseStartX,this.mouseStartY,this.mouseEndX,this.mouseEndY);
        int w = coords[2] - coords[0];
        int h = coords[3] - coords[1];

        for (TilesetSelectionChanged listener : this.tilesetSelectionListener) {
            listener.tilesetSelectionChanged(coords[0],coords[1],w,h);
        }
    }

    @Override
    public void activeMapChanged(Map map) {
        this.setTileset(map.getTileset() != null ? map.getTileset() : null);
    }
}
