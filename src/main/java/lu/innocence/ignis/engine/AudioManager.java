package lu.innocence.ignis.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Fabien Steines
 *         mailto:fabien.steines@post.lu
 *         Copyright by POST Technologies
 *         <p>
 *         <p>
 *         Last revision - $(DATE) - Fabien Steines
 */
public class AudioManager {

    private static final Logger LOGGER = LogManager.getLogger(AudioManager.class);
    private String bgmFolder;
    private String seFolder;
    private SoftwareAudioEngine[]  bgmSlots;

    /**
     *
     */
    public AudioManager() {
        this.bgmSlots = new SoftwareAudioEngine[10]; // Allocate 10 empty slots
    }

    /**
     *
     * @param bgmFolder
     */
    public void setBgmFolder(String bgmFolder) {
        this.bgmFolder = bgmFolder;
    }


    /**
     *
     * @param seFolder
     */
    public void setSEFolder(String seFolder) {
        this.seFolder = seFolder;
    }

    /**
     *
     * @return
     */
    public List<String> getBGMList() {
        return FilesystemHandler.readFolderContent(this.bgmFolder);
    }

    /**
     *
     * @return
     */
    public List<String> getSEList() {
        return FilesystemHandler.readFolderContent(this.seFolder);
    }

    /**
     *
     * @param bgmName
     * @param bgmSlot
     */
    public void loadBGMInSlot(String bgmName,int bgmSlot) {

        if (-1 < bgmSlot && bgmSlot < 10) {
            String url = String.format("file:///%s", FilesystemHandler.concat(this.bgmFolder, bgmName));
            SoftwareAudioEngine softwareAudioEngine = new SoftwareAudioEngine(url);
            this.bgmSlots[bgmSlot] = softwareAudioEngine;
        } else {
            LOGGER.error("Invalid BGM Slot... Slot must be between 0 and 10 but it is {}",bgmSlot);
        }
    }

    /**
     *
     * @param bgmSlot
     */
    public void playBGM(int bgmSlot) {

        if (-1 < bgmSlot && bgmSlot < 10) {
            SoftwareAudioEngine softwareAudioEngine = this.bgmSlots[bgmSlot];
            if (softwareAudioEngine != null) {
                if (!softwareAudioEngine.isAlive() || softwareAudioEngine.isInterrupted()) {
                    softwareAudioEngine.start();
                }
            } else {
                LOGGER.error("No active audio element in slot {}",bgmSlot);
            }
        } else {
            LOGGER.error("Invalid BGM Slot... Slot must be between 0 and 10 but it is {}",bgmSlot);
        }
    }

    /**
     *
     * @param bgmSlot
     */
    public void stopBGM(int bgmSlot) {

        if (-1 < bgmSlot && bgmSlot < 10) {
            SoftwareAudioEngine softwareAudioEngine = this.bgmSlots[bgmSlot];
            if (softwareAudioEngine != null) {
                softwareAudioEngine.endAudio();
                softwareAudioEngine.interrupt();
                this.bgmSlots[bgmSlot] = null; // Delete it from the Memory
            } else {
                LOGGER.error("No active audio element in slot {}",bgmSlot);
            }
        } else {
            LOGGER.error("Invalid BGM Slot... Slot must be between 0 and 10 but it is {}",bgmSlot);
        }
    }
}
