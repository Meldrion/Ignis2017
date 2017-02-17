package lu.innocence.ignis.view.eventeditor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.AssetStructure;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@gmail.com
 *         Copyright by Innocence Studios
 *         <p>
 *         <p>
 *         Last revision - 27.01.2017 - Fabien Steines
 */
public class EventEditor extends Stage {

    private TabPane mainTabber;
    private boolean accepted;

    /**
     *
     * @param parent
     * @param assetManager
     */
    public EventEditor(Stage parent, AssetStructure assetManager) {
        super();
        this.accepted = false;
        this.initOwner(parent);
        this.initModality(Modality.WINDOW_MODAL);
        this.setResizable(false);
        this.setTitle("Event Editor");
        this.buildUserInterface(assetManager);
        this.sizeToScene();
    }

    /**
     *
     */
    private void buildUserInterface(AssetStructure assetManager) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        this.setScene(scene);

        HBox topBox = new HBox();
        topBox.setSpacing(10);
        topBox.setPadding(new Insets(10,10,10,10));
        topBox.setAlignment(Pos.CENTER_LEFT);

        VBox nameBox = new VBox();
        Label nameLabel = new Label();
        nameLabel.setText("Event Name");
        TextField textFieldName = new TextField();
        nameBox.getChildren().addAll(nameLabel,textFieldName);

        VBox noteBox = new VBox();
        Label noteLabel = new Label();
        noteLabel.setText("Notes");
        TextField textFieldNote = new TextField();
        noteBox.getChildren().addAll(noteLabel,textFieldNote);

        Button newPageButton = new Button();
        newPageButton.setText("New Page");
        newPageButton.setOnAction(event -> this.createNewEventPage(assetManager));

        Button copyPageButton = new Button();
        copyPageButton.setText("Copy Page");

        Button pastePageButton = new Button();
        pastePageButton.setText("Paste Page");

        Button deltePageButton = new Button();
        deltePageButton.setText("Delete Page");

        Button clearPageButton = new Button();
        clearPageButton.setText("Clear Page");

        topBox.getChildren().addAll(nameBox,noteBox,newPageButton,copyPageButton,
                pastePageButton,deltePageButton,clearPageButton);
        root.setTop(topBox);

        // Center
        this.mainTabber = new TabPane();
        root.setCenter(mainTabber);

        // Tabs Part
        this.createNewEventPage(assetManager); // First Page


        this.createButtomBox(root);

    }

    /**
     *
     * @param root
     */
    private void createButtomBox(BorderPane root) {
        // Button Box
        VBox bottom = new VBox();
        Separator separator = new Separator();

        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.setPadding(new Insets(10,10,10,10));
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);


        Button okButton = new Button();
        okButton.setText("Accept");
        okButton.setOnAction(event -> {
            this.accepted = true;
            this.close();
        });

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> {
            this.accepted = false;
            this.close();
        });

        bottomBox.getChildren().addAll(okButton,cancelButton);

        bottom.getChildren().addAll(separator,bottomBox);
        root.setBottom(bottom);
    }

    /**
     *
     */
    private void createNewEventPage(AssetStructure assetManager) {

        int cCount = this.mainTabber.getTabs().size();
        String tabName = String.format("Page %s",cCount + 1);

        EventEditorTab editorTabContent = new EventEditorTab(tabName,this,assetManager);
        Tab editorPageTab = new Tab();
        editorPageTab.setText(tabName);
        editorPageTab.setClosable(false);
        editorPageTab.setContent(editorTabContent);

        this.mainTabber.getTabs().add(editorPageTab);
        this.mainTabber.getSelectionModel().select(editorPageTab);
    }

    /**
     *
     * @return
     */
    public boolean isAccepted() {
        return accepted;
    }

}
