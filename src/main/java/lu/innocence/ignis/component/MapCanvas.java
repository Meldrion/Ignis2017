package lu.innocence.ignis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lu.innocence.ignis.event.ActiveMapListener;
import lu.innocence.ignis.event.GUIButtonsUpdate;
import lu.innocence.ignis.event.TilesetSelectionChanged;
import lu.innocence.ignis.engine.Map;

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

    int lastX = -1;
    int lastY = -1;

    private Canvas frontCanvas;
    private Pane layerPane;

    private Map map;
    private int activeLayerId;
    private int activeToolId;

    private int tilesetX = 0;
    private int tilesetY = 0;
    private int tilesetWidth = 1;
    private int tilesetHeight = 1;

    private List<GUIButtonsUpdate> guiButtonsUpdate;;


    public MapCanvas(int width,int height) {
        super(width,height);
        this.guiButtonsUpdate = new ArrayList<>();
    }

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
            this.layerPane.setPrefSize(w,h);
        }

        this.render();
    }

    public void render() {
        GraphicsContext g = this.getGraphicsContext2D();
        g.clearRect(0,0,this.getWidth(),this.getHeight());
        if (this.map != null)
            this.map.renderMap(g);
    }

    public void renderPartial(int x,int y) {
        this.map.renderPartialMap(this.getGraphicsContext2D(),x,y);
    }

    /**
     *
     * @param canvas
     */
    public void linkFrontCanvas(Canvas canvas) {
        this.frontCanvas = canvas;

        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->
                this.renderPartial((int)t.getX()/32,(int)t.getY()/32));

        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, t -> {

            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;

            if (x != lastX || y != lastY) {
                this.renderPartial(x, y);
                lastX = x;
                lastY = y;
            }

        });

        this.frontCanvas.addEventHandler(MouseEvent.MOUSE_MOVED,t -> {
            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;
            if (x != lastX || y != lastY) {
                this.renderCursor(x,y,false);
                lastX = x;
                lastY = y;
            }
        });

        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_EXITED,t -> {
            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;

            this.renderCursor(x,y,true);

            lastX = x;
            lastY = y;

        });

        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_ENTERED,t -> {

            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;
            this.renderCursor(x,y,false);
            lastX = x;
            lastY = y;

        });

        this.frontCanvas.setFocusTraversable(true);
        this.frontCanvas.addEventFilter(MouseEvent.ANY, (e) -> this.frontCanvas.requestFocus());
        this.frontCanvas.setOnKeyPressed(key -> {
            if (this.map != null) {
                switch (key.getCode()) {
                    case DIGIT1:
                        this.setActiveLayerId(0,true);
                        break;
                    case DIGIT2:
                        this.setActiveLayerId(1,true);
                        break;
                    case DIGIT3:
                        this.setActiveLayerId(2,true);
                        break;
                    case DIGIT4:
                        this.setActiveLayerId(3,true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void renderCursor(int x,int y,boolean clearOnly) {

        if (this.map != null) {
            int w = this.tilesetWidth * 32;
            int h = this.tilesetHeight * 32;
            GraphicsContext g = this.frontCanvas.getGraphicsContext2D();
            g.clearRect(lastX * 32, lastY * 32, w, h);

            if (!clearOnly) {
                g.setGlobalAlpha(0.5);
                g.setFill(Color.RED);
                g.fillRect(x * 32, y * 32, w, h);
                g.drawImage(this.map.getTileset().getTilesetImage(), this.tilesetX * 32, this.tilesetY * 32,
                        w, h, x * 32, y * 32, w, h);
                g.setGlobalAlpha(1.0);
            }
        }

    }

    public void linkLayerPane(Pane pane) {
        this.layerPane = pane;
    }

    public void setActiveLayerId(int layerIndex) {
        this.setActiveLayerId(layerIndex,false);
    }

    public void setActiveLayerId(int layerIndex,boolean fireUpdate) {
        if (this.map != null && layerIndex != this.activeLayerId) {
            this.activeLayerId = layerIndex;
            this.map.setActiveLayerIndex(layerIndex);
            if (fireUpdate)
                this.fireUpdateLayerButtons();
            this.render();
        }
    }

    public void addGUIButtonsListener(GUIButtonsUpdate listener) {
        if (!this.guiButtonsUpdate.contains(listener)) {
            this.guiButtonsUpdate.add(listener);
        }
    }

    private void fireUpdateToolButtons() {
        for (GUIButtonsUpdate current : this.guiButtonsUpdate) {
            current.activeToolChanged(this.activeToolId);
        }
    }

    private void fireUpdateLayerButtons() {
        for (GUIButtonsUpdate current : this.guiButtonsUpdate) {
            current.activeLayerChanged(this.activeLayerId);
        }
    }


    @Override
    public void tilesetSelectionChanged(int startX, int startY, int width, int height) {
        this.tilesetX = startX;
        this.tilesetY = startY;
        this.tilesetWidth = width;
        this.tilesetHeight = height;
    }

    @Override
    public void activeMapChanged(Map map) {
        this.setMap(map);
    }

    public void setActiveToolId(int activeTooldId) {
        this.activeToolId = activeTooldId;
    }
}
