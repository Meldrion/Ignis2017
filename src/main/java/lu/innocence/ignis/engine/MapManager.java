package lu.innocence.ignis.engine;

import lu.innocence.ignis.event.ActiveMapListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class MapManager {

    private final MapIDGenerator idGenerator;
    private Map root;
    private Map activeMap;
    private List<ActiveMapListener> mapListener;
    private String mapFolder;
    private String jsonFolder;
    private TilesetManager tilesetManager;


    public MapManager() {
        this.idGenerator = new MapIDGenerator();
        this.root = new Map();
        this.mapListener = new ArrayList<>();
    }

    public String getMapFolder() {
        return this.mapFolder;
    }

    public void setMapFolder(String folder) {
        this.mapFolder = folder;
    }

    public String getJsonFolder() {
        return this.jsonFolder;
    }

    public void setJsonFolder(String folder) {
        this.jsonFolder = folder;
    }

    public void setActiveMap(Map activeMap) {
        this.activeMap = activeMap;
        this.fireUpdate();
    }

    public void addActiveMapListener(ActiveMapListener listener) {
        if (!this.mapListener.contains(listener)) {
            this.mapListener.add(listener);
        }
    }

    private void fireUpdate() {
        for (ActiveMapListener listener : this.mapListener) {
            listener.activeMapChanged(this.activeMap);
        }
    }

    private String generateMapId() {
        return this.idGenerator.generateUniqueName();
    }

    public void addMap(Map map) {
        this.addMap(map, null);
    }

    public void addMap(Map map, String parentId) {
        if (parentId == null) {
            this.root.addMap(map);
        } else {
            Map parent = this.root.find(parentId);
            if (parent != null)
                parent.addMap(map);
        }
    }

    public Map createNewMap() {
        Map map = new Map();
        map.setUniqueId(String.format("%s.json", this.generateMapId()));
        map.setMapFilePath(FilesystemHandler.concat(this.mapFolder, map.getMapId()));
        return map;
    }

    public void saveAll() {
        if (this.activeMap != null)
            this.activeMap.save();
    }

    public void saveMapFile(Map map) {
        map.save();
    }

    public Map find(String uniqueMapId) {
        return this.root.find(uniqueMapId);
    }

    public Map getRoot() {
        return this.root;
    }

    public List<Map> getChildren() {
        return this.root.getChildren();
    }

    public void saveMapTree() {
        JSONObject mapTreeSave = new JSONObject();
        JSONArray maps = new JSONArray();

        for (Map current : this.root.getChildren()) {
            this.saveSubMapTree(current, maps);
        }

        mapTreeSave.put("maps", maps);

        String fileName = FilesystemHandler.concat(this.jsonFolder, "maptree.json");
        FilesystemHandler.writeJson(mapTreeSave, fileName);

    }

    public void loadMapTree() {
        String fileName = FilesystemHandler.concat(this.jsonFolder, "maptree.json");
        JSONObject maptree = FilesystemHandler.readJSON(fileName);
        JSONArray maps = (JSONArray) maptree.get("maps");

        for (int i = 0; i < maps.size(); i++) {
            JSONObject currentMap = (JSONObject) maps.get(i);
            readMap(currentMap, null);
        }
    }

    public void readMap(JSONObject currentJSON, Map parent) {
        Map map = new Map();
        map.setUniqueId((String) currentJSON.get("id"));
        map.setMapFilePath(FilesystemHandler.concat(this.mapFolder, map.getMapId()));
        int idNum = this.extractMapIdNumber(map.getMapId());
        this.idGenerator.setIdUsed(idNum, true);
        map.load();
        map.setTileset(this.tilesetManager.getTilesetAtIndex(map.getTilesetId()));

        if (parent != null) {
            parent.addMap(map);
        } else {
            this.root.addMap(map);
        }

        JSONArray subMaps = (JSONArray) currentJSON.get("submaps");
        for (int i = 0; i < subMaps.size(); i++) {
            JSONObject jsonMap = (JSONObject) subMaps.get(i);
            this.readMap(jsonMap, map);
        }
    }

    public void saveSubMapTree(Map map, JSONArray mapTreeSave) {

        JSONObject currentMap = new JSONObject();
        currentMap.put("id", map.getMapId());
        JSONArray subMaps = new JSONArray();

        for (Map current : map.getChildren()) {
            saveSubMapTree(current, subMaps);
        }

        currentMap.put("submaps", subMaps);
        mapTreeSave.add(currentMap);

    }

    private int extractMapIdNumber(String input) {
        if (input != null) {
            int pos = input.indexOf(".json");
            String numbers = input.substring("map".length(), pos);
            return Integer.valueOf(numbers);
        }
        return -1;
    }

    public void setTilesetManager(TilesetManager tilesetManager) {
        this.tilesetManager = tilesetManager;
    }

}
