package lu.innocence.ignis.engine;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fabien Steines
 */
public class FilesystemHandler {

    private static final Logger LOGGER = LogManager.getLogger(FilesystemHandler.class);

    private FilesystemHandler() {}

    /**
     *
     * @param f1
     * @param f2
     * @return
     */
    public static String concat(String f1,String f2) {
        return String.format("%s/%s",f1,f2);
    }

    /**
     *
     * @return
     */
    public static String getUserHomeDir() {
        return System.getProperty("user.home");
    }

    /**
     *
     * @param folder
     * @return
     */
    public static List<String> readFolderContent(String folder) {

        File f = new File(folder);
        String[] fList = f.list();
        if (fList != null) {
            return Arrays.asList(fList);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param folder
     * @return
     */
    public static List<String> readSubFolders(String folder) {
        List<String> returnList = new ArrayList<>();

        for (String current : FilesystemHandler.readFolderContent(folder)) {
            File f = new File(FilesystemHandler.concat(folder,current));
            if (f.isDirectory() && !".".equals(current) && !"..".equals(current)) {
                returnList.add(current);
            }
        }

        return returnList;
    }

    /**
     *
     * @param folder
     * @return
     */
    public static List<String> readSubFiles(String folder) {
        List<String> returnList = new ArrayList<>();

        for (String current : FilesystemHandler.readFolderContent(folder)) {
            File f = new File(FilesystemHandler.concat(folder,current));
            if (f.isFile() && !".".equals(current) && !"..".equals(current)) {
                returnList.add(current);
            }
        }

        return returnList;
    }

    public static boolean isFile(String path) {
        return (new File(path)).isFile();
    }

    public static boolean isFolder(String path) {
        return (new File(path)).isDirectory();
    }

    public static boolean isAudio(String path) {
        String mimeType = new MimetypesFileTypeMap().getContentType( new File(path) );
        // mimeType should now be something like "image/png"
        return mimeType.substring(0,5).equalsIgnoreCase("audio");
    }

    public static boolean isImage(String path) {
        String mimeType = new MimetypesFileTypeMap().getContentType( new File(path) );
        // mimeType should now be something like "image/png"
        return mimeType.substring(0,5).equalsIgnoreCase("image");
    }

    public static boolean exists(String path) {
        return (new File(path)).exists();
    }

    /**
     *
     * @param path
     * @return
     */
    public static boolean createFolder(String path) {
        return (new File(path)).mkdir();
    }

    /**
     *
     * @param path
     * @return
     */
    public static boolean deleteFolder(String path) {
        try {
            FileUtils.deleteDirectory(new File(path));
            return true;
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }
    }

    /**
     * Copy input file to the new output folder
     * @param source inputFile
     * @param destination outputDestinationFile
     * @return true if all worked well, false if there has been an error
     */
    public static boolean copy(String source,String destination) {

        File sourceF = new File(source);
        File destF = new File(destination);

        try {
            if (sourceF.isFile())
                FileUtils.copyFile(sourceF, destF);
            else
                FileUtils.copyDirectory(sourceF, destF);
            return true;
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }
    }

    /**
     * Write an JSON Object to the Filesystem on a specific path
     * @param json the JSON Object
     * @param path the output path
     * @return true if everything worked fine, and false if there has been an error
     */
    public static boolean writeJson(JSONObject json,String path) {

        try ( FileWriter file = new FileWriter(path)) {
            file.write(json.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }

    }

    /**
     *
     * @param path
     * @return
     */
    public static JSONObject readJSON(String path) {

        JSONParser parser = new JSONParser();
        Object obj = null;

        try (FileReader fReader = new FileReader(path)) {
            obj = parser.parse(fReader);
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            LOGGER.error(e);
            return null;
        }
    }

}
