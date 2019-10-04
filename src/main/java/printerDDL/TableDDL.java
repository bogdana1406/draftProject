package printerDDL;

import dbObjectsDDL.CreateTableNodes;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import tree.MyNode;
import tree.MyTree;

//    CREATE TABLE Customers
//        (
//            Id INT AUTO_INCREMENT,
//            Age INT,
//            FirstName VARCHAR(20) NOT NULL,
//    LastName VARCHAR(20) NOT NULL,
//    Email VARCHAR(30),
//    Phone VARCHAR(20) NOT NULL,
//    CONSTRAINT customers_pk PRIMARY KEY(Id),
//    CONSTRAINT customer_phone_uq UNIQUE(Phone),
//    CONSTRAINT customer_age_chk CHECK(Age >0 AND Age<100)
//);

public class TableDDL extends CreateTableNodes {

    public void printColumnFromTableNode(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {

        MyNode tableColumns = getTableColumnNode(meta, databaseName, tableName);

        StringBuffer strBuff = new StringBuffer("CREATE TABLE ");
//        String tableName = tableColumns.getName();
        strBuff.append(tableName + "(\n");

        for (MyNode tableColumn: tableColumns.getChildren()) {
            strBuff.append(tableColumn.getName() + " ");
            Map<String, String> columnAttributes = tableColumn.getAttributes();
            int size = columnAttributes.size();
            int i = 0;
            for (String key: columnAttributes.keySet()) {
                if (i < size - 1) {
                    strBuff.append(columnAttributes.get(key) + " ");
                    i++;
                } else {
                    strBuff.append(columnAttributes.get(key) + ",\n");
                }
            }
//            strBuff.append(",\n");
        }

        System.out.println(strBuff.toString());
    }

    public void printFkTableNode(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        MyNode fkNodes = getFkNode(meta, databaseName, tableName);
        for (MyNode fkNode: fkNodes.getChildren()) {
            StringBuffer strBuff = new StringBuffer("CONSTRAINT ");
            strBuff.append(fkNode.getName() + " (");
            MyNode fkColumn = fkNode.getChild("fk_Columns");
            Map<String, String> columnAttributes = fkColumn.getAttributes();
            int size = columnAttributes.size();
            int i = 0;
            for (String key: columnAttributes.keySet()) {
                if (i < size - 1) {
                    strBuff.append(columnAttributes.get(key) + ", ");
                    i++;
                } else {
                    strBuff.append(columnAttributes.get(key) + ") ");
                }
            }

             strBuff.append(" REFERENCES ");
            MyNode refTable = findNodeInTreeFirst(fkNode, "table_references");
            Map<String, String> refTableAttributes = refTable.getAttributes();
            for (String key: refTableAttributes.keySet()) {
                strBuff.append(refTableAttributes.get(key) + "(");
            }
            MyNode refColumn = findNodeInTreeFirst(fkNode, "columns_references");
            Map<String, String> refColumnAttributes = refColumn.getAttributes();
            int sizeColRef = refColumnAttributes.size();
            int j = 0;
            for (String key: refColumnAttributes.keySet()) {
                if (j < sizeColRef - 1) {
                    strBuff.append(refColumnAttributes.get(key) + ", ");
                    j++;
                } else {
                    strBuff.append(refColumnAttributes.get(key) + ") ");
                }
            }

            MyNode rules = fkNode.getChild("rules");
            Map<String, String> rulesAttributes = rules.getAttributes();
            String delRule = rulesAttributes.get("DELETE_RULE");
            strBuff.append("ON DELETE " + delRule + " ");
            String updateRule = rulesAttributes.get("UPDATE_RULE");
            strBuff.append("ON UPDATE " + updateRule);
            strBuff.append(",");
            System.out.println(strBuff.toString());
        }
    }

    public void printPkTableNode(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        MyNode pkNode = getPkNode(meta, databaseName, tableName);
        StringBuffer strBuff = new StringBuffer("CONSTRAINT ");
        strBuff.append(pkNode.getName()+ " PRIMARY KEY (");
        Map<String, String> columnAttributes = pkNode.getAttributes();
        int size = columnAttributes.size();
        int i = 0;
        for (String key: columnAttributes.keySet()) {
            if (i < size - 1) {
                strBuff.append(columnAttributes.get(key) + ", ");
                i++;
            } else {
                strBuff.append(columnAttributes.get(key) + ")");
            }
        }
        System.out.println(strBuff);
    }

    public void printUkTableNode(DatabaseMetaData meta, String databaseName, String tableName) throws SQLException {
        MyNode ukNodes = getUkNode(meta, databaseName, tableName);
        StringBuffer strBuff = new StringBuffer("CONSTRAINT ");
        for (MyNode ukNode: ukNodes.getChildren()) {
            strBuff.append(ukNode.getName() + " UNIQUE (");
            Map<String, String> uqNodeAttributes = ukNode.getAttributes();
            int size = uqNodeAttributes.size();
            int i = 0;
            for (String key: uqNodeAttributes.keySet()) {
                if (i < size - 1) {
                    strBuff.append(uqNodeAttributes.get(key) + ", ");
                    i++;
                } else {
                strBuff.append(uqNodeAttributes.get(key) + ")\n");
                }
            }
        }
        System.out.println(strBuff);
    }
}
