package database;

import config.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import tree.MyNode;
import tree.MyTree;
import xml.TreeXML;

public class DbConnector2 {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, TransformerException, ParserConfigurationException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        List<String> databaseNames = new ArrayList<>();
        String databaseName = "productsdb";
        Connection conn= getConnection();

        // в resultSet получили все базы данных
        ResultSet resultSet = conn.getMetaData().getCatalogs();
        MyNode startNode = new MyNode();
        startNode.setName("database");
        TreeXML tree = new TreeXML();
        tree.setRoot(startNode);
        while (resultSet.next()) {
            System.out.println("Schema Name = " + resultSet.getString("TABLE_CAT"));
//            MyNode dataBaseNode = new MyNode();
//            dataBaseNode.setName(resultSet.getString("TABLE_CAT"));
            databaseNames.add(resultSet.getString("TABLE_CAT"));
//            startNode.addChild(dataBaseNode);
        }
//        tree.printTreeStructure(startNode);

        resultSet.close();

        // --- LISTING DATABASE TABLE NAMES ---

        String[] types = { "TABLE" };

        for (String dbName: databaseNames) {
            MyNode dataBaseNode = new MyNode();
            dataBaseNode.setName(dbName);
            startNode.addChild(dataBaseNode);


            System.out.println("========="+dbName);
            resultSet = conn.getMetaData()
                .getTables(dbName, null, "%", types);

            String tableName;

            while (resultSet.next()) {
                MyNode tableNode = new MyNode();
                tableName = resultSet.getString(3);
                tableNode.setName(tableName);
                dataBaseNode.addChild(tableNode);
                System.out.println("Table Name = " + tableName);

            }

            resultSet.close();
        }
        tree.saveTreeToXML(startNode, "D:\\TestXML2\\src\\main\\resources\\data_edit.xml");
//
        // --- LISTING DATABASE COLUMN NAMES ---

//        DatabaseMetaData meta = conn.getMetaData();
//
//        resultSet = meta.getColumns(databaseName, null, tableName, "%");
//
//        while (resultSet.next()) {
//
//            System.out.println("Column Name of table " + tableName + " = "
//
//                + resultSet.getString(4));
//
//    }

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

