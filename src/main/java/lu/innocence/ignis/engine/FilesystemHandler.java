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
            File f = new File(current);
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
     *
     * @param json
     * @param path
     * @return
     */
    public static boolean writeJson(JSONObject json,String path) {

        FileWriter file = null;
        try {
            file = new FileWriter(path);
            file.write(json.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        } finally {
            try {
                if (file != null)
                    file.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
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
        FileReader fReader = null;

        try {
            fReader = new FileReader(path);
            obj = parser.parse(fReader);
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            LOGGER.error(e);
            return null;
        } finally {
            try {
                if (fReader!=null)
                    fReader.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }

    }

}
