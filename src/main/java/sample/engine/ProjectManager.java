package sample.engine;

/**
 * @author Fabien Steines
 */
public class ProjectManager {

    private Project currentProject;
    private FilesystemHandler filesystemHandler;

    private String rootFolder;
    private String projectName;
    private String projectTitle;
    private String devName;
    private String devCompany;


    private ProjectManager() {

        this.currentProject = null;
        this.filesystemHandler = new FilesystemHandler();
        this.rootFolder = "";

    }

    public void setProject(Project project) {
        this.currentProject = project;
    }

    public Project getProject() {
        return this.currentProject;
    }


    public boolean createProject(String rootPath,String projectName,
                                 String projectTitle,String devName,
                                 String devCompany) {
        Project project = new Project();
        boolean worked = project.create(rootPath,projectName,projectTitle,devName,devCompany);
        this.setProject(project);
        return worked;
    }

    public Project loadProject(String path) {
        Project project = new Project();
        if (project.load()) {
            this.setProject(project);
            return project;
        } else {
            return null;
        }
    }

}
