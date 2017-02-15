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
import lu.innocence.ignis.engine.AudioManager;
import lu.innocence.ignis.engine.MapManager;
import lu.innocence.ignis.engine.Project;
import lu.innocence.ignis.engine.ProjectManager;
import lu.innocence.ignis.event.ActiveProjectListener;
import lu.innocence.ignis.event.GUIButtonsUpdate;
import lu.innocence.ignis.view.components.*;
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
    private Button openProjectBtn;
    private ButtonBase newProjectBtn;

    // RunConfig Linux
    // -Dprism.verbose=true -Dprism.forceGPU=true
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param topContainer Container for the Main Menu
     * @param mainStage The MainStage
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
     * @param topContainer Container for the Toolbar
     * @param mainStage The MainStage
     */
    private void buildToolbar(VBox topContainer, Stage mainStage) {
        ToolBar toolBar = new ToolBar();  //Creates our tool-bar to hold the buttons.
        topContainer.getChildren().add(toolBar);

        buildDialogButtons(mainStage);
        buildToolButtons();
        buildLayerButtons();

        toolBar.getItems().addAll(newProjectBtn, openProjectBtn, saveProjectBtn, new Separator(),
                penToolButton, brushToolButton, fillToolButton, eraseToolButton, new Separator(),
                layer1Button, layer2Button, layer3Button, layer4Button, new Separator(), importManagerButton, gameDBButton, audioManagerButton);

    }

    /**
     *
     * @param mainStage The MainStage
     */
    private void buildDialogButtons(Stage mainStage) {
        this.newProjectBtn = new Button();
        this.newProjectBtn.setFocusTraversable(false);

        this.newProjectBtn.setGraphic(new ImageView(IgnisGlobals.getIconNewProject()));
        this.newProjectBtn.setOnAction(e -> openCreateProjectWindow(mainStage));

        this.openProjectBtn = new Button();
        this.openProjectBtn.setOnAction(e -> openLoadProjectWindow(mainStage));
        this.openProjectBtn.setFocusTraversable(false);
        this.openProjectBtn.setGraphic(new ImageView(IgnisGlobals.getIconLoadProject()));
        this.saveProjectBtn = new Button();
        this.saveProjectBtn.setFocusTraversable(false);
        this.saveProjectBtn.setGraphic(new ImageView(IgnisGlobals.getIconSaveProject()));
        this.saveProjectBtn.setOnAction(event -> saveProject());

        this.importManagerButton = new Button();
        this.importManagerButton.setFocusTraversable(false);
        this.importManagerButton.setGraphic(new ImageView(IgnisGlobals.getIconImport()));
        this.importManagerButton.setOnAction(event -> openImportWindow(mainStage));

        this.gameDBButton = new Button();
        this.gameDBButton.setFocusTraversable(false);
        this.gameDBButton.setOnAction(event -> openGameDatabase(mainStage));
        this.gameDBButton.setGraphic(new ImageView(IgnisGlobals.getIconIgnis24px()));

        this.audioManagerButton = new Button();
        this.audioManagerButton.setFocusTraversable(false);
        this.audioManagerButton.setOnAction(event -> openAudioDialog(mainStage));
        this.audioManagerButton.setGraphic(new ImageView(IgnisGlobals.getIconAudioManager()));
    }

    /**
     *
     */
    private void buildToolButtons() {
        // Tools Button Group
        ToggleGroup toolsGroup = new ToggleGroup();
        this.penToolButton = new ToggleButton();
        penToolButton.setFocusTraversable(false);
        penToolButton.setGraphic(new ImageView(IgnisGlobals.getIconPen()));
        penToolButton.setToggleGroup(toolsGroup);
        penToolButton.setSelected(true);
        this.brushToolButton = new ToggleButton();
        brushToolButton.setFocusTraversable(false);
        brushToolButton.setGraphic(new ImageView(IgnisGlobals.getIconBrush()));
        brushToolButton.setToggleGroup(toolsGroup);
        this.fillToolButton = new ToggleButton();
        fillToolButton.setFocusTraversable(false);
        fillToolButton.setGraphic(new ImageView(IgnisGlobals.getIconFill()));
        fillToolButton.setToggleGroup(toolsGroup);
        this.eraseToolButton = new ToggleButton();
        eraseToolButton.setFocusTraversable(false);
        eraseToolButton.setGraphic(new ImageView(IgnisGlobals.getIconErase()));
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
    }

    /**
     *
     */
    private void buildLayerButtons() {
        // Tools Button Group
        ToggleGroup layersGroup = new ToggleGroup();
        this.layer1Button = new ToggleButton();
        this.layer1Button.setFocusTraversable(false);
        this.layer1Button.setGraphic(new ImageView(IgnisGlobals.getIconLayer1()));
        this.layer1Button.setToggleGroup(layersGroup);
        this.layer1Button.setSelected(true);
        this.layer2Button = new ToggleButton();
        this.layer2Button.setFocusTraversable(false);
        this.layer2Button.setGraphic(new ImageView(IgnisGlobals.getIconLayer2()));
        this.layer2Button.setToggleGroup(layersGroup);
        this.layer3Button = new ToggleButton();
        this.layer3Button.setFocusTraversable(false);
        this.layer3Button.setGraphic(new ImageView(IgnisGlobals.getIconLayer3()));
        this.layer3Button.setToggleGroup(layersGroup);
        this.layer4Button = new ToggleButton();
        this.layer4Button.setFocusTraversable(false);
        this.layer4Button.setGraphic(new ImageView(IgnisGlobals.getIconLayerEvent()));
        this.layer4Button.setToggleGroup(layersGroup);
        layersGroup.selectedToggleProperty().addListener((ov, toggle, newSelected) -> {
            if (newSelected == null) {
                toggle.setSelected(true);
            } else {
                if (newSelected == layer1Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.LAYER_1);
                }
                if (newSelected == layer2Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.LAYER_2);
                }
                if (newSelected == layer3Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.LAYER_3);
                }
                if (newSelected == layer4Button) {
                    this.mapCanvas.setActiveLayerId(MapCanvas.LAYER_EVENT);
                }
            }
        });
    }


    /**
     * @param primaryStage The Main Stage
     */
    private void buildUserInterface(Stage primaryStage) {

        BorderPane root = new BorderPane();
        primaryStage.setTitle("Ignis");
        primaryStage.setScene(new Scene(root, 800, 600));

        VBox topContainer = new VBox();
        root.setTop(topContainer);

        buildMainMenu(topContainer, primaryStage);
        buildToolbar(topContainer, primaryStage);

        this.mapCanvas = new MapCanvas(primaryStage,640, 480);
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

        tilesetScroller.heightProperty().addListener(observable ->
                this.tilesetCanvas.containerSizeChanged((int) tilesetScroller.getWidth(),
                (int) tilesetScroller.getHeight()));

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

        Pane bottomBox = new Pane();
        bottomBox.setPrefHeight(20);
        root.setBottom(bottomBox);

        this.userInterfaceChanges(null);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    /**
     * @param primaryStage The Main Stage
     * @throws Exception if building process went wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        LOGGER.info("Init Ignis Game Editor");

        ProjectManager.getInstance().init();
        ProjectManager.getInstance().addActiveProjectListener(this);
        primaryStage.getIcons().add(IgnisGlobals.getIconIgnis());

        // Build Stage
        buildUserInterface(primaryStage);

        // Center on Screen
        primaryStage.centerOnScreen();

        AudioManager.initAudioSystem();
        primaryStage.setOnCloseRequest(event -> {
            if (IgnisDialogs.openConfirmationDialog(
                    primaryStage,
                    "Exit Program",
                    "Are you sure that you want to close Ignis ?",
                    "Any unsaved changes will be lost !")) {
                AudioManager.shutdownAudioSystem();
            } else {
                event.consume(); // This prevents the closing action
            }
        });

    }

    /**
     * @param p The project that is now active... can be null
     */
    @Override
    public void activeProjectChanged(Project p) {

        this.mapTree.setRoot(null);
        this.project = p;

        if (p != null) {

            MapManager mapManager = p.getMapManager();
            mapManager.addActiveMapListener(tilesetCanvas);
            mapManager.addActiveMapListener(mapCanvas);

            mapCanvas.setAssetManager(p.getAssetStructure());

            this.mapTree.setProject(p);
        } else {
            mapCanvas.setAssetManager(null);
        }

        this.userInterfaceChanges(p);

    }

    /**
     *
     * @param p The project that is now active... can be null
     */
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
     * @param layerIndex the currently active Layer
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
     * @param toolIndex the currently active Tool Index
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


    /**
     *
     * @param mainStage The MainStage
     */
    private void openCreateProjectWindow(Stage mainStage) {
        CreateProjectDialog createProjectDialog = new CreateProjectDialog(mainStage);
        CenterWindowOnParent.center(mainStage,createProjectDialog);
        createProjectDialog.show();
    }

    /**
     *
     * @param mainStage The MainStage
     */
    private void openLoadProjectWindow(Stage mainStage) {
        LoadProjectDialog loadProjectDialog = new LoadProjectDialog(mainStage);
        CenterWindowOnParent.center(mainStage,loadProjectDialog);
        loadProjectDialog.show();
    }

    /**
     *
     */
    private void saveProject() {
        if (this.project != null) {
            this.project.saveProject();
        }
    }

    /**
     *
     * @param mainStage The MainStage
     */
    private void openImportWindow(Stage mainStage) {
        ImportDialog importDialog = new ImportDialog(mainStage, this.project);
        CenterWindowOnParent.center(mainStage,importDialog);
        importDialog.show();
    }

    /**
     *
     * @param mainStage The MainStage
     */
    private void openAudioDialog(Stage mainStage) {
        AudioDialog audioDialog = new AudioDialog(mainStage,this.project.getAudioManager());
        CenterWindowOnParent.center(mainStage,audioDialog);
        audioDialog.show();

    }

    /**
     *
     * @param mainStage The MainStage
     */
    private void openGameDatabase(Stage mainStage) {
        GameDatabase gameDatabase = new GameDatabase(mainStage, this.project);
        CenterWindowOnParent.center(mainStage,gameDatabase);
        gameDatabase.show();
    }


}
