package lu.innocence.ignis.view.resourceView;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.AssetStructure;
import lu.innocence.ignis.engine.FilesystemHandler;
import lu.innocence.ignis.engine.Tileset;

/**
 * @author Fabien Steines
 */
public class ImageView extends ResourceView {

    private AssetStructure assetManager;
    private String category;

    /**
     * @param parentStage
     */
    public ImageView(Stage parentStage) {
        super(parentStage);
    }

    @Override
    protected void resourceSelectionChanged(int index) {
        this.resourceCanvas.setImage(getSelected());
    }

    public void setAssetManager(AssetStructure assetManager,String category) {

        this.category = category;
        this.resourceList.getItems().clear();
        this.assetManager = assetManager;

        if (this.assetManager != null) {

            String path = this.assetManager.getPath(category);
            for (String file : FilesystemHandler.readSubFiles(path)) {
                if (AssetStructure.isImage(FilesystemHandler.concat(path,file))) {
                    this.resourceList.getItems().add(file);
                }
            }

        }
    }

    public Image getSelected() {
        int index = this.resourceList.getSelectionModel().getSelectedIndex();
        String name = this.resourceList.getSelectionModel().getSelectedItem();

        if (index > -1 && this.assetManager != null) {
            String fName = FilesystemHandler.concat(this.assetManager.getPath(category),name);
            return new Image(String.format("file:%s", fName));
        } else {
            return null;
        }

    }

}
