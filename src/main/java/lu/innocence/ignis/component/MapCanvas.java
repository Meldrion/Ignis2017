package lu.innocence.ignis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lu.innocence.ignis.event.ActiveMapListener;
import lu.innocence.ignis.event.TilesetSelectionChanged;
import lu.innocence.ignis.engine.Map;


/**
 * @author Fabien Steines
 */
public class MapCanvas extends Canvas implements TilesetSelectionChanged, ActiveMapListener {

    int lastX = -1;
    int lastY = -1;

    private Canvas frontCanvas;
    private Pane layerPane;

    private Map map;
    private int activeLayerId;

    private int tilesetX = 0;
    private int tilesetY = 0;
    private int tilesetWidth = 1;
    private int tilesetHeight = 1;


    public MapCanvas(int width,int height) {
        super(width,height);
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
        this.frontCanvas.setOnKeyPressed((key) -> {

            if (this.map != null) {
                switch (key.getCode()) {
                    case DIGIT1:
                        this.map.setActiveLayerIndex(0);
                        this.render();
                        break;
                    case DIGIT2:
                        this.map.setActiveLayerIndex(1);
                        this.render();
                        break;
                    case DIGIT3:
                        this.map.setActiveLayerIndex(2);
                        this.render();
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
        if (this.map != null) {
            this.map.setActiveLayerIndex(layerIndex);
            this.render();
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
}
