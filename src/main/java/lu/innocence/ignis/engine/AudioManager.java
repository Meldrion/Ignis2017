package lu.innocence.ignis.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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
     * There is a problem with the Audio Manager on Windows 32 and Windows 64
     * It will work just fine, but generate the following error:
     *
     *Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002
     *
     * The fix to this is to generate the following reg keys:
     * 32 bit Windows
     * HKEY_LOCAL_MACHINE\Software\JavaSoft\Prefs

     * 64 bit Windows
     * HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\JavaSoft\Prefs
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

        try {

            LOGGER.info("Using Library Java Sound");
            SoundSystemConfig.addLibrary( LibraryJavaSound.class );
            LOGGER.info("Registering WAV Codec");
            SoundSystemConfig.setCodec( "wav", CodecWav.class );
            LOGGER.info("Registering OGG Codec");
            SoundSystemConfig.setCodec( "ogg", CodecJOrbis.class );

            AudioManager.bgmSoundSystem = new SoundSystem();
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

        String fullFilePath = FilesystemHandler.concat(this.bgmFolder, bgmName);
        File musicFile = new File(fullFilePath);

        if (musicFile.exists() && musicFile.isFile()) {

            try {

                URL musicFileUrl = musicFile.toURI().toURL();
                this.activeBGM = bgmName;
                AudioManager.bgmSoundSystem.newStreamingSource(true,bgmName,musicFileUrl,bgmName,
                        true,0,0,0,SoundSystemConfig.ATTENUATION_NONE,0);
                AudioManager.bgmSoundSystem.play(bgmName);


            } catch (MalformedURLException e) {
                LOGGER.error(e);
            }

        }

    }

    /**
     *
     */
    public void stopBGM() {
        AudioManager.bgmSoundSystem.stop(this.activeBGM);
    }
}
