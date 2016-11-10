package lu.innocence.ignis.view.components;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.AssetStructure;

/**
 * @author Fabien Steines
 */
public class CustomListCell extends ListCell<String> {

    private static final String FILE_PREF = "file:";
    private static final Image imageIcon  =
            new Image(FILE_PREF + IgnisGlobals.loadFromResourceFolder("icons/thumbnail.png").getFile());

    private static final Image audioIcon  =
            new Image(FILE_PREF + IgnisGlobals.loadFromResourceFolder("icons/audio-x-generic-16.png").getFile());

    private static final Image jsIcon  =
            new Image(FILE_PREF + IgnisGlobals.loadFromResourceFolder("icons/jsIcon.png").getFile());

    private static final Image jsonIcon  =
            new Image(FILE_PREF + IgnisGlobals.loadFromResourceFolder("icons/jsonIcon.png").getFile());

    @Override
    public void updateItem(String name, boolean empty) {
        updateCell(this, name);
    }

    private void updateCell(ListCell<String> cell,String name) {
        final ImageView imageView = new ImageView();
        if (AssetStructure.isAudio(name)) {
            imageView.setImage(audioIcon);
        }
        if (AssetStructure.isImage(name)) {
            imageView.setImage(imageIcon);
        }
        if (AssetStructure.isScript(name)) {
            imageView.setImage(jsIcon);
        }
        if (AssetStructure.isJSON(name)) {
            imageView.setImage(jsonIcon);
        }
        cell.setText(name);
        cell.setGraphic(imageView);
    }

}
