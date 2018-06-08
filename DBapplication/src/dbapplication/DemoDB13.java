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

import java.io.IOException;
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
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
        /*Scanner keyboard = new Scanner(System.in);
        String command = "";
        while (!command.equals("1") && !command.equals("2")){
            System.out.print("Kies SQL-server 1(MySQL) of 2(MS SQL): ");
            command = keyboard.nextLine();
        }*/
        
        String command = "2";
        String propertiesFile;
        if (command.equals("1")){
            propertiesFile = "MySQL.properties";
        } else {
            propertiesFile = "MSSQL.properties";
        }
        int ppp = 0;
        
        try {
            // prompt the user to enter their name
            String name = JOptionPane.showInputDialog("What's your name?");

            // get the user's input. note that if they press Cancel, 'name' will be null
            System.out.printf("The user's name is '%s'.\n", name);
            String inlogName = name;
            // prompt the user to enter their name
            String password = JOptionPane.showInputDialog("What's your password?");

            // get the user's input. note that if they press Cancel, 'name' will be null
            System.out.printf("The user's password is '%s'.\n", password);
            String inlogPassword = password;
            
            Query updatenBijOpstarten = new Query();
            updatenBijOpstarten.query();
            JTable tabel = new JTable();
            DemoTabel demoGUI = new DemoTabel(tabel);
            demoGUI.show();
            demoGUI.optieDB();
            
            
            String gekozenDB = demoGUI.getGekozenDB();
            String connectionString = "jdbc:sqlserver://localhost:1433;database=" + gekozenDB;
            Connection conn = DriverManager.getConnection(connectionString, "" + inlogName + "", "" + inlogPassword + "");
            JOptionPane.showInputDialog("Your connection string: " + conn);
            
            /*SimpleDataSource.init(propertiesFile);
            Connection conn = SimpleDataSource.getConnection();
            System.out.println("verbinding gemaakt middels " + propertiesFile + "...");*/
            tabel.getTableHeader().setReorderingAllowed(false);
            while(ppp < 10){
            try {
                Statement stat = conn.createStatement();
                
                //Hiermee krijg je alle tabelnamen uit de database alleen staat er geen dbo. voor
                DatabaseMetaData dbmd = conn.getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = dbmd.getTables(null, null, "%", types);
                
        
                while (rs.next()) {
                    //System.out.println(rs.getString("TABLE_NAME"));
                    alleTabellen.add(rs.getString("TABLE_NAME"));
                }
                
                //String qryUse = "USE " + kk + ";";

                //stat.execute(qryUse);
                System.out.println("database gevonden...");

                
                //JTable tabel = new JTable();
                //DemoTabel demoGUI = new DemoTabel(tabel);
                //demoGUI.show();
                //demoGUI.optieDB();
                demoGUI.opties(alleTabellen);
                
                String qryPrepStat;
                qryPrepStat = "SELECT * FROM [" + gekozenDB + "].[dbo].[" + demoGUI.getGekozenTabel() + "]";
                PreparedStatement prepStat = conn.prepareStatement(qryPrepStat);
                ResultSet res = prepStat.executeQuery();
                setTableSimple(res, tabel); 
                
                //Nu kan je hier zelf een query uitzoeken
                qryPrepStat = demoGUI.getQuery();
                //qryPrepStat = null;
                //qryPrepStat = null;
                while(qryPrepStat == null){
                   // qryPrepStat = "";
                    //if(qryPrepStat == null){
                    //qryPrepStat = demoGUI.getQuery();
                    //}
//                    System.out.println(demoGUI.getQuery());
                    int row = tabel.getSelectedRow();
                    int column = tabel.getSelectedColumn();
                    
                    if(row == -1){
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
                        qryPrepStat = updatenBijOpstarten.organisatieQueryCode(username);
                    }
                    
                }
                PreparedStatement prepStat1 = conn.prepareStatement(qryPrepStat);

                //System.out.print("Geef woonplaats: ");
                //String woonplaats = keyboard.nextLine();
                //prepStat.setString(1, woonplaats);
                ResultSet res1 = prepStat1.executeQuery();
                // uncomment one of the following two lines
                //demoGUI.setSignalenTabel(tabel);
                //System.out.println("Tabel in db13: " + tabel);
                //demoGUI.show();
                //demoGUI.show2(tabel);
               
                //demoGUI.backButton(tabel);
               
                setTableSimple(res1, tabel);  
                //setTable(res, tabel);
                
            } finally {
                //conn.close();
                ppp++;
                System.out.println("... verbinding afgesloten.");
            }}
        } 
        
        catch (SQLException e) {
            System.out.println("Fout: " + e.getMessage());
        }
        
        /*catch (IOException e) {
            System.out.println("Fout: kan " + propertiesFile + " niet openen.");
        }
        
        catch (ClassNotFoundException e) {
            System.out.println("Fout: JDBC-driver niet gevonden.");
        }*/
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
     * @param result the result set
     * @param table the table
     * @author W. Pijnacker Hordijk
     */
    public static void setTableSimple(ResultSet rs, JTable table) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        
        // names of columns
        Vector<String> columns = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columns.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        //table.setModel(new DefaultTableModel(data, columns));
        //maakt de cell niet editable
        //maakt de cell niet editable
        table.setModel(new DefaultTableModel(data, columns){
            @Override
            public boolean isCellEditable(int row, int column){
            return false;
            }
        });   
    }
  
    /**
     * Puts the contents of a result set in to a table<br>
     * extended version:<br>
     * 1. table cells can't be edited<br>
     * 2. columns are formatted depending on data type<br>
     * 3. column widths are adjusted to length of header and field size
     * @param result the result set
     * @author W. Pijnacker Hordijk
     */
    public static void setTable(ResultSet rs, JTable table) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columns = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columns.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        // create anonymous DefaultTableModel class to override two methods
        // official approach should be "class MyTableModel extends AbstractTableModel {..."
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            // override isCellEditable with code snippet from 
            // http://www.codejava.net/java-se/swing/a-simple-jtable-example-for-display
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
            // override getColumnClass with code snippet from
            // https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableDemoProject/src/components/TableDemo.java 
            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                return getValueAt(0, columnIndex).getClass();
            }
        };

        table.setModel(model);

        // adjust preferred width of the columns
        TableColumn column = null;
        for (int i = 0; i < columnCount; i++) {
            column = table.getColumnModel().getColumn(i);
            if (metaData.getColumnDisplaySize(i + 1) > metaData.getColumnLabel(i + 1).length()) {
                column.setPreferredWidth(10*metaData.getColumnDisplaySize(i + 1));
            } else {
                column.setPreferredWidth(10*metaData.getColumnLabel(i + 1).length());
            }
        }
    }

}
