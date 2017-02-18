package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class EventLayer extends Layer<Event> {

    private static final Logger LOGGER = LogManager.getLogger(EventLayer.class);

    /**
     *
     */
    public EventLayer() {
        super();
        LOGGER.info("Created Event Layer");
    }

    @Override
    JSONObject save(Event event) {
        return event.save();
    }

    /**
     *
     * @param g
     */
    public void render(GraphicsContext g) {
        for (int x = 0;x < this.getMatrix().size();x++) {
            for (int y = 0;y < this.getMatrix().get(x).size();y++) {
                Event event = this.getFrom(x,y);
                if (event != null) {
                    event.render(g);
                }
            }
        }
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     */
    void renderPartial(GraphicsContext g, int x, int y) {
        if (this.isInRange(x,y)) {
            Event event = this.getFrom(x,y);
            if (event != null) {
                event.render(g);
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Event handleEvent(int x,int y) {
        return this.getFrom(x,y) == null ? new Event(x,y) : this.getFrom(x,y);
    }



}
