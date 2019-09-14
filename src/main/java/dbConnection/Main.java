package dbConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DbConnection con = new DbConnection("localhost", "root", "08041938", "test");
        con.initProperties();
        con.init();

//        String url = "jdbc:mysql://localhost/productsdb?useSSL=false";
//        String username = "root";
//        String password = "08041938";
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.newDocument();
//        Element results = doc.createElement("Results");
//        doc.appendChild(results);

//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection con = DriverManager
//            .getConnection(url, username, password);

//        DatabaseMetaData dbMetaData = con.getMetaData();
//
//
//        // getting Database Schema Names
//        ResultSet rs = con.getMetaData().getCatalogs();
//        while (rs.next()) {
//            System.out.println("Schema Name - " + rs.getString("TABLE_CAT"));
////
//        }
//
//
//        con.close();
//        rs.close();
    }
}
