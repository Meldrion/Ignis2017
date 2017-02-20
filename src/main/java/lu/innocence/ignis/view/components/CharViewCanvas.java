package lu.innocence.ignis.view.components;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lu.innocence.ignis.engine.AssetStructure;
import lu.innocence.ignis.view.resourceView.ImageView;

/**
 * Created by Fabien Steines
 * Last Update on: 28.01.2017.
 */
public class CharViewCanvas extends Canvas {

    private Image charImage;
    private String charName;
    private AssetStructure assetManager;
    private Stage parent;

    /**
     * @param assetManager
     * @param parent
     */
    public CharViewCanvas(AssetStructure assetManager, Stage parent) {

        this.assetManager = assetManager;
        this.parent = parent;
        this.render();

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, t -> {
            if (t.getClickCount() > 1) {
                action();
            }
        });
    }

    /**
     *
     */
    public void render() {
        GraphicsContext g = this.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        ChessBGDrawer.drawChessBackground(g, (int) this.getWidth(), (int) this.getHeight(), 32, 32);

        if (this.charImage != null) {

            Dimension2D newRatio = AspectRatioCalculator.
                    getScaledDimension(new Dimension2D(this.charImage.getWidth(),
                            this.charImage.getHeight()), new Dimension2D(getWidth() - 20, getHeight() - 20));

            double centerX = (getWidth() - newRatio.getWidth()) / 2;
            double centerY = (getHeight() - newRatio.getHeight()) / 2;

            g.drawImage(this.charImage, 0, 0, this.charImage.getWidth() / 4, this.charImage.getHeight() / 4,
                    centerX, centerY, newRatio.getWidth(), newRatio.getHeight());
        }

        g.setStroke(Color.LIGHTGRAY);
        g.strokeRect(1, 1, getWidth() - 1, getHeight() - 1);
    }

    /**
     * @param selectedName
     * @param image
     */
    public void setCharacter(String selectedName, Image image) {
        this.charImage = image;
        this.charName = selectedName;
        this.render();
    }

    /**
     *
     * @return
     */
    public Image getCharImage() {
        return this.charImage;
    }

    /**
     *
     * @return
     */
    public String getCharName() {
        return this.charName;
    }

    /**
     *
     */
    public void action() {
        ImageView imageView = new ImageView(parent);
        imageView.setAssetManager(this.assetManager, AssetStructure.CHARACTER);
        imageView.setOnHidden(event -> {
            if (imageView.isAccepted()) {
                this.setCharacter(imageView.getSelectedName(), imageView.getSelected());
            }
        });
        CenterWindowOnParent.center(parent,imageView);
        imageView.show();
    }

}
