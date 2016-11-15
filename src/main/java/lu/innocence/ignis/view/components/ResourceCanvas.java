package lu.innocence.ignis.view.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lu.innocence.ignis.component.ChessBGDrawer;

/**
 * @author Fabien Steines
 */
public class ResourceCanvas extends Canvas {

    private Image linkedImage;

    public void render() {

        GraphicsContext g = this.getGraphicsContext2D();
        ChessBGDrawer.drawChessBackground(g,(int)this.getWidth()/32,(int)this.getHeight()/32,32,32);

        if (this.linkedImage != null) {
            g.drawImage(linkedImage,0,0);
        }

    }

}
