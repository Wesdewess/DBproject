/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbapplication;

import com.sun.rowset.CachedRowSetImpl;
import static dbapplication.SimpleDataSource.getConnection;
import static dbapplication.SimpleDataSource.init;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.sql.rowset.CachedRowSet;

/**
 * @author Wessel Bakker 17094801
 * @author Wouter Wesselink 17133041
 * @author Stijn Appúnn 17080932
 * @author Remon Turk 17071682
 */
public class Query {
    
    public static void query(){
        Connection conn;
        Statement stat;
        
        try{
            //De connectie met de database word gemaakt
            init("MSSQL.properties");
            conn = getConnection();

            try{
                //De text file met de queries om de signalen op te halen word geladen
                FileInputStream in = new FileInputStream("DB.properties");
                Properties props = new Properties();
                props.load(in);

                try{
                    //De datum word opgehaald en geformateert
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date date = new Date();
                    int count = 0;
                    String values = "";
                    
                    for (int i = 1; i <= 10; i++){
                        stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        
                        //Deze query haalt alle signalen uit de database
                        ResultSet current = stat.executeQuery("SELECT * FROM [Signalen].[dbo].[Signaal] WHERE Signaal_ID = " + i + ";");
                        
                        ResultSetMetaData metaDataCurrent = current.getMetaData();
                        String columnNameCurrent = metaDataCurrent.getColumnLabel(1);
                        
                        stat = conn.createStatement();
                        ResultSet res = stat.executeQuery(props.getProperty(Integer.toString(i)));
                        
                        ResultSetMetaData metaDataNew = res.getMetaData();
                        String columnNameNew = metaDataNew.getColumnLabel(1);
                        
                        CachedRowSet rowset = new CachedRowSetImpl();
                        rowset.populate(res);
                        
                        while (current.next()) {
                            boolean present = false;
                            while (rowset.next()) {
                                //De rowset word vergeleken met current. Alles wat zowel in current als rowset voor komt word het uit rowset verwijderd
                                if (current.getString(columnNameCurrent).equals(rowset.getString(columnNameNew))) {
                                    present = true;
                                    rowset.deleteRow();
                                    break;
                                }
                            }
                            if (!present) {
                                //De enddate word geupdate
                                date = new Date();
                                current.updateString(metaDataCurrent.getColumnLabel(4), dateFormat.format(date));
                                current.updateRow();
                            }                           
                            rowset.beforeFirst();
                        }                        
                        //Deze while loop zet data klaar in rowset
                        while (rowset.next()) {
                            count++;
                            date = new Date();
                            values += "('" + (rowset.getString(columnNameNew)) + "', '" + i + "', '" + dateFormat.format(date) + "'), ";
                            //Dit if statement zet alles uit de rowset in een query en voert deze uit, de query zet de signalen in de signaal database
                            if (count == 1000) {
                                count = 0;
                                
                                stat = conn.createStatement();
                                String query2 = "INSERT INTO [Signalen].[dbo].[Signaal] (Username, Signaal_ID, Start_Datum_Signaal)" +
                                    " VALUES " + values.substring(0, values.length() - 2) + ";";
                                stat.executeUpdate(query2);
                                values = "";
                            }
                        }
                        //Dit if statement zet de rest van de rowset in een query, de query zet de signalen in de signaal database
                        if (i == 10 && count > 0) {
                            stat = conn.createStatement();
                            String query2 = "INSERT INTO [Signalen].[dbo].[Signaal] (Username, Signaal_ID, Start_Datum_Signaal)" +
                                " VALUES " + values.substring(0, values.length() - 2) + ";";
                            stat.executeUpdate(query2);
                        }                        
                    }
                }
                finally {
                    conn.close();
                    System.out.println("verbinding verbroken");
                }
            }
            catch (IOException e){
                    System.out.println("Fout: Document can not be loaded or has wrong information");
            }
        }
        catch (SQLException e) {
            System.out.println("Fout: " + e.getMessage());
        }
        
        catch (IOException e) {
            System.out.println("Fout: kan de properties file niet openen.");
        }
        
        catch (ClassNotFoundException e) {
            System.out.println("Fout: JDBC-driver niet gevonden.");
        }
    }
}
