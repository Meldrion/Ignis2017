package lu.innocence.ignis.view.gamedb;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Copyright by Fabien Steines
 * Innocence Studios 2016
 */
public class GameDBTab extends BorderPane {

    public GameDBTab(String categoryName) {

        VBox leftPanel = new VBox();
        leftPanel.setSpacing(10);
        leftPanel.setPadding(new Insets(10,10,10,10));

        Text text = new Text();
        text.setText(categoryName);
        leftPanel.getChildren().add(text);

        ListView<String> contentList = new ListView<>();
        leftPanel.getChildren().add(contentList);

        Button changeContentCount = new Button();
        changeContentCount.setText("Set Count");
        changeContentCount.setMaxWidth(Integer.MAX_VALUE);
        leftPanel.getChildren().add(changeContentCount);

        this.setLeft(leftPanel);

    }

}
