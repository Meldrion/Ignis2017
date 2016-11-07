package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.component.MapTree;
import sample.engine.Map;
import sample.component.MapCanvas;
import sample.engine.MapManager;
import sample.engine.Project;
import sample.engine.Tileset;
import sample.component.TilesetCanvas;

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

        MenuItem newProject = new MenuItem("New Project...");
        newProject.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
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

        final MapCanvas mapCanvas = new MapCanvas(640,480);
        final Canvas testCanvas = new Canvas(640,480);
        mapCanvas.linkFrontCanvas(testCanvas);

        Pane pane = new Pane(mapCanvas,testCanvas);
        mapCanvas.linkLayerPane(pane);

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(640  , 480);
        s1.setContent(pane);

        root.setCenter(s1);

        Map newMap =  new Map();
        newMap.setDimension(50,50);

        Tileset tileset = new Tileset();
        tileset.loadImage("tileset.png");

        Tileset cave = new Tileset();
        cave.loadImage("cave.png");

        newMap.setTileset(tileset);


        TilesetCanvas tilesetCanvas = new TilesetCanvas();
        ScrollPane tilesetScroller = new ScrollPane();
        tilesetScroller.setContent(tilesetCanvas);
        tilesetScroller.setPrefWidth(280);

        MapManager mapManager = new MapManager();
        mapManager.addActiveMapListener(tilesetCanvas);
        mapManager.addActiveMapListener(mapCanvas);

        mapManager.setActiveMap(newMap);

        tilesetCanvas.addSelecitonListener(mapCanvas);

        SplitPane leftSplitter = new SplitPane();
        leftSplitter.setPrefWidth(280);
        leftSplitter.setOrientation(Orientation.VERTICAL);
        leftSplitter.getItems().add(tilesetScroller);

        mapManager.addMap(newMap);
        mapManager.addMap(new Map());
        Map caveLevel = new Map();
        caveLevel.setTileset(cave);
        caveLevel.setDimension(30,30);
        newMap.addMap(caveLevel);
        mapManager.addMap(new Map());


        MapTree mapTree = new MapTree();
        mapTree.buildFromMapManager(mapManager);
        leftSplitter.getItems().add(mapTree);
        leftSplitter.setDividerPosition(0,0.7);

        root.setLeft(leftSplitter);
        primaryStage.show();

        Project p = new Project();
        p.create("/home/fabien/Desktop","EndlessSorrow","Endless Sorrow","Fabien Steines","Innocence Studios");

    }

    public static void main(String[] args) {
        launch(args);
    }

    // RunConfig Linux
    // -Dprism.verbose=true -Dprism.forceGPU=true
}
