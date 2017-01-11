package lu.innocence.ignis.view.gamedb;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Copyright by Fabien Steines
 * Innocence Studios 2016
 */
public abstract class GameDBTab extends BorderPane {

    protected ListView<String> contentList;

    public GameDBTab(String categoryName,Stage parent) {

        VBox leftPanel = new VBox();
        leftPanel.setSpacing(10);
        leftPanel.setPadding(new Insets(10, 10, 10, 10));

        Text text = new Text();
        text.setText(categoryName);
        leftPanel.getChildren().add(text);

        this.contentList = new ListView<>();
        this.contentList.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    this.selectionChanged(this.contentList.getSelectionModel().getSelectedIndex());
                });
        leftPanel.getChildren().add(contentList);

        Button changeContentCount = new Button();
        changeContentCount.setOnAction(event -> {
            SetMaxCountDialog setMaxCountDialog = new SetMaxCountDialog(parent);
            setMaxCountDialog.setMaxCount(this.getMaxCount());
            setMaxCountDialog.showAndWait();
            if (setMaxCountDialog.accepted()) {
                this.maxCountChanged(setMaxCountDialog.getMaxCount());
            }
        });
        changeContentCount.setText("Change Maximum");
        changeContentCount.setMaxWidth(Integer.MAX_VALUE);
        leftPanel.getChildren().add(changeContentCount);

        this.setLeft(leftPanel);
    }

    public abstract void selectionChanged(int index);

    public abstract void maxCountChanged(int max);

    public abstract int getMaxCount();


}
