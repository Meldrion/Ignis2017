package lu.innocence.ignis.engine;

import org.json.simple.JSONObject;

import java.util.List;

/**
 * @author Fabien Steines
 */
public class Project {

    private String rootFolder;
    private MapManager mapManager;
    private AssetStructure assetStructure;
    private TilesetManager tilesetManager;
    private AudioManager audioManager;
    private AssetManager assetManager;
    private String projectTitle;
    private String author;
    private String devCompany;

    /**
     * Create an instance of the project
     */
    public Project() {
        this.mapManager = new MapManager();
        this.tilesetManager = new TilesetManager();
        this.audioManager = new AudioManager();
        this.assetManager = new AssetManager();
    }


    /**
     * @param rootPath root Path of the Project
     * @param projectName the name of the Project Folder
     * @param projectTitle the title of the project
     * @param author the name of the author
     * @param devCompany the name of the company
     * @return true of the project was created without any errors
     */
    boolean create(String rootPath, String projectName, String projectTitle, String author, String devCompany) {

        this.rootFolder = FilesystemHandler.concat(rootPath, projectName);

        if (FilesystemHandler.createFolder(this.rootFolder)) {
            JSONObject projectJSON = new JSONObject();
            //noinspection unchecked
            projectJSON.put("title", projectTitle);
            //noinspection unchecked
            projectJSON.put("author", author);
            //noinspection unchecked
            projectJSON.put("company", devCompany);

            this.assetStructure = new AssetStructure(this.rootFolder);
            boolean allOk = FilesystemHandler.createFolder(this.assetStructure.getAsset());
            List<String> assetNames = AssetStructure.getAssetFolderNames();

            for (String name : assetNames) {
                allOk &= FilesystemHandler.createFolder(this.assetStructure.getPath(name));
            }

            this.projectTitle = projectTitle;
            this.author = author;
            this.devCompany = devCompany;

            this.init();
            this.tilesetManager.save();

            allOk &= FilesystemHandler.writeJson(projectJSON, this.assetStructure.getProjectJSON());
            this.mapManager.saveMapTree();

            return allOk;

        } else {
            return false;
        }

    }

    /**
     *
     * @param rootFolder the project root folder
     * @return true if the project has been loaded without any errors
     */
    public boolean load(String rootFolder) {
        this.rootFolder = rootFolder;
        this.assetStructure = new AssetStructure(this.rootFolder);
        JSONObject projectJSON = FilesystemHandler.readJSON(this.assetStructure.getProjectJSON());

        if (projectJSON != null) {

            this.projectTitle = (String) projectJSON.get("title");
            this.author = (String) projectJSON.get("author");
            this.devCompany = (String) projectJSON.get("company");

            this.init();

            tilesetManager.load();
            mapManager.loadMapTree();

            return true;

        } else {
            return false;
        }

    }

    /**
     *
     */
    private void init() {
        this.mapManager.setAssetManager(this.assetManager);
        this.mapManager.setMapFolder(this.assetStructure.getPath(AssetStructure.MAP));
        this.mapManager.setJsonFolder(this.assetStructure.getPath(AssetStructure.JSON));
        this.mapManager.setTilesetManager(this.tilesetManager);
        this.tilesetManager.setJSONFolder(this.assetStructure.getPath(AssetStructure.JSON));
        this.tilesetManager.setTerrainFolder(this.assetStructure.getPath(AssetStructure.TERRAIN));
        this.tilesetManager.setTilesetFolder(this.assetStructure.getPath(AssetStructure.TILESET));
        this.audioManager.setBgmFolder(this.assetStructure.getPath(AssetStructure.BACKGROUNDMUSIC));
        this.audioManager.setSEFolder(this.assetStructure.getPath(AssetStructure.SOUNDEFFECT));
    }

    /**
     *
     * @return
     */
    public boolean saveProject() {

        this.mapManager.saveAll();
        this.tilesetManager.save();

        return true;
    }

    /**
     *
     * @return
     */
    public MapManager getMapManager() {
        return this.mapManager;
    }

    /**
     *
     * @return
     */
    public AssetStructure getAssetStructure() {
        return this.assetStructure;
    }

    /**
     *
     * @return
     */
    public TilesetManager getTilesetManager() {
        return this.tilesetManager;
    }

    /**
     *
     * @return
     */
    public AudioManager getAudioManager() {
        return this.audioManager;
    }

    /**
     *
     * @return
     */
    public String getProjectTitle() {
        return projectTitle;
    }

    /**
     *
     * @param projectTitle
     */
    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     */
    public String getDevCompany() {
        return devCompany;
    }

    /**
     *
     * @param devCompany
     */
    public void setDevCompany(String devCompany) {
        this.devCompany = devCompany;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
