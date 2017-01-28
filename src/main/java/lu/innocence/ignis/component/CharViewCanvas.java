package lu.innocence.ignis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Fabien Steines
 * Last Update on: 28.01.2017.
 */
public class CharViewCanvas extends Canvas {

    public CharViewCanvas() {
        this.render();
    }

    public void render() {
        GraphicsContext g = this.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setStroke(Color.LIGHTGRAY);
        g.strokeRect(1,1,getWidth()-1,getHeight()-1);
    }

}
