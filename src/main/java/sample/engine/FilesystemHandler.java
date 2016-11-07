package sample.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    public void writeJson(JSONObject json,String path) {
        try {

            FileWriter file = new FileWriter(path);
            file.write(json.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public JSONObject read(String path) {

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(path));
            return (JSONObject) obj;

        } catch (IOException | ParseException e) {
            LOGGER.error(e);
            return null;
        }
    }

}
