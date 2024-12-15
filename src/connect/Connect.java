package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Connect {
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String NAME = "calouself";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, NAME);
    
    private Connection con;
    
    private static Connect instance;
    
    public static Connect getInstance() {
        if (instance == null) {
            instance = new Connect();
        }
        return instance;
    }
    
    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public PreparedStatement prepareStatement(String query) {
        try {
            return con.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Connection getConn() {
        return con;
    }

    // Add these new methods
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        con.setAutoCommit(autoCommit);
    }

    public void commit() throws SQLException {
        con.commit();
    }

    public void rollback() throws SQLException {
        con.rollback();
    }
}

