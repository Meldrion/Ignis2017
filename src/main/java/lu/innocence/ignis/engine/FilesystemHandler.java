package lu.innocence.ignis.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Fabien Steines
 */
public class FilesystemHandler {

    private static final Logger LOGGER = LogManager.getLogger(FilesystemHandler.class);

    public static String concat(String f1,String f2) {
        return String.format("%s/%s",f1,f2);
    }


    public static String getUserHomeDir() {
        return System.getProperty("user.home");
    }

    public static boolean createFolder(String path) {
        return (new File(path)).mkdir();
    }

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
