package lu.innocence.ignis.view.resourceView;


import javafx.stage.Stage;
import lu.innocence.ignis.ZeroStringGenerator;
import lu.innocence.ignis.engine.Tileset;
import lu.innocence.ignis.engine.TilesetManager;

/**
 * @author Fabien Steines
 */
public class TilesetResourceView extends ResourceView {

    private TilesetManager tsManager;

    /**
     * @param parentStage
     */
    public TilesetResourceView(Stage parentStage) {
        super(parentStage);
    }

    /**
     * @param tsManager
     */
    public void setTilesetManager(TilesetManager tsManager) {
        this.tsManager = tsManager;

        this.resourceList.getItems().clear();
        for (int i = 0; i < tsManager.getTilesetList().size(); i++) {
            Tileset tileset = tsManager.getTilesetAtIndex(i);
            this.resourceList.getItems().add(String.format("%s: %s",
                    ZeroStringGenerator.addZeros(i,TilesetManager.MAX_TILESET_COUNT),
                    tileset != null ? tileset.getName() : ""));
        }
    }

    /**
     * @param index
     */
    @Override
    protected void resourceSelectionChanged(int index) {
        Tileset tileset = getSelected();
        if (tileset != null) {
            this.resourceCanvas.setImage(tileset.getTilesetImage());
        } else {
            this.resourceCanvas.setImage(null);
        }
    }

    public Tileset getSelected() {
        int index = this.resourceList.getSelectionModel().getSelectedIndex();
        return (index > -1 && this.tsManager != null) ? this.tsManager.getTilesetAtIndex(index) : null;
    }
}
