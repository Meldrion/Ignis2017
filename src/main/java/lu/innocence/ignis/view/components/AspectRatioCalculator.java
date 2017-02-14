package lu.innocence.ignis.view.components;

import javafx.geometry.Dimension2D;


/**
 * Created by Fabien Steines
 * Last Update on: 14.02.2017.
 */
public class AspectRatioCalculator {

    public static Dimension2D getScaledDimension(Dimension2D imgSize, Dimension2D boundary) {

        double originalWidth = imgSize.getWidth();
        double originalHeight = imgSize.getHeight();
        double boundWidth = boundary.getWidth();
        double boundHeight = boundary.getHeight();
        double newWidth = originalWidth;
        double newHeight = originalHeight;

        // first check if we need to scale width
        if (originalWidth > boundWidth) {
            //scale width to fit
            newWidth = boundWidth;
            //scale height to maintain aspect ratio
            newHeight = (newWidth * originalHeight) / originalWidth;
        }

        // then check if we need to scale even with the new height
        if (newHeight > boundHeight) {
            //scale height to fit instead
            newHeight = boundHeight;
            //scale width to maintain aspect ratio
            newWidth = (newHeight * originalWidth) / originalHeight;
        }

        return new Dimension2D(newWidth, newHeight);
    }

}
