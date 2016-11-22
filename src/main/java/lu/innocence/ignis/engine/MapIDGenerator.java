package lu.innocence.ignis.engine;

/**
 * @author Fabien Steines
 */
public class MapIDGenerator {

    private static final int MAX_ID_COUNTER = 99999;
    private boolean[] usedIds;

    public MapIDGenerator() {
        usedIds = new boolean[MAX_ID_COUNTER];
        for (int i = 0; i < MAX_ID_COUNTER; i++) {
            usedIds[i] = false;
        }
    }

    public void setIdUsed(int id, boolean taken) {
        this.usedIds[id] = taken;
    }

    public boolean isIdTaken(int id) {
        return this.usedIds[id];
    }

    public String generateUniqueName() {

        int id = getId();

        int zeroCount = String.valueOf(MAX_ID_COUNTER).length() - String.valueOf(id).length();
        String zeros = "";
        for (int i = 0; i < zeroCount; i++) {
            zeros += "0";
        }

        return String.format("map%s%d", zeros, id);

    }

    public int getId() {

        boolean foundId = false;
        int counter = 0;

        while (!foundId && counter < MAX_ID_COUNTER) {
            if (!this.usedIds[counter]) {
                this.setIdUsed(counter, true);
                foundId = true;
            } else {
                counter++;
            }
        }

        return foundId ? counter : -1;
    }

}
