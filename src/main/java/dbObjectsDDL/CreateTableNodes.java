package dbObjectsDDL;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import tree.MyNode;
import tree.MyTree;

public class CreateTableNodes extends MyTree {

    public MyNode getPkNode(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getPrimaryKeys(databaseName, null, tableName);
        MyNode pkNodes = new MyNode();
        pkNodes.setName("PK_key");
        MyNode pkNode = new MyNode();
        Map<String, String> pkColumnAttributes = new LinkedHashMap<>();
        int i = 1;
        while (resultSet.next()) {
            String pkName = resultSet.getString("PK_NAME");
            pkNode.setName(pkName);
            String columnName = resultSet.getString("COLUMN_NAME");
            pkColumnAttributes.put("column" + i, columnName);
            i++;
        }
        for (String key : pkColumnAttributes.keySet()) {
//            System.out.println(key + " " + pkColumnAttributes.get(key));
            pkNode.addAttributes(key, pkColumnAttributes.get(key));
        }
        pkNodes.addChild(pkNode);
        return pkNode;
    }

    public MyNode getUkNode(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getIndexInfo(databaseName, null, tableName, true, true);
        MyNode uniqueNodes = new MyNode();
        uniqueNodes.setName("unique_nodes");
        MyNode uniqueKeyNode;
        while (resultSet.next()) {
            int i = 1;
            String uqName = resultSet.getString("INDEX_NAME");
            MyNode ukNode = uniqueNodes.getChild(uqName);
            if (ukNode == null) {
                uniqueKeyNode = new MyNode();
                uniqueKeyNode.setName(uqName);
            } else {
                uniqueKeyNode = ukNode;
                i++;
            }
            String uqColumnName = resultSet.getString("COLUMN_NAME");
            uniqueKeyNode.addAttributes("column" + i, uqColumnName);

            MyNode isNode = findNodeInTreeFirst(uniqueNodes, uniqueKeyNode.getName());
            if (isNode == null) {
                uniqueNodes.addChild(uniqueKeyNode);
            }
        }
        return uniqueNodes;
    }


    private MyNode createFkNode(String fkName) {
        MyNode fkNode = new MyNode();
        fkNode.setName(fkName);
// узел с колонками внешнего ключа
        MyNode fkColumn = new MyNode();
        fkColumn.setName("fk_Columns");
        fkNode.addChild(fkColumn);
//            узел с references
        MyNode fkReferences = new MyNode();
        fkReferences.setName("references");
        fkNode.addChild(fkReferences);
        //            узел c привилами
        MyNode fkRules = new MyNode();
        fkRules.setName("rules");
        fkNode.addChild(fkRules);
//            узел со связанной таблицей
        MyNode fkReferencesTable = new MyNode();
        fkReferencesTable.setName("table_references");
        fkReferences.addChild(fkReferencesTable);
        //            узел со связанной колонкой
        MyNode fkReferencesColumn = new MyNode();
        fkReferencesColumn.setName("columns_references");
        fkReferences.addChild(fkReferencesColumn);
        return fkNode;
    }

    public MyNode getFkNode(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet = meta.getImportedKeys(databaseName, null, tableName);

        MyNode fkNodes = new MyNode();
        fkNodes.setName("foreign_keys");
        MyNode foreignKeyNode = null;
        while (resultSet.next()) {
            int i = 1;
//            constrainName
            String fkName = resultSet.getString("FK_NAME");
            MyNode fkNode = findNodeInTreeFirst(fkNodes, fkName);
            if (fkNode == null) {
                foreignKeyNode = createFkNode(fkName);
            } else {
                foreignKeyNode = fkNode;
                i++;
            }

//              ForeingKeyColumn
            String fkcolumnName = resultSet.getString("FKCOLUMN_NAME");
            MyNode fkColumns = foreignKeyNode.getChild("fk_Columns");
            fkColumns.addAttributes("column" + i, fkcolumnName);
//              REFERENCES
//            refTableName
            String refTableName = resultSet.getString("PKTABLE_NAME");
            MyNode tableReferences = findNodeInTreeFirst(foreignKeyNode, "table_references");
            tableReferences.addAttributes("table_name", refTableName);
//            refColumnName
            String refColumnName = resultSet.getString("PKCOLUMN_NAME");
            MyNode columnsReferences = findNodeInTreeFirst(foreignKeyNode, "columns_references");
            columnsReferences.addAttributes("column_ref" + i, refColumnName);
//            rules
            MyNode rulesNode = foreignKeyNode.getChild("rules");
            String updateRule = resultSet.getString("UPDATE_RULE");
            rulesNode.addAttributes("UPDATE_RULE", updateRule);
            String deleteRule = resultSet.getString("DELETE_RULE");
            rulesNode.addAttributes("DELETE_RULE", deleteRule);

            MyNode isNode = findNodeInTreeFirst(fkNodes, foreignKeyNode.getName());
            if (isNode == null) {
                fkNodes.addChild(foreignKeyNode);
            }
        }
        return fkNodes;
    }


    // получить все колонки
    public MyNode getTableColumnNode (DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        ResultSet resultSet;
        MyNode tableNode = new MyNode();
        tableNode.setName(tableName);
        resultSet = meta.getColumns(databaseName, null, tableName, null);

        while (resultSet.next()) {
            MyNode columnNode = new MyNode();
            String columnName = resultSet.getString("COLUMN_NAME");
            columnNode.setName(columnName);
            Map<String, String> attributeSet = new HashMap<>();
//  TYPE
            String typeName = resultSet.getString("TYPE_NAME");
            if ("VARCHAR".equals(typeName)) {
                int colimnSize = resultSet.getInt("COLUMN_SIZE");
                typeName = typeName + "(" + colimnSize + ")";
            }
            attributeSet.put("type", typeName);
//  NULLABLE
            String nullable = resultSet.getString("IS_NULLABLE");
            if ("FALSE".equals(nullable)) {
                attributeSet.put("nullable", "NOT NULL");
            }
//  DEFAULT
            String columnDef = resultSet.getString("COLUMN_DEF");
            if (!"".equals(columnDef)) {
                attributeSet.put("columnDef", "DEFAULT " + columnDef);
            }
            for (String key : attributeSet.keySet()) {
                if ((attributeSet.get(key) != null) && (!"".equals(attributeSet.get(key)))) {
                    columnNode.addAttributes(key, attributeSet.get(key));
                }
            }
            tableNode.addChild(columnNode);
        }
        return tableNode;
    }

}
