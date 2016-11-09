package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.AssetStructure;

import java.util.List;


/**
 * @author Fabien Steines
 */
public class ImportDialog extends Stage {

    private ListView<String> categoriesListView;
    private ListView<String> elementsListView;

    public ImportDialog(Stage parent) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Load Project Window");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.initOwner(parent);
        this.show();
    }


    private void buildGUI() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,640, 350);
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

        ColumnConstraints column1 = new ColumnConstraints(200);
        ColumnConstraints column2 = new ColumnConstraints(300);
        ColumnConstraints column3 = new ColumnConstraints(100);

        grid.getColumnConstraints().addAll(column1,column2,column3);

        this.categoriesListView = new ListView<>();
        grid.add(this.categoriesListView,0,0);

        this.elementsListView = new ListView<>();
        grid.add(elementsListView,1,0);


        VBox rightPanel = new VBox();
        rightPanel.setSpacing(10);

        Button importButton = new Button();
        importButton.setText("Import");
        importButton.setPrefWidth(120);

        Button deleteButton = new Button();
        deleteButton.setText("Delete");
        deleteButton.setPrefWidth(120);

        Button previewButton = new Button();
        previewButton.setText("Preview");
        previewButton.setPrefWidth(120);

        rightPanel.getChildren().addAll(importButton,previewButton,deleteButton);

        grid.add(rightPanel,2,0);

        root.setCenter(grid);

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

    private void initData() {
        for (String current : AssetStructure.getAssetNames()) {
            this.categoriesListView.getItems().add(current);
        }
    }



}
