package properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Prop {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("one", "1");
        System.out.println(properties.getProperty("one"));

        FileOutputStream out = new FileOutputStream("My.properties");
        properties.store(out, "comments");
        FileInputStream in = new FileInputStream("My.properties");
        properties.load(in);
        System.out.println(properties.getProperty("one"));

    }
}
