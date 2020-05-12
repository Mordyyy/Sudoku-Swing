import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class View extends JFrame{

    private JTextField metropolisField,continentField,populationField;
    private JLabel metropolisLabel, continentLabel, populationLabel;
    private JButton addButton, searchButton;
    private JComboBox populationBox, matchBox;
    private JTable table;
    private Functions fun;

    public View(Functions fun){
        super("Metropolis view");
        setLayout(new BorderLayout(5,5));

        this.fun = fun;

        addTopPanel();
        addTable();
        addEastPanel();

        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addTopPanel() {
        JPanel top = new JPanel();
        metropolisLabel = new JLabel("Metropolis: ");
        continentLabel = new JLabel("Continent: ");
        populationLabel = new JLabel("Population: ");
        metropolisField = new JTextField(10);
        continentField = new JTextField(10);
        populationField = new JTextField(10);
        top.add(metropolisLabel);
        top.add(metropolisField);
        top.add(continentLabel);
        top.add(continentField);
        top.add(populationLabel);
        top.add(populationField);
        add(top, BorderLayout.NORTH);
    }

    private void addTable() {
        JPanel pan = new JPanel();
        DefaultTableModel dt = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.setColumnCount(3);
        String[] names = new String[]{"Metropolis", "Continent", "Population"};
        dt.setColumnIdentifiers(names);
        table = new JTable(dt);
        pan.add(table);
        pan.add(new JScrollPane(table));
        add(pan, BorderLayout.CENTER);
    }

    private void addEastPanel(){
        JPanel eastPanel = new JPanel(new GridLayout(2,1));
        Box buttons = Box.createVerticalBox();
        addButton = new JButton("Add");
        addButton.addActionListener(new AddListener());
        searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchListener());
        buttons.add(addButton);
        buttons.add(searchButton);
        eastPanel.add(buttons);
        Box boxes = Box.createVerticalBox();
        boxes.setBorder(new TitledBorder("Search Options"));
        String[] population = new String[]{"Population Larger Than", "Population Smaller than"};
        String[] exact = new String[]{"Exact Match", "Partial Match"};
        populationBox = new JComboBox<>(population);
        matchBox = new JComboBox<>(exact);
        boxes.add(populationBox);
        boxes.add(matchBox);
        eastPanel.add(boxes);
        add(eastPanel,BorderLayout.EAST);
    }

    private void clearRow(){
        DefaultTableModel mod = (DefaultTableModel)table.getModel();
        while(mod.getRowCount() > 0)
            mod.removeRow(0);
    }

    private void addRow(Object[] arr){
        DefaultTableModel mod = (DefaultTableModel)table.getModel();
        mod.addRow(arr);
    }

    private void searchResult(ResultSet rs){
        while(true){
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                Object[] arr = {rs.getString(1), rs.getString(2), rs.getInt(3)};
                addRow(arr);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public class AddListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String metro = metropolisField.getText();
            String cont = continentField.getText();
            String pop = populationField.getText();
            Integer p = -1;
            if(!pop.equals(""))
                p = Integer.parseInt(pop);
            Object[] arr = {metro, cont, p};
            Information info = new Information(metro, cont, p);
            try {
                fun.add(info);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            clearRow();
            addRow(arr);
            metropolisField.setText("");
            continentField.setText("");
            populationField.setText("");
        }


    }

    public class SearchListener implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String metro = metropolisField.getText();
            String cont = continentField.getText();
            String pop = populationField.getText();
            Integer p = -1;
            if(!pop.equals(""))
                p = Integer.parseInt(pop);
            Information info = new Information(metro, cont,p);
            String large = (String)populationBox.getSelectedItem();
            boolean isLarge = large.equals("Population Larger Than");
            String exact = (String)matchBox.getSelectedItem();
            boolean isExact = exact.equals("Exact Match");
            ResultSet rs = null;
            try {
                rs = fun.search(info, isLarge, isExact);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            clearRow();
            searchResult(rs);
        }

    }
}
