package sample.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


/**
 * @author Fabien Steines
 */
public class MapCanvas extends Canvas {

    int lastX = -1;
    int lastY = -1;

    private Canvas frontCanvas;
    private Pane layerPane;

    private Map map;

    public MapCanvas(int width,int height) {
        super(width,height);
    }

    public void setMap(Map map) {
        this.map = map;
        this.setWidth(map.getWidth() * 32);
        this.setHeight(map.getHeight() * 32);

        if (this.frontCanvas != null) {
            this.frontCanvas.setWidth(map.getWidth() * 32);
            this.frontCanvas.setHeight(map.getHeight() * 32);
        }
        if (this.layerPane != null) {
            this.layerPane.setPrefSize(map.getWidth() * 32,map.getHeight() * 32);
        }
    }

    public void render() {
        this.map.renderMap(this.getGraphicsContext2D());
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
            lastX = x;
            lastY = y;

            this.renderCursor(x,y,true);

        });

        this.frontCanvas.addEventFilter(MouseEvent.MOUSE_ENTERED,t -> {

            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;
            this.renderCursor(x,y,false);
            lastX = x;
            lastY = y;

        });
    }

    private void renderCursor(int x,int y,boolean clearOnly) {

        int w = 5 * 32;
        GraphicsContext g = this.frontCanvas.getGraphicsContext2D();
        g.clearRect(lastX * 32,lastY * 32,w,w);

        if (!clearOnly) {
            g.setGlobalAlpha(0.5);
            g.setFill(Color.RED);
            g.fillRect(x * 32, y * 32, w, w);
            g.drawImage(this.map.getTileset().getTilesetImage(), 0, 0, w, w, x * 32, y * 32, w, w);
            g.setGlobalAlpha(1.0);
        }

    }

    public void linkLayerPane(Pane pane) {
        this.layerPane = pane;
    }




}
