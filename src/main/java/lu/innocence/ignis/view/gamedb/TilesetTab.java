package lu.innocence.ignis.view.gamedb;

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
    }

    private void fromTilesetManager(TilesetManager tsManager) {
        this.contentList.getItems().clear();
        int max = tsManager.getTilesetList().size();
        for (int i = 0; i < max; i++) {
            Tileset ts = this.tsManager.getTilesetAtIndex(i);
            if (ts != null) {
                this.contentList.getItems().add(String.format("%s:%s", String.valueOf(i), ts.getName()));
            }
        }
    }

}
