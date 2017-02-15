package lu.innocence.ignis.view.eventeditor;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;



/**
 * Created by Fabien Steines
 * Last Update on: 15.02.2017.
 */
public class EventEditorScriptDialogMain extends Stage {

    /**
     *
     * @param parent
     */
    public EventEditorScriptDialogMain(Stage parent) {
        super();
        this.initOwner(parent);
        this.initModality(Modality.WINDOW_MODAL);
        this.setResizable(false);
        this.setTitle("Script");
        this.buildUserInterface();
        this.sizeToScene();
    }

    /**
     *
     */
    private void buildUserInterface() {
        GridPane root = new GridPane();
        Scene scene = new Scene(root);

        this.setScene(scene);

    }

}
