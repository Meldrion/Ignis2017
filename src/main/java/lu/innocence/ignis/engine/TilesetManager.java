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

        this.tilesetList.clear(); // dummy for now
        for (int i = 0; i < maxCount; i++) {
            Tileset ts = new Tileset();
            ts.setIndex(i);
            this.tilesetList.add(ts);
        }
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
