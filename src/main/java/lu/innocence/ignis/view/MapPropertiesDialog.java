package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.Map;
import lu.innocence.ignis.engine.Project;
import lu.innocence.ignis.view.resourceView.TilesetResourceView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Fabien Steines
 */
public class MapPropertiesDialog extends Stage {

    private static final Logger LOGGER = LogManager.getLogger(MapPropertiesDialog.class);
    private Project project;

    private int selectedTilesetIndex;
    private boolean accepted;

    private Spinner<Integer> widthSpinner;
    private Spinner<Integer> heightSpinner;
    private TextField textFieldMapName;
    private TextField tilesetTextField;

    public static final int MODE_CREATE = 0x0;
    public static final int MODE_EDIT = 0x1;

    private static final String CREATE_MAP_TITLE = "Create Map";
    private static final String EDIT_MAP_TITLE = "Edit Map";

    public MapPropertiesDialog(Stage parentStage, Project project, int mode) {
        this.accepted = false;
        this.project = project;
        this.selectedTilesetIndex = -1;
        this.initModality(Modality.APPLICATION_MODAL);

        if (mode == MODE_EDIT) {
            this.setTitle(EDIT_MAP_TITLE);
        } else {
            this.setTitle(CREATE_MAP_TITLE);
        }

        this.setResizable(false);
        this.buildGUI();
        this.initOwner(parentStage);
        this.sizeToScene();
    }

    /**
     *
     * @param map
     */
    public void initMap(Map map) {
        if (map != null) {
            this.textFieldMapName.setText(map.getName());
            this.widthSpinner.getEditor().setText(String.valueOf(map.getWidth()));
            this.heightSpinner.getEditor().setText(String.valueOf(map.getHeight()));
            this.selectedTilesetIndex = map.getTilesetId();
            if (map.getTileset() != null) {
                this.tilesetTextField.setText(TilesetResourceView.
                        generateTilesetName(this.selectedTilesetIndex, map.getTileset().getName()));
            }
        } else {
            LOGGER.error("Map is null");
            this.close();
        }
    }

    /**
     *
     * @return
     */
    public Map createMap() {

        Map newMap = this.project.getMapManager().createNewMap();
        this.changeMap(newMap);
        newMap.save();
        return newMap;
    }

    /**
     *
     * @param map
     */
    public void changeMap(Map map) {
        int w = Integer.parseInt(this.widthSpinner.getEditor().getText());
        int h = Integer.parseInt(this.heightSpinner.getEditor().getText());
        map.setDimension(w, h);
        map.setName(this.textFieldMapName.getText());
        map.setTileset(this.project.getTilesetManager().getTilesetAtIndex(selectedTilesetIndex));
    }

    /**
     *
     */
    private void buildGUI() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        this.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                this.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 0, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label labelMapName = new Label();
        labelMapName.setText("Map Name: ");
        this.textFieldMapName = new TextField();

        grid.add(labelMapName, 0, 0);
        grid.add(textFieldMapName, 1, 0, 3, 1);

        Label labelMapWidth = new Label();
        labelMapWidth.setText("Width: ");
        grid.add(labelMapWidth, 0, 1);

        widthSpinner = new Spinner<>();
        widthSpinner.setEditable(true);
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 200));
        grid.add(widthSpinner, 1, 1);

        Label labelMapHeight = new Label();
        labelMapHeight.setText("Height: ");
        grid.add(labelMapHeight, 2, 1);

        heightSpinner = new Spinner<>();
        heightSpinner.setEditable(true);
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 200));
        grid.add(heightSpinner, 3, 1);

        Label labelTileset = new Label();
        labelTileset.setText("Tileset: ");
        grid.add(labelTileset, 0, 2);

        this.tilesetTextField = new TextField();
        this.tilesetTextField.setMaxWidth(Integer.MAX_VALUE);
        this.tilesetTextField.setEditable(false);

        Button tilesetSearchButton = new Button();
        tilesetSearchButton.setText("...");
        tilesetSearchButton.setOnAction(event -> {

            TilesetResourceView tsView = new TilesetResourceView(this);
            tsView.setTilesetManager(this.project.getTilesetManager());
            tsView.setSelectedIndex(selectedTilesetIndex);
            tsView.showAndWait();

            if (tsView.isAccepted()) {
                tilesetTextField.setText(tsView.getSelectedString());
                this.selectedTilesetIndex = tsView.getSelectedIndex();
            }

        });

        HBox lineEditWithButton = new HBox();
        lineEditWithButton.setSpacing(10);
        lineEditWithButton.getChildren().addAll(tilesetTextField, tilesetSearchButton);
        lineEditWithButton.setMaxWidth(Integer.MAX_VALUE);
        HBox.setHgrow(tilesetTextField, Priority.ALWAYS);
        grid.add(lineEditWithButton, 1, 2, 3, 1);

        root.setCenter(grid);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button acceptButton = new Button();
        acceptButton.setText("Ok");
        acceptButton.setOnAction(event -> {
            this.accepted = true;
            this.close();
        });

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(cancelButton, acceptButton);
        root.setBottom(bottomBar);
    }

    /**
     *
     * @return
     */
    public boolean isAccepted() {
        return accepted;
    }
}
