package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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
public class CreateMapDialog extends Stage {

    private Logger LOGGER = LogManager.getLogger(CreateMapDialog.class);
    private Project project;

    private int selectedTilesetIndex;
    private boolean accepted;

    private Spinner<Integer> widthSpinner;
    private Spinner<Integer> heightSpinner;
    private TextField textFieldMapName;

    public CreateMapDialog(Stage parentStage, Project project) {
        this.accepted = false;
        this.project = project;
        this.selectedTilesetIndex = -1;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Create Map...");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.initOwner(parentStage);
        this.sizeToScene();
    }

    public Map createMap() {
        int w = Integer.parseInt(this.widthSpinner.getEditor().getText());
        int h = Integer.parseInt(this.heightSpinner.getEditor().getText());

        Map newMap = this.project.getMapManager().createNewMap();
        newMap.setDimension(w, h);
        newMap.setName(this.textFieldMapName.getText());
        newMap.setTileset(this.project.getTilesetManager().getTilesetAtIndex(selectedTilesetIndex));
        newMap.save();

        return newMap;
    }

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

        Text labelMapName = new Text();
        labelMapName.setText("Map Name: ");
        this.textFieldMapName = new TextField();

        grid.add(labelMapName, 0, 0);
        grid.add(textFieldMapName, 1, 0, 3, 1);

        Text labelMapWidth = new Text();
        labelMapWidth.setText("Width: ");
        grid.add(labelMapWidth, 0, 1);

        widthSpinner = new Spinner<>();
        widthSpinner.setEditable(true);
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 200));
        grid.add(widthSpinner, 1, 1);

        Text labelMapHeight = new Text();
        labelMapHeight.setText("Height: ");
        grid.add(labelMapHeight, 2, 1);

        heightSpinner = new Spinner<>();
        heightSpinner.setEditable(true);
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 200));
        grid.add(heightSpinner, 3, 1);

        Text labelTileset = new Text();
        labelTileset.setText("Tileset: ");
        grid.add(labelTileset, 0, 2);

        TextField tilesetTextField = new TextField();
        tilesetTextField.setMaxWidth(Integer.MAX_VALUE);
        tilesetTextField.setEditable(false);

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

    private void initData() {

    }

    public boolean isAccepted() {
        return accepted;
    }
}
