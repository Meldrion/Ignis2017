package lu.innocence.ignis.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paulscode.sound.Library;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;

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

    private static SoundSystem bgmSoundSystem;
    private static boolean audioSystemIsOnline;

    private String activeBGM;

    /**
     *
     */
    public AudioManager() {
        if (!AudioManager.audioSystemIsOnline) {
            AudioManager.initAudioSystem();
        }
    }

    /**
     *
     */
    public static void initAudioSystem() {
        AudioManager.bgmSoundSystem = new SoundSystem();
        try {

            LOGGER.info("Using Library Java Sound");
            SoundSystemConfig.addLibrary( LibraryJavaSound.class );

            LOGGER.info("Registering WAV Codec");
            SoundSystemConfig.setCodec( "wav", CodecWav.class );
            LOGGER.info("Registering OGG Codec");
            SoundSystemConfig.setCodec( "ogg", CodecJOrbis.class );

            AudioManager.audioSystemIsOnline = true;

        } catch( SoundSystemException e ) {
            LOGGER.error( "Error linking with the LibraryJavaSound plug-in" );
            LOGGER.error(e);
        }

    }

    /**
     *
     */
    public static void shutdownAudioSystem() {
        if (audioSystemIsOnline) {
            AudioManager.bgmSoundSystem.cleanup();
            AudioManager.audioSystemIsOnline = false;
        }
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
     */
    public void playBGM(String bgmName) {

        String fullFilePath = String.format("file:///%s", FilesystemHandler.concat(this.bgmFolder, bgmName));
        this.activeBGM = bgmName;
        this.bgmSoundSystem.backgroundMusic(bgmName,fullFilePath,true);
    }

    /**
     *
     */
    public void stopBGM() {
        this.bgmSoundSystem.stop(this.activeBGM);
    }
}
