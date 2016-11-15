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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Fabien Steines
 */
public class CreateMapDialog extends Stage {

    public CreateMapDialog(Stage parentStage) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Create Map...");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.initOwner(parentStage);
        this.sizeToScene();
        this.show();
    }

    private void buildGUI() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        this.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE) {
                this.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,0,10));
        grid.setHgap(10);
        grid.setVgap(10);

        Text labelMapName = new Text();
        labelMapName.setText("Map Name: ");
        TextField textFieldMapName = new TextField();

        grid.add(labelMapName,0,0);
        grid.add(textFieldMapName,1,0,3,1);

        Text labelMapWidth = new Text();
        labelMapWidth.setText("Width: ");
        grid.add(labelMapWidth,0,1);

        Spinner<Integer> widthSpinner = new Spinner<>();
        widthSpinner.setEditable(true);
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 200));
        grid.add(widthSpinner,1,1);

        Text labelMapHeight = new Text();
        labelMapHeight.setText("Height: ");
        grid.add(labelMapHeight,2,1);

        Spinner<Integer> heightSpinner = new Spinner<>();
        heightSpinner.setEditable(true);
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 200));
        grid.add(heightSpinner,3,1);

        Text labelTileset = new Text();
        labelTileset.setText("Tileset: ");
        grid.add(labelTileset,0,2);

        TextField tilesetTextField = new TextField();
        tilesetTextField.setEditable(false);
        grid.add(tilesetTextField,1,2,2,1);

        Button tilesetSearchButton = new Button();
        tilesetSearchButton.setText("...");
        grid.add(tilesetSearchButton,3,2);

        root.setCenter(grid);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button acceptButton = new Button();
        acceptButton.setText("Ok");
        acceptButton.setOnAction(event -> {

        });

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(cancelButton,acceptButton);
        root.setBottom(bottomBar);
    }

    private void initData() {

    }

}
