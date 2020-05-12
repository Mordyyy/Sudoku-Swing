import javax.swing.table.AbstractTableModel;
import javax.xml.transform.Result;
import java.sql.*;

public class Functions {
    private Connection con;

    public Functions(Connection con) {
        this.con = con;
    }

    public void add(Information info) throws SQLException {
            if(info.getPopulation() == -1 && info.getMetropolis().equals("")){
                if(info.getContinent().equals("")) {
                   // System.out.println("viyavi");
                    return;
                }else{
                  //  System.out.println("qvevitac");
                }
            }
            PreparedStatement statement = con.prepareStatement("INSERT INTO metropolises VALUES(?,?,?);");
            statement.setString(1, info.getMetropolis());
            statement.setString(2, info.getContinent());
            if (info.getPopulation() == -1) {
                statement.setInt(3, -1);
            } else
                statement.setInt(3, info.getPopulation());
            statement.execute();
    }

    public ResultSet search(Information info, boolean larger, boolean exact) throws SQLException {
        ResultSet res = null;
        String query = "select * from metropolises where ";
        if(exact && info.getMetropolis().equals("") && info.getPopulation() == -1){
            if(info.getContinent().equals("")) {
                query += "1 = 2;";
                PreparedStatement stat = con.prepareStatement(query);
                res = stat.executeQuery(query);
                return res;
            }
        }
        String populationCondition = getPopulation(info, larger);
        String metropolisCondition = getMetropolis(info, exact);
        String continentCondition = getContinent(info, exact);
        query += metropolisCondition + " and " + continentCondition + " and " + populationCondition;
        PreparedStatement stat = con.prepareStatement(query);
        res = stat.executeQuery(query);
        return res;
    }

    private String getContinent(Information info, boolean exact) {
        String continentCondition;
        if (info.getContinent().equals("")) {
            continentCondition = "1 = 1 ";
        } else {
            if (exact) {
                continentCondition = "continent = \"" + info.getContinent() + "\" ";
            } else {
                continentCondition = "continent LIKE \"" + info.getContinent() + "%\" ";
            }
        }
        return continentCondition;
    }

    private String getMetropolis(Information info, boolean exact) {
        String metropolisCondition;
        if (info.getMetropolis().equals("")) {
            metropolisCondition = "1 = 1 ";
        } else {
            if (exact) {
                metropolisCondition = "metropolis = \"" + info.getMetropolis() + "\" ";
            } else {
                metropolisCondition = "metropolis LIKE \"" + info.getMetropolis() + "%\" ";
            }
        }
        return metropolisCondition;
    }

    private String getPopulation(Information info, boolean larger) {
        String populationCondition;
        if (info.getPopulation() == -1) {
            populationCondition = "1 = 1 ";
        } else {
            if (larger)
                populationCondition = info.getPopulation() + " < " + "population;";
            else
                populationCondition = info.getPopulation() + " > " + "population;";
        }
        return populationCondition;
    }

}
