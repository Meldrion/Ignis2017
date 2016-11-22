package lu.innocence.ignis.engine;

import org.json.simple.JSONObject;

/**
 * @author Fabien Steines
 */
public class TilesetCell {

    int x;
    int y;
    int tsX;
    int tsY;

    public TilesetCell(int x, int y, int tsX, int tsY) {
        this.x = x;
        this.y = y;
        this.tsX = tsX;
        this.tsY = tsY;
    }

    public JSONObject save() {
        JSONObject tile = new JSONObject();
        tile.put("x", x);
        tile.put("y", y);
        tile.put("tsX", tsX);
        tile.put("tsY", tsY);
        return tile;
    }

}
