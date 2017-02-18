package lu.innocence.ignis.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lu.innocence.ignis.event.MapPropertiesUpdated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class Map {

    private static final Logger LOGGER = LogManager.getLogger(Map.class);
    private List<MapPropertiesUpdated> mapPropertiesListeners;
    private List<TilesetLayer> layers;
    private EventLayer eventLayer;
    private int width;
    private int height;
    private int cellSize;
    private Tileset tileset;
    private int activeLayerIndex;
    private List<Map> children;
    private Map parent;
    private String uniqueId;
    private String name;
    private String mapFilePath;
    private int tilesetId;

    /**
     *
     */
    Map() {

        this.uniqueId = "root";
        this.cellSize = 32;
        this.activeLayerIndex = 0;
        this.name = "Untitled";
        this.children = new ArrayList<>();
        this.parent = null;
        this.mapPropertiesListeners = new ArrayList<>();
        this.layers = new ArrayList<>();

        // Create the Tile Layers
        for (int i = 0; i < 3; i++) {
            this.layers.add(new TilesetLayer());
        }

        // Create the Event Layer
        this.eventLayer = new EventLayer();
    }

    /**
     *
     */
    public void addMapPropertiesListener(MapPropertiesUpdated mapPropertiesUpdated) {
        if (!this.mapPropertiesListeners.contains(mapPropertiesUpdated)) {
            this.mapPropertiesListeners.add(mapPropertiesUpdated);
        }
    }

    /**
     *
     * @param path
     */
    void setMapFilePath(String path) {
        this.mapFilePath = path;
    }

    /**
     *
     * @return
     */
    public String getMapId() {
        return this.uniqueId;
    }

    /**
     *
     * @param uniqueId
     */
    void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setDimension(int x, int y) {
        this.width = x;
        this.height = y;
        for (TilesetLayer layer : this.layers) {
            layer.setDimension(x, y);
        }
        this.eventLayer.setDimension(x,y);
        this.fireMapProprtiesUpdated();
    }

    /**
     *
     */
    public void clearMap() {
        for (TilesetLayer layer : this.layers) {
            layer.clearLayer();
        }
        this.fireMapProprtiesUpdated();
    }

    /**
     *
     * @param g
     */
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

                this.layers.get(index).render(g, this.tileset);
                g.setGlobalAlpha(1.0);

            }
        }

        this.eventLayer.render(g);
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     */
    public void renderPartialMap(GraphicsContext g, int x, int y) {

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

                this.layers.get(index).renderPartial(g, x, y, this.tileset);
                g.setGlobalAlpha(1.0);
            }

            this.eventLayer.renderPartial(g,x,y);
        }
    }

    /**
     *
     * @param layerIndex
     * @param x
     * @param y
     * @return
     */
    public TileCell removeCell(int layerIndex, int x, int y) {
        return this.layers.get(layerIndex).removeAt(x, y);
    }

    /**
     *
     * @return
     */
    public Tileset getTileset() {
        return this.tileset;
    }

    /**
     *
     * @param tileset
     */
    public void setTileset(Tileset tileset) {
        if (tileset != null) {
            this.tilesetId = tileset.getIndex();
        } else {
            this.tilesetId = -1;
        }
        this.tileset = tileset;
        this.fireMapProprtiesUpdated();
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
     * @return
     */
    public int getHeight() {
        return this.height;
    }

    /**
     *
     * @param activeLayerIndex
     */
    public void setActiveLayerIndex(int activeLayerIndex) {
        this.activeLayerIndex = activeLayerIndex;
    }

    /**
     *
     * @param map
     */
    public void addMap(Map map) {
        if (!this.children.contains(map)) {
            this.children.add(map);
            map.parent = this;
        }
    }

    /**
     *
     * @param uniqueId
     * @return
     */
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

    /**
     *
     * @return
     */
    public List<Map> getChildren() {
        return this.children;
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public void save() {
        JSONObject mapJSON = new JSONObject();
        mapJSON.put("name", this.getName());
        mapJSON.put("width", this.getWidth());
        mapJSON.put("height", this.getHeight());
        mapJSON.put("tilesetIndex", this.getTilesetId());

        JSONArray tileLayers = new JSONArray();
        for (TilesetLayer layer : this.layers) {
            JSONArray layerJSON = layer.saveLayer();
            tileLayers.add(layerJSON);
        }

        mapJSON.put("tileLayers", tileLayers);
        mapJSON.put("eventLayer",this.eventLayer.saveLayer());

        FilesystemHandler.writeJson(mapJSON, this.mapFilePath);
    }

    /**
     *
     */
    void load() {

        JSONObject mapData = FilesystemHandler.readJSON(this.mapFilePath);
        LOGGER.info(this.getMapId());
        this.name = (String) mapData.get("name");
        this.width = (int) (long) mapData.get("width");
        this.height = (int) (long) mapData.get("height");
        this.setDimension(this.width, this.height);
        this.tilesetId = (int) (long) mapData.get("tilesetIndex");
        JSONArray tileLayersJSON = (JSONArray) mapData.get("tileLayers");
        if (tileLayersJSON != null) {
            for (int layer = 0; layer < tileLayersJSON.size(); layer++) {
                JSONArray layerData = (JSONArray) tileLayersJSON.get(layer);
                for (Object aLayerData : layerData) {
                    JSONObject tileData = (JSONObject) aLayerData;
                    int x = (int) (long) tileData.get("x");
                    int y = (int) (long) tileData.get("y");
                    int tsX = (int) (long) tileData.get("tsX");
                    int tsY = (int) (long) tileData.get("tsY");
                    this.addTile(layer, x, y, tsX, tsY);
                }
            }
        }

        JSONArray eventLayerJSON = (JSONArray) mapData.get("eventLayer");
        if (eventLayerJSON != null) {

            LOGGER.info("Found Event Layer");
            for (int eventIndex = 0; eventIndex < eventLayerJSON.size(); eventIndex++) {
                JSONObject eventData = (JSONObject) eventLayerJSON.get(eventIndex);
                int x = (int) (long) eventData.get("x");
                int y = (int) (long) eventData.get("y");
                this.addEvent(x,y,new Event(x,y));
            }
        }

    }

    /**
     *
     * @param layerId
     * @param x
     * @param y
     * @param tsX
     * @param tsY
     * @return
     */
    public TileCell addTile(int layerId, int x, int y, int tsX, int tsY) {
        TileCell tCell = new TileCell(x,y,tsX, tsY);
        this.layers.get(layerId).setAt(x, y, tCell);
        return tCell;
    }

    /**
     *
     * @return
     */
    public int getTilesetId() {
        return this.tilesetId;
    }

    /**
     *
     * @return
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     *
     * @param cellSize
     */
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    /**
     *
     * @return
     */
    public Map getParent() {
        return this.parent;
    }

    /**
     *
     */
    private void fireMapProprtiesUpdated() {
        for (MapPropertiesUpdated current : this.mapPropertiesListeners) {
            current.mapPropertiesUpdated(this);
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Event handleEvent(int x,int y) {
        return this.eventLayer.handleEvent(x,y);
    }

    /**
     *
     * @param x
     * @param y
     * @param event
     */
    public void addEvent(int x, int y, Event event) {
        this.eventLayer.setAt(x,y,event);
    }
}
