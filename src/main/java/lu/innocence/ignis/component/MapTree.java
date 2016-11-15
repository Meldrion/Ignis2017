package lu.innocence.ignis.component;

import javafx.scene.control.*;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.Map;
import lu.innocence.ignis.engine.MapManager;
import lu.innocence.ignis.engine.Project;
import lu.innocence.ignis.view.CreateMapDialog;

import java.util.List;

/**
 * @author Fabien Steines
 */
public class MapTree extends TreeView<String> {

    private Project project;
    private Stage parentStage;

    public MapTree(Stage parentStage) {
        this.parentStage = parentStage;
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            MapTreeNode selectedItem = (MapTreeNode) newValue;

            if (selectedItem != null) {
                Map map = this.project.getMapManager().find(selectedItem.getMapId());
                this.project.getMapManager().setActiveMap(map);
            } else {
                this.project.getMapManager().setActiveMap(null);
            }

        });
        this.buildContextMenu();
    }

    private void buildContextMenu() {
        ContextMenu mapTreeMenu = new ContextMenu();
        MenuItem createMap = new MenuItem("Create Map...");
        createMap.setOnAction(event -> {
            if (this.getSelectionModel().getSelectedIndex() > -1) {
                new CreateMapDialog(this.parentStage);
            }
        });
        MenuItem editMap = new MenuItem("Edit Map...");
        MenuItem copyMap = new MenuItem("Copy");

        mapTreeMenu.getItems().addAll(createMap,new SeparatorMenuItem(),editMap,copyMap);
        this.setContextMenu(mapTreeMenu);
    }

    public void setProject(Project p) {
        if (p != null) {
            this.project = p;
            this.buildFromMapManager(p.getMapManager());
        } else {
            this.project = null;
        }
    }

    private void buildFromMapManager(MapManager mapManager) {

        MapTreeNode rootNode = new MapTreeNode("TEST");
        rootNode.setMapId("-1");
        this.setRoot(rootNode);
        buildFromNode(mapManager.getRoot(),rootNode);
        rootNode.setExpanded(true);
    }

    private void buildFromNode(Map map,TreeItem<String> node) {

        List<Map> maps = map.getChildren();
        for (Map current : maps) {
            MapTreeNode currentTreeNode = new MapTreeNode(current.getName());
            currentTreeNode.setMapId(current.getMapId());
            node.getChildren().add(currentTreeNode);
            buildFromNode(current,currentTreeNode);
        }

    }

}
