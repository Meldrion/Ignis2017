package lu.innocence.ignis.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class AssetStructure {

    public static final String ASSET = "asset";
    public static final String ANIMATION = "animation";
    public static final String BACKGROUNDMUSIC = "bgm";
    public static final String BATTLEBACKGROUND = "battlebackground";
    public static final String BATTLESPRITE = "battlesprite";
    public static final String CHARACTER = "character";
    public static final String JSON = "json";
    public static final String OVERLAY  = "overlay";
    public static final String MAP = "map";
    public static final String GAMEOVER = "gameover";
    public static final String SCENEBACKGROUND = "scenebackground";
    public static final String SCRIPT = "script";
    public static final String SOUNDEFFECT = "soundeffect";
    public static final String TILESET = "tileset";
    public static final String TERRAIN = "terrain";
    public static final String TITLE = "title";
    public static final String UI = "ui";

    private String rootPath;

    public static List<String> getAssetNames() {

        List<String> assetNames = new ArrayList<>();

        assetNames.add(AssetStructure.ANIMATION);
        assetNames.add(AssetStructure.BACKGROUNDMUSIC);
        assetNames.add(AssetStructure.BATTLESPRITE);
        assetNames.add(AssetStructure.CHARACTER);
        assetNames.add(AssetStructure.JSON);
        assetNames.add(AssetStructure.OVERLAY);
        assetNames.add(AssetStructure.MAP);
        assetNames.add(AssetStructure.GAMEOVER);
        assetNames.add(AssetStructure.SCENEBACKGROUND);
        assetNames.add(AssetStructure.SCRIPT);
        assetNames.add(AssetStructure.SOUNDEFFECT);
        assetNames.add(AssetStructure.TILESET);
        assetNames.add(AssetStructure.TERRAIN);
        assetNames.add(AssetStructure.TITLE);
        assetNames.add(AssetStructure.UI);

        return assetNames;
    }

    public AssetStructure(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getAsset() {
        return FilesystemHandler.concat(this.rootPath,AssetStructure.ASSET);
    }

    public String getPath(String assetName) {
        return FilesystemHandler.concat(this.getAsset(),assetName);
    }

    public String getProjectJSON() {
            return FilesystemHandler.concat(this.rootPath,"project.json");
    }


}
