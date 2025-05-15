package commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();
    private static String environment;

    static {
        try {
            // Get environment from system property, default to "dev" if not specified
            environment = System.getProperty("env", "dev");
            LogHelper.info("Loading configuration for environment: " + environment);
            
            // Load environment-specific properties file
            String configFile = String.format("/src/main/resources/config-%s.properties", environment);
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + configFile);
            properties.load(fis);
            fis.close();
            
            LogHelper.info("Loaded configuration from: " + configFile);
            LogHelper.info("Base URL: " + properties.getProperty("baseUrl"));
            LogHelper.info("retailer/username/password: " + properties.getProperty("retailer") + " " + properties.getProperty("username") + " " + properties.getProperty("password"));
        } catch (IOException e) {
            LogHelper.error("Error loading config properties: " + e.getMessage());
            // Fallback to default config.properties if environment-specific file not found
            try {
                LogHelper.info("Falling back to default config.properties");
                FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
                properties.load(fis);
                fis.close();
            } catch (IOException ex) {
                LogHelper.error("Error loading default config.properties: " + ex.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        // For authentication credentials, prioritize properties file over environment variables
        if (key.equals("username") || key.equals("password") || key.equals("retailer")) {
            String propValue = properties.getProperty(key);
            if (propValue != null) {
                return propValue;
            }
            // Fall back to environment variable if property not found
            return System.getenv(key.toUpperCase());
        } else {
            // For other properties, check environment variable first
            String envValue = System.getenv(key.toUpperCase());
            if (envValue != null) {
                return envValue;
            }
            // Then fall back to properties file
            return properties.getProperty(key);
        }
    }
    
    /**
     * Gets a property value with a default fallback if the property is not found.
     * 
     * @param key The property key
     * @param defaultValue The default value to return if property is not found
     * @return The property value or defaultValue if not found
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null) ? value : defaultValue;
    }
    
    public static String getEnvironment() {
        return environment;
    }
}
