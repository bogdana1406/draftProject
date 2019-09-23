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
        parseStoredProceduresDatabase(meta);

        TreeDatabase treeDatabase = new TreeDatabase();
        MyNode node = treeDatabase.parsDatabasesToTree(meta);
//        MyNode searchNode = treeDatabase.findNodeInTreeFirst(node, "mysql");
//        System.out.println("search node " + searchNode.getName());
        treeDatabase.parseNodesToDatabase("productsdb", node);
//        treeDatabase.parseNodesToDatabase("sakila", node);
//        treeDatabase.parseNodesToDatabase("sakila", node);
//        treeDatabase.parseNodesToDatabase("world", node);
        treeDatabase.parsTablesToDatabase(meta, "productsdb", node);
        treeDatabase.parseNodesToTable("productsdb", "customer", node);
//        treeDatabase.parsTableToDatabase(meta, "sakila", node);
        treeDatabase.parseColumnToTable(meta, "productsdb","customer", node);
//        treeDatabase.parseColumnToTable(meta, "sakila","customer", node);
//        treeDatabase.parsTableToDatabase(meta, "world", node);
//        treeDatabase.parsTableToDatabase(meta, "sys", node);



//        MyNode node = treeDatabase.parseDataBase(meta);
//        MyNode node = treeDatabase.parsDatabasesToTree(meta);
//        treeDatabase.parsTableToTree(meta, node);
//        treeDatabase.printTree(node, " ");
        TreeXML treeXML = new TreeXML();
        treeXML.saveTreeToXML(node, "D:\\TestXML2\\src\\main\\resources\\data_edit.xml");

//        pareseColumnToTableST(conn, "productsdb", "customer");


    }

    private static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
            Config.getProperty(Config.DB_URL),
            Config.getProperty(Config.DB_LOGIN),
            Config.getProperty(Config.DB_PASSWORD)
        );
        return con;
    }

    private static void pareseColumnToTableST(Connection conn, String dbName, String tableName) throws SQLException {
        Statement st = conn.createStatement();

        ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);

        ResultSetMetaData rsMetaData = rs.getMetaData();

        int numberOfColumns = rsMetaData.getColumnCount();
        System.out.println("resultSet MetaData column Count=" + numberOfColumns);

        for (int i = 1; i <= numberOfColumns; i++) {
            System.out.println("column MetaData ");
            System.out.println("column number " + i);
            System.out.print(rsMetaData.getColumnName(i) + " ");
            System.out.print(rsMetaData.getColumnType(i) + " ");
            // indicates the designated column's normal maximum width in
            // characters
            System.out.println(rsMetaData.getColumnDisplaySize(i));
            // gets the designated column's suggested title
            // for use in printouts and displays.
        }

        st.close();
        conn.close();
    }

    public static void parseStoredProceduresDatabase(DatabaseMetaData meta) throws SQLException {
        ResultSet resultSet = meta.getProcedures("sakila", null, "%");
        while (resultSet.next()) {
            String spName = resultSet.getString("PROCEDURE_NAME");
            int spType = resultSet.getInt("PROCEDURE_TYPE");
            System.out.println("Stored Procedure Name: " + spName);
            if (spType == DatabaseMetaData.procedureReturnsResult) {
                System.out.println("procedure Returns Result");
            } else if (spType == DatabaseMetaData.procedureNoResult) {
                System.out.println("procedure No Result");
            } else {
                System.out.println("procedure Result unknown");
            }

        }
//        return parent;
    }
}