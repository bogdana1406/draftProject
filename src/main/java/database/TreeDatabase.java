package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import tree.MyNode;
import tree.MyTree;


public class TreeDatabase extends MyTree {

    // +++++++++++++++создаем дерево, содержащее все БД+++++++++++++++++++++++++++
    //============================================================================
    public MyNode parsDatabasesToTree(DatabaseMetaData meta) throws SQLException {
        String databaseName;
        //создаем корневой узел
        MyNode startNode = new MyNode();
        // присваиваем название корневому узлу
        startNode.setName("databases");
        // устанавливаем startNode (с названием "databases") корнем дерева
        setRoot(startNode);
        //в resultSet получаем все БД
        ResultSet resultSet = meta.getCatalogs();
        while (resultSet.next()) {
//            System.out.println("Schema Name = " + resultSet.getString("TABLE_CAT"));
            // для каждой БД создаем новый узел
            MyNode dataBaseNode = new MyNode();
            // получаем имя БД
            databaseName = resultSet.getString("TABLE_CAT");
            // устанавливаем название узла, соответственно названию БД
            dataBaseNode.setName(databaseName);
            // к startNode (который установлен как корень дерева) добавляем узкл, с названием БД
            startNode.addChild(dataBaseNode);
        }
//        resultSet.close();
        // возвращаем корень дерева (startNode)
        return startNode;
    }

    // ++++++++++++++++++++++добавляем узлы таблиц в БД (для одной любой БД)+++++++++++++++++++++++
    //=============================================================================================

    // функция получает входными параметрами meta, название БД, корень дерева, в который добавлены все БД.
    // К этому дереву будут добавленны новые узлы (таблицы). его функция и вернет
    public MyNode parsTableToDatabase(DatabaseMetaData meta, String databaseName, MyNode parent) throws SQLException {
        String[] types = { "TABLE" };
        // в resultSet получаем все таблицы конкретной БД
        ResultSet resultSet = meta.getTables(databaseName, null, null, types);
        String tableName;
        // из дерева получаем узел (ребенка) с названием БД
        MyNode dbNode = parent.getChild(databaseName);
        while (resultSet.next()) {
            // пока в resultSet содежаться таблицы создаем новый узел
            MyNode tableNode = new MyNode();
            // получаем имя таблицы
            tableName = resultSet.getString("TABLE_NAME");
            // устанавливаем новому узлу имя, соответсвующее названию таблицы
            tableNode.setName(tableName);
            // добавляем в узел БД узел таблицы (каждой)
            dbNode.addChild(tableNode);
        }

        // добавить узлы процедур
        // добавить узлы вьюшек
//        resultSet.close();
//        }
        return parent;
    }

    // ++++++++++++++++++++Добавляем узлы колонок к узлу одной (любой) таблицы+++++++++++++++++++++++++
    // ================================================================================================

    // функция получает входными параметрами meta, название таблицы, название БД, корень дерева, в который добавлены все БД.
    // К этому дереву будут добавленны новые узлы (колонки), а также ключи
    // к узлам колонок в качестве атрибутов установленны данные колонки (тип, размер...)
    // функция вернет измененное, полученное дерево
    public MyNode pareseColumnToTable(DatabaseMetaData meta, String databaseName, String tableName, MyNode parent) throws SQLException {

        String columnName;


        MyNode dbNode = parent.getChild(databaseName);

        MyNode tableNode = dbNode.getChild(tableName);
//        MyNode dbNode = tableNode.getParent();

        ResultSet resultSet = meta.getColumns(dbNode.getName(), null, tableNode.getName(), null);

        while (resultSet.next()) {
            MyNode columnNode = new MyNode();


            columnName = resultSet.getString("COLUMN_NAME");
            columnNode.setName(columnName);


            String columnType = resultSet.getString("TYPE_NAME");


            // получить данные по столбцам
            // размер колонки (разобратья со значением для int)
            int size = resultSet.getInt("COLUMN_SIZE");
            // допустимо ли NULL
            int nullable = resultSet.getInt("NULLABLE");
            // значние по умолчанию
            String columnDef = resultSet.getString("COLUMN_DEF");

            columnNode.addAttributes("type", columnType);
            columnNode.addAttributes("size", Integer.toString(size));
            columnNode.addAttributes("NULLABLE", Integer.toString(nullable));
            columnNode.addAttributes("Default value", columnDef );
            tableNode.addChild(columnNode);
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
