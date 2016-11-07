package sample.engine;

import org.json.simple.JSONObject;

/**
 * @author Fabien Steines
 */
public class Project {

    private String rootFolder;
    private MapManager mapManager;
    private FilesystemHandler filesystemHandler;

    public Project() {
        this.filesystemHandler = new FilesystemHandler();
    }


    public boolean create(String rootPath,String projectName,String projectTitle,String author,String devCompany) {

        JSONObject projectJSON = new JSONObject();
        projectJSON.put("title",projectTitle);
        projectJSON.put("author",author);
        projectJSON.put("company",devCompany);

        this.filesystemHandler.writeJson(projectJSON,"/home/fabien/Desktop/project.json");
        return true;
    }

}
