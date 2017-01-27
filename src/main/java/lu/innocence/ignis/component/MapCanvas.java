package lu.innocence.ignis.component;

import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.Map;
import lu.innocence.ignis.engine.TileCell;
import lu.innocence.ignis.engine.Tileset;
import lu.innocence.ignis.event.ActiveMapListener;
import lu.innocence.ignis.event.GUIButtonsUpdate;
import lu.innocence.ignis.event.MapPropertiesUpdated;
import lu.innocence.ignis.event.TilesetSelectionChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fabien Steines
 */
public class MapCanvas extends Canvas implements TilesetSelectionChanged, ActiveMapListener, MapPropertiesUpdated {

    // TOOLS
    public static final int TOOL_PEN = 0x0;
    public static final int TOOL_BRUSH = 0x1;
    public static final int TOOL_FILL = 0x2;
    public static final int TOOL_ERASE = 0x3;
    // LAYERS
    public static final int LAYER_1 = 0x0;
    public static final int LAYER_2 = 0x1;
    public static final int LAYER_3 = 0x2;
    public static final int LAYER_EVENT = 0x3;

    private static final Logger LOGGER = LogManager.getLogger(MapCanvas.class);

    private int lastX = -1;
    private int lastY = -1;
    private Canvas frontCanvas;
    private Pane layerPane;
    private Map map;
    private int activeLayerId;
    private int activeToolId;
    private int tilesetX = 0;
    private int tilesetY = 0;
    private int tilesetWidth = 1;
    private int tilesetHeight = 1;
    private List<GUIButtonsUpdate> guiButtonsUpdate;
    private boolean mouseIsDown;
    private int brushStartX;
    private int brushStartY;

    /**
     * @param width
     * @param height
     */
    public MapCanvas(int width, int height) {
        super(width, height);
        this.guiButtonsUpdate = new ArrayList<>();
    }

    /**
     * @param map
     */
    public void setMap(Map map) {

        if (map != null) {
            map.addMapPropertiesListener(this);
        }

        this.map = map;
        this.applyMapProperties();
    }

    private void applyMapProperties() {
        int w = this.map != null ? this.map.getWidth() * 32 : 0;
        int h = this.map != null ? this.map.getHeight() * 32 : 0;

        this.setWidth(w);
        this.setHeight(h);

        if (this.frontCanvas != null) {
            this.frontCanvas.setWidth(w);
            this.frontCanvas.setHeight(h);
        }
        if (this.layerPane != null) {
            this.layerPane.setPrefSize(w, h);
        }

        this.render();
    }

    /**
     *
     */
    public void render() {
        GraphicsContext g = this.getGraphicsContext2D();
        if (this.map != null) {
            ChessBGDrawer.drawChessBackground(g, this.map.getWidth(), this.map.getHeight(), 32, 32);
            this.map.renderMap(g);

            // Check if we shall draw the Map Grid
            if (this.activeLayerId == MapCanvas.LAYER_EVENT) {

                // Draw the Grid
                for (int i=0;i<this.map.getWidth();i++) {
                    int x = i * this.map.getCellSize();
                    g.strokeLine(x,0,x,this.map.getHeight() * this.map.getCellSize());
                }

                for (int j=0;j<this.map.getHeight();j++) {
                    int y = j * this.map.getCellSize();
                    g.strokeLine(0,y,this.map.getWidth() * this.map.getCellSize(),y);
                }
            }
        }

        //if (this.activeLayerId == )
    }

    /**
     * @param x
     * @param y
     */
    public void renderPartial(int x, int y,boolean renderOthersArround) {

        if (this.map != null) {
            GraphicsContext g = this.getGraphicsContext2D();
            ChessBGDrawer.drawChessBackgroundSingle(g, x, y, 32, 32);
            if (!renderOthersArround) {
                this.map.renderPartialMap(g, x, y);
                return;
            }

            // Render other arround
            for (int i=-1;i<2;i++) {
                for (int j=-1;j<2;j++) {
                    this.map.renderPartialMap(g, x + i, y + j);
                }
            }

        }
    }

    /**
     * @param canvas
     */
    public void linkFrontCanvas(Canvas canvas) {
        this.frontCanvas = canvas;

        // Mouse Press Event
        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            this.handleAction(x, y, MouseEvent.MOUSE_PRESSED);
        });

        // Mouse Release Event
        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_RELEASED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            this.handleAction(x, y, MouseEvent.MOUSE_RELEASED);
        });

        // Mouse Drag Event
        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            if (x != lastX || y != lastY) {
                this.handleAction(x, y, MouseEvent.MOUSE_DRAGGED);
                lastX = x;
                lastY = y;
            }
        });

        // Mouse Move Event
        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_MOVED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            if ((x != lastX || y != lastY) && this.activeLayerId != LAYER_EVENT) {
                this.renderCursor(x, y, false, false);
                lastX = x;
                lastY = y;
            }
        });

        // Mouse Exit Event
        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_EXITED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;

            this.renderCursor(x, y, true, false);

            lastX = x;
            lastY = y;

        });

        // Mouse Enter Event
        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_ENTERED, t -> {

            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            if (this.activeLayerId != LAYER_EVENT) {
                this.renderCursor(x, y, false, false);
            }
            lastX = x;
            lastY = y;

        });


        // Link the Keyboard to the Canvas
        this.frontCanvas.setFocusTraversable(true);
        this.frontCanvas.addEventFilter(MouseEvent.ANY, (e) -> this.frontCanvas.requestFocus());
        this.frontCanvas.setOnKeyPressed(key -> {
            if (this.map != null) {
                switch (key.getCode()) {
                    case DIGIT1:
                        this.setActiveLayerId(LAYER_1, true);
                        break;
                    case DIGIT2:
                        this.setActiveLayerId(LAYER_2, true);
                        break;
                    case DIGIT3:
                        this.setActiveLayerId(LAYER_3, true);
                        break;
                    case DIGIT4:
                        this.setActiveLayerId(LAYER_EVENT, true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param x
     * @param y
     * @param mouseEvent
     */
    private void handleAction(int x, int y, EventType<MouseEvent> mouseEvent) {

        if (mouseEvent == MouseEvent.MOUSE_PRESSED || mouseEvent == MouseEvent.MOUSE_DRAGGED) {
            if (this.activeToolId == TOOL_PEN) {
                this.penAdd(x, y);
                this.renderCursor(x, y, false, false);
            } else if (this.activeToolId == TOOL_ERASE) {
                this.erase(x, y);
                this.renderCursor(x, y, false, false);
            }
        }

        if (this.activeToolId == TOOL_BRUSH) {

            if (mouseEvent == MouseEvent.MOUSE_PRESSED) {
                this.mouseIsDown = true;
                this.brushStartX = x;
                this.brushStartY = y;
                this.renderCursor(x, y, false, false);
            }

            if (mouseEvent == MouseEvent.MOUSE_DRAGGED) {
                this.renderCursor(x, y, false, false);
            }

            if (mouseEvent == MouseEvent.MOUSE_RELEASED && this.mouseIsDown) {

                this.mouseIsDown = false;
                this.renderCursor(x, y, false, true);
                this.brushAdd(x, y);

            }
        }

    }


    /**
     *
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param clearOnly
     * @param mouseUpEvent
     */
    private void renderCursorRegular(GraphicsContext g, int x, int y, int w, int h, boolean clearOnly, boolean mouseUpEvent) {

        if (mouseUpEvent && this.activeToolId == MapCanvas.TOOL_BRUSH) {

            int[] oldCoord = IgnisGlobals.fixCoords(this.brushStartX, this.brushStartY, this.lastX, this.lastY);
            int lastSelectionWidth = oldCoord[2] - oldCoord[0];
            int lastSelectionHeight = oldCoord[3] - oldCoord[1];

            g.clearRect(oldCoord[0] * 32, oldCoord[1] * 32,
                    lastSelectionWidth * 32, lastSelectionHeight * 32);
        } else {
            g.clearRect(lastX * 32, lastY * 32, w, h);
        }

        if (!clearOnly) {

            g.setGlobalAlpha(0.5);
            g.setFill(Color.RED);
            g.fillRect(x * 32, y * 32, w, h);

            // Draw Pen
            for (int i = 0; i < this.tilesetWidth; i++) {
                for (int j = 0; j < this.tilesetHeight; j++) {

                    int yTSCoord = this.tilesetY + j;

                    if (Tileset.isTilesetCell(yTSCoord)) {
                        // Tileset Case
                        this.map.getTileset().drawTileTo(g, x + i,
                                y + j,
                                this.tilesetX + i,
                                yTSCoord - 1);
                    } else {
                        // Terrain Case
                    }
                }
            }

            g.setGlobalAlpha(1.0);
        }
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     */
    private void renderCursorBrushRect(GraphicsContext g, int x, int y) {

        int[] oldCoord = IgnisGlobals.fixCoords(this.brushStartX, this.brushStartY, this.lastX, this.lastY);
        int[] newCoord = IgnisGlobals.fixCoords(this.brushStartX, this.brushStartY, x, y);

        int lastSelectionWidth = oldCoord[2] - oldCoord[0];
        int lastSelectionHeight = oldCoord[3] - oldCoord[1];

        if (lastSelectionWidth < this.tilesetWidth) {
            lastSelectionWidth = this.tilesetWidth;
        }

        if (lastSelectionHeight < this.tilesetHeight) {
            lastSelectionHeight = this.tilesetHeight;
        }

        int selectionWidth = newCoord[2] - newCoord[0];
        int selectionHeight = newCoord[3] - newCoord[1];

        g.clearRect(oldCoord[0] * 32, oldCoord[1] * 32,
                lastSelectionWidth * 32, lastSelectionHeight * 32);

        g.setGlobalAlpha(0.5);
        g.setFill(Color.RED);
        g.fillRect(newCoord[0] * 32, newCoord[1] * 32,
                selectionWidth * 32, selectionHeight * 32);

        int tsX = 0;
        int tsY = 0;

        // Loop over the selection
        for (int i = 0; i < selectionWidth; i++) {
            for (int j = 0; j < selectionHeight; j++) {
                this.renderBrushDecideWhatToDraw(g,tsX,tsY,i,j,newCoord);

                // Reset TSY if it reached the maximum
                tsY = tsY == this.tilesetHeight - 1 ? 0 : tsY + 1;
            }

            // Reset TSX if it reached the maximum
            tsX = tsX == this.tilesetWidth - 1 ? 0 : tsX + 1;
            tsY = 0;

        }

        // Reset the Opacity back to the default value
        g.setGlobalAlpha(1.0);

    }

    /**
     *
     * @param g
     * @param tsX
     * @param tsY
     * @param i
     * @param j
     * @param coords
     */
    private void renderBrushDecideWhatToDraw(GraphicsContext g, int tsX, int tsY, int i, int j, int[] coords) {
        int yTSCoord = this.tilesetY + tsY;

        if (Tileset.isTilesetCell(yTSCoord)) {
            // Tileset Case
            this.map.getTileset().drawTileTo(g, coords[0] + i, coords[1] + j,
                    this.tilesetX + tsX, yTSCoord - 1);
        } else {
            // Terrain Case
        }

    }


    /**
     * @param x
     * @param y
     * @param clearOnly
     */
    private void renderCursor(int x, int y, boolean clearOnly, boolean mouseUpEvent) {

        if (this.map != null && this.map.getTileset() != null) {
            int w = this.tilesetWidth * 32;
            int h = this.tilesetHeight * 32;
            GraphicsContext g = this.frontCanvas.getGraphicsContext2D();

            if (this.activeToolId == TOOL_PEN || (this.activeToolId == MapCanvas.TOOL_BRUSH && !this.mouseIsDown)) {
                this.renderCursorRegular(g,x,y,w,h,clearOnly,mouseUpEvent);
            } else {
                if (this.activeToolId == MapCanvas.TOOL_BRUSH) {
                    this.renderCursorBrushRect(g,x,y);
                }
            }
        }
    }

    /**
     * @param pane
     */
    public void linkLayerPane(Pane pane) {
        this.layerPane = pane;
    }

    /**
     * @param layerIndex
     */
    public void setActiveLayerId(int layerIndex) {
        this.setActiveLayerId(layerIndex, false);
    }

    /**
     * @param layerIndex
     * @param fireUpdate
     */
    public void setActiveLayerId(int layerIndex, boolean fireUpdate) {
        if (this.map != null && layerIndex != this.activeLayerId) {
            this.activeLayerId = layerIndex;
            this.map.setActiveLayerIndex(layerIndex);
            if (fireUpdate)
                this.fireUpdateLayerButtons();
            this.render();
        }
    }

    /**
     * @param listener
     */
    public void addGUIButtonsListener(GUIButtonsUpdate listener) {
        if (!this.guiButtonsUpdate.contains(listener)) {
            this.guiButtonsUpdate.add(listener);
        }
    }

    /**
     *
     */
    private void fireUpdateToolButtons() {
        for (GUIButtonsUpdate current : this.guiButtonsUpdate) {
            current.activeToolChanged(this.activeToolId);
        }
    }

    /**
     *
     */
    private void fireUpdateLayerButtons() {
        for (GUIButtonsUpdate current : this.guiButtonsUpdate) {
            current.activeLayerChanged(this.activeLayerId);
        }
    }

    /**
     * @param startX
     * @param startY
     * @param width
     * @param height
     */
    @Override
    public void tilesetSelectionChanged(int startX, int startY, int width, int height) {
        this.tilesetX = startX;
        this.tilesetY = startY;
        this.tilesetWidth = width;
        this.tilesetHeight = height;
    }

    /**
     * @param map
     */
    @Override
    public void activeMapChanged(Map map) {
        this.setMap(map);
    }

    /**
     * @param activeTooldId
     */
    public void setActiveToolId(int activeTooldId) {
        this.activeToolId = activeTooldId;
    }


    /**
     * @param x
     * @param y
     */
    private void penAdd(int x, int y) {
        if (this.map != null && this.map.getTileset() != null) {
            for (int i = 0; i < this.tilesetWidth; i++) {
                for (int j = 0; j < this.tilesetHeight; j++) {

                    TileCell cell = this.map.addTile(this.activeLayerId, x + i, y + j,
                            this.tilesetX + i, this.tilesetY + j);

                    this.renderPartial(x + i, y + j,
                            !Tileset.isTilesetCell(this.tilesetY + j) ||
                                    (cell != null && !Tileset.isTilesetCell(cell.getTsY())));
                }
            }
        }
    }

    /**
     *
     */
    private void brushAdd(int x, int y) {

        if (this.map != null && this.map.getTileset() != null) {
            int tsX = 0;
            int tsY = 0;

            int[] newCoord = IgnisGlobals.fixCoords(this.brushStartX, this.brushStartY, x, y);
            int selectionWidth = newCoord[2] - newCoord[0];
            int selectionHeight = newCoord[3] - newCoord[1];

            for (int i = 0; i < selectionWidth; i++) {
                for (int j = 0; j < selectionHeight; j++) {

                    TileCell cell = this.map.addTile(this.activeLayerId, newCoord[0] + i, newCoord[1] + j,
                            this.tilesetX + tsX, this.tilesetY + tsY);

                    this.renderPartial(newCoord[0] + i, newCoord[1] + j,
                            !Tileset.isTilesetCell(this.tilesetY + tsY) ||
                                    (cell != null && !Tileset.isTilesetCell(cell.getTsY())));
                    tsY += 1;

                    if (tsY == this.tilesetHeight) {
                        tsY = 0;
                    }
                }

                tsX += 1;
                tsY = 0;

                if (tsX == this.tilesetWidth) {
                    tsX = 0;
                }
            }
        }
    }

    /**
     * @param x
     * @param y
     */
    private void erase(int x, int y) {
        if (this.map != null) {
            TileCell cell = this.map.removeCell(this.activeLayerId, x, y);
            this.renderPartial(x, y,cell != null && !Tileset.isTilesetCell(cell.getTsY()));
        }
    }


    @Override
    public void mapPropertiesUpdated() {
        this.applyMapProperties();
    }

}
