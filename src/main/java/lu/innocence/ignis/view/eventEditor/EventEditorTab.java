package lu.innocence.ignis.view.eventEditor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lu.innocence.ignis.component.CharViewCanvas;
import lu.innocence.ignis.component.EventEditorSwitchComponent;

/**
 * Created by Fabien Steines
 * Last Update on: 27.01.2017.
 */
public class EventEditorTab extends BorderPane {

    public EventEditorTab(String categoryName,Stage parent) {

        this.setPadding(new Insets(10,10,10,10));

        BorderPane centerBox = new BorderPane();
        centerBox.setPadding(new Insets(10,10,10,10));


        VBox leftBox = new VBox();


        Text charViewLabel = new Text();
        charViewLabel.setText("Character Image");

        CharViewCanvas charViewCanvas = new CharViewCanvas();
        int widthForLeftObjects = 120;
        charViewCanvas.setWidth(widthForLeftObjects);
        charViewCanvas.setHeight(150);
        charViewCanvas.render();

        Button charViewChangeButton = new Button();
        charViewChangeButton.setText("Change");
        charViewChangeButton.setMinWidth(widthForLeftObjects);

        VBox conditionsPane = new VBox();
        // Switch 1
        EventEditorSwitchComponent switchHBox01 = new EventEditorSwitchComponent();
        // Switch 2
        EventEditorSwitchComponent switchHBox02 = new EventEditorSwitchComponent();

        conditionsPane.setSpacing(5);
        conditionsPane.getChildren().addAll(switchHBox01,switchHBox02);


        TitledPane titledPaneCondition = new TitledPane("Conditions",conditionsPane);
        titledPaneCondition.setCollapsible(false);

        leftBox.getChildren().addAll(titledPaneCondition,charViewLabel,charViewCanvas,charViewChangeButton);
        this.setLeft(leftBox);

        this.setCenter(centerBox);
    }

}
