package lu.innocence.ignis.engine;

import lu.innocence.ignis.event.ActiveMapListener;
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


    public MapManager() {
        this.idGenerator = new MapIDGenerator();
        this.root = new Map();
        this.mapListener = new ArrayList<>();
    }

    public void setMapFolder(String folder) {
        this.mapFolder = folder;
    }

    public String getMapFolder() {
        return this.mapFolder;
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
        this.addMap(map,null);
    }

    public void addMap(Map map,String parentId) {
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
        map.setUniqueId(String.format("%s.json",this.generateMapId()));
        map.setMapFilePath(FilesystemHandler.concat(this.mapFolder,map.getMapId()));
        return map;
    }

    public void saveAll() {
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
    }

}
