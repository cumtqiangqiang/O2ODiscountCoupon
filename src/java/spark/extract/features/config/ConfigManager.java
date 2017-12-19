package spark.extract.features.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by UC227911 on 12/19/2017.
 */
public class ConfigManager {

    private static Properties properties = new Properties();


    static {
       try {

           InputStream input = ConfigManager.class.getClassLoader()
                   .getResourceAsStream("properties/config.properties");

           properties.load(input);

        } catch (IOException e) {
           e.printStackTrace();
       }


    }

    public static Boolean getBoolean(String key){
        String value = properties.getProperty(key);

        try {

            return Boolean.valueOf(value);
        }catch (Exception e){

            e.printStackTrace();

        }

        return false;

    }



}
