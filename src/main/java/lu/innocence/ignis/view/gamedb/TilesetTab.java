package lu.innocence.ignis.view.gamedb;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lu.innocence.ignis.ZeroStringGenerator;
import lu.innocence.ignis.component.TilesetManagerCanvas;
import lu.innocence.ignis.engine.*;
import lu.innocence.ignis.view.resourceView.ImageView;

/**
 * Copyright by Fabien Steines
 * Innocence Studios 2016
 */
public class TilesetTab extends GameDBTab {

    private final TilesetManagerCanvas tsManagerCanvas;
    private final TextField edtTilesetImage;
    private final TextField edtTilesetName;
    private TilesetManager tsManager;
    private Tileset tileset;

    public TilesetTab(Project project, Stage parent) {
        super("Tileset List:",parent);
        this.tsManager = project.getTilesetManager();
        VBox centerBox = new VBox();

        this.tsManagerCanvas = new TilesetManagerCanvas();

        GridPane topGrid = new GridPane();
        topGrid.setHgap(10);
        topGrid.setVgap(10);
        topGrid.setPadding(new Insets(10, 0, 10, 0));
        topGrid.setMaxWidth(Integer.MAX_VALUE);

        Text tilesetName = new Text();
        tilesetName.setText("Tileset Name:");
        topGrid.add(tilesetName, 0, 0);
        this.edtTilesetName = new TextField();
        this.edtTilesetName.setMaxWidth(Integer.MAX_VALUE);
        topGrid.add(this.edtTilesetName, 1, 0);

        Text lblTilesetImage = new Text();
        lblTilesetImage.setText("Tileset Image:");
        topGrid.add(lblTilesetImage, 0, 1);

        HBox tilesetImageInputAndButtonLayout = new HBox();
        tilesetImageInputAndButtonLayout.setSpacing(10);
        tilesetImageInputAndButtonLayout.setMaxWidth(Integer.MAX_VALUE);
        this.edtTilesetImage = new TextField();
        this.edtTilesetImage.setMaxWidth(Integer.MAX_VALUE);
        this.edtTilesetImage.setEditable(false);

        Button btnLookForTilesetImage = new Button();
        btnLookForTilesetImage.setText("...");
        btnLookForTilesetImage.setOnAction(event -> {
            ImageView imageView = new ImageView(parent);
            imageView.setAssetManager(project.getAssetStructure(), AssetStructure.TILESET);
            imageView.showAndWait();
            if (imageView.isAccepted()) {
                this.edtTilesetImage.setText(imageView.getSelectedName());
                String path = FilesystemHandler.concat(project.getAssetStructure().getPath(AssetStructure.TILESET),
                        imageView.getSelectedName());
                this.tileset.loadImage(path);
                this.tsManagerCanvas.setTileset(this.tileset);
            }
        });

        tilesetImageInputAndButtonLayout.getChildren().addAll(edtTilesetImage, btnLookForTilesetImage);
        topGrid.add(tilesetImageInputAndButtonLayout, 1, 1);
        centerBox.getChildren().add(topGrid);

        HBox.setHgrow(edtTilesetImage, Priority.ALWAYS);
        GridPane.setHgrow(edtTilesetName, Priority.ALWAYS);


        HBox centerHPanel = new HBox();
        centerBox.setPadding(new Insets(10, 10, 10, 10));
        centerBox.setSpacing(10);
        centerHPanel.setSpacing(10);

        // Terrain
        GridPane innerLeftPanel = new GridPane();
        Text lblTerrain = new Text();
        lblTerrain.setText("Terrain");

        innerLeftPanel.add(lblTerrain, 0, 0, 2, 1);
        innerLeftPanel.setHgap(10);
        innerLeftPanel.setVgap(10);

        for (int i = 0; i < 8; i++) {
            TextField edtTerrain = new TextField();
            innerLeftPanel.add(edtTerrain, 0, 1 + i);

            Button btnTerrain = new Button();
            btnTerrain.setText("...");
            innerLeftPanel.add(btnTerrain, 1, 1 + i);
        }

        centerHPanel.getChildren().add(innerLeftPanel);

        // TilesetCanvas with Scrollbox
        ScrollPane tilesetScroller = new ScrollPane();
        tilesetScroller.setPrefWidth(280);
        tilesetScroller.setMaxHeight(Integer.MAX_VALUE);

        tilesetScroller.setContent(tsManagerCanvas);

        VBox rightBox = new VBox();
        rightBox.setSpacing(10);

        ToggleButton btnPassage = new ToggleButton();
        btnPassage.setText("Passage");
        btnPassage.setPrefWidth(100);
        rightBox.getChildren().add(btnPassage);

        ToggleButton btnPriority = new ToggleButton();
        btnPriority.setText("Priority");
        btnPriority.setPrefWidth(100);
        rightBox.getChildren().add(btnPriority);

        ToggleButton btnBush = new ToggleButton();
        btnBush.setText("Bush");
        btnBush.setPrefWidth(100);
        rightBox.getChildren().add(btnBush);

        ToggleButton btnCounter = new ToggleButton();
        btnCounter.setText("Counter");
        btnCounter.setPrefWidth(100);
        rightBox.getChildren().add(btnCounter);

        ToggleButton btnTerrainTag = new ToggleButton();
        btnTerrainTag.setText("Terain Tag");
        btnTerrainTag.setPrefWidth(100);
        rightBox.getChildren().add(btnTerrainTag);

        centerHPanel.getChildren().add(tilesetScroller);
        centerHPanel.getChildren().add(rightBox);
        centerBox.getChildren().add(new Separator());
        centerBox.getChildren().add(centerHPanel);

        centerBox.setMaxHeight(Integer.MAX_VALUE);
        VBox.setVgrow(centerHPanel, Priority.ALWAYS);

        this.setCenter(centerBox);

    }

    private void initTileset(Tileset tileset) {
        tsManagerCanvas.setTileset(tileset);
        this.edtTilesetName.setText(tileset.getName());
        this.edtTilesetImage.setText(tileset.getImageName());
        this.tileset = tileset;
    }

    @Override
    public void selectionChanged(int index) {
        if (index > -1) {
            this.initTileset(this.tsManager.getTilesetAtIndex(index));
        }
    }

    @Override
    public void maxCountChanged(int max) {
        this.tsManager.setTilesetMax(max);
        this.init();
    }

    @Override
    public int getMaxCount() {
        return this.tsManager.getTilesetList().size();
    }

    public void init() {
        this.contentList.getItems().clear();
        int max = tsManager.getTilesetList().size();
        for (int i = 0; i < max; i++) {
            Tileset ts = this.tsManager.getTilesetAtIndex(i);
            if (ts != null) {
                this.contentList.getItems().add(String.format("%s: %s",
                        ZeroStringGenerator.addZeros(i,9999), ts.getName()));
            }
        }

        if (max > 0) {
            this.contentList.getSelectionModel().selectFirst();
        }
    }
}