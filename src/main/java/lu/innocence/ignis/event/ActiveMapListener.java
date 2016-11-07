package lu.innocence.ignis.event;

import lu.innocence.ignis.engine.Map;

/**
 * @author Fabien Steines
 */
public interface ActiveMapListener {
    void activeMapChanged(Map map);
}
