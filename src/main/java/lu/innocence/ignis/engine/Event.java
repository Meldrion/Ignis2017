package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class Event {

    private int xPos;
    private int yPos;
    private String name;

    public Event(int x,int y) {
        this.setPosition(x,y);
    }

    public void setPosition(int x,int y) {
        this.xPos = x;
        this.yPos = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void render(GraphicsContext gc) {
        gc.fillRect(this.xPos,this.yPos,32,32);
    }
}
