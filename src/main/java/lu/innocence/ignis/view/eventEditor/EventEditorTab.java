package lu.innocence.ignis.view.eventEditor;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lu.innocence.ignis.component.CharViewCanvas;

/**
 * Created by Fabien Steines
 * Last Update on: 27.01.2017.
 */
public class EventEditorTab extends BorderPane {

    private final int widthForLeftObjects = 120;

    public EventEditorTab(String categoryName,Stage parent) {

        HBox centerBox = new HBox();
        centerBox.setPadding(new Insets(10,10,10,10));
        VBox leftBox = new VBox();
        leftBox.setSpacing(10);
        ToggleButton codeExecButton = new ToggleButton();
        codeExecButton.setText("Executed Code");
        codeExecButton.setMinWidth(widthForLeftObjects);
        codeExecButton.setSelected(true);
        ToggleButton triggerButton = new ToggleButton();
        triggerButton.setText("Trigger");
        triggerButton.setMinWidth(widthForLeftObjects);
        ToggleButton conditionButton = new ToggleButton();
        conditionButton.setText("Conditions");
        conditionButton.setMinWidth(widthForLeftObjects);
        ToggleButton behaviourButton = new ToggleButton();
        behaviourButton.setText("Behaviour");
        behaviourButton.setMinWidth(widthForLeftObjects);

        ToggleGroup eventDesignerGroup = new ToggleGroup();
        eventDesignerGroup.getToggles().addAll(codeExecButton,triggerButton,conditionButton,behaviourButton);


        Pane spacer = new Pane();
        spacer.setMinHeight(25);

        Text charViewLabel = new Text();
        charViewLabel.setText("Character Image");

        CharViewCanvas charViewCanvas = new CharViewCanvas();
        charViewCanvas.setWidth(widthForLeftObjects);
        charViewCanvas.setHeight(150);
        charViewCanvas.render();

        Button charViewChangeButton = new Button();
        charViewChangeButton.setText("Change");
        charViewChangeButton.setMinWidth(widthForLeftObjects);

        leftBox.getChildren().addAll(codeExecButton,triggerButton,conditionButton,behaviourButton,
                spacer,charViewLabel,charViewCanvas,charViewChangeButton);

        centerBox.getChildren().addAll(leftBox);
        this.setCenter(centerBox);
    }

}
