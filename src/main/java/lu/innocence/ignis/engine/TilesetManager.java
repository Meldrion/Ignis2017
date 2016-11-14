package lu.innocence.ignis.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetManager {

    private List<Tileset> tilesetList;

    public TilesetManager() {
        this.tilesetList = new ArrayList<>();
    }

    public void setTilesetMax(int maxCount) {

        this.tilesetList.clear(); // dummy for now
        for (int i=0;i<maxCount;i++) {
            this.tilesetList.add(null);
        }
    }

    public void setTileset(Tileset tileset,int index) {
        if (tileset != null) {
            tileset.setIndex(index);
        }
        this.tilesetList.set(index,tileset);
    }

    public Tileset getTilesetAtIndex(int index) {
        return index > -1 ? tilesetList.get(index) : null;
    }

}
