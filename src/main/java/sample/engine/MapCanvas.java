package sample.engine;

import javafx.scene.canvas.Canvas;

/**
 * @author Fabien Steines
 */
public class MapCanvas extends Canvas {

    private Map map;

    public MapCanvas(int width,int height) {
        super(width,height);
        this.map = new Map();
        this.map.setDimension(50,50);

        Tileset tileset = new Tileset();
        tileset.loadImage("tileset.png");

        this.map.setTileset(tileset);
    }

    public void render() {
        this.map.renderMap(this.getGraphicsContext2D());
    }

    public void renderPartial(int x,int y) {
        this.map.renderPartialMap(this.getGraphicsContext2D(),x,y);
    }




}
