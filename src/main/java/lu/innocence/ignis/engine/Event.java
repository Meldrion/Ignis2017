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
public class Event {

    private static final Logger LOGGER = LogManager.getLogger(Event.class);
    private int xPos;
    private int yPos;
    private String name;

    Event(int x, int y) {
        this.setPosition(x,y);
        LOGGER.info("Created Event at x: {} y: {} ",x,y);
    }

    /**
     *
     * @return
     */
    public JSONObject save() {
        JSONObject event = new JSONObject();
        //noinspection unchecked
        event.put("name", name);
        //noinspection unchecked
        event.put("x", xPos);
        //noinspection unchecked
        event.put("y", yPos);
        return event;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setPosition(int x,int y) {
        this.xPos = x;
        this.yPos = y;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param gc
     */
    public void render(GraphicsContext gc) {
        gc.fillRect(this.xPos * 32 + 3,this.yPos * 32 + 3,
                    32 - 6,32 - 6 );
    }
}
