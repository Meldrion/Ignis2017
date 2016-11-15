package lu.innocence.ignis.view.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lu.innocence.ignis.component.ChessBGDrawer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Fabien Steines
 */
public class ResourceCanvas extends Canvas {

    private static final Logger LOGGER = LogManager.getLogger(Canvas.class);
    private Image linkedImage;


    private void renderBorder(GraphicsContext g) {
        g.setStroke(Color.LIGHTGRAY);
        g.setLineWidth(2);
        g.strokeRect(0,0,getWidth(),getHeight());
    }

    public void render() {

        GraphicsContext g = this.getGraphicsContext2D();

        ChessBGDrawer.drawChessBackground(g,(int)this.getWidth()/32,(int)this.getHeight()/32,32,32);
        renderBorder(g);
        if (this.linkedImage != null) {
            g.drawImage(linkedImage,0,0);
        }

    }

}
