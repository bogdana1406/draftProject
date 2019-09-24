package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        startNode.setName("SCHEMAS");
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

    // +++++++++++++++добавляем все узлы в БД (Tables, Views, Stored_Procedures, Functions)+++++++++++++++++++++++++++
    //============================================================================

    // функция принимает корень дерева и название БД, в которую нужно подгружать узлы,
    // возвращает дерево с БД, в которую добавленны узлы
    public MyNode parseNodesToDatabase(String databaseName, MyNode parent) {

        // находим узел, название которого соответствует названию БД
        MyNode dbNode = parent.getChild(databaseName);
        // узел таблиц
        MyNode tableNode = new MyNode();
        tableNode.setName("Tables");
        // узел Views
        MyNode viewNode = new MyNode();
        viewNode.setName("Views");
        // узел Stored_Procedures
        MyNode procedureNode = new MyNode();
        procedureNode.setName("Stored_Procedures");
        // узел Functions
        MyNode functionNode = new MyNode();
        functionNode.setName("Functions");

        dbNode.addChild(tableNode);
        dbNode.addChild(viewNode);
        dbNode.addChild(procedureNode);
        dbNode.addChild(functionNode);

       return parent;
    }

    // добавляем узлы в таблицу (Columns, Primary_Keys, Foreing_Keys).
    // функция принимает газвание БД, название таблицы, корень дерева.
    // возвращает дерево, в таблицу которого добавлены пустые узлы
    public MyNode parseNodesToTable(String databaseName, String tableName, MyNode parent) {
        // определяем узел название которого соответствует названию БД
        MyNode dbNode = parent.getChild(databaseName);
        // определяем узел, содержащийся с найденом узле БД,
        // название которого соответствует названию таблицы
        MyNode tableNode = findNodeInTreeFirst(dbNode, tableName);
        // узел колонок
        MyNode columnNode = new MyNode();
        columnNode.setName("Columns");
        // узел первичных ключей
        MyNode primaryNode = new MyNode();
        primaryNode.setName("Indexes");
        // узел внешних ключей
        MyNode foreingNode = new MyNode();
        foreingNode.setName("Foreing_Keys");

        tableNode.addChild(columnNode);
        tableNode.addChild(primaryNode);
        tableNode.addChild(foreingNode);

        return parent;
    }
    // ++++++++++++++++++++++добавляем узлы таблиц в БД (для одной любой БД)+++++++++++++++++++++++
    //=============================================================================================

    // функция получает входными параметрами meta, название БД, корень дерева, в который добавлены все БД.
    // К этому дереву будут добавленны новые узлы (таблицы). его функция и вернет
    public MyNode parseTablesToDatabase(DatabaseMetaData meta, String databaseName, MyNode parent) throws SQLException {
        String[] types = { "TABLE" };
        // в resultSet получаем все таблицы конкретной БД
        ResultSet resultSet = meta.getTables(databaseName, null, null, types);
        String tableName;
        // из дерева получаем узел (ребенка) с названием БД, а у него узел таблиц
        MyNode dbTableNode = parent.getChild(databaseName).getChild("Tables");
        while (resultSet.next()) {
            // пока в resultSet содежаться таблицы создаем новый узел
            MyNode tableNode = new MyNode();
            // получаем имя таблицы
            tableName = resultSet.getString("TABLE_NAME");
            // устанавливаем новому узлу имя, соответсвующее названию таблицы
            tableNode.setName(tableName);
            // добавляем в узел БД узел таблицы (каждой)
            dbTableNode.addChild(tableNode);
        }
        return parent;
    }

    // парсим сохраненные процедур в БД
    public MyNode parseStoredProceduresToDatabase(DatabaseMetaData meta, String databaseName, MyNode parent) throws SQLException {
        ResultSet resultSet = meta.getProcedures(databaseName, null, null);
        MyNode dbStoredProceduresNode = parent.getChild(databaseName).getChild("Stored_Procedures");
        String spName;
        while (resultSet.next()) {
            spName = resultSet.getString("PROCEDURE_NAME");
            int spType = resultSet.getInt("PROCEDURE_TYPE");
//            System.out.println("Stored Procedure Name: " + spName);
            MyNode spNode = new MyNode();
            spNode.setName(spName);
            if (spType == DatabaseMetaData.procedureNoResult) {
                dbStoredProceduresNode.addChild(spNode);
//                System.out.println("procedure No Result");
            }
        }
        return parent;
    }

    // парсим вьюшки в БД
    public MyNode parseViewsToDatabase(DatabaseMetaData meta, String databaseName, MyNode parent) throws SQLException {
        String[] types = { "VIEW" };
        // в resultSet получаем все таблицы конкретной БД
        ResultSet resultSet = meta.getTables(databaseName, null, null, types);
        String viewName;
        // из дерева получаем узел (ребенка) с названием БД, а у него узел Views
        MyNode dbTableNode = parent.getChild(databaseName).getChild("Views");
        while (resultSet.next()) {
            // пока в resultSet содежаться таблицы создаем новый узел
            MyNode tableNode = new MyNode();
            // получаем имя таблицы
            viewName = resultSet.getString("TABLE_NAME");
            // устанавливаем новому узлу имя, соответсвующее названию таблицы
            tableNode.setName(viewName);
            // добавляем в узел БД узел таблицы (каждой)
            dbTableNode.addChild(tableNode);
        }

        return parent;
    }

    // парсим функции в БД
    public MyNode parseFunctionsToDatabase(DatabaseMetaData meta, String databaseName, MyNode parent) throws SQLException {
        ResultSet resultSet = meta.getProcedures(databaseName, null, null);
        MyNode dbFunctionsNode = parent.getChild(databaseName).getChild("Functions");
        String functionName;
        while (resultSet.next()) {
            functionName = resultSet.getString("PROCEDURE_NAME");
            int spType = resultSet.getInt("PROCEDURE_TYPE");
//            System.out.println("Stored Procedure Name: " + spName);
            MyNode spNode = new MyNode();
            spNode.setName(functionName);
            if (spType == DatabaseMetaData.procedureReturnsResult) {
                dbFunctionsNode.addChild(spNode);
//                System.out.println("procedure Returns Result");
            }
        }
        return parent;
    }
    // ++++++++++++++++++++Добавляем узлы колонок к узлу одной (любой) таблицы+++++++++++++++++++++++++
    // ================================================================================================

    // функция получает входными параметрами meta, название таблицы, название БД, корень дерева, в который добавлены все БД.
    // К этому дереву будут добавленны новые узлы (колонки), а также ключи
    // к узлам колонок в качестве атрибутов установленны данные колонки (тип, размер...)
    // функция вернет измененное, полученное дерево
    public MyNode parseColumnToTable(DatabaseMetaData meta, String databaseName, String tableName, MyNode parent) throws SQLException {

        String columnName;
        // получаем узел, который соотвтствует БД (находим его как дочерний узел parent)
        MyNode dbTableNode = parent.getChild(databaseName).getChild("Tables");
        // получаем узел, который соотвтствует таблице (находим его как дочерний узел БД)
        MyNode tableColumnNode = dbTableNode.getChild(tableName).getChild("Columns");
        // получаем в resultSet колонки таблицы
        ResultSet resultSet = meta.getColumns(dbTableNode.getParent().getName(), null, tableColumnNode.getParent().getName(), null);
        // пока есть колонки
        while (resultSet.next()) {
            // создаем новый узел
            MyNode columnNode = new MyNode();
            // получаем название колонки
            columnName = resultSet.getString("COLUMN_NAME");
            // устанавливаем имя узла, соответственно названию колонки
            columnNode.setName(columnName);

            // ++++++++получить данные по столбцам+++++++++++++++++++
            Map<String, String> attributeSet = new HashMap<>();
            // получаем тип колонки
            String columnType = resultSet.getString("TYPE_NAME");
            attributeSet.put("Type", columnType);
            // размер колонки (разобратья со значением для int)
            int size = resultSet.getInt("COLUMN_SIZE");
            attributeSet.put("Size", Integer.toString(size));
            // значние по умолчанию
            String columnDef = resultSet.getString("COLUMN_DEF");
            attributeSet.put("Default_Value", columnDef);
            // допустимо ли NULL
            int nullable = resultSet.getInt("NULLABLE");
            attributeSet.put("Nullable", Integer.toString(nullable));
            // получить коммент
            String comment = resultSet.getString("REMARKS");
            attributeSet.put("Comment", comment);

            for (String key: attributeSet.keySet()) {
                if (attributeSet.get(key) != null) {
                    columnNode.addAttributes(key, attributeSet.get(key));
                }
            }
            tableColumnNode.addChild(columnNode);
        }
        return parent;
    }

    // парсим первичные ключи в таблицу
    public MyNode parsePrimaryKeyToTable(DatabaseMetaData meta, String databaseName, String tableName, MyNode parent) throws SQLException {
        String PKColumnName;
        // получаем узел, который соотвтствует БД (находим его как дочерний узел parent)
        MyNode dbTableNode = parent.getChild(databaseName);
        // получаем узел, который соотвтствует таблице (в найденной БД)
        MyNode tableNode = findNodeInTreeFirst(dbTableNode, tableName);
        // получаем узел индексов в данной таблице
        MyNode indexesNode = findNodeInTreeFirst(tableNode, "Indexes");
        MyNode PKNode = new MyNode();
        PKNode.setName("Primary_Key");
        indexesNode.addChild(PKNode);
        // получаем в resultSet колонки таблицы
        ResultSet resultSet = meta.getPrimaryKeys(databaseName, null, tableName);
//        if (resultSet != null) {
//
//        }
        // пока есть колонки
        while (resultSet.next()) {
            PKColumnName = resultSet.getString("COLUMN_NAME");
            PKNode.addAttributes("Сolumn", PKColumnName);

        }
        return parent;
    }

    // парсим внешние ключи в таблицу
    public MyNode parseForeingKeyToTable(DatabaseMetaData meta, String databaseName, String tableName, MyNode parent) throws SQLException {
//        String FKColumnName;
        // получаем узел, который соотвтствует БД (находим его как дочерний узел parent)
        MyNode dbTableNode = parent.getChild(databaseName);
        // получаем узел, который соотвтствует таблице (в найденной БД)
        MyNode tableNode = findNodeInTreeFirst(dbTableNode, tableName);
        // получаем узел индексов в данной таблице
        MyNode FKNode = findNodeInTreeFirst(tableNode, "Foreing_Keys");

        // получаем в resultSet колонки таблицы
        ResultSet resultSet = meta.getExportedKeys(databaseName, null, tableName);
//        if (resultSet != null) {
//
//        }
        // пока есть колонки
        while (resultSet.next()) {
            String FKColumnName = resultSet.getString("FKCOLUMN_NAME");
            String FKTableName = resultSet.getString("FKTABLE_NAME");
            FKNode.addAttributes("Сolumn", FKColumnName);
            FKNode.addAttributes("Table", FKTableName);

        }
        return parent;
    }
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