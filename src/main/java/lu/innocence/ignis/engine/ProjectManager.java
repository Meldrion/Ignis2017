package lu.innocence.ignis.engine;

import lu.innocence.ignis.event.ActiveProjectListener;

import java.util.ArrayList;
import java.util.List;

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

    private List<ActiveProjectListener> projectListeners;


    private ProjectManager() {

        this.projectListeners = new ArrayList<>();
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
        this.fireUpdate();
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
        if (project.load(path)) {
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

    public void addActiveProjectListener(ActiveProjectListener listener) {
        if (!this.projectListeners.contains(listener)) {
            this.projectListeners.add(listener);
        }
    }

    private void fireUpdate() {
        for (ActiveProjectListener listener : this.projectListeners) {
            listener.activeProjectChanged(this.currentProject);
        }
    }

}
