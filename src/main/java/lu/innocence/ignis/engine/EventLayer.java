package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * @return
     */
    public Event handleEvent(int x,int y) {
        return this.getFrom(x,y) == null ? new Event(x,y) : this.getFrom(x,y);
    }


}
