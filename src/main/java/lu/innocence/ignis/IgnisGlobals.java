package lu.innocence.ignis;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.ProjectManager;

import java.io.File;
import java.net.URL;

/**
 * @author Fabien Steines
 */
public class IgnisGlobals {

    public static int[] fixCoords(int startX, int startY, int endX, int endY) {
        int swap;

        if (endX < startX) {
            swap = endX;
            endX = startX;
            startX = swap;
            endX += 1;
        } else {
            endX += 1;
        }

        if (endY < startY) {
            swap = endY;
            endY = startY;
            startY = swap;
            endY += 1;
        } else {
            endY += 1;
        }

        int[] returnArray = {startX, startY, endX, endY};
        return returnArray;
    }

    public static URL loadFromResourceFolder(String path) {
        return IgnisGlobals.class.getClassLoader().getResource(path);
    }

    public static boolean chooseProjectRoot(Stage parent) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setInitialDirectory(new File(ProjectManager.getInstance().getRootFolder()));
        directoryChooser.setTitle("Select Root Folder");
        File dir = directoryChooser.showDialog(parent);

        if (dir != null) {
            ProjectManager.getInstance().setRootFolder(dir.getAbsolutePath());
            return true;
        }

        return false;
    }

    public static int getTextWidth(GraphicsContext g,String text) {
        return (int) com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(text, g.getFont());
    }

    public static int getTextHeight(GraphicsContext g) {
        return (int) com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(g.getFont()).getLineHeight();
    }


}
