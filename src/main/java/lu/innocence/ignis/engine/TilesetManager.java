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
    private String terrainFolder;
    private String tilesetFolder;
    private static final String TILETREEFILE = "tiletree.json";

    public TilesetManager() {
        this.tilesetList = new ArrayList<>();
    }

    public void setJSONFolder(String jsonFolder) {
        this.jsonFolder = jsonFolder;
    }

    public void setTerrainFolder(String terrainFolder) {
        this.terrainFolder = terrainFolder;
    }

    public void setTilesetFolder(String tilesetFolder) {
        this.tilesetFolder = tilesetFolder;
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

    @SuppressWarnings("unchecked")
    public void save() {

        JSONObject tilesetListJSON = new JSONObject();
        JSONArray tilesets = new JSONArray();

        for (Tileset tileset : this.tilesetList) {
            JSONObject tilesetJSONObject = new JSONObject();
            tilesetJSONObject.put("name", tileset.getName());
            tilesetJSONObject.put("image", tileset.getTilesetImage() != null ? tileset.getImageName() : "");
            JSONArray terrains = new JSONArray();
            for (int i=0;i<8;i++) {
                Terrain terrain = tileset.getTerrain(i);
                if (terrain != null) {
                    JSONObject terrainObject = new JSONObject();
                    terrainObject.put("index",i);
                    terrainObject.put("filename",terrain.getImageName());
                    terrains.add(terrainObject);
                }
            }
            tilesetJSONObject.put("terrain",terrains);

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
        FilesystemHandler.writeJson(tilesetListJSON, FilesystemHandler.concat(this.jsonFolder,TILETREEFILE));
    }

    public void load() {
        JSONObject tilesetListJSON = FilesystemHandler.readJSON(FilesystemHandler.concat(this.jsonFolder,TILETREEFILE));
        JSONArray tilesetList = (JSONArray) tilesetListJSON.get("tilesets");

        this.setTilesetMax(tilesetList.size());
        for (int i=0;i<tilesetList.size();i++) {
            JSONObject tileset = (JSONObject) tilesetList.get(i);
            Tileset current = this.tilesetList.get(i);
            current.setIndex(i);
            current.setName((String)tileset.get("name"));
            current.loadImage(FilesystemHandler.concat(this.tilesetFolder,(String)tileset.get("image")));
            JSONArray terrains = (JSONArray) tileset.get("terrain");

            for (int ti = 0;ti < terrains.size();ti++) {
                JSONObject currentTerrainJson = (JSONObject) terrains.get(ti);
                int terrainIndexTileset = (int)(long) currentTerrainJson.get("index");
                String fileName = (String) currentTerrainJson.get("filename");
                Terrain newTerrain = new Terrain();
                newTerrain.loadImage(FilesystemHandler.concat(this.terrainFolder,fileName));
                current.setTerrain(terrainIndexTileset,newTerrain);
            }

            JSONArray collisionMatrix = (JSONArray) tileset.get("blocking");

            for (int j=0;j<collisionMatrix.size();j++) {
                JSONObject collisionLine = (JSONObject) collisionMatrix.get(j);
                int x = (int) (long) collisionLine.get("x");
                int y = (int) (long) collisionLine.get("y");
                current.setCollisionAt(x,y,true);
            }

        }
    }

}
