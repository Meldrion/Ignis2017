package lu.innocence.ignis.view.dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.AssetStructure;
import lu.innocence.ignis.engine.FilesystemHandler;
import lu.innocence.ignis.engine.Project;
import lu.innocence.ignis.view.components.CustomListCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


/**
 * @author Fabien Steines
 */
public class ImportDialog extends Stage {

    private static final Logger LOGGER = LogManager.getLogger(ImportDialog.class);
    private Project project;
    private ListView<String> categoriesListView;
    private ListView<String> elementsListView;

    /**
     *
     * @param parent the parent of this dialog
     * @param project the currently loaded project
     */
    public ImportDialog(Stage parent, Project project) {
        this.project = project;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Import Manager Window");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.initOwner(parent);
        this.sizeToScene();
        LOGGER.info("Import Dialog created");
    }

    /**
     * Call to build the User Interface
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

        ColumnConstraints column1 = new ColumnConstraints(200);
        ColumnConstraints column2 = new ColumnConstraints(300);
        ColumnConstraints column3 = new ColumnConstraints(100);

        grid.getColumnConstraints().addAll(column1, column2, column3);

        this.categoriesListView = new ListView<>();
        this.categoriesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> this.initSelectedCategory(newValue));
        grid.add(this.categoriesListView, 0, 0);

        this.elementsListView = new ListView<>();
        grid.add(elementsListView, 1, 0);

        this.categoriesListView.setCellFactory(param -> new CustomListCell());

        grid.add(createRightPanel(), 2, 0);
        root.setCenter(grid);
        buildButtonBoxButtom();

        root.setBottom(buildButtonBoxButtom());

    }

    /**
     *
     * @return HBox holding the Bottombox
     */
    private HBox buildButtonBoxButtom() {
        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER);

        Button cancelButton = new Button();
        cancelButton.setText("Close");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(cancelButton);
        return bottomBar;
    }

    /**
     *
     * @return VBox holding the right panel
     */
    private VBox createRightPanel() {

        VBox rightPanel = new VBox();
        rightPanel.setSpacing(10);

        Button importButton = new Button();
        importButton.setText("Import");
        importButton.setPrefWidth(120);
        importButton.setOnAction(event -> importAction());

        Button deleteButton = new Button();
        deleteButton.setText("Delete");
        deleteButton.setPrefWidth(120);

        Button previewButton = new Button();
        previewButton.setText("Preview");
        previewButton.setPrefWidth(120);

        rightPanel.getChildren().addAll(importButton, previewButton, deleteButton);

        return rightPanel;
    }

    /**
     * What happens when we click on the import button
     */
    private void importAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Resource...");
        File file = fileChooser.showOpenDialog(this);
        if (file != null) {

            String cat = this.categoriesListView.getSelectionModel().getSelectedItem();
            String dir = this.project.getAssetStructure().getPath(cat);
            if (FilesystemHandler.copy(file.getAbsolutePath(), FilesystemHandler.concat(dir, file.getName()))) {
                this.initSelectedCategory(cat);
            }
        }
    }

    /**
     *  Init the Category list, then load the data for the first one
     */
    private void initData() {
        for (String current : AssetStructure.getAssetNames()) {
            this.categoriesListView.getItems().add(current);
        }
        initSelectedCategory(this.categoriesListView.getItems().get(0));
    }

    /**
     * Get called to pass the data from the project into the list
     * @param category The category of which we want tob receive the data
     */
    private void initSelectedCategory(String category) {

        this.elementsListView.getItems().clear();

        if (category != null && project != null) {
            String path = this.project.getAssetStructure().getPath(category);


            for (String file : FilesystemHandler.readSubFiles(path)) {

                boolean audio = AssetStructure.isAudio(category)
                        && FilesystemHandler.isAudio(FilesystemHandler.concat(path, file));
                boolean image = AssetStructure.isImage(category)
                        && FilesystemHandler.isImage(FilesystemHandler.concat(path, file));
                boolean json = AssetStructure.isJSON(category)
                        && FilesystemHandler.isFile(FilesystemHandler.concat(path, file));

                if (audio || image || json) {
                    ImportDialog.this.elementsListView.getItems().add(file);
                }

            }
        }
    }

}
