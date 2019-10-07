package dbObjectsDDL;

import config.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import printerDDL.TableDDL;
import tree.MyNode;
import xml.TreeXML;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, TransformerException, ParserConfigurationException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn= getConnection();

        DatabaseMetaData meta = conn.getMetaData();
        CreateTableNodes createTableNodesNode = new CreateTableNodes();
//        createTableNodesNode.createTableDDL(meta, "sakila", "address");
//        createTableNodesNode.getPrimaryKey(meta, "productsdb", "ptable");
//        createTableNodesNode.getPrimaryKey(meta, "productsdb", "ptable");
//        createTableNodesNode.getUniqueKey(meta, "sakila", "address");
//        Helper.getPK(meta, "productsdb", "ptable");
//        Helper.getImportedKeys(meta, "productsdb", "thirdtable");
        Helper.getIndexInfo(meta, "productsdb", "customers4");
//        MyNode uniqueKey = createTableNodesNode.getUkNode(meta, "productsdb", "customers4");
//        MyNode pkNode = createTableNodesNode.getPkNode(meta, "productsdb", "ptable");
//
//        MyNode foreignKeys = createTableNodesNode.getFkNode(meta, "productsdb", "customer_new2");
//        System.out.println("==============================");
////        createTableNodesNode.getIndexInfo(meta, "sakila", "address");
//        MyNode columns = createTableNodesNode.getTableColumnNode(meta, "sakila", "address");
//        TreeXML treeXML = new TreeXML();
//        treeXML.saveTreeToXML(foreignKeys, "D:\\TestXML2\\src\\main\\resources\\tableFKNode_edit.xml");
//        treeXML.saveTreeToXML(columns, "D:\\TestXML2\\src\\main\\resources\\tablColumns_edit.xml");
//        treeXML.saveTreeToXML(uniqueKey, "D:\\TestXML2\\src\\main\\resources\\unique_edit.xml");
//        TableDDL tableDDL = new TableDDL();
////        tableDDL.printColumnFromTableNode(columns);
//        System.out.println("================================");
////        tableDDL.printFKTableNode(foreignKeys);
////        DatabaseMetaData meta = conn.getMetaData();
//        tableDDL.printColumnFromTableNode(meta, "productsdb", "customer_new2");
//        tableDDL.printPkTableNode(meta, "productsdb", "customer_new2");
//        tableDDL.printUkTableNode(meta, "productsdb", "customers4");
//        tableDDL.printFkTableNode(meta, "productsdb", "customer_new2");
//        tableDDL.printPKTableNode(pkNode);
//        tableDDL.printUniqueTableNode(uniqueKey);
//
//        ResultSet resultSet = meta.getColumns("sakila", null, "actor", null);
//
//        while (resultSet.next()) {
//            System.out.print(" column_name  - ");
//            System.out.println(resultSet.getString("IS_AUTOINCREMENT"));
//        }

//        Map<String, Map<String, String>> actor = pareseColumnToTableST(conn, "actor");
//        printColumnData(actor);
    }

    private static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
            Config.getProperty(Config.DB_URL),
            Config.getProperty(Config.DB_LOGIN),
            Config.getProperty(Config.DB_PASSWORD)
        );
        return con;
    }

    private static Map<String, Map<String, String>> pareseColumnToTableST(Connection conn, String tableName) throws SQLException {

        // ВАЖНО!!!! подключение к КОНКРЕТНОЙ БД, поэтому достаточно передать имя таблицы

        // создали объект - запрос
        Statement st = conn.createStatement();
        // сам SQL-запрос (получаем все колонки)
        ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);
        // создали объект ResultSetMetaData у rs - который является результатом выполнения запроса.
        // Возвращает колонки (информация о колонках)
        ResultSetMetaData rsMetaData = rs.getMetaData();
        // получим количество колонок
        int numberOfColumns = rsMetaData.getColumnCount();

        String columnName;
//        System.out.println("resultSet MetaData column Count=" + numberOfColumns);
        Map<String, Map<String, String>> colDescription = new HashMap<>();

        for (int i = 1; i <= numberOfColumns; i++) {
            Map<String, String> columnAttrr = new HashMap<>();
            columnName = rsMetaData.getColumnName(i);
            int columnType = rsMetaData.getColumnType(i);
            columnAttrr.put("columnType", Integer.toString(columnType));
            int columnDisplaySize = rsMetaData.getColumnDisplaySize(i);
            columnAttrr.put("DisplaySize", Integer.toString(columnDisplaySize));
            boolean autoIncrement = rsMetaData.isAutoIncrement(i);
            columnAttrr.put("autoIncrement", Boolean.toString(autoIncrement));
            colDescription.put(columnName, columnAttrr);
        }

        st.close();
        conn.close();
        return colDescription;
    }

    public static void printColumnData(Map<String, Map<String, String>> colDescription) {

        for (String columnName: colDescription.keySet()) {
            System.out.println(columnName + ": ");
            Map<String, String> attrPair = colDescription.get(columnName);
            for (String columnAtr: attrPair.keySet()) {
                System.out.println(columnAtr + " - " + attrPair.get(columnAtr));
            }
        }

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
    }
}
