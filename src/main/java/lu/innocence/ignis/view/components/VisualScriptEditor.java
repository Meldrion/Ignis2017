package lu.innocence.ignis.view.components;

import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Fabien Steines
 * Last Update on: 03.02.2017.
 */
public class VisualScriptEditor extends TreeView<String> {

    private static final Logger LOGGER = LogManager.getLogger(VisualScriptEditor.class);

    public VisualScriptEditor(Stage parentStage) {
        this.setMinWidth(450);
    }
}
