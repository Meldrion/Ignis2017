package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Fabien Steines
 */
public class CreateProjectDialog extends Stage{

    public CreateProjectDialog() {

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Create Project Window");
        this.setResizable(false);
        this.buildGUI();
        this.setWidth(400);
        this.setHeight(400);
        this.show();

    }

    private void buildGUI() {
        BorderPane root = new BorderPane();
        this.setScene(new Scene(root, 400, 400));


        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button confirmButton = new Button();
        confirmButton.setText("Create Project");

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(confirmButton,cancelButton);

        root.setBottom(bottomBar);
    }

}
