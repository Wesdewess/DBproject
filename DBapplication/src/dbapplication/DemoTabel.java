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

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * DemoTabel: tabel met schaalbare lettergrootte en regelafstand
 header toegevoegd via Borderlayout
 mogelijkheid om te sorteren op kolom
 * @author W Pijnacker Hordijk
 */
public class DemoTabel {
    JTable myTable;
    JFrame myFrame;
    int size;
    Font myFont;
    
    static String gekozenTabel;
    
    public DemoTabel (JTable table){
        myTable = table;
        myFrame = new JFrame();
        myFrame.setSize(800, 600);
        myFrame.setTitle("Tabel met resultaten:");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        size = 36;
        myFont = new Font("Tahoma", 0, size);
        myTable.setFont(myFont);
        myTable.setRowHeight((int) (1.5 * size));
        myTable.getTableHeader().setFont(myFont); // don't forget!
        
        myFrame.add(new JScrollPane(myTable));
        
        myTable.setAutoCreateRowSorter(true);
        
    }
    
    public void show(){
        myFrame.setVisible(true);
    }
    
    public void opties(ArrayList<String> k){
        //final String[] pizzas = { "Cheese", "Pepperoni", "Sausage", "Veggie" };
        String[] tabelNamen = new String[k.size()];
        for(int i = 0; i < k.size(); i++){
            tabelNamen[i] = k.get(i);
        }
        
        gekozenTabel = (String) JOptionPane.showInputDialog(myFrame, 
        "Uit welke tabel zou je de gegevens willen zien?",
        "Select tabel",
        JOptionPane.QUESTION_MESSAGE, 
        null, 
        tabelNamen, 
        tabelNamen[0]);

    // favoritePizza will be null if the user clicks Cancel
    System.out.printf("De gekozen tabel is: %s.\n", gekozenTabel);
    //System.exit(0);
    }

    public static String getGekozenTabel() {
        return gekozenTabel;
    }
    
}
