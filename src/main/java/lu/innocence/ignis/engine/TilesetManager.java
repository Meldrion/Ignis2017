package lu.innocence.ignis.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetManager {

    public static final int MAX_TILESET_COUNT = 9999;
    private List<Tileset> tilesetList;

    public TilesetManager() {
        this.tilesetList = new ArrayList<>();
    }

    public void setTilesetMax(int maxCount) {

        int cMax = tilesetList.size();
        List<Tileset> tsListTMP = new ArrayList<>();

        for (int i = 0; i < maxCount; i++) {
            if (i < cMax) {
                tsListTMP.add(tilesetList.get(i));
            } else {
                Tileset ts = new Tileset();
                ts.setIndex(i);
                tsListTMP.add(ts);
            }
        }

        this.tilesetList = tsListTMP;
    }

    public void setTileset(Tileset tileset, int index) {
        if (tileset != null) {
            tileset.setIndex(index);
        }
        this.tilesetList.set(index, tileset);
    }

    public Tileset getTilesetAtIndex(int index) {
        return index > -1 ? tilesetList.get(index) : null;
    }

    public List<Tileset> getTilesetList() {
        return this.tilesetList;
    }

}
