package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Fabien Steines
 */
public class ImportDialog extends Stage {

    public ImportDialog(Stage parent) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Load Project Window");
        this.setResizable(false);
        this.buildGUI();
        //this.initData();
        this.initOwner(parent);
        this.setWidth(500);
        this.setHeight(350);
        this.show();
    }

    private void buildGUI() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,500, 350);
        this.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE) {
                this.close();
            }
        });

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER);


        Button cancelButton = new Button();
        cancelButton.setText("Close");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(cancelButton);
        root.setBottom(bottomBar);

    }



}
