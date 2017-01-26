package lu.innocence.ignis;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.ProjectManager;

import java.io.File;
import java.net.URL;

/**
 * @author Fabien Steines
 */
public class IgnisGlobals {

    private static Image iconIgnis = IgnisGlobals.loadResource("icons/ignis.png");
    private static Image iconIgnis24px = IgnisGlobals.loadResource("icons/ignis24px.png");
    private static Image iconNewProject = IgnisGlobals.loadResource("icons/Document-Blank-icon-24.png");
    private static Image iconLoadProject = IgnisGlobals.loadResource("icons/Files-icon-24.png");
    private static Image iconSaveProject = IgnisGlobals.loadResource("icons/Actions-document-save-icon.png");
    private static Image iconCloseProject = IgnisGlobals.loadResource("icons/Document-Blank-icon-24.png");
    private static Image iconPen = IgnisGlobals.loadResource("icons/draw-line-2.png");
    private static Image iconBrush = IgnisGlobals.loadResource("icons/draw-brush.png");
    private static Image iconFill = IgnisGlobals.loadResource("icons/draw-fill-2.png");
    private static Image iconErase = IgnisGlobals.loadResource("icons/draw-eraser-2.png");
    private static Image iconLayer1 = IgnisGlobals.loadResource("icons/layer_bottom_24.png");
    private static Image iconLayer2 = IgnisGlobals.loadResource("icons/layer_middle_24.png");
    private static Image iconLayer3 = IgnisGlobals.loadResource("icons/layer_top_24.png");
    private static Image iconLayerEvent = IgnisGlobals.loadResource("icons/layer_top_24.png");
    private static Image iconImport = IgnisGlobals.loadResource("icons/import-icon-24.png");
    private static Image iconAudioManager = IgnisGlobals.loadResource("icons/audioManager22.png");
    private static Image iconMap = IgnisGlobals.loadResource("icons/Document-Blank-icon-16.png");
    private static Image iconMapFolder = IgnisGlobals.loadResource("icons/document-open-2.png");

    private IgnisGlobals() {

    }

    public static int[] fixCoords(int startX, int startY, int endX, int endY) {
        int swap;

        int sX;
        int sY;
        int eX;
        int eY;

        if (endX < startX) {
            swap = endX;
            sX = swap;
            eX = startX + 1;
        } else {
            sX = startX;
            eX = endX + 1;
        }

        if (endY < startY) {
            swap = endY;
            eY = startY + 1;
            sY = swap;
        } else {
            sY = startY;
            eY = endY + 1;
        }

        return new int[] {sX, sY, eX, eY};
    }

    public static URL loadFromResourceFolder(String path) {
        return IgnisGlobals.class.getClassLoader().getResource(path);
    }

    public static Image loadResource(URL url) {
        return url != null ? new Image("file:" +  url.getFile()) : null;
    }

    public static Image loadResource(String resource) {
        return loadResource(loadFromResourceFolder(resource));
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

    public static Image getIconNewProject() {
        return iconNewProject;
    }

    public static Image getIconLoadProject() {
        return iconLoadProject;
    }

    public static Image getIconSaveProject() {
        return iconSaveProject;
    }

    public static Image getIconCloseProject() {
        return iconCloseProject;
    }

    public static Image getIconPen() {
        return iconPen;
    }

    public static Image getIconBrush() {
        return iconBrush;
    }

    public static Image getIconFill() {
        return iconFill;
    }

    public static Image getIconErase() {
        return iconErase;
    }

    public static Image getIconLayer1() {
        return iconLayer1;
    }

    public static Image getIconLayer2() {
        return iconLayer2;
    }

    public static Image getIconLayer3() {
        return iconLayer3;
    }

    public static Image getIconLayerEvent() {
        return iconLayerEvent;
    }

    public static Image getIconIgnis() {
        return iconIgnis;
    }

    public static Image getIconIgnis24px() {
        return iconIgnis24px;
    }

    public static Image getIconAudioManager() {
        return iconAudioManager;
    }

    public static Image getIconImport() {
        return iconImport;
    }

    public static Image getIconMapFolder() {
        return iconMapFolder;
    }

    public static Image getIconMap() {
        return iconMap;
    }
}
