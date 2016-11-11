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

    private String projectTitle;
    private String author;
    private String devCompany;

    /**
     *
     */
    public Project() {
        this.mapManager = new MapManager();
    }


    /**
     *
     * @param rootPath
     * @param projectName
     * @param projectTitle
     * @param author
     * @param devCompany
     * @return
     */
    public boolean create(String rootPath,String projectName,String projectTitle,String author,String devCompany) {

        this.rootFolder = FilesystemHandler.concat(rootPath,projectName);

        if (FilesystemHandler.createFolder(this.rootFolder)) {
            JSONObject projectJSON = new JSONObject();
            projectJSON.put("title",projectTitle);
            projectJSON.put("author",author);
            projectJSON.put("company",devCompany);

            this.assetStructure = new AssetStructure(this.rootFolder);
            boolean allOk = FilesystemHandler.createFolder(this.assetStructure.getAsset());
            List<String> assetNames = AssetStructure.getAssetFolderNames();

            for (String name : assetNames) {
                allOk &= FilesystemHandler.createFolder(this.assetStructure.getPath(name));
            }

            this.projectTitle = projectTitle;
            this.author = author;
            this.devCompany = devCompany;

            this.mapManager.setMapFolder(this.assetStructure.getPath(AssetStructure.MAP));

            allOk &= FilesystemHandler.writeJson(projectJSON,this.assetStructure.getProjectJSON());
            return allOk;

        } else {
            return false;
        }

    }


    public boolean load(String rootFolder) {
        this.rootFolder = rootFolder;
        this.assetStructure = new AssetStructure(this.rootFolder);
        JSONObject projectJSON = FilesystemHandler.readJSON(this.assetStructure.getProjectJSON());

        if (projectJSON != null) {

            this.projectTitle = (String) projectJSON.get("title");
            this.author = (String) projectJSON.get("author");
            this.devCompany = (String) projectJSON.get("company");

            this.mapManager.setMapFolder(this.assetStructure.getPath(AssetStructure.MAP));

            return true;

        } else {
            return false;
        }

    }

    public boolean saveProject() {

        this.mapManager.saveAll();
        return true;
    }

    public MapManager getMapManager() {
        return this.mapManager;
    }


    public AssetStructure getAssetStructure() {
        return this.assetStructure;
    }

}
