package utils;

import java.io.File;
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
        if (PROPERTY == null) {
            try {
                String configPath =System.getProperty("config");
                if (configPath==null||(!new File(configPath).isFile())) {
                    configPath = "config/config.properties";
                }

                if (!new File(configPath).isFile()) {
                    configPath = "src/main/resources/config.properties";
                }
                PROPERTY = new Properties();
                PROPERTY.load(new FileInputStream(configPath));
            } catch (IOException e) {
            }
        }
        return PROPERTY.getProperty(parameter);
    }
}