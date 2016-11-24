package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.Project;
import lu.innocence.ignis.view.gamedb.TilesetTab;

/**
 * Copyright by Fabien Steines
 * Innocence Studios 2016
 */
public class GameDatabase extends Stage {

    private TilesetTab tilesetTabContent;
    private boolean accepted;
    private Project project;

    public GameDatabase(Stage parentStage, Project project) {
        this.accepted = false;
        this.project = project;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Game Database");
        this.setResizable(false);
        this.buildGUI(project);
        this.initData();
        this.initOwner(parentStage);
        this.sizeToScene();
    }

    private void buildGUI(Project project) {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        this.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                this.close();
            }
        });


        TabPane mainTabber = new TabPane();

        Tab actorTab = new Tab();
        actorTab.setText("Actor");


        Tab classTab = new Tab();
        classTab.setText("Classes");

        Tab skillTab = new Tab();
        skillTab.setText("Skills");

        Tab itemTab = new Tab();
        itemTab.setText("Items");

        Tab tilesetTab = new Tab();
        tilesetTab.setText("Tileset");
        tilesetTabContent = new TilesetTab(project.getTilesetManager());
        tilesetTab.setContent(tilesetTabContent);

        mainTabber.getTabs().addAll(actorTab, classTab, itemTab, skillTab, tilesetTab);

        root.setCenter(mainTabber);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button acceptButton = new Button();
        acceptButton.setText("Ok");
        acceptButton.setOnAction(event -> {
            this.accepted = true;
            this.close();
        });

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(cancelButton, acceptButton);
        root.setBottom(bottomBar);

        this.setOnShown(event -> {
            this.tilesetTabContent.init();
        });
    }

    private void initData() {

    }

}
