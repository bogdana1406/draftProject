package dbObjectsDDL;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import tree.MyNode;

public class Helper {
    public static void getColumns (DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        MyNode tableNode = new MyNode();
        tableNode.setName(tableName);
        ResultSet resultSet = meta.getColumns(databaseName, null, tableName, null);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int noofcols = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 0 ; i < noofcols ; i++ ) {

                System.out.println(rsmd.getColumnName( i + 1 ) + ": \t\t" + resultSet.getString( i + 1 ));
            }
            System.out.println("++++++++++++++++");
        }
    }

    public static void getIndexInfo(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getIndexInfo(databaseName, null, tableName, true, false);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int cols = rsmd.getColumnCount();
        while(resultSet.next()) {
            for (int i = 1; i <= cols; i++) {

                String str = resultSet.getString(i);
                System.out.println(rsmd.getColumnName(i) + "   " + str);
            }
            System.out.println("+++++++++++++++++");
        }
    }

    public static void getPK(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getPrimaryKeys(databaseName, null, tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int cols = rsmd.getColumnCount();
        while(resultSet.next()) {
            for (int i = 1; i <= cols; i++) {

                String str = resultSet.getString(i);
                System.out.println(rsmd.getColumnName(i) + "   " + str);
            }
            System.out.println("+++++++++++++++++");
        }
    }

    public static void getImportedKeys(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getImportedKeys(databaseName, null, tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int cols = rsmd.getColumnCount();

        while (resultSet.next()) {
            for (int i = 1; i <= cols; i++) {
                String str = resultSet.getString(i);
                System.out.println(rsmd.getColumnName(i) + " - " + resultSet.getString(i));
            }
            System.out.println("+++++++++++++++++++");
        }
    }

    public static void getExportedKeys(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getExportedKeys(databaseName, null, tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int cols = rsmd.getColumnCount();

        while (resultSet.next()) {
            for (int i = 1; i <= cols; i++) {
                String str = resultSet.getString(i);
                System.out.println(rsmd.getColumnName(i) + " - " + resultSet.getString(i));
            }
            System.out.println("+++++++++++++++++++");
        }
    }


}
