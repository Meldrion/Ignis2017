package sample.engine;

import sample.event.ActiveMapListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class MapManager {

    private Map root;
    private Map activeMap;
    private List<ActiveMapListener> mapListener;


    public MapManager() {
        this.root = new Map();
        this.mapListener = new ArrayList<>();
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

    public Map find(String uniqueMapId) {
        return this.root.find(uniqueMapId);
    }

    public Map getRoot() {
        return this.root;
    }

    public List<Map> getChildren() {
        return this.root.getChildren();
    }

}
