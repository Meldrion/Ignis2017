package lu.innocence.ignis.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.innocence.ignis.IgnisGlobals;
import lu.innocence.ignis.engine.AudioManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class AudioDialog extends Stage {

    private static final Logger LOGGER = LogManager.getLogger(AudioDialog.class);
    private AudioManager audioManager;
    private ListView<String> bgmList;
    private ListView<String> seList;

    public AudioDialog(Stage parentStage, AudioManager audioManager) {

        this.audioManager = audioManager;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Audio Manager");
        this.setResizable(false);
        this.buildGUI();
        this.initGUI(audioManager);

        this.initOwner(parentStage);
        this.sizeToScene();
    }

    private void buildGUI() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        TabPane mainTabber = new TabPane();

        Tab bgmTab = new Tab();
        bgmTab.setText("Background Music");
        bgmTab.setClosable(false);
        BorderPane bgmTabContent = new BorderPane();
        bgmTabContent.setPadding(new Insets(15));
        bgmTabContent.setRight(this.buildRightButtonBox(false,true));

        bgmTab.setContent(bgmTabContent);

        this.bgmList = new ListView<>();
        bgmTabContent.setCenter(this.bgmList);

        Tab seTab = new Tab();
        seTab.setText("Sound Effect");
        seTab.setClosable(false);
        BorderPane seTabContent = new BorderPane();
        seTabContent.setPadding(new Insets(15));
        seTab.setContent(seTabContent);

        this.seList = new ListView<>();
        seTabContent.setCenter(this.seList);
        seTabContent.setRight(this.buildRightButtonBox(true,false));

        mainTabber.getTabs().addAll(bgmTab,seTab);
        root.setCenter(mainTabber);

        // Box on the Bottom
        HBox bottomBar = new HBox();

        bottomBar.setSpacing(10);
        bottomBar.setPadding(new Insets(0,15,15,15));
        bottomBar.setAlignment(Pos.CENTER);

        Button cancelButton = new Button();
        cancelButton.setText("Close");
/*        cancelButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder
                ("icons/exit.png").getFile()));*/
        cancelButton.setOnAction(event -> this.close());

        bottomBar.getChildren().addAll(cancelButton);
        root.setBottom(bottomBar);

        this.setScene(scene);
    }

    private Node buildRightButtonBox(boolean ignorePause,boolean isBGM) {
        // Box on the Right
        VBox rightBox = new VBox();
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(15,0,15,15));

        Button playButton = new Button();
        playButton.setMinWidth(120);
        playButton.setText("Play");
        playButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder
                ("icons/Play-1-Normal-icon-16.png").getFile()));
        playButton.setOnAction(event -> {
            if (isBGM) {
                if (this.bgmList.getSelectionModel().getSelectedIndex() > -1) {
                    this.audioManager.loadBGMInSlot(this.bgmList.getSelectionModel().getSelectedItem(),0);
                    this.audioManager.playBGM(0);
                }
            }
        });

        Button pauseButton = new Button();
        pauseButton.setText("Pause");
        pauseButton.setMinWidth(120);
        pauseButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder
                ("icons/pause-icon.-16png.png").getFile()));
        pauseButton.setOnAction(event -> {

        });

        Button stopButton = new Button();
        stopButton.setText("Stop");
        stopButton.setMinWidth(120);
        stopButton.setGraphic(new ImageView("file:" + IgnisGlobals.loadFromResourceFolder
                ("icons/Stop-icon-16.png").getFile()));
        stopButton.setOnAction(event -> {
            if (isBGM) {
                this.audioManager.stopBGM(0);
            }
        });

        rightBox.getChildren().addAll(playButton,pauseButton,stopButton);
        return rightBox;
    }

    private void initGUI(AudioManager audioManager) {
        for (String cBGM : audioManager.getBGMList()) {
            this.bgmList.getItems().add(cBGM);
        }

        for (String cSE : audioManager.getSEList()) {
            this.seList.getItems().add(cSE);
        }
    }

}
