package lu.innocence.ignis.view;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
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
import lu.innocence.ignis.event.GUIButtonsUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class Ignis extends Application implements ActiveProjectListener, GUIButtonsUpdate {

    private static final Logger LOGGER = LogManager.getLogger(Ignis.class);
    private MapCanvas mapCanvas;
    private TilesetCanvas tilesetCanvas;
    private MapTree mapTree;

    private ToggleButton layer1Button;
    private ToggleButton layer2Button;
    private ToggleButton layer3Button;
    private ToggleButton layer4Button;
    private Project project;
    private ToggleButton eraseToolButton;
    private ToggleButton penToolButton;
    private ToggleButton brushToolButton;
    private ToggleButton fillToolButton;
    private Button importManagerButton;
    private Button gameDBButton;
    private Button audioManagerButton;
    private Button saveProjectBtn;

    // RunConfig Linux
    // -Dprism.verbose=true -Dprism.forceGPU=true
    public static void main(String args[]) {
        launch(args);
    }

    /**
     * @param topContainer
     * @param mainStage
     */
    private void buildMainMenu(VBox topContainer, Stage mainStage) {
        MenuBar menuBar = new MenuBar();
        // Use system menu bar
        menuBar.setUseSystemMenuBar(true);
        topContainer.getChildren().add(menuBar);
        Menu fileMenu = new Menu("File");
        MenuItem newProject = new MenuItem("New Project...");
        newProject.setOnAction(e -> new CreateProjectDialog(mainStage));
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
        Menu sqlMenu = new Menu("View");
        menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);
        menuBar.setUseSystemMenuBar(true);
    }

    /**
     * @param topContainer
     * @param mainStage
     */
    private void buildToolbar(VBox topContainer, Stage mainStage) {
        ToolBar toolBar = new ToolBar();  //Creates our tool-bar to hold the buttons.
        topContainer.getChildren().add(toolBar);
        Button newProjectBtn = new Button();
        newProjectBtn.setFocusTraversable(false);
        newProjectBtn.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/Document-Blank-icon-24.png").getFile()));
        newProjectBtn.setOnAction(e -> new CreateProjectDialog(mainStage));
        Button openProjectBtn = new Button();
        openProjectBtn.setOnAction(e -> new LoadProjectDialog(mainStage));
        openProjectBtn.setFocusTraversable(false);
        openProjectBtn.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/Files-icon-24.png").getFile()));
        this.saveProjectBtn = new Button();
        this.saveProjectBtn.setFocusTraversable(false);
        this.saveProjectBtn.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/Actions-document-save-icon.png").getFile()));
        this.saveProjectBtn.setOnAction(event -> {
            if (this.project != null) {
                this.project.saveProject();
            }
        });
        // Tools Button Group
        ToggleGroup toolsGroup = new ToggleGroup();
        this.penToolButton = new ToggleButton();
        penToolButton.setFocusTraversable(false);
        penToolButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/draw-line-2.png").getFile()));
        penToolButton.setToggleGroup(toolsGroup);
        penToolButton.setSelected(true);
        this.brushToolButton = new ToggleButton();
        brushToolButton.setFocusTraversable(false);
        brushToolButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/draw-brush.png").getFile()));
        brushToolButton.setToggleGroup(toolsGroup);
        this.fillToolButton = new ToggleButton();
        fillToolButton.setFocusTraversable(false);
        fillToolButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/draw-fill-2.png").getFile()));
        fillToolButton.setToggleGroup(toolsGroup);
        this.eraseToolButton = new ToggleButton();
        eraseToolButton.setFocusTraversable(false);
        eraseToolButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/draw-eraser-2.png").getFile()));
        eraseToolButton.setToggleGroup(toolsGroup);
        toolsGroup.selectedToggleProperty().addListener((ov, toggle, newSelected) -> {
            if (newSelected == null) {
                toggle.setSelected(true);
            } else {
                if (newSelected == penToolButton) {
                    this.mapCanvas.setActiveToolId(MapCanvas.TOOL_PEN);
                }
                if (newSelected == brushToolButton) {
                    this.mapCanvas.setActiveToolId(MapCanvas.TOOL_BRUSH);
                }
                if (newSelected == fillToolButton) {
                    this.mapCanvas.setActiveToolId(MapCanvas.TOOL_FILL);
                }
                if (newSelected == eraseToolButton) {
                    this.mapCanvas.setActiveToolId(MapCanvas.TOOL_ERASE);
                }
            }
        });
        // Tools Button Group
        ToggleGroup layersGroup = new ToggleGroup();
        this.layer1Button = new ToggleButton();
        this.layer1Button.setFocusTraversable(false);
        this.layer1Button.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/layer_bottom_24.png").getFile()));
        this.layer1Button.setToggleGroup(layersGroup);
        this.layer1Button.setSelected(true);
        this.layer2Button = new ToggleButton();
        this.layer2Button.setFocusTraversable(false);
        this.layer2Button.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/layer_middle_24.png").getFile()));
        this.layer2Button.setToggleGroup(layersGroup);
        this.layer3Button = new ToggleButton();
        this.layer3Button.setFocusTraversable(false);
        this.layer3Button.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/layer_top_24.png").getFile()));
        this.layer3Button.setToggleGroup(layersGroup);
        this.layer4Button = new ToggleButton();
        this.layer4Button.setFocusTraversable(false);
        this.layer4Button.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/layer_top_24.png").getFile()));
        this.layer4Button.setToggleGroup(layersGroup);
        layersGroup.selectedToggleProperty().addListener((ov, toggle, newSelected) -> {
            if (newSelected == null) {
                toggle.setSelected(true);
            } else {
                if (newSelected == layer1Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.TOOL_PEN);
                }
                if (newSelected == layer2Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.TOOL_BRUSH);
                }
                if (newSelected == layer3Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.TOOL_FILL);
                }
                if (newSelected == layer4Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.TOOL_ERASE);
                }
            }
        });
        this.importManagerButton = new Button();
        this.importManagerButton.setFocusTraversable(false);
        this.importManagerButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/import-icon-24.png").getFile()));
        this.importManagerButton.setOnAction(event -> {
            new ImportDialog(mainStage, this.project);
        });

        this.gameDBButton = new Button();
        this.gameDBButton.setFocusTraversable(false);
        this.gameDBButton.setOnAction(event -> {
            GameDatabase gameDatabase = new GameDatabase(mainStage, this.project);
            gameDatabase.showAndWait();
        });
        this.gameDBButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/ignis24px.png").getFile()));

        this.audioManagerButton = new Button();
        this.audioManagerButton.setFocusTraversable(false);
        this.audioManagerButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder("icons/audioManager22.png").getFile()));
        toolBar.getItems().addAll(newProjectBtn, openProjectBtn, saveProjectBtn, new Separator(),
                penToolButton, brushToolButton, fillToolButton, eraseToolButton, new Separator(),
                layer1Button, layer2Button, layer3Button, layer4Button, new Separator(), importManagerButton, gameDBButton, audioManagerButton);

    }

    /**
     * @param primaryStage
     */
    private void buildUserInterface(Stage primaryStage) {

        BorderPane root = new BorderPane();
        primaryStage.setTitle("Ignis");
        primaryStage.setScene(new Scene(root, 800, 600));

        VBox topContainer = new VBox();
        root.setTop(topContainer);

        buildMainMenu(topContainer, primaryStage);
        buildToolbar(topContainer, primaryStage);

        this.mapCanvas = new MapCanvas(640, 480);
        Canvas uiLayer = new Canvas(640, 480);
        mapCanvas.linkFrontCanvas(uiLayer);
        Pane pane = new Pane(mapCanvas, uiLayer);
        mapCanvas.linkLayerPane(pane);
        mapCanvas.setMap(null);
        mapCanvas.addGUIButtonsListener(this);

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(640, 480);
        s1.setContent(pane);
        root.setCenter(s1);

        this.tilesetCanvas = new TilesetCanvas();
        ScrollPane tilesetScroller = new ScrollPane();

        tilesetScroller.heightProperty().addListener(observable -> {
            this.tilesetCanvas.containerSizeChanged((int) tilesetScroller.getWidth(),
                    (int) tilesetScroller.getHeight());
        });

        tilesetScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tilesetScroller.setContent(tilesetCanvas);
        tilesetScroller.setPrefWidth(280);

        tilesetCanvas.addSelecitonListener(mapCanvas);

        SplitPane leftSplitter = new SplitPane();
        leftSplitter.setPrefWidth(280);
        leftSplitter.setOrientation(Orientation.VERTICAL);
        leftSplitter.getItems().add(tilesetScroller);

        this.mapTree = new MapTree(primaryStage);

        leftSplitter.getItems().add(mapTree);
        leftSplitter.setDividerPosition(0, 0.7);

        root.setLeft(leftSplitter);

        this.userInterfaceChanges(null);
        primaryStage.show();

    }

    /**
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        ProjectManager.getInstance().init();
        ProjectManager.getInstance().addActiveProjectListener(this);

        buildUserInterface(primaryStage);

    }

    /**
     * @param p
     */
    @Override
    public void activeProjectChanged(Project p) {

        this.mapTree.setRoot(null);
        this.project = p;

        if (p != null) {

            MapManager mapManager = p.getMapManager();

            mapManager.addActiveMapListener(tilesetCanvas);
            mapManager.addActiveMapListener(mapCanvas);

            Tileset tileset = new Tileset();
            tileset.loadImage("tileset.png");
            tileset.setName("Desert");

            Tileset cave = new Tileset();
            cave.loadImage("cave.png");
            cave.setName("Dark Cave");

            TilesetManager tsManager = p.getTilesetManager();
            tsManager.setTilesetMax(10);
            tsManager.setTileset(tileset, 0);
            tsManager.setTileset(cave, 1);

            mapManager.loadMapTree();
            this.mapTree.setProject(p);
        }

        this.userInterfaceChanges(p);

    }

    private void userInterfaceChanges(Project p) {

        this.saveProjectBtn.setDisable(p == null);

        this.penToolButton.setDisable(p == null);
        this.brushToolButton.setDisable(p == null);
        this.fillToolButton.setDisable(p == null);
        this.eraseToolButton.setDisable(p == null);

        this.layer1Button.setDisable(p == null);
        this.layer2Button.setDisable(p == null);
        this.layer3Button.setDisable(p == null);
        this.layer4Button.setDisable(p == null);

        this.importManagerButton.setDisable(p == null);
        this.gameDBButton.setDisable(p == null);
        this.audioManagerButton.setDisable(p == null);

    }

    /**
     * @param layerIndex
     */
    @Override
    public void activeLayerChanged(int layerIndex) {
        switch (layerIndex) {
            case MapCanvas.LAYER_1:
                this.layer1Button.setSelected(true);
                break;
            case MapCanvas.LAYER_2:
                this.layer2Button.setSelected(true);
                break;
            case MapCanvas.LAYER_3:
                this.layer3Button.setSelected(true);
                break;
            case MapCanvas.LAYER_EVENT:
                this.layer4Button.setSelected(true);
                break;
            default:
                break;
        }
    }

    /**
     * @param toolIndex
     */
    @Override
    public void activeToolChanged(int toolIndex) {
        switch (toolIndex) {
            case MapCanvas.TOOL_PEN:
                this.penToolButton.setSelected(true);
                break;
            case MapCanvas.TOOL_BRUSH:
                this.brushToolButton.setSelected(true);
                break;
            case MapCanvas.TOOL_FILL:
                this.fillToolButton.setSelected(true);
                break;
            case MapCanvas.TOOL_ERASE:
                this.eraseToolButton.setSelected(true);
                break;
            default:
                break;
        }
    }
}
