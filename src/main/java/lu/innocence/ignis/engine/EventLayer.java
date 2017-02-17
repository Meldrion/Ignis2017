package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class EventLayer extends Layer{


    private List<List<Event>> matrix;
    private int height;
    private int width;

    /**
     *
     */
    public EventLayer() {
        this.width = 0;
        this.height = 0;
    }



    /**
     *
     * @return
     */
    private boolean isInsideRange(int x,int y) {
        return 0 <= x && x < this.matrix.size() && 0<= y && y < this.matrix.get(0).size();
    }

    /**
     *
     * @param gc
     */
    public void render(GraphicsContext gc) {

    }

    /**
     *
     * @param x
     * @param y
     * @param event
     * @return
     */
    public boolean setEventAt(int x,int y,Event event) {
        if (this.getEventAt(x,y) != null) {
            return false;
        }

        this.matrix.get(x).set(y,event);
        return true;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Event getEventAt(int x,int y) {
        return this.isInsideRange(x,y) ? this.matrix.get(x).get(y) : null;
    }

    /**
     *
     * @param name
     * @param x
     * @param y
     * @return
     */
    public Event handleEvent(String name,int x,int y) {
        return this.getEventAt(x,y) == null ? new Event(x,y,name) : this.getEventAt(x,y);
    }

    /**
     *
     */
    public void clearLayer() {
        this.matrix.clear();
    }

}
