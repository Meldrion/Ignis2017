package lu.innocence.ignis.engine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public abstract class Layer<T> {

    private List<List<T>> matrix;
    private int width;
    private int height;

    Layer() {
        this.width = 0;
        this.height = 0;
    }

    /**
     * @param x
     * @param y
     */
    void setDimension(int x, int y) {

        if (this.width != x || this.height != y) {
            this.width = x;
            this.height = y;
            this.buildMatrix();
        }

    }

    /**
     *
     */
    private void buildMatrix() {

        List<List<T>> old = this.matrix;

        this.matrix = new ArrayList<>();
        for (int i = 0; i < this.width; i++) {

            List<T> innerMatrix = new ArrayList<>();
            for (int j = 0; j < this.height; j++) {
                if (old != null && this.isInRange(i,j,old.size(),old.get(0).size())) {
                    innerMatrix.add(old.get(i).get(j));
                } else {
                    innerMatrix.add(null);
                }
            }
            this.matrix.add(innerMatrix);
        }

    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    boolean isInRange(int x, int y) {
        return this.isInRange(x,y,this.width,this.height);
    }

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    private boolean isInRange(int x,int y,int width,int height) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }


    /**
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    public int getWidth() {
       return this.width;
    }

    /**
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return
     */
    public int getHeight() {
       return this.height;
    }

    /**
     *
     * @param x
     * @param y
     * @param type
     */
    void setAt(int x, int y, T type) {

        if (this.isInRange(x,y)) {
            this.matrix.get(x).set(y, type);
        }
    }

    /**
     *
     * @param x
     * @param y
     */
    T removeAt(int x, int y) {
        T current = this.getFrom(x,y);
        this.setAt(x,y,null);
        return current;
    }

    /**
     *
     * @param x x pos
     * @param y y pos
     * @return element from the matrix
     */
    T getFrom(int x,int y) {
        return this.isInRange(x,y) ? this.matrix.get(x).get(y) : null;
    }


    /**
     * Returns the inner Matrix
     * @return Matrix containing the elements
     */
    List<List<T>> getMatrix() {
       return this.matrix;
    }


    /**
     *
     */
    void clearLayer() {
        this.matrix.clear();
    }


    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    JSONArray saveLayer() {
        JSONArray layer = new JSONArray();
        for (int i = 0; i < this.getMatrix().size(); i++) {
            for (int j = 0; j < this.getMatrix().get(0).size(); j++) {
                T c = this.getMatrix().get(i).get(j);
                if (c != null) {
                    layer.add(save(c));
                }
            }
        }
        return layer;
    }

    abstract JSONObject save(T t);
}
