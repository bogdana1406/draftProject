package database;

import config.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class DbConnector {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, TransformerException, ParserConfigurationException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        String databaseName = "productsdb";

        Connection conn= getConnection();
        DatabaseMetaData meta = conn.getMetaData();

        // в resultSet получили все базы данных

        ResultSet resultSet = meta.getCatalogs();

        while (resultSet.next()) {
            System.out.println("Schema Name = " + resultSet.getString("TABLE_CAT"));

        }

        resultSet.close();

        // --- LISTING DATABASE TABLE NAMES ---

        String[] types = { "TABLE" };

        resultSet = meta.getTables(databaseName, null, "%", types);

        String tableName = "";

        while (resultSet.next()) {
            tableName = resultSet.getString(3);
            System.out.println("CreateTableNodes Name = " + tableName);
        }

        resultSet.close();
//
        // --- LISTING DATABASE COLUMN NAMES ---

        resultSet = meta.getColumns(databaseName, null, tableName, "%");
        while (resultSet.next()) {
            System.out.println("Column Name of table " + tableName + " = "
                + resultSet.getString(4));

    }

    }

    private static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
            Config.getProperty(Config.DB_URL),
            Config.getProperty(Config.DB_LOGIN),
            Config.getProperty(Config.DB_PASSWORD)
        );
        return con;
    }
}
