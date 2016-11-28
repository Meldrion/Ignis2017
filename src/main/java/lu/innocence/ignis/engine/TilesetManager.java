package lu.innocence.ignis.engine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class TilesetManager {

    public static final int MAX_TILESET_COUNT = 9999;
    private List<Tileset> tilesetList;
    private String jsonFolder;

    public TilesetManager() {
        this.tilesetList = new ArrayList<>();
    }

    public void setJSONFolder(String jsonFolder) {
        this.jsonFolder = jsonFolder;
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

    public void save() {

        JSONObject tilesetListJSON = new JSONObject();
        JSONArray tilesets = new JSONArray();

        for (Tileset tileset : this.tilesetList) {
            JSONObject tilesetJSONObject = new JSONObject();
            tilesetJSONObject.put("name", tileset.getName());
            tilesetJSONObject.put("image", tileset.getTilesetImage() != null ? tileset.getImageName() : "");
            JSONArray blockingMatrix = new JSONArray();

            boolean[][] collisionMatrix = tileset.getCollisionMatrix();
            if (collisionMatrix != null) {
                for (int i = 0; i < collisionMatrix.length; i++) {
                    for (int j = 0; j < collisionMatrix[0].length; j++) {

                        if (collisionMatrix[i][j]) {

                            JSONObject collisionLine = new JSONObject();
                            collisionLine.put("x", i);
                            collisionLine.put("y", j);
                            blockingMatrix.add(collisionLine);
                        }

                    }
                }
            }

            tilesetJSONObject.put("blocking", blockingMatrix);
            tilesets.add(tilesetJSONObject);
        }

        tilesetListJSON.put("tilesets", tilesets);
        FilesystemHandler.writeJson(tilesetListJSON, FilesystemHandler.concat(this.jsonFolder,"tilesettree.json"));
    }

    public void load() {

    }

}
