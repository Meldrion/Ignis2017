package lu.innocence.ignis.view.resourceView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.view.components.ResourceCanvas;

/**
 * @author Fabien Steines
 */
public abstract class ResourceView extends Stage {

    protected ListView<String> resourceList;
    protected ResourceCanvas resourceCanvas;
    protected boolean accepted;
    protected boolean inNestedEventLoop;

    /**
     *
     * @param parentStage
     */
    public ResourceView(Stage parentStage) {
        this.accepted = false;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Choose..");
        this.setResizable(false);
        this.buildGUI();
        this.initOwner(parentStage);
        this.sizeToScene();
    }

    /**
     *
     */
    private void buildGUI() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        this.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE) {
                this.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,0,10));
        grid.setHgap(10);
        grid.setVgap(10);

        ColumnConstraints column1 = new ColumnConstraints(200);
        ColumnConstraints column2 = new ColumnConstraints(300);

        grid.getColumnConstraints().addAll(column1,column2);

        this.resourceList = new ListView<>();
        this.resourceList.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    this.resourceSelectionChanged(this.resourceList.getSelectionModel().getSelectedIndex());
                });
        grid.add(this.resourceList,0,0);

        this.resourceCanvas = new ResourceCanvas();
        this.resourceCanvas.setWidth(column2.getPrefWidth());

        grid.add(this.resourceCanvas,1,0);
        root.setCenter(grid);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Button acceptButton = new Button();
        acceptButton.setText("Ok");
        acceptButton.setOnAction(event -> {
            this.accepted = true;
            this.close();
        });

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(cancelButton,acceptButton);
        root.setBottom(bottomBar);


        this.setOnShown(event -> {
            this.resourceCanvas.setHeight(this.resourceList.getHeight());
            this.resourceCanvas.render();
        });

    }

    /**
     *
     * @param index
     */
    protected abstract void resourceSelectionChanged(int index);

    /**
     *
     * @param index
     */
    public void setSelectedIndex(int index) {
        if (index > -1) {
            this.resourceList.getSelectionModel().select(index);
        }
    }

    /**
     *
     * @return
     */
    public int getSelectedIndex() {
        return this.resourceList.getSelectionModel().getSelectedIndex();
    }

    /**
     *
     * @return
     */
    public String getSelectedString() {
        return getSelectedIndex() > -1 ? this.resourceList.getSelectionModel().getSelectedItem() : null;
    }

    /**
     *
     * @return
     */
    public boolean isAccepted() {
        return this.accepted;
    }

}
