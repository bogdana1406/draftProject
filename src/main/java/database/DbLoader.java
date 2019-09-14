package database;

import java.sql.Connection;

import java.sql.DatabaseMetaData;

import java.sql.DriverManager;

import java.sql.ResultSet;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class DbLoader {
    private static Logger log = Logger.getLogger(DbLoader.class);

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String url = "jdbc:mysql://localhost/test?serverTimezone=Europe/Moscow&useSSL=false";
            String databaseName = "test";

            String userName = "root";

            String password = "08041938";

            String mySQLPort = "3306";

            String hostUrl = "127.0.0.1";

            // Setup the connection with the DB

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, userName, password);

            // --- LISTING DATABASE SCHEMA NAMES ---

            ResultSet resultSet = conn.getMetaData().getCatalogs();

            while (resultSet.next()) {

                System.out.println("Schema Name = " + resultSet.getString("TABLE_CAT"));
                log.info("Schema Name = " + resultSet.getString("TABLE_CAT"));

            }

            resultSet.close();

            // --- LISTING DATABASE TABLE NAMES ---

            String[] types = { "TABLE" };

            resultSet = conn.getMetaData()

                .getTables(databaseName, null, "%", types);

            String tableName = "";

            while (resultSet.next()) {

                tableName = resultSet.getString(3);

                System.out.println("Table Name = " + tableName);
                log.info("Table Name = " + tableName);

            }

            resultSet.close();

            // --- LISTING DATABASE COLUMN NAMES ---

            DatabaseMetaData meta = conn.getMetaData();

            resultSet = meta.getColumns(databaseName, null, tableName, "%");

            while (resultSet.next()) {

                System.out.println("Column Name of table " + tableName + " = "

                    + resultSet.getString(4));
                log.info("Column Name of table " + tableName + " = "

                    + resultSet.getString(4));

            }
    }
}
