package lu.innocence.ignis.component;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.Map;
import lu.innocence.ignis.engine.MapManager;
import lu.innocence.ignis.engine.Project;
import lu.innocence.ignis.view.MapPropertiesDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


/**
 * @author Fabien Steines
 */
public class MapTree extends TreeView<String> {

    private static final Logger LOGGER = LogManager.getLogger(MapTree.class);
    private Project project;
    private Stage parentStage;


    /**
     *
     * @param parentStage
     */
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

    /**
     *
     */
    private void buildContextMenu() {
        ContextMenu mapTreeMenu = new ContextMenu();
        MenuItem createMap = new MenuItem("Create Map...");

        // Create Dialog
        createMap.setOnAction(event -> {
            if (this.getSelectionModel().getSelectedIndex() > -1) {
                callToMapCreationDialog();
            }
        });

        // Edit Dialog
        MenuItem editMap = new MenuItem("Edit Map...");
        editMap.setOnAction(event -> {
            if (this.getSelectionModel().getSelectedIndex() > -1) {

                MapTreeNode cNode = (MapTreeNode) this.getSelectionModel().getSelectedItem();
                Map map = this.project.getMapManager().getRoot().find(cNode.getMapId());

                if (map != null)
                    callToMapEditDialog(map);
                else
                    LOGGER.error("Unable to find Map with Id {}",cNode.getMapId());
            }
        });


        MenuItem copyMap = new MenuItem("Copy");

        mapTreeMenu.getItems().addAll(createMap, new SeparatorMenuItem(), editMap, copyMap);
        this.setContextMenu(mapTreeMenu);
    }

    /**
     *
     */
    private void callToMapCreationDialog() {

        // Open the Map Dialog || Tell the Dialog that it was opened in creation mode
        MapPropertiesDialog mapDialog = new MapPropertiesDialog(this.parentStage, this.project, MapPropertiesDialog.MODE_CREATE);
        mapDialog.showAndWait();

        // Check if the Dialog has been accepted
        if (mapDialog.isAccepted()) {

            Map newMap = mapDialog.createMap();
            MapTreeNode currentSelected = (MapTreeNode) this.getSelectionModel().getSelectedItem();

            // Add the NewMap to the MapManager
            if (currentSelected != null && currentSelected != this.getRoot()) {
                this.project.getMapManager().addMap(newMap, currentSelected.getMapId());
            } else {
                this.project.getMapManager().addMap(newMap);
            }
            // Save the MapTree, so that the Map keeps registered
            this.project.getMapManager().saveMapTree();

            // Create the Node for the MapTreeView
            MapTreeNode treeItem = new MapTreeNode(newMap.getName());
            treeItem.setMapId(newMap.getMapId());
            treeItem.setGraphic(new ImageView(IgnisGlobals.getIconMap()));
            this.getSelectionModel().getSelectedItem().getChildren().add(treeItem);

            // Expand the Parent Node (if any)
            if (currentSelected != null)
                currentSelected.setExpanded(true);
        }
    }

    /**
     *
     */
    private void callToMapEditDialog(Map map) {

        // Open the Map Dialog || Tell the Dialog that it was opened in edit mode
        MapPropertiesDialog mapDialog = new MapPropertiesDialog(this.parentStage, this.project, MapPropertiesDialog.MODE_EDIT);
        mapDialog.initMap(map);
        mapDialog.showAndWait();

        // Check if the Dialog has been accepted
        if (mapDialog.isAccepted()) {
            mapDialog.changeMap(map);
        }

    }

    /**
     *
     * @param p
     */
    public void setProject(Project p) {
        if (p != null) {
            this.project = p;
            this.buildFromMapManager(p.getMapManager());
        } else {
            this.project = null;
        }
    }

    /**
     *
     * @param map
     * @param node
     */
    private void buildFromNode(Map map, TreeItem<String> node) {

        List<Map> maps = map.getChildren();
        for (Map current : maps) {
            MapTreeNode currentTreeNode = new MapTreeNode(current.getName());
            currentTreeNode.setGraphic(new ImageView(IgnisGlobals.getIconMap()));
            currentTreeNode.setMapId(current.getMapId());
            node.getChildren().add(currentTreeNode);
            this.buildFromNode(current, currentTreeNode);
        }

        // If the map has any submaps, then change the icon to the folder icon
        if (!maps.isEmpty()) {
            node.setGraphic(new ImageView(IgnisGlobals.getIconMapFolder()));
        }

    }

    /**
     *
     * @param mapManager
     */
    private void buildFromMapManager(MapManager mapManager) {

        MapTreeNode rootNode = new MapTreeNode(this.project.getProjectTitle());
        rootNode.setMapId("-1");
        this.setRoot(rootNode);
        this.buildFromNode(mapManager.getRoot(), rootNode);
        rootNode.setExpanded(true);
    }



}
