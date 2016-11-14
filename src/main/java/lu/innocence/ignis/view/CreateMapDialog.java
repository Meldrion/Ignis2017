package lu.innocence.ignis.view;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Fabien Steines
 */
public class CreateMapDialog extends Stage {

    public CreateMapDialog(Stage parentStage) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Create Map...");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.initOwner(parentStage);
        this.sizeToScene();
        this.show();
    }

    private void buildGUI() {

    }

    private void initData() {

    }

}
