package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.AssetStructure;
import lu.innocence.ignis.engine.FilesystemHandler;
import lu.innocence.ignis.engine.Project;

import java.util.List;


/**
 * @author Fabien Steines
 */
public class ImportDialog extends Stage {


    private static final Image imageIcon  =
            new Image("file:" + IgnisGlobals.loadFromResourceFolder("icons/thumbnail.png").getFile());

    private static final Image audioIcon  =
            new Image("file:" + IgnisGlobals.loadFromResourceFolder("icons/audio-x-generic-16.png").getFile());

    private static final Image jsIcon  =
            new Image("file:" + IgnisGlobals.loadFromResourceFolder("icons/jsIcon.png").getFile());

    private static final Image jsonIcon  =
            new Image("file:" + IgnisGlobals.loadFromResourceFolder("icons/jsonIcon.png").getFile());

    private Project project;
    private ListView<String> categoriesListView;
    private ListView<String> elementsListView;

    public ImportDialog(Stage parent, Project project) {
        this.project = project;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Import Manager Window");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.initOwner(parent);
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

        ColumnConstraints column1 = new ColumnConstraints(200);
        ColumnConstraints column2 = new ColumnConstraints(300);
        ColumnConstraints column3 = new ColumnConstraints(100);

        grid.getColumnConstraints().addAll(column1,column2,column3);

        this.categoriesListView = new ListView<>();
        grid.add(this.categoriesListView,0,0);

        this.elementsListView = new ListView<>();
        grid.add(elementsListView,1,0);

        this.categoriesListView.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String name, boolean empty) {

                super.updateItem(name, empty);

                if (AssetStructure.isAudio(name)) {
                    imageView.setImage(audioIcon);
                }

                if (AssetStructure.isImage(name)) {
                    imageView.setImage(imageIcon);
                }

                if (AssetStructure.isScript(name)) {
                    imageView.setImage(jsIcon);
                }

                if (AssetStructure.isJSON(name)) {
                    imageView.setImage(jsonIcon);
                }


                setText(name);
                setGraphic(imageView);
            }

        });


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
        initSelectedCategory(this.categoriesListView.getItems().get(0));
    }

    private void initSelectedCategory(String category) {

        this.elementsListView.getItems().clear();

        if (project != null) {
            String path = this.project.getAssetStructure().getPath(category);
            FilesystemHandler.readFolderContent(path).stream().filter(file ->
                    (AssetStructure.isAudio(category) && FilesystemHandler.isAudio(path)) ||
                            AssetStructure.isImage(category) && FilesystemHandler.isImage(path)).forEach(file
                    -> this.elementsListView.getItems().add(file));
        }
    }



}
