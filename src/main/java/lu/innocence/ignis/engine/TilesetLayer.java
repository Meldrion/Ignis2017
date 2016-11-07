package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetLayer {

    private List<List<TilesetCell>> matrix;
    private int width;
    private int height;
    private final int cellSize = 32;


    public void setDimension(int x,int y) {
        this.width = x;
        this.height = y;
        this.buildMatrix();
    }

    public void buildMatrix() {

        this.matrix = new ArrayList<>();
        for (int i=0;i<this.width;i++) {

            List<TilesetCell> innerMatrix = new ArrayList<>();
            for (int j=0;j<this.height;j++) {
                innerMatrix.add(null);
            }

            this.matrix.add(innerMatrix);

        }

    }

    public void addCell(int x,int y,int tsX,int tsY) {
        this.matrix.get(x).set(y,new TilesetCell(x,y,tsX,tsY));
    }

    public void render(GraphicsContext g) {

    }

    public void renderPartial(GraphicsContext g,int x, int y) {

    }

}