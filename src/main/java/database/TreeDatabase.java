package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import tree.MyNode;
import tree.MyTree;


public class TreeDatabase extends MyTree {

    public MyNode parsDatabasesToTree(DatabaseMetaData meta) throws SQLException {
        ResultSet resultSet = meta.getCatalogs();
        MyNode startNode = new MyNode();
        startNode.setName("database");
        setRoot(startNode);
        while (resultSet.next()) {
//            System.out.println("Schema Name = " + resultSet.getString("TABLE_CAT"));
            MyNode dataBaseNode = new MyNode();
            dataBaseNode.setName(resultSet.getString("TABLE_CAT"));
            startNode.addChild(dataBaseNode);
        }
//        resultSet.close();
        return startNode;
    }

    public MyNode parsTableToTree(DatabaseMetaData meta, String databaseName, MyNode parent) throws SQLException {
        String[] types = { "TABLE" };
        ResultSet resultSet = meta.getTables(databaseName, null, "%", types);

        String tableName;
        for (MyNode databaseNode: parent.getChildren()) {
            if (databaseName.equals(databaseNode.getName())) {
                while (resultSet.next()) {
                    MyNode tableNode = new MyNode();
                    tableName = resultSet.getString(3);
                    tableNode.setName(tableName);
                    databaseNode.addChild(tableNode);
//                    System.out.println("Table Name = " + tableName);
                }
            }
//            resultSet.close();
        }
        return parent;
    }

    public MyNode parsTableToTree(DatabaseMetaData meta, MyNode parent) throws SQLException {
        String[] types = { "TABLE" };

        String tableName;
        for (MyNode databaseNode: parent.getChildren()) {
            ResultSet resultSet = meta.getTables(databaseNode.getName(), null, "%", types);
                while (resultSet.next()) {
                    MyNode tableNode = new MyNode();
                    tableName = resultSet.getString(3);
                    tableNode.setName(tableName);
                    databaseNode.addChild(tableNode);
//                    System.out.println("Table Name = " + tableName);
                }
            }
//            resultSet.close();
        return parent;
    }

//    public MyNode parseCatalogToTree(DatabaseMetaData meta, MyNode parent) throws SQLException {
//        for (MyNode dbNode : parent.getChildren()) {
//            for (MyNode tableNode : dbNode.getChildren()) {
//                ResultSet resultSet = meta.getColumns(dbNode.getName(), null, tableNode.getName(), "%");
//                while (resultSet.next()) {
//                    System.out.println("Column Name of table " + tableNode.getName() + " = "
//                        + resultSet.getString(4));
//
//                }
//            }
//        }
//    return parent;
//    }

    public MyNode parseDataBase (DatabaseMetaData meta) throws SQLException {

        String[] types = {"TABLE"};
        String tableName;
        String columnName;

        ResultSet resultSet = meta.getCatalogs();
        MyNode startNode = new MyNode();
        startNode.setName("schemas");
        setRoot(startNode);

        //пока есть БД (resultSet.next()) добавляем в узел все БД
        while (resultSet.next()) {
//            System.out.println("Schema Name = " + resultSet.getString("TABLE_CAT"));
            // создается новый узел
            MyNode dataBaseNode = new MyNode();
            // узлу устанавливаем имя БД
            dataBaseNode.setName(resultSet.getString("TABLE_CAT"));
            // к startNode добавляем Child (dataBaseNode, у которого имя БД)
            startNode.addChild(dataBaseNode);
        }

        for (MyNode dbNode : startNode.getChildren()) {
            // переопределили resultSet, получает таблицы БД (dbNode)
            resultSet = meta.getTables(dbNode.getName(), null, "%", types);
            // пока в БД (dbNode) есть таблицы добавляем в узел БД все узлы таблицы (tableNode)
            while (resultSet.next()) {
                // создаем узел для таблицы
                MyNode tableNode = new MyNode();
                // получаем имя таблицы
                tableName = resultSet.getString(3);
                // устанавливаем узлу имя таблицы
                tableNode.setName(tableName);
                dbNode.addChild(tableNode);
//                    System.out.println("Table Name = " + tableName);
            }
            for (MyNode tableNode : dbNode.getChildren()) {
                // переопределили resultSet, получает таблицы БД (dbNode)
                resultSet = meta.getColumns(dbNode.getName(), null, tableNode.getName(), "%");

                while (resultSet.next()) {
                    MyNode columnNode = new MyNode();
                    columnName = resultSet.getString(4);
                    columnNode.setName(columnName);
                    tableNode.addChild(columnNode);
                }
            }
        }
        resultSet.close();
        return startNode;
    }

}
