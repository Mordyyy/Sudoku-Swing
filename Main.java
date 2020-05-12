import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Main{
    private static String useQuery,dropQuery,createTableQuery,insertQuery;

    public static void main(String[] args) throws SQLException {
        Connector connector = new Connector("root", "password");
        Connection con = connector.getConnection();
        initQueries();
        PreparedStatement s1 = con.prepareStatement(useQuery);
        s1.execute();
        PreparedStatement s2 = con.prepareStatement(dropQuery);
        s2.execute();
        PreparedStatement s3 = con.prepareStatement(createTableQuery);
        s3.execute();
        PreparedStatement s4 = con.prepareStatement(insertQuery);
        s4.execute();
        Functions fun = new Functions(con);
        View vw = new View(fun);
    }

    private static void initQueries(){
        useQuery = "USE tab;";
        dropQuery = "DROP TABLE IF EXISTS metropolises;";
        createTableQuery = "CREATE TABLE metropolises (\n" +
                "    metropolis CHAR(64),\n" +
                "    continent CHAR(64),\n" +
                "    population BIGINT\n" +
                ");";
        insertQuery = "INSERT INTO metropolises VALUES\n" +
                "\t(\"Mumbai\",\"Asia\",20400000),\n" +
                "        (\"New York\",\"North America\",21295000),\n" +
                "\t(\"San Francisco\",\"North America\",5780000),\n" +
                "\t(\"London\",\"Europe\",8580000),\n" +
                "\t(\"Rome\",\"Europe\",2715000),\n" +
                "\t(\"Melbourne\",\"Australia\",3900000),\n" +
                "\t(\"San Jose\",\"North America\",7354555),\n" +
                "\t(\"Rostov-on-Don\",\"Europe\",1052000);\n";
    }
}
