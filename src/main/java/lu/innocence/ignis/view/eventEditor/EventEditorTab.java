package lu.innocence.ignis.view.eventEditor;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lu.innocence.ignis.view.components.CharViewCanvas;
import lu.innocence.ignis.view.components.EventEditorSwitchComponent;
import lu.innocence.ignis.view.components.EventEditorVariableComponent;
import lu.innocence.ignis.view.components.VisualScriptEditor;

/**
 * Created by Fabien Steines
 * Last Update on: 27.01.2017.
 */
public class EventEditorTab extends BorderPane {

    public EventEditorTab(String categoryName,Stage parent) {

        this.setPadding(new Insets(10,10,10,10));

        VBox leftBox = new VBox();
        leftBox.setSpacing(5);

        CharViewCanvas charViewCanvas = new CharViewCanvas();
        int widthForLeftObjects = 100;
        charViewCanvas.setWidth(widthForLeftObjects);
        charViewCanvas.setHeight(115);
        charViewCanvas.render();

        Button charViewChangeButton = new Button();
        charViewChangeButton.setText("Change");
        charViewChangeButton.setMinWidth(widthForLeftObjects);

        VBox conditionsPane = new VBox();
        // Switch 1
        EventEditorSwitchComponent switchHBox01 = new EventEditorSwitchComponent();
        // Switch 2
        EventEditorSwitchComponent switchHBox02 = new EventEditorSwitchComponent();
        // Varable 1
        EventEditorVariableComponent varableHBox01 = new EventEditorVariableComponent();

        conditionsPane.setSpacing(5);
        conditionsPane.getChildren().addAll(switchHBox01,switchHBox02,varableHBox01);

        TitledPane titledPaneCondition = new TitledPane("Conditions",conditionsPane);
        titledPaneCondition.setCollapsible(false);

        HBox middleBox = new HBox();
        middleBox.setSpacing(5);
        VBox middleBoxLeft = new VBox();
        middleBoxLeft.getChildren().addAll(charViewCanvas,charViewChangeButton);

        VBox middleBoxRight = new VBox();
        middleBoxRight.setSpacing(5);
        Label triggerLabel = new Label();
        triggerLabel.setText("Trigger");

        ComboBox<String> triggerComboBox = new ComboBox<>();
        triggerComboBox.getItems().addAll("Push Key","Actor Touch","Event Touch",
                "Parallel Event","Auto Start","Called Only");
        triggerComboBox.getSelectionModel().select(0);


        GridPane movementPane = new GridPane();

        movementPane.setHgap(3);
        movementPane.setVgap(3);

        ColumnConstraints column1 = new ColumnConstraints(80);
        ColumnConstraints column2 = new ColumnConstraints(130);

        movementPane.getColumnConstraints().addAll(column1, column2);
        movementPane.setPadding(new Insets(3, 10, 0, 15));

        Label movementLabel = new Label();
        movementLabel.setText("Type");

        movementPane.add(movementLabel,0,0);

        ComboBox<String> movementComboBox = new ComboBox<>();
        movementComboBox.getItems().addAll("Fixed","Random",
                "Follow","Move Away","Scripted");
        movementComboBox.getSelectionModel().select(0);

        movementPane.add(movementComboBox,1,0);

        Button defineMovementButton = new Button();
        defineMovementButton.setText("Define Route");

        movementPane.add(defineMovementButton,1,1);

        Label movementLabelSpeed = new Label();
        movementLabelSpeed.setText("Speed");

        movementPane.add(movementLabelSpeed,0,2);

        ComboBox<String> movementSpeedComboBox = new ComboBox<>();
        movementSpeedComboBox.getItems().addAll("Very slow","slow",
                "normal","Fast","Very fast");
        movementSpeedComboBox.getSelectionModel().select(2);

        movementPane.add(movementSpeedComboBox,1,2);

        Label movementLabelFreq = new Label();
        movementLabelFreq.setText("Freq");

        movementPane.add(movementLabelFreq,0,3);

        ComboBox<String> movementFreqComboBox = new ComboBox<>();
        movementFreqComboBox.getItems().addAll("Very Low","Low","Normal","High","Very High");
        movementFreqComboBox.getSelectionModel().select(2);

        movementPane.add(movementFreqComboBox,1,3);


        TitledPane titledPaneMove = new TitledPane("Movement",movementPane);
        titledPaneMove.setCollapsible(false);



        middleBoxRight.getChildren().addAll(triggerLabel,triggerComboBox, titledPaneMove);

        middleBox.getChildren().addAll(middleBoxLeft,middleBoxRight);

        leftBox.getChildren().addAll(titledPaneCondition,middleBox);
        this.setLeft(leftBox);

        BorderPane centerBox = new BorderPane();
        centerBox.setPadding(new Insets(0,0,0,10));
        this.setCenter(centerBox);
        VisualScriptEditor visualScriptEditor = new VisualScriptEditor(parent);
        centerBox.setCenter(visualScriptEditor);
    }

}
