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


    public void setImage(Image image) {
        this.linkedImage = image;
        render();
    }

    private void renderBorder(GraphicsContext g) {
        g.setStroke(Color.LIGHTGRAY);
        g.setLineWidth(2);
        g.strokeRect(0, 0, getWidth(), getHeight());
    }

    public void render() {

        GraphicsContext g = this.getGraphicsContext2D();

        ChessBGDrawer.drawChessBackground(g, (int) this.getWidth() / 32, (int) this.getHeight() / 32, 32, 32);
        renderBorder(g);
        if (this.linkedImage != null) {


            int w1 = (int) this.getWidth();
            int h1 = (int) this.getHeight();

            int w2 = (int) this.linkedImage.getWidth();
            int h2 = (int) this.linkedImage.getHeight();


            float aspect;

            if (w2 < h2) {
                aspect = h2 / (float) (h1 - 30);
            } else {
                aspect = w2 / (float) w1;
            }

            int x = (int) (w1 - w2 / aspect) / 2;
            int y = (int) (h1 - h2 / aspect) / 2;

            g.drawImage(linkedImage, x, y, w2 / aspect, h2 / aspect);
        }

    }

}
