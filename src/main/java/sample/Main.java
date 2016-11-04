package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.engine.MapCanvas;

import java.net.URL;

public class Main extends Application {

    int lastX = -1;
    int lastY = -1;

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = new BorderPane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 640, 480));

        MenuBar menuBar = new MenuBar();
        // Use system menu bar
        menuBar.setUseSystemMenuBar(true);
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        Menu fileMenu = new Menu("File");
        Menu webMenu = new Menu("Web");
        Menu sqlMenu = new Menu("SQL");

        menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);

        Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));

        final MapCanvas canvas = new MapCanvas(50 * 32,50 * 32);



        final Canvas testCanvas = new Canvas(50 * 32,50 * 32);
        testCanvas.getGraphicsContext2D().fillRect(50,50,320,320);

        testCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            canvas.renderPartial((int)t.getX()/32,(int)t.getY()/32);
        });

        testCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, t -> {

            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;

            if (x != lastX || y != lastY) {
                canvas.renderPartial(x, y);
                lastX = x;
                lastY = y;
            }

        });

        Image testImage = new Image(String.format("file:%s","tileset.png"));
        testCanvas.addEventHandler(MouseEvent.MOUSE_MOVED,t -> {
            int x = (int)t.getX()/32;
            int y = (int)t.getY()/32;

            if (x != lastX || y != lastY) {
                int w = 5 * 32;
                GraphicsContext g = testCanvas.getGraphicsContext2D();
                g.clearRect(lastX * 32,lastY * 32,w,w);
                g.drawImage(testImage,0,0,w,w,x * 32,y * 32,w,w);

                lastX = x;
                lastY = y;
            }
        });

        canvas.render();
        Pane pane = new Pane(canvas,testCanvas);
        pane.setPrefSize(50*32,50*32);

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(640  , 480);
        s1.setContent(pane);

        root.setCenter(s1);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
