package lu.innocence.ignis.view.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

        AtomicInteger xPos = new AtomicInteger();
        AtomicInteger yPos = new AtomicInteger();
        AtomicBoolean xDone = new AtomicBoolean(false);
        AtomicBoolean yDone = new AtomicBoolean(false);

        ChangeListener<Number> listenerW = (observable, oldValue, newValue) -> {
            double newX = (parent.getWidth() - newValue.doubleValue()) / 2 + parent.getX();
            xPos.set((int)newX);
            xDone.set(true);
            if (xDone.get() && yDone.get()) {
                toBeCentered.setX(xPos.doubleValue());
                toBeCentered.setY(yPos.doubleValue());
                CenterWindowOnParent.setPosition(xDone,yDone,xPos,yPos,toBeCentered);
            }
        };
        toBeCentered.widthProperty().addListener(listenerW);

        ChangeListener<Number> listenerH = (observable, oldValue, newValue) -> {
            double newY = (parent.getHeight() - newValue.doubleValue()) / 2 + parent.getY();
            yPos.set((int)newY);
            yDone.set(true);
            CenterWindowOnParent.setPosition(xDone,yDone,xPos,yPos,toBeCentered);
        };

        toBeCentered.widthProperty().addListener(listenerW);
        toBeCentered.heightProperty().addListener(listenerH);
    }

    /**
     *
     * @param xDone
     * @param yDone
     * @param xPos
     * @param yPos
     * @param toBeCentered
     */
    private static void setPosition(AtomicBoolean xDone,AtomicBoolean yDone,
                                    AtomicInteger xPos,AtomicInteger yPos,
                                    Stage toBeCentered) {
        if (xDone.get() && yDone.get()) {
            toBeCentered.setX(xPos.doubleValue());
            toBeCentered.setY(yPos.doubleValue());
        }
    }

}
