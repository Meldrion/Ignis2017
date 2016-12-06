package lu.innocence.ignis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.Terrain;
import lu.innocence.ignis.engine.Tileset;

import java.awt.image.BufferedImage;

/**
 * @author Fabien Steines
 */
public class TilesetManagerCanvas extends Canvas {

    private final int cellSize = 32;
    private Tileset tileset;
    private int mode;

    public static final int MODE_COLLISION = 0x0;


    /**
     *
     */
    public TilesetManagerCanvas() {
        this.mode = MODE_COLLISION;

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {

            int x = (int) t.getX() / 32;
            int y = (int) t.getY() / 32;

            if (this.tileset != null && this.tileset.inRange(x,y)) {
                this.tileset.setCollisionAt(x,y,!this.tileset.collisionAt(x,y));
            }

            this.render();

        });
    }


    /**
     * @param tileset
     */
    public void setTileset(Tileset tileset) {

        if (tileset != null) {
            Image image = tileset.getTilesetImage();
            this.setWidth(image != null ? image.getWidth() : 0);
            this.setHeight(image != null ? image.getHeight() + cellSize : 0);
        } else {
            if (this.tileset != null)
                this.setHeight(0);
        }

        this.tileset = tileset;
        this.render();
    }

    /**
     *
     */
    public void render() {

        if (this.getWidth() > 0 && this.getHeight() > 0) {
            GraphicsContext g = this.getGraphicsContext2D();

            ChessBGDrawer.drawChessBackground(g, (int) this.getWidth() / cellSize, (int) this.getHeight() / cellSize,
                    cellSize, cellSize);

            if (this.tileset != null && this.tileset.getTilesetImage() != null) {

                for (int i=0;i<8;i++) {
                    Terrain cTerrain = this.tileset.getTerrain(i);
                    if (cTerrain != null)
                        cTerrain.draw(g,i,0,2,0);
                }

                g.drawImage(this.tileset.getTilesetImage(), 0, cellSize);

                int maxW = this.tileset.getCellWidth();
                int maxH = this.tileset.getCellHeight();

                for (int i = 0; i <= maxW; i++) {
                    g.strokeLine(i * cellSize,0,i*cellSize,this.getHeight());
                }

                for (int i = 0; i <= maxH; i++) {
                    g.strokeLine(0,i * cellSize,this.getWidth(),i*cellSize);
                }

                g.setFont(Font.font(null, FontWeight.BOLD, 20));
                Color colorWhite = new Color(1,1,1,0.90);
                Color colorBlack = new Color(0,0,0,0.90);

                for (int i=0;i<maxW; i++) {
                    for (int j=0;j<maxH; j++) {

                        String text;
                        if (this.tileset.collisionAt(i,j)) {
                            text = "X";
                        } else {
                            text = "O";
                        }

                        int tw = IgnisGlobals.getTextWidth(g,text);
                        int th = IgnisGlobals.getTextHeight(g);
                        int xCenter = (this.cellSize - tw) / 2;
                        g.setFill(colorWhite);
                        g.fillText(text,i * cellSize + xCenter,j * cellSize + th);
                        g.setStroke(colorBlack);
                        g.setLineWidth(1.25);
                        g.strokeText(text,i * cellSize + xCenter,j * cellSize + th);
                    }
                }

                // Remove the Effect
                g.setEffect(null);

            }
        }
    }

}
