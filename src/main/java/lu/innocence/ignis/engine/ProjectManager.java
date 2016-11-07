package lu.innocence.ignis.engine;

/**
 * @author Fabien Steines
 */
public class ProjectManager {

    private static ProjectManager instance;
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

    public static ProjectManager getInstance() {
        if (ProjectManager.instance == null ) {
            ProjectManager.instance = new ProjectManager();
        }

        return ProjectManager.instance;
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

    public void init() {

        this.rootFolder = FilesystemHandler.concat(FilesystemHandler.getUserHomeDir(),"ignis");

        // Hidden Folder for config files
        FilesystemHandler.createFolder(
                FilesystemHandler.concat(FilesystemHandler.getUserHomeDir(),".ignis"));

        // Project Folder
        FilesystemHandler.createFolder(this.rootFolder);
    }


    public String getRootFolder() {
        return this.rootFolder;
    }

    public void setRootFolder(String rootPath) {
        this.rootFolder = rootPath;
    }

}
