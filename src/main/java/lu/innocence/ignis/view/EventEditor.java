package lu.innocence.ignis.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

        VBox nameBox = new VBox();
        Text nameLabel = new Text();
        nameLabel.setText("Event Name");
        TextField textFieldName = new TextField();
        nameBox.getChildren().addAll(nameLabel,textFieldName);

        VBox noteBox = new VBox();
        Text noteLabel = new Text();
        noteLabel.setText("Notes");
        TextField textFieldNote = new TextField();
        noteBox.getChildren().addAll(noteLabel,textFieldNote);

        topBox.getChildren().addAll(nameBox,noteBox);
        // Center Part
        root.setTop(topBox);

        // Button Box
        HBox hLayout = new HBox();
        hLayout.setAlignment(Pos.BOTTOM_RIGHT);

        Button okButton = new Button();
        okButton.setText("Accept");

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");

        hLayout.getChildren().addAll(okButton,cancelButton);
        root.setBottom(hLayout);


    }

}
