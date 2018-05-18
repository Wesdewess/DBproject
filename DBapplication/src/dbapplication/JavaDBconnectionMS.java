/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbapplication;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Wouter
 */
public class JavaDBconnectionMS {
    static ArrayList<String> output = new ArrayList<String>();

    public ArrayList<String> getOutput() {
        return output;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException{
        hallo();
        
    }
    
public static void hallo(){
    String connectionString = "jdbc:sqlserver://localhost:1433;database=AuditBlackBox";
        
        
        try{
            Connection conn = DriverManager.getConnection(connectionString, "testuser", "testuser");
            System.out.println("Verbinding gemaakt...");
            
            try{
                Statement stat = conn.createStatement();
                //String query = "select * from dbo.Activiteit";
                
                //String query = "select [Username_Pre2000], [EmployeeUsername] from [AuditBlackBox].[dbo].[AD-Export], [AuditBlackBox].[dbo].[AfasProfit-Export] where [Username_Pre2000] = [EmployeeUsername]";
                
                /*String query = 
                        "select [Username_Pre2000], [EmployeeUsername] "
                        + "from  [AuditBlackBox].[dbo].[AfasProfit-Export] left join  [AuditBlackBox].[dbo].[AD-Export] "
                        + "on [Username_Pre2000] = [EmployeeUsername]"
                        + "where [Username_Pre2000] is null";*/
                
                /*String query = 
                        "select [Username_Pre2000], [EmployeeUsername] "
                        + "from  [AuditBlackBox].[dbo].[AfasProfit-Export] left join  [AuditBlackBox].[dbo].[AD-Export] "
                        + "on [Username_Pre2000] = [EmployeeUsername]"
                        + "where [ContractEndDate] is not null and [Disabled] = 0";*/
                
                /*String query = 
                        "select [Username_Pre2000], [Code] "
                        + "from [AuditBlackBox].[dbo].[AD-Export] left join [AuditBlackBox].[dbo].[PersoonCodes]"
                        + "on [Username_Pre2000] = [Code]"
                        + "where [Disabled] = 1 and [Code] is not null";*/
                
                 String query = 
                        "select [Username_Pre2000], [Code] "
                        + "from [AuditBlackBox].[dbo].[PersoonCodes] left join [AuditBlackBox].[dbo].[AD-Export]"
                        + "on [Code] = [Username_Pre2000]"
                        + "where [CodesoortenID] = 981 and [IsVerwijderd] = 1  and [Username_Pre2000] is not null";
                
                ResultSet res = stat.executeQuery(query);
                while(res.next()){
                    //System.out.println(res.getRow() + ": " + res.getString("Username_Pre2000"));
                   // System.out.println("-------------------------");
                    //System.out.println(res.getString("EmployeeUsername") + " ");
                    output.add(res.getString("Username_Pre2000"));
                    
                }
                //System.out.println("--------------------");
                System.out.println("Query uitgevoerd..");
            }
            
            finally{
                conn.close();
                System.out.println("... Verbinding afgesloten");
            }
            
        }
        
        catch(SQLException e){
            System.out.println("Fout: SQL-server is niet beschikbaar");
            System.out.println(e.getMessage());
        }
}

    public JavaDBconnectionMS() {
        hallo();
    }
    

}

