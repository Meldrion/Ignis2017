package lu.innocence.ignis.view.eventEditor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - 27.01.2017 - Fabien Steines
 */
public class EventEditor extends Stage {

    /**
     *
     * @param parent
     */
    public EventEditor(Stage parent) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Event Editor");
        this.buildUserInterface();
        this.setResizable(false);
        this.initOwner(parent);
        this.sizeToScene();
    }

    /**
     *
     */
    private void buildUserInterface() {
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
        TabPane mainTabber = new TabPane();
        root.setCenter(mainTabber);

        // Tabs Part
        EventEditorTab editorTabContent = new EventEditorTab("Page 1 ",this);
        Tab editorPageTab = new Tab();
        editorPageTab.setText("Page 01");
        editorPageTab.setClosable(false);
        editorPageTab.setContent(editorTabContent);

        mainTabber.getTabs().add(editorPageTab);

        // Button Box
        VBox bottom = new VBox();
        Separator separator = new Separator();

        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.setPadding(new Insets(10,10,10,10));
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);


        Button okButton = new Button();
        okButton.setText("Accept");

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");

        bottomBox.getChildren().addAll(okButton,cancelButton);

        bottom.getChildren().addAll(separator,bottomBox);
        root.setBottom(bottom);


    }

}
