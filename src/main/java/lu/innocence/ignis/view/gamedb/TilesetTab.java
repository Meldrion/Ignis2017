package lu.innocence.ignis.view.gamedb;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
        tilesetImageInputAndButtonLayout.setMaxWidth(Integer.MAX_VALUE);
        TextField edtTilesetImage = new TextField();
        edtTilesetImage.setMaxWidth(Integer.MAX_VALUE);

        Button btnLookForTilesetImage = new Button();
        btnLookForTilesetImage.setText("...");

        tilesetImageInputAndButtonLayout.getChildren().addAll(edtTilesetImage,btnLookForTilesetImage);
        topGrid.add(tilesetImageInputAndButtonLayout,1,1);

        GridPane.setHgrow(edtTilesetName, Priority.ALWAYS);
        GridPane.setHgrow(tilesetImageInputAndButtonLayout, Priority.ALWAYS);

        centerBox.getChildren().add(topGrid);

        HBox centerHPanel = new HBox();

        // Terrain
        GridPane innerLeftPanel = new GridPane();
        Text lblTerrain = new Text();
        lblTerrain.setText("Terrain 01:");
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
        centerHPanel.getChildren().add(tilesetScroller);

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
