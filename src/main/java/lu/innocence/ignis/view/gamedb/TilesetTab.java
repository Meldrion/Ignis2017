package lu.innocence.ignis.view.gamedb;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lu.innocence.ignis.component.TilesetManagerCanvas;
import lu.innocence.ignis.engine.Tileset;
import lu.innocence.ignis.engine.TilesetManager;

/**
 * Copyright by Fabien Steines
 * Innocence Studios 2016
 */
public class TilesetTab extends GameDBTab {

    private TilesetManager tsManager;

    public TilesetTab(TilesetManager tsManager) {
        super("Tileset List:");
        this.tsManager = tsManager;
        this.fromTilesetManager(tsManager);

        VBox centerBox = new VBox();

        GridPane topGrid = new GridPane();
        topGrid.setHgap(10);
        topGrid.setVgap(10);
        topGrid.setPadding(new Insets(10,10,10,10));
        topGrid.setMaxWidth(Integer.MAX_VALUE);

        Text tilesetName = new Text();
        tilesetName.setText("Tileset Name:");
        topGrid.add(tilesetName,0,0);
        TextField edtTilesetName = new TextField();
        edtTilesetName.setMaxWidth(Integer.MAX_VALUE);
        topGrid.add(edtTilesetName,1,0);

        Text lblTilesetImage = new Text();
        lblTilesetImage.setText("Tileset Image:");
        topGrid.add(lblTilesetImage,0,1);

        HBox tilesetImageInputAndButtonLayout = new HBox();
        tilesetImageInputAndButtonLayout.setSpacing(10);
        tilesetImageInputAndButtonLayout.setMaxWidth(Integer.MAX_VALUE);
        TextField edtTilesetImage = new TextField();
        edtTilesetImage.setMaxWidth(Integer.MAX_VALUE);
        edtTilesetImage.setEditable(false);

        Button btnLookForTilesetImage = new Button();
        btnLookForTilesetImage.setText("...");

        tilesetImageInputAndButtonLayout.getChildren().addAll(edtTilesetImage,btnLookForTilesetImage);
        topGrid.add(tilesetImageInputAndButtonLayout,1,1);
        centerBox.getChildren().add(topGrid);

        HBox.setHgrow(edtTilesetImage, Priority.ALWAYS);
        GridPane.setHgrow(edtTilesetName, Priority.ALWAYS);


        HBox centerHPanel = new HBox();
        centerBox.setPadding(new Insets(10,10,10,10));
        centerHPanel.setSpacing(10);

        // Terrain
        GridPane innerLeftPanel = new GridPane();
        Text lblTerrain = new Text();
        lblTerrain.setText("Terrain");
        innerLeftPanel.add(lblTerrain,0,0,2,1);
        TextField edtTerrain = new TextField();
        innerLeftPanel.add(edtTerrain,0,1);
        Button btnTerrain = new Button();
        btnTerrain.setText("...");
        innerLeftPanel.add(btnTerrain,1,1);
        centerHPanel.getChildren().add(innerLeftPanel);

        // TilesetCanvas with Scrollbox
        ScrollPane tilesetScroller = new ScrollPane();
        tilesetScroller.setPrefWidth(280);
        TilesetManagerCanvas tsManagerCanvas = new TilesetManagerCanvas();
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
        btnTerrain.setPrefWidth(100);
        rightBox.getChildren().add(btnTerrainTag);

        centerHPanel.getChildren().add(tilesetScroller);
        centerHPanel.getChildren().add(rightBox);
        centerBox.getChildren().add(centerHPanel);

        this.setCenter(centerBox);
    }

    private void fromTilesetManager(TilesetManager tsManager) {
        this.contentList.getItems().clear();
        int max = tsManager.getTilesetList().size();
        for (int i=0;i<max;i++) {
            Tileset ts = this.tsManager.getTilesetAtIndex(i);
            if (ts != null) {
                this.contentList.getItems().add(String.format("%s:%s",String.valueOf(i),ts.getName()));
            }
        }
    }

}
