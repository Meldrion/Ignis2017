package lu.innocence.ignis.view.eventEditor;

import javafx.geometry.Insets;
import javafx.scene.control.*;
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

    /**
     *
     * @param categoryName
     * @param parent
     */
    public EventEditorTab(String categoryName,Stage parent) {

        this.setPadding(new Insets(10,10,10,10));

        VBox leftBox = new VBox();
        leftBox.setSpacing(5);

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

        TabPane tabber = new TabPane();


        HBox basicBox = new HBox();
        basicBox.setSpacing(5);
        basicBox.setPadding(new Insets(10,5,5,5));
        basicBox.getChildren().addAll(createCharView(),createTriggerPane());
        Tab triggerTab = new Tab("Basic",basicBox);
        tabber.getTabs().add(triggerTab);
        triggerTab.setClosable(false);

        Tab movementTab = new Tab("Movement",createMovementPane());
        movementTab.setClosable(false);
        tabber.getTabs().add(movementTab);

        Tab optionsTab = new Tab("Options",createOptionsPane());
        optionsTab.setClosable(false);
        tabber.getTabs().add(optionsTab);


        leftBox.getChildren().addAll(titledPaneCondition,tabber);
        this.setLeft(leftBox);

        BorderPane centerBox = new BorderPane();
        centerBox.setPadding(new Insets(0,0,0,10));
        this.setCenter(centerBox);
        VisualScriptEditor visualScriptEditor = new VisualScriptEditor(parent);
        centerBox.setCenter(visualScriptEditor);
    }

    /**
     *
     * @return
     */
    private GridPane createMovementPane() {
        GridPane movementPane = new GridPane();

        movementPane.setHgap(3);
        movementPane.setVgap(3);

        ColumnConstraints column1 = new ColumnConstraints(80);
        ColumnConstraints column2 = new ColumnConstraints(130);

        movementPane.getColumnConstraints().addAll(column1, column2);
        movementPane.setPadding(new Insets(10, 10, 3, 15));

        Label movementLabel = new Label();
        movementLabel.setText("Type");

        movementPane.add(movementLabel,0,0);


        Button defineMovementButton = new Button();
        defineMovementButton.setText("Define Route");
        defineMovementButton.setDisable(true);

        ComboBox<String> movementComboBox = new ComboBox<>();
        movementComboBox.getItems().addAll("Fixed","Random",
                "Follow","Move Away","Scripted");
        movementComboBox.getSelectionModel().select(0);
        movementComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            defineMovementButton.setDisable(!"Scripted".equals(newValue));
        });

        movementPane.add(movementComboBox,1,0);

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

        return movementPane;
    }

    /**
     *
     * @return
     */
    private TitledPane createTriggerPane() {

        RadioButton rbTriggerPush = new RadioButton();
        rbTriggerPush.setText("Push Key");

        RadioButton rbTriggerTouchActor = new RadioButton();
        rbTriggerTouchActor.setText("Actor Touch");

        RadioButton rbTriggerTouchEvent = new RadioButton();
        rbTriggerTouchEvent.setText("Event Touch");

        RadioButton rbTriggerParallel = new RadioButton();
        rbTriggerParallel.setText("Parallel Event");

        RadioButton rbTriggerAutostart = new RadioButton();
        rbTriggerAutostart.setText("Auto Start");

        VBox triggerPane = new VBox();
        triggerPane.setSpacing(5);
        triggerPane.getChildren().addAll(rbTriggerPush,rbTriggerTouchActor,
                rbTriggerTouchEvent,rbTriggerParallel,rbTriggerAutostart);
        TitledPane triggerTitledPane = new TitledPane("Trigger",triggerPane);
        triggerTitledPane.setCollapsible(false);
        return triggerTitledPane;
    }

    /**
     *
     * @return
     */
    private VBox createOptionsPane() {

        VBox optionsPane = new VBox();
        optionsPane.setSpacing(5);
        optionsPane.setPadding(new Insets(10,5,5,5));
        CheckBox moveAnimation = new CheckBox();
        moveAnimation.setText("Move Animation");
        CheckBox stopAnimation = new CheckBox();
        stopAnimation.setText("Stop Animation");
        CheckBox directionFix = new CheckBox();
        directionFix.setText("Direction Fix");
        CheckBox through = new CheckBox();
        through.setText("Through");
        CheckBox alwaysOnTop = new CheckBox();
        alwaysOnTop.setText("Always on top");
        optionsPane.getChildren().addAll(moveAnimation,stopAnimation,directionFix,through,alwaysOnTop);
        return optionsPane;
    }

    /**
     *
     * @return
     */
    private VBox createCharView() {
        VBox charView = new VBox();
        charView.setSpacing(5);
        CharViewCanvas charViewCanvas = new CharViewCanvas();
        int widthForLeftObjects = 125;
        charViewCanvas.setWidth(widthForLeftObjects);
        charViewCanvas.setHeight(125);
        charViewCanvas.render();

        Button charViewChangeButton = new Button();
        charViewChangeButton.setText("Change");
        charViewChangeButton.setMinWidth(widthForLeftObjects);

        charView.getChildren().addAll(charViewCanvas,charViewChangeButton);
        return charView;
    }
}
