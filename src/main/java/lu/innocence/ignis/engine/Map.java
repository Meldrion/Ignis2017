package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class Map {

    private List<TilesetLayer> layers;
    private int width;
    private int height;
    private Tileset tileset;
    private int activeLayerIndex;
    private List<Map> children;
    private Map parent;
    private String uniqueId;
    private String name;
    private String mapFilePath;


    public Map() {

        this.uniqueId = "root";
        this.activeLayerIndex = 0;
        this.name = "Untitled";
        this.children = new ArrayList<>();
        this.parent = null;
        this.layers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            this.layers.add(new TilesetLayer());
        }
    }

    public void setMapFilePath(String path) {
        this.mapFilePath = path;
    }

    public String getMapId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDimension(int x, int y) {
        this.width = x;
        this.height = y;
        for (TilesetLayer layer : this.layers) {
            layer.setDimension(x, y);
        }
    }

    public void renderMap(GraphicsContext g) {

        if (this.tileset != null) {
            for (int index = 0; index < this.layers.size(); index++) {

                if (this.activeLayerIndex == index && index != 0) {
                    g.setGlobalAlpha(0.5);
                    g.setFill(Color.BLACK);
                    g.fillRect(0, 0, width * 32, height * 32);
                    g.setGlobalAlpha(1.0);
                } else {
                    if (this.activeLayerIndex < index) {
                        g.setGlobalAlpha(0.5);
                    }
                }

                this.layers.get(index).render(g,this.tileset);
                g.setGlobalAlpha(1.0);
            }
        }
    }

    public void renderPartialMap(GraphicsContext g, int x, int y) {
        g.clearRect(x * 32 + 1, y * 32 + 1, 31, 31);

        if (this.tileset != null) {
            for (int index = 0; index < this.layers.size(); index++) {

                if (this.activeLayerIndex == index && index != 0) {
                    g.setGlobalAlpha(0.5);
                    g.setFill(Color.BLACK);
                    g.fillRect(x * 32, y * 32, 32, 32);
                    g.setGlobalAlpha(1.0);
                } else {
                    if (this.activeLayerIndex < index) {
                        g.setGlobalAlpha(0.5);
                    }
                }

                this.layers.get(index).renderPartial(g,x,y,this.tileset);
                g.setGlobalAlpha(1.0);
            }
        }
    }


    public void addCell(int layerIndex, int x, int y, int tsX, int tsY) {
        this.layers.get(layerIndex).addCell(x, y, tsX, tsY);
    }

    public void removeCell(int layerIndex,int x,int y) {
        this.layers.get(layerIndex).removeCell(x,y);
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }

    public Tileset getTileset() {
        return this.tileset;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setActiveLayerIndex(int activeLayerIndex) {
        this.activeLayerIndex = activeLayerIndex;
    }

    public void addMap(Map map) {
        if (!this.children.contains(map)) {
            this.children.add(map);
            map.parent = this;
        }
    }

    public Map find(String uniqueId) {

        int index = 0;
        int maxIndex = this.children.size();
        Map foundMap = this.uniqueId.equals(uniqueId) ? this : null;
        while (foundMap == null && index < maxIndex) {
            foundMap = this.children.get(index).find(uniqueId);
            index++;
        }
        return foundMap;

    }

    public List<Map> getChildren() {
        return this.children;
    }

    public void save() {
        JSONObject mapJSON = new JSONObject();
        mapJSON.put("name", this.getName());
        mapJSON.put("width", this.getWidth());
        mapJSON.put("height", this.getHeight());

        JSONArray layers = new JSONArray();
        for (TilesetLayer layer : this.layers) {
            JSONArray layerJSON = layer.saveLayer();
            layers.add(layerJSON);
        }

        mapJSON.put("layers",layers);

        FilesystemHandler.writeJson(mapJSON, this.mapFilePath);
    }


    public void addTile(int layerId, int x, int y, int tsX, int tsY) {
        this.layers.get(layerId).addCell(x, y, tsX, tsY);
    }
}
