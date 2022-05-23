package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class Config {
    private static final String dataSetPath = "src/main/resources/dataSet.json";
    private static Properties PROPERTY;

    public static String get(String parameter) {
        String configPath;
        configPath = (System.getProperty("configFile") == null) ? "src/main/resources/config.properties"
                : System.getProperty("configFile");
        if (PROPERTY == null) {
            try {
                PROPERTY = new Properties();
                PROPERTY.load(new FileInputStream(configPath));
            } catch (IOException e) {
            }
        }
        return PROPERTY.getProperty(parameter);
    }

    public static JsonArray getDataSet() {
        JsonArray dataSet = new JsonArray();

        try (Reader testDataFile = new FileReader(dataSetPath)) {
            dataSet = JsonParser.parseReader(testDataFile).getAsJsonArray();
        } catch (Exception e) {
            System.out.println("Cannot load testData file " + e);
        }
        return dataSet;
    }
}