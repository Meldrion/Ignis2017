package lu.innocence.ignis.component;

import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lu.innocence.ignis.engine.Map;
import lu.innocence.ignis.event.ActiveMapListener;
import lu.innocence.ignis.event.GUIButtonsUpdate;
import lu.innocence.ignis.event.TilesetSelectionChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fabien Steines
 */
public class MapCanvas extends Canvas implements TilesetSelectionChanged, ActiveMapListener {

    public static final int TOOL_PEN = 0x0;
    public static final int TOOL_BRUSH = 0x1;
    public static final int TOOL_FILL = 0x2;
    public static final int TOOL_ERASE = 0x3;
    public static final int LAYER_1 = 0x4;
    public static final int LAYER_2 = 0x5;
    public static final int LAYER_3 = 0x6;
    public static final int LAYER_EVENT = 0x7;

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
        this.map = map;

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
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        if (this.map != null)
            this.map.renderMap(g);
    }

    /**
     * @param x
     * @param y
     */
    public void renderPartial(int x, int y) {
        if (this.map != null) {
            this.map.renderPartialMap(this.getGraphicsContext2D(), x, y);
        }
    }

    /**
     * @param canvas
     */
    public void linkFrontCanvas(Canvas canvas) {
        this.frontCanvas = canvas;

        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_PRESSED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            this.handleAction(x, y, MouseEvent.MOUSE_PRESSED);
        });

        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            if (x != lastX || y != lastY) {
                this.handleAction(x, y, MouseEvent.MOUSE_DRAGGED);
                lastX = x;
                lastY = y;
            }
        });

        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_MOVED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            if (x != lastX || y != lastY) {
                this.renderCursor(x, y, false);
                lastX = x;
                lastY = y;
            }
        });

        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_EXITED, t -> {
            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;

            this.renderCursor(x, y, true);

            lastX = x;
            lastY = y;

        });

        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_ENTERED, t -> {

            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;
            this.renderCursor(x, y, false);
            lastX = x;
            lastY = y;

        });

        this.frontCanvas.setFocusTraversable(true);
        this.frontCanvas.addEventFilter(MouseEvent.ANY, (e) -> this.frontCanvas.requestFocus());
        this.frontCanvas.setOnKeyPressed(key -> {
            if (this.map != null) {
                switch (key.getCode()) {
                    case DIGIT1:
                        this.setActiveLayerId(0, true);
                        break;
                    case DIGIT2:
                        this.setActiveLayerId(1, true);
                        break;
                    case DIGIT3:
                        this.setActiveLayerId(2, true);
                        break;
                    case DIGIT4:
                        this.setActiveLayerId(3, true);
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
                this.renderCursor(x, y, false);
            } else if (this.activeToolId == TOOL_ERASE) {
                this.erase(x, y);
                this.renderCursor(x, y, false);
            }
        }

        if (this.activeToolId == TOOL_BRUSH) {

            if (mouseEvent == MouseEvent.MOUSE_PRESSED) {
                this.mouseIsDown = true;
                this.brushStartX = x;
                this.brushStartY = y;
                this.renderCursor(x, y, false);
            }

            if (mouseEvent == MouseEvent.MOUSE_DRAGGED) {
                this.renderCursor(x, y, false);
            }

            if (mouseEvent == MouseEvent.MOUSE_RELEASED) {

                if (this.mouseIsDown) {
                    this.mouseIsDown = false;
                }

            }
        }

    }


    /**
     * @param x
     * @param y
     * @param clearOnly
     */
    private void renderCursor(int x, int y, boolean clearOnly) {

        if (this.map != null) {
            int w = this.tilesetWidth * 32;
            int h = this.tilesetHeight * 32;
            GraphicsContext g = this.frontCanvas.getGraphicsContext2D();

            if (this.activeToolId == TOOL_PEN || (this.activeToolId == MapCanvas.TOOL_BRUSH && !this.mouseIsDown)) {

                g.clearRect(lastX * 32, lastY * 32, w, h);

                if (!clearOnly) {
                    g.setGlobalAlpha(0.5);
                    g.setFill(Color.RED);
                    g.fillRect(x * 32, y * 32, w, h);
                    g.drawImage(this.map.getTileset().getTilesetImage(), this.tilesetX * 32, this.tilesetY * 32,
                            w, h, x * 32, y * 32, w, h);
                    g.setGlobalAlpha(1.0);
                }

            } else {

                if (this.activeToolId == MapCanvas.TOOL_BRUSH) {

                    int selectionWidth = this.brushStartX - x;
                    int selectionHeight = this.brushStartY - y;

                    g.clearRect(this.brushStartX * 32, this.brushStartY * 32,
                            selectionWidth * 32, selectionHeight * 32);

                    g.setGlobalAlpha(0.5);
                    g.setFill(Color.RED);
                    g.fillRect(this.brushStartX * 32, this.brushStartY * 32,
                            selectionWidth * 32, selectionHeight * 32);

                    int tsX = 0;
                    int tsY = 0;

                    for (int i = 0; i < selectionWidth; i++) {
                        for (int j = 0; j < selectionHeight; j++) {

                            this.map.getTileset().drawTileTo(g, this.brushStartX + i, this.brushStartY + j,
                                    this.tilesetX + tsX, this.tilesetY + tsY);
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

                    g.setGlobalAlpha(1.0);

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
        if (this.map != null) {
            for (int i = 0; i < this.tilesetWidth; i++) {
                for (int j = 0; j < this.tilesetHeight; j++) {

                    this.map.addCell(this.activeLayerId, x + i, y + j,
                            this.tilesetX + i, this.tilesetY + j);
                    this.renderPartial(x + i, y + j);
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
            this.map.removeCell(this.activeLayerId, x, y);
            this.renderPartial(x, y);
        }
    }
}
