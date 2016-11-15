package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.view.components.ResourceCanvas;

/**
 * @author Fabien Steines
 */
public class ResourceView extends Stage {

    private ListView<String> resourceList;
    private ResourceCanvas resourceCanvas;

    public ResourceView(Stage parentStage) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Choose..");
        this.setResizable(false);
        this.buildGUI();
        //this.initData();
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

        this.resourceList = new ListView<>();
        this.resourceList.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {});
        grid.add(this.resourceList,0,0);

        this.resourceCanvas = new ResourceCanvas();
        grid.add(this.resourceCanvas,1,0);


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

}
