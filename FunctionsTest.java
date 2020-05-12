import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {
    private static final String username = "root";
    private static final String password = "password";
    private static final String city = "Kutaisi";
    private static final String cont = "Europe";
    private  Connection c;
    private  Functions f;

    @BeforeEach
    public void init() throws SQLException {
        Connector connector = new Connector(username,password);
        c = connector.getConnection();
        PreparedStatement s1 = c.prepareStatement("use tb;");
        s1.execute();
        PreparedStatement s2 = c.prepareStatement("DROP TABLE IF EXISTS metropolises;");
        s2.execute();
        PreparedStatement s3 = c.prepareStatement("CREATE TABLE metropolises (\n" +
                "    metropolis CHAR(64),\n" +
                "    continent CHAR(64),\n" +
                "    population BIGINT\n" +
                ");");
        s3.execute();
        f = new Functions(c);
    }

    @Test
    public void constructor(){
        Functions func;
        assertDoesNotThrow(()-> new Functions(c));
    }

    @Test
    public void allFilled() throws SQLException {
        f.add(new Information("Kutaisi", "Europe", 200000));
        ResultSet res = f.search(new Information("Kutaisi", "Europe", 200),true,true);
        assertTrue(res.next());
        assertEquals(res.getString(1), "Kutaisi");
        assertEquals(res.getString(2), "Europe");
        assertEquals(res.getInt(3), 200000);
        res = f.search(new Information("Kutaisi", "Europe", 200),true,false);
        assertTrue(res.next());
        assertEquals(res.getString(1), "Kutaisi");
        assertEquals(res.getString(2), "Europe");
        assertEquals(res.getInt(3), 200000);
        res = f.search(new Information("Kutaisi", "Europe", 200000000),false,false);
        assertTrue(res.next());
        assertEquals(res.getString(1), "Kutaisi");
        assertEquals(res.getString(2), "Europe");
        assertEquals(res.getInt(3), 200000);
        res = f.search(new Information("Kutaisi", "Europe", 200000000),false,true);
        assertTrue(res.next());
        assertEquals(res.getString(1), "Kutaisi");
        assertEquals(res.getString(2), "Europe");
        assertEquals(res.getInt(3), 200000);
    }

    @Test
    public void firstEmpty() throws SQLException {
        ResultSet res;
        f.add(new Information("", "Europe", 10));
        res = f.search(new Information("", "Europe", 1), true,true);
        assertTrue(res.next());
        f.add(new Information("", "Europe", 10));
        res = f.search(new Information("", "Europe", 1), true,false);
        assertTrue(res.next());
        f.add(new Information("", "Europe", 10));
        res = f.search(new Information("", "Europe", 100), false,true);
        assertTrue(res.next());
        f.add(new Information("", "Europe", 10));
        res = f.search(new Information("", "Europe", 100), false,false);
        assertTrue(res.next());
    }

    @Test
    public void secondEmpty() throws SQLException {
        ResultSet res;
        f.add(new Information(city,"",2 ));
        res = f.search(new Information(city,"",1), true,true);
        assertTrue(res.next());
        assertEquals(res.getString(1), city);
        assertEquals(res.getInt(3), 2);
        f.add(new Information(city,"",2 ));
        res = f.search(new Information(city,"",1), true,false);
        assertTrue(res.next());
        assertEquals(res.getString(1), city);
        assertEquals(res.getInt(3), 2);
        f.add(new Information(city,"",2 ));
        res = f.search(new Information(city,"",3), false,true);
        assertTrue(res.next());
        assertEquals(res.getString(1), city);
        assertEquals(res.getInt(3), 2);
        f.add(new Information(city,"",2 ));
        res = f.search(new Information(city,"",3), false,false);
        assertTrue(res.next());
        assertEquals(res.getString(1), city);
        assertEquals(res.getInt(3), 2);
    }

    @Test
    public void thirdEmpty() throws SQLException {
        ResultSet res;
        f.add(new Information(city,cont,-1));
        res = f.search(new Information(city,cont,-1), true,true);
        assertTrue(res.next());
        assertEquals(city, res.getString(1));
        assertEquals(cont, res.getString(2));
        f.add(new Information(city,cont,-1));
        res = f.search(new Information(city,cont,-1), false,true);
        assertTrue(res.next());
        assertEquals(city, res.getString(1));
        assertEquals(cont, res.getString(2));f.add(new Information(city,cont,-1));
        res = f.search(new Information(city,cont,-1), true,false);
        assertTrue(res.next());
        assertEquals(city, res.getString(1));
        assertEquals(cont, res.getString(2));f.add(new Information(city,cont,-1));
        res = f.search(new Information(city,cont,-1), false,false);
        assertTrue(res.next());
        assertEquals(city, res.getString(1));
        assertEquals(cont, res.getString(2));
    }

    @Test
    public void allEmpty() throws SQLException {
        ResultSet res;
        Information info = new Information("", "", -1);
        f.add(info);
        res = f.search(info, true,true);
        assertFalse(res.next());
        f.add(info);
        res = f.search(info, true,false);
        assertFalse(res.next());
    }

    @Test
    public void secondFilled() throws SQLException {
        ResultSet res;
        f.add(new Information("", "WF", -1));
        res = f.search(new Information("", "WF", -1), true,true);
        assertTrue(res.next());
        f.add(new Information("", "WF", -1));
        res = f.search(new Information("", "WF", -1), false,true);
        assertTrue(res.next());
        f.add(new Information("", "WF", -1));
        res = f.search(new Information("", "WF", -1), true,false);
        assertTrue(res.next());
        f.add(new Information("", "WF", -1));
        res = f.search(new Information("", "WF", -1), false,false);
        assertTrue(res.next());
        res = f.search(new Information("","",-1), true,true);
        assertFalse(res.next());
        res = f.search(new Information("","",-1), true,false);
        assertTrue(res.next());
    }

}