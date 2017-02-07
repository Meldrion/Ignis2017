package lu.innocence.ignis.view.components;

import javafx.scene.control.TreeItem;

/**
 * @author Fabien Steines
 */
public class MapTreeNode extends TreeItem<String> {

    private String mapId;

    public MapTreeNode(String title) {
        super(title);
    }

    public String getMapId() {
        return this.mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

}
