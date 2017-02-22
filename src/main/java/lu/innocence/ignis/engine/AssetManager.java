package lu.innocence.ignis.engine;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class AssetManager {
    Map<String,Map<String,Image>> loadedImages;

    AssetManager() {
        this.loadedImages = new HashMap<>();
    }

    public Image loadImage() {
        return null;
    }
}
