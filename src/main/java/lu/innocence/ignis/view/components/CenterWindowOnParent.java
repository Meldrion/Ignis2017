package lu.innocence.ignis.view.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class CenterWindowOnParent {

    private CenterWindowOnParent() {}

    public static void center(Stage parent,Stage toBeCentered) {

        ChangeListener<Number> listenerW = (observable, oldValue, newValue) -> {
            double newX = (parent.getWidth() - newValue.doubleValue()) / 2 + parent.getX();
            toBeCentered.setX(newX);
        };
        toBeCentered.widthProperty().addListener(listenerW);

        ChangeListener<Number> listenerH = (observable, oldValue, newValue) -> {
            double newY = (parent.getHeight() - newValue.doubleValue()) / 2 + parent.getY();
            toBeCentered.setY(newY);
        };

        toBeCentered.widthProperty().addListener(listenerW);
        toBeCentered.heightProperty().addListener(listenerH);

    }

}
