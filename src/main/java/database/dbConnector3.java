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
//        parseStoredProceduresDatabase(meta);

        TreeDatabase treeDatabase = new TreeDatabase();
        MyNode node = treeDatabase.parsDatabasesToTree(meta);
        treeDatabase.parseDatabaseToTree(meta, "sakila", node);
        treeDatabase.parseDatabaseToTree(meta, "productsdb", node);
        TreeXML treeXML = new TreeXML();
        treeXML.saveTreeToXML(node, "D:\\TestXML2\\src\\main\\resources\\database_edit.xml");
//        MyNode searchNode = treeDatabase.findNodeInTreeFirst(node, "mysql");
//        System.out.println("search node " + searchNode.getName());
        treeDatabase.parseNodesToDatabase("productsdb", node);
        treeDatabase.parseNodesToDatabase("sakila", node);
//        treeDatabase.parseStoredProceduresToDatabase(meta, "sakila", node);
//        treeDatabase.parseFunctionsToDatabase(meta, "sakila", node);

//        treeDatabase.parseViewsToDatabase(meta, "sakila", node);
//        treeDatabase.parseNodesToDatabase("sakila", node);
//        treeDatabase.parseNodesToDatabase("sakila", node);
//        treeDatabase.parseNodesToDatabase("world", node);
        treeDatabase.parseTablesToDatabase(meta, "productsdb", node);
        treeDatabase.parseTablesToDatabase(meta, "sakila", node);
        treeDatabase.parseNodesToTable("productsdb", "customers", node);
        treeDatabase.parseNodesToTable("productsdb", "orders", node);
        treeDatabase.parseNodesToTable("sakila", "customer", node);
//        treeDatabase.parsTableToDatabase(meta, "sakila", node);
//        treeDatabase.parseColumnToTable(meta, "productsdb","customers", node);
        treeDatabase.parseIndexesToTable(meta, "productsdb", "customers", node);
        treeDatabase.parseIndexesToTable(meta, "productsdb", "orders", node);
//        treeDatabase.parseForeingKeyToTable(meta, "productsdb", "customers", node);
        treeDatabase.parseForeingKeyToTable(meta, "productsdb", "orders", node);
        treeDatabase.parseForeingKeyToTable(meta, "sakila", "customer", node);
//        treeDatabase.parseColumnToTable(meta, "sakila","customer", node);
//        treeDatabase.parsTableToDatabase(meta, "world", node);
//        treeDatabase.parsTableToDatabase(meta, "sys", node);



//        MyNode node = treeDatabase.parseDataBase(meta);
//        MyNode node = treeDatabase.parsDatabasesToTree(meta);
//        treeDatabase.parsTableToTree(meta, node);
//        treeDatabase.printTree(node, " ");
//        TreeXML treeXML = new TreeXML();
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
        ResultSet resultSet = meta.getProcedures("sys", null, "%");
        while (resultSet.next()) {
            String spName = resultSet.getString("PROCEDURE_NAME");
            int spType = resultSet.getInt("PROCEDURE_TYPE");
//            System.out.print("Stored Procedure Name: " + spName + "   -   ");
            if (spType == DatabaseMetaData.procedureReturnsResult) {
                System.out.println("functions: ");
                System.out.println(spName);
            }
            if (spType == DatabaseMetaData.procedureNoResult) {
                System.out.println("procedure");
                System.out.println(spName);
            }
            if((spType != DatabaseMetaData.procedureNoResult) && (spType != DatabaseMetaData.procedureReturnsResult)) {
                System.out.println("procedure Result unknown");
            }

        }
//        return parent;
    }
}