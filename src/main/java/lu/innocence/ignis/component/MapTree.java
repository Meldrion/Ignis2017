package lu.innocence.ignis.component;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lu.innocence.ignis.engine.Map;
import lu.innocence.ignis.engine.MapManager;

import java.util.List;

/**
 * @author Fabien Steines
 */
public class MapTree extends TreeView<String> {

    private MapManager mapManager;

    public MapTree() {
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            MapTreeNode selectedItem = (MapTreeNode) newValue;
            Map map = this.mapManager.find(selectedItem.getMapId());
            this.mapManager.setActiveMap(map);
        });
    }

    public void buildFromMapManager(MapManager mapManager) {

        MapTreeNode rootNode = new MapTreeNode("TEST");
        rootNode.setMapId("-1");
        this.setRoot(rootNode);
        buildFromNode(mapManager.getRoot(),rootNode);
        rootNode.setExpanded(true);

        this.mapManager = mapManager;
    }

    public void buildFromNode(Map map,TreeItem<String> node) {

        List<Map> maps = map.getChildren();
        for (Map current : maps) {
            MapTreeNode currentTreeNode = new MapTreeNode(current.getName());
            currentTreeNode.setMapId(current.getMapId());
            node.getChildren().add(currentTreeNode);
            buildFromNode(current,currentTreeNode);
        }
    }

}
