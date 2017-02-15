package lu.innocence.ignis.view.components;

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

    public static void center(Stage parent,Stage toBeCentered) {
        double newX = (parent.getWidth() - toBeCentered.getWidth()) / 2 + parent.getX();
        double newY = (parent.getHeight() - toBeCentered.getHeight()) / 2 + parent.getY();

        toBeCentered.setX(newX);
        toBeCentered.setY(newY);
    }

}
