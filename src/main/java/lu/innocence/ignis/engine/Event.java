package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
public class Event {

    private static final Logger LOGGER = LogManager.getLogger(Event.class);
    private int xPos;
    private int yPos;
    private String name;

    private List<EventPage> eventPages;

    Event(int x, int y) {
        this.eventPages = new ArrayList<>();
        this.setPosition(x,y);
        this.eventPages.add(new EventPage());
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

        JSONArray pagesJSON = new JSONArray();
        for (EventPage page : this.eventPages) {
            //noinspection unchecked
            pagesJSON.add(page.save());
        }

        //noinspection unchecked
        event.put("pages",pagesJSON);

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
        int x = this.xPos * 32 + 3;
        int y = this.yPos * 32 + 3;

        int cW = 32 - 6;
        int cH = 32 - 6;

        gc.fillRect(x,y, cW,cH);

        if (!this.eventPages.isEmpty() && this.eventPages.get(0).getSpriteImage() != null) {
            Image spriteImage = this.eventPages.get(0).getSpriteImage();
            int w = (int) spriteImage.getWidth() / 4;
            int h = (int) spriteImage.getHeight() / 4;
            gc.drawImage(spriteImage,0,0,w,h,x,y,cW,cH);
        }
    }

    /**
     *
     * @param eventPages
     */
    public void setEventPages(List<EventPage> eventPages) {
        this.eventPages.clear();
        this.eventPages.addAll(eventPages);
    }

    /**
     *
     * @return
     */
    public List<EventPage> getEventPages() {
        return this.eventPages;
    }


}
