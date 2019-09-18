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
//        MyNode searchNode = treeDatabase.findNodeInTreeFirst(node, "mysql");
//        System.out.println("search node " + searchNode.getName());
        treeDatabase.parsTableToDatabase(meta, "productsdb", node);
        treeDatabase.parsTableToDatabase(meta, "sakila", node);
        treeDatabase.pareseColumnToTable(meta, "productsdb","customer", node);
        treeDatabase.pareseColumnToTable(meta, "sakila","customer", node);
        treeDatabase.parsTableToDatabase(meta, "world", node);
        treeDatabase.parsTableToDatabase(meta, "sys", node);



//        MyNode node = treeDatabase.parseDataBase(meta);
//        MyNode node = treeDatabase.parsDatabasesToTree(meta);
        treeDatabase.parsTableToTree(meta, node);
        treeDatabase.printTree(node, " ");
        TreeXML treeXML = new TreeXML();
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
