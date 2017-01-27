package lu.innocence.ignis.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

        HBox hLayout = new HBox();

        Button okButton = new Button();
        okButton.setText("Accept");

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");

        hLayout.getChildren().addAll(okButton,cancelButton);
        root.setBottom(hLayout);
    }

}
