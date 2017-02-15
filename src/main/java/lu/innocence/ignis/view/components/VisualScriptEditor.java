package lu.innocence.ignis.view.components;

import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import lu.innocence.ignis.view.eventeditor.EventEditor;
import lu.innocence.ignis.view.eventeditor.EventEditorScriptDialogMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Fabien Steines
 * Last Update on: 03.02.2017.
 */
public class VisualScriptEditor extends TreeView<String> {

    private static final Logger LOGGER = LogManager.getLogger(VisualScriptEditor.class);

    public VisualScriptEditor(Stage parentStage) {
        LOGGER.info("Visual Script Editor Created");
        this.setMinWidth(450);
        this.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                openEventDialog(parentStage);
            }
        });
    }

    private void openEventDialog(Stage parent) {
        EventEditorScriptDialogMain eventEditorScriptDialogMain = new EventEditorScriptDialogMain(parent);
        CenterWindowOnParent.center(parent,eventEditorScriptDialogMain);
        eventEditorScriptDialogMain.show();
    }

}
