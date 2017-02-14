package lu.innocence.ignis.view.components;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by Fabien Steines
 * Last Update on: 05.02.2017.
 */
public class EventEditorVariableComponent extends VBox {

    private CheckBox variableCheckbox;
    private TextField variableTextField;
    private Spinner<Integer> variableValueField;
    private Button variableButton;
    private ComboBox<String> variableCombo;

    /**
     * Created by Fabien Steines
     * Last Update on: 05.02.2017.
     */
    public EventEditorVariableComponent() {
        this.setSpacing(5);
        HBox upperBox = new HBox();
        upperBox.setAlignment(Pos.CENTER_LEFT);
        upperBox.setSpacing(5);

        this.variableCheckbox = new CheckBox("Variable");
        this.variableCheckbox.setMinWidth(80);

        HBox variableButtonBox01 = new HBox();
        this.variableTextField = new TextField();
        this.variableTextField.setEditable(false);
        this.variableTextField.setPrefWidth(130);

        this.variableButton = new Button();
        this.variableButton.setText("...");
        variableButtonBox01.getChildren().addAll(variableTextField,variableButton);

        this.variableCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> this.update());

        this.variableCombo = new ComboBox<>();
        this.variableCombo.getItems().addAll("==","!=","<=",">=","<",">");
        this.variableCombo.setMinWidth(80);
        this.variableCombo.getSelectionModel().select(1);

        upperBox.getChildren().addAll(this.variableCheckbox, variableButtonBox01,variableCombo);

        HBox lowerBox = new HBox();
        lowerBox.setAlignment(Pos.CENTER_LEFT);

        Label valueText = new Label();
        valueText.setText("Value:");
        valueText.setMinWidth(85);

        this.variableValueField = new Spinner<>();
        this.variableValueField.setPrefWidth(130);
        lowerBox.getChildren().addAll(valueText,this.variableValueField);

        this.getChildren().addAll(upperBox,lowerBox);
        this.update();
    }


    private void update() {
        this.variableTextField.setDisable(!this.variableCheckbox.isSelected());
        this.variableButton.setDisable(!this.variableCheckbox.isSelected());
        this.variableValueField.setDisable(!this.variableCheckbox.isSelected());
        this.variableCombo.setDisable(!this.variableCheckbox.isSelected());
    }
}
