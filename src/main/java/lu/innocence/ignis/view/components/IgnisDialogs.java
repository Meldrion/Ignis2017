package lu.innocence.ignis.view.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class IgnisDialogs {

    /**
     *
     * @param title Dialog Title
     * @param header Dialog Header Message
     * @param content Dialog Content Message
     * @return true if the user confirmed the message, false otherwise
     */
    public static boolean openConfirmationDialog(Stage parentStage,String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(parentStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
