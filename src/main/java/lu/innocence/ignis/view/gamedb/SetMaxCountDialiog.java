package lu.innocence.ignis.view.gamedb;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.FilesystemHandler;
import lu.innocence.ignis.engine.ProjectManager;


/**
 * Created by fabien on 26/11/16.
 */
public class SetMaxCountDialiog extends Stage {

    private Spinner<Integer> spMaxCount;

    public SetMaxCountDialiog(Stage parent) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Load Project Window");
        this.setResizable(false);
        this.buildGUI();
        this.initOwner(parent);
        this.sizeToScene();
    }

    private void buildGUI() {
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        this.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                this.close();
            }
        });

        root.setPadding(new Insets(10,10,0,10));
        Text lblMaxCount = new Text();
        lblMaxCount.setText("Set Max Count: ");
        root.add(lblMaxCount,0,0);

        spMaxCount = new Spinner<>();
        spMaxCount.setEditable(true);
        spMaxCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999));
        root.add(spMaxCount, 1, 0);

        HBox bottomBar = new HBox();
        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button confirmButton = new Button();
        confirmButton.setText("Ok");
        confirmButton.setOnAction(event -> {

        });

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(confirmButton, cancelButton);

        VBox bottom = new VBox();
        bottom.setPadding(new Insets(10,0,0,0));
        bottom.getChildren().addAll(new Separator(),bottomBar);

        root.add(bottom,0,1,2,1);

    }

}
