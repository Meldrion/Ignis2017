package lu.innocence.ignis.view.dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.ProjectManager;

/**
 * @author Fabien Steines
 */
public class CreateProjectDialog extends Stage {

    private TextField rootPathTextField;

    public CreateProjectDialog(Stage parentStage) {

        this.initOwner(parentStage);
        this.initModality(Modality.WINDOW_MODAL);
        this.setTitle("Create Project Window");
        this.setResizable(false);
        this.buildGUI();
        this.initData();
        this.sizeToScene();
    }

    private void buildGUI() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        this.setScene(scene);


        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                this.close();
            }
        });

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);

        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(200);

        grid.getColumnConstraints().addAll(column1, column2);
        grid.setPadding(new Insets(15, 10, 0, 15));

        Label rootLabel = new Label("Project Root ");
        grid.add(rootLabel, 0, 0);

        this.rootPathTextField = new TextField();
        this.rootPathTextField.setEditable(false);
        grid.add(this.rootPathTextField, 1, 0);

        Button lookUpButton = new Button();
        lookUpButton.setText("Search");
        lookUpButton.setOnAction(t -> {
            IgnisGlobals.chooseProjectRoot(this);
            this.rootPathTextField.setText(ProjectManager.getInstance().getRootFolder());
        });

        grid.add(lookUpButton, 2, 0);

        Label folderNameLabel = new Label("Project Folder Name ");
        grid.add(folderNameLabel, 0, 1);

        TextField folderNameTextField = new TextField();
        grid.add(folderNameTextField, 1, 1);

        GridPane.setColumnSpan(folderNameTextField, 2);

        Label titleLabel = new Label("Project Title ");
        grid.add(titleLabel, 0, 2);

        TextField titleTextField = new TextField();
        grid.add(titleTextField, 1, 2);

        GridPane.setColumnSpan(titleTextField, 2);

        Label author = new Label("Author ");
        grid.add(author, 0, 3);

        TextField authorTextField = new TextField();
        grid.add(authorTextField, 1, 3);

        GridPane.setColumnSpan(authorTextField, 2);

        Label company = new Label("Company ");
        grid.add(company, 0, 4);

        TextField companyTextField = new TextField();
        grid.add(companyTextField, 1, 4);

        GridPane.setColumnSpan(companyTextField, 2);

        root.setCenter(grid);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button confirmButton = new Button();
        confirmButton.setText("Create Project");
        confirmButton.setOnAction(event -> {

            if (ProjectManager.getInstance().createProject(rootPathTextField.getText(), folderNameTextField.getText(),
                    titleTextField.getText(), author.getText(), company.getText())) {
                this.close();
            } else {

            }

        });

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(confirmButton, cancelButton);

        root.setBottom(bottomBar);
    }

    private void initData() {
        this.rootPathTextField.setText(ProjectManager.getInstance().getRootFolder());
    }

}
