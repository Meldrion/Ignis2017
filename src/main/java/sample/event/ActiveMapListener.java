package sample.event;

import sample.engine.Map;

/**
 * @author Fabien Steines
 */
public interface ActiveMapListener {
    void activeMapChanged(Map map);
}
