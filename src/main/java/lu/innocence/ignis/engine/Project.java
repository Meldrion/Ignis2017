package lu.innocence.ignis.engine;

import org.json.simple.JSONObject;

import java.util.List;

/**
 * @author Fabien Steines
 */
public class Project {

    private String rootFolder;
    private MapManager mapManager;
    private FilesystemHandler filesystemHandler;
    private AssetStructure assetStructure;

    /**
     *
     */
    public Project() {
        this.filesystemHandler = new FilesystemHandler();
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
            List<String> assetNames = AssetStructure.getAssetNames();

            for (String name : assetNames) {
                allOk &= FilesystemHandler.createFolder(this.assetStructure.getPath(name));
            }

            allOk &= this.filesystemHandler.writeJson(projectJSON,this.assetStructure.getProjectJSON());
            return allOk;

        } else {
            return false;
        }

    }


    public boolean load() {
        return true;
    }

    public boolean saveProject() {
        return true;
    }

    public MapManager getMapManager() {
        return this.mapManager;
    }

}
