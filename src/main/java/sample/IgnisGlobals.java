package sample;

/**
 * @author Fabien Steines
 */
public class IgnisGlobals {

    public static int[] fixCoords(int startX,int startY, int endX,int endY) {
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

        int[] returnArray = {startX,startY,endX,endY};
        return returnArray;
    }


}
