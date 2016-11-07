package sample.component;

import javafx.scene.control.TreeItem;

/**
 * @author Fabien Steines
 */
public class MapTreeNode extends TreeItem<String> {

    private String mapId;

    public MapTreeNode(String title) {
        super(title);
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getMapId() {
        return this.mapId;
    }

}
