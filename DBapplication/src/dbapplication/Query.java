/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbapplication;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Stijn
 */
public class Query {
    
    //Een ArrayList van ResultSet
    private static ArrayList<String> test = new ArrayList<String>();
    
    //in query() wordt gekozen wat er moet gebeuren
    public static void query(){
        
    
    boolean loop = true;
        while (loop == true){
            String testName = readCharacter("press R to run, press Q to quit\n");
        
            switch (testName){
                case "R": Run(); break;
                case "Q": loop = false; break;
            }
        }
    }
    
    
    //Run() voert alle query's achterelkaar uit
    private static void Run(){
        Connection conn;
        Statement stat;
        
        try{
            init("MSSQL.properties");
            conn = getConnection();
            stat = conn.createStatement();

            try{
                FileInputStream in = new FileInputStream("DB.properties");

                Properties props = new Properties();
                props.load(in);

                try{
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date date = new Date();
                    
                    for (int i = 1; i < 10; i++){
                        String q = Integer.toString(i);
                        String query = props.getProperty(q);

                        ResultSet res = stat.executeQuery(query);
                        System.out.println(res);
                        
                        ResultSetMetaData metaData = res.getMetaData();
                        String columnName = metaData.getColumnLabel(1);
                        System.out.println(columnName);
                        
                        while (res.next()){
//                            test.add(res.getString(columnName));
                            String query2 = "INSERT INTO [Singalen].[dbo].[Singaal] (Username, Singaal_ID, Start_Datum_Singaal)" +
                                    "VALUES ('" + (res.getString(columnName)) + "', '" + i + "', '" + dateFormat.format(date) + "');";
                            stat.executeQuery(query2);
                        }
                    }
//                    System.out.println("Printing Starts");
//                    for (int i =0; i<test.size();i++){
//                        System.out.println(test.get(i));
//                    }
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
    
    
    
     public static String readCharacter(String prompt){
        Scanner keyboard = new Scanner(System.in);
        System.out.print(prompt);
        String input = keyboard.next();
        if (input.length() == 0){
            return " ";
        }
        else {
            return input;
        }
    }
}
