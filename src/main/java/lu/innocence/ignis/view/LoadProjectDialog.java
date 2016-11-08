package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.FilesystemHandler;
import lu.innocence.ignis.engine.ProjectManager;

import java.io.File;
import java.util.List;


/**
 * @author Fabien Steines
 */
public class LoadProjectDialog extends Stage {

    //ProjectManager.getInstance().loadProject(ProjectManager.getInstance().getRootFolder() + "/ES2016")
    private TextField rootPathTextField;
    private ListView<String> projectsList;

    public LoadProjectDialog(Stage parent) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Load Project Window");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.initOwner(parent);
        this.setWidth(305);
        this.setHeight(300);
        this.show();
    }

    private void buildGUI() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,305, 300);
        this.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE) {
                this.close();
            }
        });

        // Center Box
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 0, 10));


        Text rootPathLabel = new Text();
        rootPathLabel.setText("Root Path");
        grid.add(rootPathLabel,0,0);

        this.rootPathTextField = new TextField();
        this.rootPathTextField.setEditable(false);
        grid.add(this.rootPathTextField,1,0);

        Button button = new Button();
        button.setText("...");
        button.setOnAction(t -> {
            if (IgnisGlobals.chooseProjectRoot(this)) {
                this.rootPathTextField.setText(ProjectManager.getInstance().getRootFolder());
                this.buildProjectList();
            }
        });

        grid.add(button,2,0);

        Text projectsLabel = new Text();
        projectsLabel.setText("Projects in this path: ");
        grid.add(projectsLabel,0,1,3,1);

        this.projectsList = new ListView<>();
        grid.add(projectsList,0,2,3,1);


        Button deleteButton = new Button();
        deleteButton.setText("Delete");
        deleteButton.setMaxWidth(500);
        deleteButton.setOnAction(event -> {
            if (this.projectsList.getSelectionModel().getSelectedIndex() > -1) {
                String selected = this.projectsList.getSelectionModel().getSelectedItem();
                String path = FilesystemHandler.concat(ProjectManager.getInstance().getRootFolder(),selected);
                ProjectManager.getInstance().deleteProject(path);
                this.buildProjectList();
            }
        });
        grid.add(deleteButton,0,3,3,1);

        root.setCenter(grid);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button confirmButton = new Button();
        confirmButton.setText("Load Project");

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(confirmButton,cancelButton);

        root.setBottom(bottomBar);

    }

    private void initData() {
        this.rootPathTextField.setText(ProjectManager.getInstance().getRootFolder());
        this.buildProjectList();
    }

    private void buildProjectList() {

        this.projectsList.getItems().clear();
        List<String> projects = ProjectManager.getInstance().listAllProjectsInFolder
                (ProjectManager.getInstance().getRootFolder());
        for (String current : projects) {
            this.projectsList.getItems().add(current);
        }

    }

}
