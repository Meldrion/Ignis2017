package lu.innocence.ignis.view;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;


/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class LuaEditor extends Stage {

    public LuaEditor(Stage parent) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Import Manager Window");
        this.setResizable(false);
        this.buildGUI();
        this.initOwner(parent);
        this.sizeToScene();
    }

    private void buildGUI() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        this.setScene(scene);


        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));


        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            //codeArea.setStyleSpans(0, computeHighlighting(newText));
        });

        root.setCenter(codeArea);

        //codeArea.replaceText(0, 0, sampleCode);
    }

}
