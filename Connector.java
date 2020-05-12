import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    Connection con;
    public Connector(String username, String password) throws SQLException {
        String url = "jdbc:mysql://localhost";
        con = DriverManager.getConnection(url,username,password);
    }

    public Connection getConnection(){
        return con;
    }
}
