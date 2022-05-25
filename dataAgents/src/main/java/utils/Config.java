package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Берёт конфиг по следующим путям:
 * Если находит дальше не ищет
 * 1.-Dconfig=config_file_path
 * 2.config/config.properties
 * 3.src/main/resources/config.properties
 */
public class Config {

    private static Properties PROPERTY;

    public static String get(String parameter) {
        String configPath;
        configPath = (System.getProperty("configFile") == null) ? "src/main/resources/config.properties" : System.getProperty("configFile");
        if (PROPERTY == null) {
            try {
                PROPERTY = new Properties();
                PROPERTY.load(new FileInputStream(configPath));
            } catch (IOException e) {
            }
        }
        return PROPERTY.getProperty(parameter);
    }
}