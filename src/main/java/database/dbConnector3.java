package database;

import config.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import tree.MyNode;
import xml.TreeXML;

public class dbConnector3 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, TransformerException, ParserConfigurationException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection conn= getConnection();



        DatabaseMetaData meta = conn.getMetaData();

        TreeDatabase treeDatabase = new TreeDatabase();
        MyNode node = treeDatabase.parsDatabasesToTree(meta);
        treeDatabase.parseDatabaseToTree(meta, "sakila", node);
        treeDatabase.parseDatabaseToTree(meta, "productsdb", node);
        TreeXML treeXML = new TreeXML();
        treeXML.saveTreeToXML(node, "D:\\TestXML2\\src\\main\\resources\\database_edit.xml");

        treeDatabase.parseNodesToDatabase("productsdb", node);
        treeDatabase.parseNodesToDatabase("sakila", node);

        treeDatabase.parseTablesToDatabase(meta, "productsdb", node);
        treeDatabase.parseTablesToDatabase(meta, "sakila", node);
        treeDatabase.parseNodesToTable("productsdb", "customers", node);
        treeDatabase.parseNodesToTable("productsdb", "orders", node);
        treeDatabase.parseNodesToTable("sakila", "customer", node);

        treeDatabase.parseIndexesToTable(meta, "productsdb", "customers", node);
        treeDatabase.parseIndexesToTable(meta, "productsdb", "orders", node);

        treeDatabase.parseForeingKeyToTable(meta, "productsdb", "orders", node);
        treeDatabase.parseForeingKeyToTable(meta, "sakila", "customer", node);


        treeXML.saveTreeToXML(node, "D:\\TestXML2\\src\\main\\resources\\data_edit.xml");



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