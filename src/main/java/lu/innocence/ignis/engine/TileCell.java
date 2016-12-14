package lu.innocence.ignis.engine;

import org.json.simple.JSONObject;

/**
 * @author Fabien Steines
 */
public class TileCell {

    int x;
    int y;
    int tsX;
    int tsY;

    public TileCell(int x, int y, int tsX, int tsY) {
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

    /**
     *
     * @param other
     * @return
     */
    public boolean sameTileAs(TileCell other) {
        return other != null && other.tsX == tsX && other.tsY == tsY;
    }


    public int getTsX() {
        return tsX;
    }

    public int getTsY() {
        return tsY;
    }
}
