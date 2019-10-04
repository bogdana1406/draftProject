package dbObjectsDDL;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tree.MyTree;

public class TableToString extends MyTree {

    public Map<String, List<String>> getPrimaryKey (DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getPrimaryKeys(databaseName, null, tableName);
        Map<String, List<String>> primaryKeyData = new LinkedHashMap<>();
        String pkName = null;
        List<String> pkColumns = new ArrayList<>();
        while (resultSet.next()) {
            pkName = resultSet.getString("PK_NAME");
            String columnName = resultSet.getString("COLUMN_NAME");
            pkColumns.add(columnName);
        }
        primaryKeyData.put(pkName, pkColumns);

        return primaryKeyData;
    }

    public Map<String, List<String>> fromColumns (DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        Map <String, List<String>> columnsData = new LinkedHashMap<>();

        ResultSet resultSet = meta.getColumns(databaseName, null, tableName, null);

        while (resultSet.next()) {
            List<String> columnAttributes = new ArrayList<>();
//            ColumnName
            String columnName = resultSet.getString("COLUMN_NAME");
//            columnType
            String typeName = resultSet.getString("TYPE_NAME");
//            (add decimal)
            if (("VARCHAR".equals(typeName)) || ("NVARCHAR").equals(typeName)) {
                int colimnSize = resultSet.getInt("COLUMN_SIZE");
                typeName = typeName + "(" + colimnSize + ")";
            }
            columnAttributes.add(typeName);
//            Nullable
            String isNullable = resultSet.getString("IS_NULLABLE");
            if ("YES".equals(isNullable)) {
                columnAttributes.add("NOT NULL");
            }
//            Default Value
            String columnDef = resultSet.getString("COLUMN_DEF");
            if (columnDef != null) {
                columnAttributes.add("DEFAULT " + columnDef);
            }
//            autoIncrement
            String isAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
            if ("YES".equals(isAutoincrement)) {
                columnAttributes.add("AUTO_INCREMENT");
            }

//            //            isGeneratedcolumn
//            String isGeneratedcolumn = resultSet.getString("IS_GENERATEDCOLUMN");
//            if ("YES".equals(isAutoincrement)) {
////                get expression for generation
//                String exp = "";
//                columnAttributes.add("GENERATED ALWAYS AS " + exp);
//            }

            columnsData.put(columnName, columnAttributes);
        }
        return columnsData;
    }

    public String createTableDDL (DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        String tableDDL;
        Map<String, List<String>> columns = fromColumns(meta, databaseName, tableName);

        StringBuffer createTableStr = new StringBuffer("CREATE TABLE ");
        createTableStr.append(tableName);
        createTableStr.append(" (\n");

        Set<String> column = columns.keySet();
        for (String columnName: columns.keySet()) {
            createTableStr.append(columnName + " ");
//            System.out.println(columnName + ": ");
            List<String> columnAttrs = columns.get(columnName);
            for (String columnAttr: columnAttrs) {
                createTableStr.append(columnAttr);
//                System.out.println(columnAtr);
            } createTableStr.append(",\n");
        }
        tableDDL = createTableStr.toString();
        System.out.println(tableDDL);
        return tableDDL;
    }
}
