package lu.innocence.ignis.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Fabien Steines
 */
public class CreateProjectDialog extends Stage{

    public CreateProjectDialog() {

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Create Project Window");
        this.setResizable(false);
        this.buildGUI();
        this.setWidth(380);
        this.setHeight(270);
        this.show();
    }

    private void buildGUI() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,380, 270);
        this.setScene(scene);


        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE) {
                this.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 0, 10));

        Text rootLabel = new Text("Project Root ");
        grid.add(rootLabel, 0, 0);

        TextField rootPathTextField = new TextField();
        grid.add(rootPathTextField,1,0);

        Button lookUpButton = new Button();
        lookUpButton.setText("...");
        grid.add(lookUpButton,2,0);

        Text folderNameLabel = new Text("Project Folder Name ");
        grid.add(folderNameLabel, 0, 1);

        TextField folderNameTextField = new TextField();
        grid.add(folderNameTextField,1,1);

        GridPane.setColumnSpan(folderNameTextField, 2);

        Text titleLabel = new Text("Project Title ");
        grid.add(titleLabel, 0, 2);

        TextField titleTextField = new TextField();
        grid.add(titleTextField,1,2);

        GridPane.setColumnSpan(titleTextField, 2);

        Text author = new Text("Author ");
        grid.add(author, 0, 3);

        TextField authorTextField = new TextField();
        grid.add(authorTextField,1,3);

        GridPane.setColumnSpan(authorTextField, 2);

        Text company = new Text("Company ");
        grid.add(company, 0, 4);

        TextField companyTextField = new TextField();
        grid.add(companyTextField,1,4);

        GridPane.setColumnSpan(companyTextField, 2);

        root.setCenter(grid);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button confirmButton = new Button();
        confirmButton.setText("Create Project");

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(confirmButton,cancelButton);

        root.setBottom(bottomBar);
    }

}
