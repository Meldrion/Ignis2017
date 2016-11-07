package sample.component;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import sample.engine.Map;
import sample.engine.MapManager;

import java.util.List;

/**
 * @author Fabien Steines
 */
public class MapTree extends TreeView<String> {


    public MapTree() {
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            MapTreeNode selectedItem = (MapTreeNode) newValue;
            System.out.println("Selected Text : " + selectedItem.getMapId());
            // do what ever you want
        });
    }

    public void buildFromMapManager(MapManager mapManager) {

        MapTreeNode rootNode = new MapTreeNode("TEST");
        rootNode.setMapId("-1");
        this.setRoot(rootNode);
        buildFromNode(mapManager.getRoot(),rootNode);
        rootNode.setExpanded(true);
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
