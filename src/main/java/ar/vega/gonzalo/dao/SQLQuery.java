package ar.vega.gonzalo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//Patron de diseño Singleton
public class SQLQuery {

    private static SQLQuery instance;

    private Connection conn;
    
//    private final String dbUser = "root";
//    private final String dbPass = "";
    private final String dbUser = "root";
    private final String dbPass = "root";
    private final String dbHost = "localhost:3306";
    private final String dbName = "sga_vega";


    private SQLQuery() {
        // Constructor privado para que no se pueda crear instancia fuera de esa clase
    }

    public static SQLQuery getInstance() {
        if (instance == null) {
            instance = new SQLQuery();
        }
        return instance;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:mysql://" +dbHost + "/" + dbName, dbUser, dbPass);
        }
        return conn;
    }

    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
