package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";
    public static final String PATH_TO_PROPERTIES = "D:\\TestXML2\\src\\main\\resources\\dao.properties";
    private static FileInputStream in;



    private static Properties properties = new Properties();

    public static String getProperty(String name) {
        if (properties.isEmpty()) {
            try{
                in = new FileInputStream(PATH_TO_PROPERTIES);
                properties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }
        System.out.println(properties.getProperty(name));
        return properties.getProperty(name);
    }

}
