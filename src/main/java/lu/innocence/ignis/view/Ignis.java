package lu.innocence.ignis.view;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.component.MapCanvas;
import lu.innocence.ignis.component.MapTree;
import lu.innocence.ignis.component.TilesetCanvas;
import lu.innocence.ignis.engine.*;
import lu.innocence.ignis.event.ActiveProjectListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

public class Ignis extends Application implements ActiveProjectListener{

    private static final Logger LOGGER = LogManager.getLogger(Ignis.class);
    private MapCanvas mapCanvas;
    private TilesetCanvas tilesetCanvas;
    private MapTree mapTree;

    private void buildMainMenu(VBox topContainer) {
        MenuBar menuBar = new MenuBar();

        // Use system menu bar
        menuBar.setUseSystemMenuBar(true);

        topContainer.getChildren().add(menuBar);
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
        menuBar.setUseSystemMenuBar(true);
    }

    private void buildToolbar(VBox topContainer) {
        ToolBar toolBar = new ToolBar();  //Creates our tool-bar to hold the buttons.
        topContainer.getChildren().add(toolBar);

        Button newProjectBtn = new Button();
        newProjectBtn.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/Document-Blank-icon-24.png").getFile()));
        Button openProjectBtn = new Button();
        openProjectBtn.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/Files-icon-24.png").getFile()));
        Button saveProjectBtn = new Button();
        saveProjectBtn.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/Actions-document-save-icon.png").getFile()));

        toolBar.getItems().addAll(newProjectBtn,openProjectBtn,saveProjectBtn);
    }

    private void buildUserInterface(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 640, 480));

        VBox topContainer = new VBox();
        root.setTop(topContainer);

        buildMainMenu(topContainer);
        buildToolbar(topContainer);

        this.mapCanvas = new MapCanvas(640,480);
        Canvas uiLayer = new Canvas(640,480);
        mapCanvas.linkFrontCanvas(uiLayer);
        Pane pane = new Pane(mapCanvas,uiLayer);
        mapCanvas.linkLayerPane(pane);

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(640  , 480);
        s1.setContent(pane);
        root.setCenter(s1);

        this.tilesetCanvas = new TilesetCanvas();
        ScrollPane tilesetScroller = new ScrollPane();
        tilesetScroller.setContent(tilesetCanvas);
        tilesetScroller.setPrefWidth(280);

        tilesetCanvas.addSelecitonListener(mapCanvas);

        SplitPane leftSplitter = new SplitPane();
        leftSplitter.setPrefWidth(280);
        leftSplitter.setOrientation(Orientation.VERTICAL);
        leftSplitter.getItems().add(tilesetScroller);

        this.mapTree = new MapTree();

        leftSplitter.getItems().add(mapTree);
        leftSplitter.setDividerPosition(0,0.7);

        root.setLeft(leftSplitter);
        primaryStage.show();

    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        ProjectManager.getInstance().init();
        ProjectManager.getInstance().addActiveProjectListener(this);

        buildUserInterface(primaryStage);
/*        ProjectManager.getInstance().createProject(ProjectManager.getInstance().getRootFolder(),"ES2016","Endless Sorrow",
                "Fabien Steines","Innocence Studios");*/
        ProjectManager.getInstance().loadProject(ProjectManager.getInstance().getRootFolder() + "/ES2016");

    }

    @Override
    public void activeProjectChanged(Project p) {

        this.mapTree.setRoot(null);

        if (p != null) {

            MapManager mapManager = p.getMapManager();

            mapManager.addActiveMapListener(tilesetCanvas);
            mapManager.addActiveMapListener(mapCanvas);


            Tileset tileset = new Tileset();
            tileset.loadImage("tileset.png");

            Tileset cave = new Tileset();
            cave.loadImage("cave.png");

            Map newMap =  mapManager.createNewMap();
            newMap.setName("First map");
            newMap.setDimension(50,50);
            newMap.setTileset(tileset);

            newMap.save();

            mapManager.addMap(newMap);
            mapManager.addMap(mapManager.createNewMap());

            Map caveLevel = mapManager.createNewMap();
            caveLevel.setTileset(cave);
            caveLevel.setDimension(30,30);
            newMap.addMap(caveLevel);
            caveLevel.save();
            mapManager.addMap(mapManager.createNewMap());
            mapManager.setActiveMap(newMap);
            this.mapTree.buildFromMapManager(mapManager);
        }

    }


    // RunConfig Linux
    // -Dprism.verbose=true -Dprism.forceGPU=true
    public static void main(String args[]) {
        launch(args);
    }

}
