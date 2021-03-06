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
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
        //myFrame.setSize(800, 600);
        myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        myFrame.setTitle("Tabel met resultaten:");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JTextField textField = new JTextField("Username..");
        textField.setPreferredSize(new Dimension(200, 35));
        
        JButton zoekButton = new JButton("Zoek");
        zoekButton.setPreferredSize(new Dimension(200, 35));
        
        //moet hier een methode van maken zodat ik met get en set selectedItem de geselecteerde value ook echt in het display tonen want zo lukt dat niet
        String[] signalen = new String[] {"Signaal1", "Signaal2",
                                    "Signaal3", "Signaal4"};
 
        JComboBox<String> signalenLijst = new JComboBox<>(signalen);
        signalenLijst.setPreferredSize(new Dimension(200, 35));
        //Zodat er ook wat wordt gedaan wanneer een "optie" wordt geselecteerd
        signalenLijst.addActionListener(e -> {
            System.out.println((String) signalenLijst.getSelectedItem());
        });
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        
        contentPane.add(textField, BorderLayout.LINE_START);
        contentPane.add(zoekButton, BorderLayout.CENTER);
        contentPane.add(signalenLijst, BorderLayout.LINE_END);
        
        
        JPanel contentPane2 = new JPanel(new BorderLayout());
        
        JTextArea area = new JTextArea(50,50);
        area.setEditable(false);
        area.setFont(new Font("Serif", Font.PLAIN, 20));
        area.append("# 1 = RDS User naam in Profit bestaat niet in de AD\n" +
                    "# 2 = Medewerker uit dienst in Profit, account is in AD actief\n" +
                    "# 3 = AD Account, onbekend in Profit\n" +
                    "# 4 = RDS naam in Clevernew is niet ingevuld\n" +
                    "# 5 = RDS naam in Clevernew bestaat niet in AD\n" +
                    "# 6 = Medewerker uit dienst in CleverNew, account in AD actief\n" +
                    "# 7 = AD Account, onbekend in Clever\n" +
                    "# 8 = RDS User naam in Profit bestaat niet in Clever\n" +
                    "# 9 = Medewerker uit dienst in Profit, account is in Clever actief\n" +
                    "# 10 = RDS User naam in Clever bestaat niet in Afas Profit");
        contentPane2.add(area);
        myFrame.add(contentPane, BorderLayout.NORTH);
        myFrame.add(contentPane2, BorderLayout.EAST);
        
        size = 36;
        myFont = new Font("Tahoma", 0, size);
        myTable.setFont(myFont);
        myTable.setRowHeight((int) (1.5 * size));
        myTable.getTableHeader().setFont(myFont); // don't forget!
        
        myFrame.add(new JScrollPane(myTable));
        
        myTable.setAutoCreateRowSorter(true);
        
        
        //String selectedSignaal = (String) signalenLijst.getSelectedItem();
        //System.out.println(selectedSignaal);
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
    
    public void wegschrijven(){
        Query.query();
    }
}
