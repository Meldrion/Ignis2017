package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.engine.Map;
import sample.engine.MapCanvas;
import sample.engine.Tileset;

import java.net.URL;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = new BorderPane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 640, 480));

        MenuBar menuBar = new MenuBar();
        // Use system menu bar
        menuBar.setUseSystemMenuBar(true);
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        Menu fileMenu = new Menu("File");

        MenuItem newProject = new MenuItem("New Project");
        MenuItem loadProject = new MenuItem("Load Project");
        MenuItem saveProject = new MenuItem("Save Project");
        MenuItem closeProject = new MenuItem("Close Project");
        MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().add(newProject);
        fileMenu.getItems().add(loadProject);
        fileMenu.getItems().add(saveProject);
        fileMenu.getItems().add(closeProject);
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(exit);


        Menu webMenu = new Menu("Edit");
        Menu sqlMenu = new Menu("SQL");

        menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);

        Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));

        final MapCanvas canvas = new MapCanvas(640,480);
        final Canvas testCanvas = new Canvas(640,480);
        canvas.linkFrontCanvas(testCanvas);

        Pane pane = new Pane(canvas,testCanvas);
        canvas.linkLayerPane(pane);

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(640  , 480);
        s1.setContent(pane);

        root.setCenter(s1);

        Map newMap =  new Map();
        newMap.setDimension(200,200);

        Tileset tileset = new Tileset();
        tileset.loadImage("tileset.png");

        newMap.setTileset(tileset);
        canvas.setMap(newMap);
        canvas.render();

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
