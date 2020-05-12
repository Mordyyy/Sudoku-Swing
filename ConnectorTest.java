import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectorTest {

    private static final String username = "root";
    private static final String password = "password";

    @Test
    public void goodConstructor() throws SQLException {
        Connector connector = new Connector(username, password);
        Connection con = connector.getConnection();
    }

    @Test
    public void badConstructor(){
        assertThrows(SQLException.class, ()-> {new Connector("wfafafw", "fwaawfa");});
    }
}