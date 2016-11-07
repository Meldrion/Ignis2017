package lu.innocence.ignis.event;

import lu.innocence.ignis.engine.Project;

/**
 * @author Fabien Steines
 */
public interface ActiveProjectListener {
    void activeProjectChanged(Project p);
}
