package lu.innocence.ignis.component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by Fabien Steines
 * Last Update on: 05.02.2017.
 */
public class EventEditorVariableComponent extends HBox {

    private CheckBox variableCheckbox;
    private TextField variableTextField;
    private Button variableButton;

    /**
     * Created by Fabien Steines
     * Last Update on: 05.02.2017.
     */
    public EventEditorVariableComponent() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(5);
        this.variableCheckbox = new CheckBox("Variable");
        this.variableCheckbox.setMinWidth(75);

        HBox variableButtonBox01 = new HBox();
        this.variableTextField = new TextField();

        this.variableButton = new Button();
        this.variableButton.setText("...");
        variableButtonBox01.getChildren().addAll(variableTextField,variableButton);

        this.variableCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> this.update());

/*        this.onOffCombobox = new ComboBox<>();
        this.onOffCombobox.getItems().addAll("True","False");
        this.onOffCombobox.getSelectionModel().select(1);
        this.getChildren().addAll(switch01, variableButtonBox01,onOffCombobox);*/

        this.getChildren().addAll(this.variableCheckbox, variableButtonBox01);
        this.update();
    }


    private void update() {
/*        this.switch01TextField.setDisable(!switch01.isSelected());
        this.switch01TexButton.setDisable(!switch01.isSelected());
        this.onOffCombobox.setDisable(!switch01.isSelected());*/
    }
}
