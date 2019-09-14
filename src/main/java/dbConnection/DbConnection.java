package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private String host;
    private String root;
    private String password;
    private String nameDb;
    private String url;

    private Properties properties = new Properties();
    private Connection mysqlConnect;

    public DbConnection(String host, String root, String password, String nameDb) {
        this.host = host;
        this.root = root;
        this.password = password;
        this.nameDb = nameDb;
    }

    public void initProperties() {

        url = "jdbc:mysql://" + host + "/" + nameDb + "?useSSL=false";

        properties.setProperty("user", root);
        properties.setProperty("password", password);
        properties.setProperty("characterEncoding", "UTF-8");
        properties.setProperty("useUnicode", "true");

        System.out.println("URL: " + url);

    }
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            mysqlConnect = DriverManager.getConnection(url, root, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
