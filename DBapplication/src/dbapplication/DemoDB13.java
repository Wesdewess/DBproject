/*
 * This code is provided bij The Hague University of Applied Science
 * It is developed as instruction material and comes with no warranty
 * 
 * You are free to use parts of this source under 3 conditons:
 * 1. include the full text of this license header in your code
 * 2. include relevant parts of the package header in your code
 * 3. add your own header to your code with your name in it
 * 
 * If you paraphrase parts of this code, at least refer to it as your source
 */
package dbapplication;

import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Demonstration of JTable
 * Part 3: presenting a ResultSet in a Table
 * @author W Pijnacker Hordijk
 */
public class DemoDB13 {
    private static String username;
    private static int signaal;
    private static ArrayList<String> alleTabellen = new ArrayList<String>();

    /**
     * @param args no command line arguments
     */
    public static void main(String[] args) {       
        String command = "2";
        String propertiesFile;
        if (command.equals("1")){
            propertiesFile = "MySQL.properties";
        } else {
            propertiesFile = "MSSQL.properties";
        }
        
        try {
            Query updatenBijOpstarten = new Query();
            updatenBijOpstarten.query();
            JTable tabel = new JTable();
            
            DemoTabel demoGUI = new DemoTabel(tabel);
            demoGUI.show();
            demoGUI.optieDB();
            String gekozenDB = demoGUI.getGekozenDB();
            String connectionString = "jdbc:sqlserver://localhost:1433;database=" + gekozenDB;
            Connection conn = DriverManager.getConnection(connectionString, "testuser", "testuser");

            try {
                Statement stat = conn.createStatement();
                
                //Hiermee krijg je alle tabelnamen uit de database alleen staat er geen dbo. voor
                DatabaseMetaData dbmd = conn.getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = dbmd.getTables(null, null, "%", types);
                
        
                while (rs.next()) {
                    alleTabellen.add(rs.getString("TABLE_NAME"));
                }

                System.out.println("database gevonden...");

                demoGUI.opties(alleTabellen);
                
                String qryPrepStat;
                qryPrepStat = "SELECT * FROM [" + gekozenDB + "].[dbo].[" + demoGUI.getGekozenTabel() + "]";
                PreparedStatement prepStat = conn.prepareStatement(qryPrepStat);
                ResultSet res = prepStat.executeQuery();
                setTableSimple(res, tabel); 
                
                PreparedStatement prepStat1 = conn.prepareStatement(qryPrepStat);
                ResultSet res1 = prepStat1.executeQuery();
                setTableSimple(res1, tabel);  
                tabel.getTableHeader().setReorderingAllowed(false);
                //Nu kan je hier zelf een query uitzoeken
                qryPrepStat = demoGUI.getQuery();
                while(qryPrepStat == null){

                    int row = tabel.getSelectedRow();                    
                    if(row < 0){
                        try {
                        sleep(3000);
                        row = tabel.getSelectedRow();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(DemoDB13.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{                       
                        username = tabel.getValueAt(row, 0).toString();
                        signaal = (int) tabel.getValueAt(row, 1);
                        System.out.println(username);
                        System.out.println(signaal);
                        
                        System.out.println("Selected row: " + tabel.getSelectedRow());
                        System.out.println("Value at: " + tabel.getValueAt(row, 0));
                        qryPrepStat = updatenBijOpstarten.activiteitenQueryCode(username);
                    }
                }
<<<<<<< HEAD
                PreparedStatement prepStat1 = conn.prepareStatement(qryPrepStat);

                //System.out.print("Geef woonplaats: ");
                //String woonplaatsd = keyboard.nextLine();
                //prepStat.setString(1, woonplaats);
                ResultSet res1 = prepStat1.executeQuery();
                // uncomment one of the following two lines
                setTableSimple(res1, tabel);  
                //setTable(res, tabel);
                
=======
>>>>>>> 966a816d05c3526eeccbb4be88f39b1e5169ee07
            } finally {
                conn.close();
                System.out.println("... verbinding afgesloten.");
            }
        }
        
        catch (SQLException e) {
            System.out.println("Fout: " + e.getMessage());
        }

    }

    public static String getUsername(){
        return username;
    }
   
    public static int getSignaal(){
       return signaal;
    }
    
    /**
     * Puts the contents of a result set into a table<br>
     * simple version
     * @param rs
     * @param table the table
     * @author W. Pijnacker Hordijk
     * @throws java.sql.SQLException
     */
    public static void setTableSimple(ResultSet rs, JTable table) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        
        //namen van de kollomen
        Vector<String> columns = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columns.add(metaData.getColumnName(column));
        }

        //data van het tabel
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        //maakt de cell niet editable
        table.setModel(new DefaultTableModel(data, columns){
            @Override
            public boolean isCellEditable(int row, int column){
            return false;
            }
        });   
    }
}