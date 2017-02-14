package lu.innocence.ignis.view.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class EventEditorSwitchComponent extends HBox{

    private CheckBox switch01;
    private TextField switch01TextField;
    private Button switch01TexButton;
    private ComboBox<String> onOffCombobox;

    public EventEditorSwitchComponent() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(5);
        this.switch01 = new CheckBox("Switch");
        this.switch01.setMinWidth(80);

        HBox switchButtonBox01 = new HBox();
        this.switch01TextField = new TextField();
        this.switch01TextField.setEditable(false);
        this.switch01TextField.setPrefWidth(130);

        this.switch01TexButton = new Button();
        this.switch01TexButton.setText("...");
        switchButtonBox01.getChildren().addAll(switch01TextField,switch01TexButton);

        this.switch01.selectedProperty().addListener((observable, oldValue, newValue) -> this.update());

        this.onOffCombobox = new ComboBox<>();
        this.onOffCombobox.getItems().addAll("True","False");
        this.onOffCombobox.setMinWidth(75);
        this.onOffCombobox.getSelectionModel().select(1);
        this.getChildren().addAll(switch01, switchButtonBox01,onOffCombobox);
        this.update();
    }

    private void update() {
        this.switch01TextField.setDisable(!switch01.isSelected());
        this.switch01TexButton.setDisable(!switch01.isSelected());
        this.onOffCombobox.setDisable(!switch01.isSelected());
    }

}
