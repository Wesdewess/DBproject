/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbapplication;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wouter
 */
public class GUI2 {
    
    public static void main(String[] args){
        JavaDBconnectionMS connectieMetDB = new JavaDBconnectionMS();
        
        JPanel pane = new JPanel();
        JPanel textPane = new JPanel();
                
        JButton button = new JButton("Button 1");
        pane.add(button);
        
        button = new JButton("Button 2");
        //button.setPreferredSize(new Dimension(200, 100));
        pane.add(button);
        
        button = new JButton("Button 3");
        pane.add(button);
        
        button = new JButton("Button 4");
        pane.add(button);
        
        button = new JButton("Button 5");
        pane.add(button);
        
    String content = "Here men from the planet Earth\n"
      + "first set foot upon the Moon,\n" + "July 1969, AD.\n"
      + "We came in peace for all mankind."
      + "test test test test test test test test test test test test test test test test test test test test test";
    
    String content2 = "Here men from the planet Earth\n"
      + "first set foot upon the Moon,\n" + "July 1969, AD.\n"
      + "We came in peace for all mankind."
      + "\ntest test test\n test\n test test\n test test\n test test test\n test test\n test test test\n test\n test test\n test test";
    
    JTextArea textArea = new JTextArea(content2, 10,10);
    JScrollPane scrollPane = new JScrollPane(textArea,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    textPane.add(scrollPane);
      
    textPane.add(new JScrollPane(new JTextArea(content, 10, 10)));
    
    JTextArea ta = new JTextArea(content, 10, 10);
    ta.setLineWrap(true);
    textPane.add(new JScrollPane(ta));

    ta = new JTextArea(content, 10, 10);
    ta.setLineWrap(true);
    ta.setWrapStyleWord(true);
    textPane.add(new JScrollPane(ta));
    
    //hier draait alles eigenlijk om
    String columNames[] = {"nummer", "username"};
    DefaultTableModel tblModel = new DefaultTableModel(columNames, 0);
    
    JTable table = new JTable();
    table = new JTable(tblModel);
    table.setModel(tblModel);
    
    for(int i =0; i<connectieMetDB.getOutput().size();i++) {
        String nummer = Integer.toString(i);
        String username = connectieMetDB.getOutput().get(i);
 
        Object[] data = {nummer,username};         
        tblModel.addRow(data);
        }
     
    JScrollPane sp = new JScrollPane(table);  
    
    JFrame frame = new JFrame("TestApplicatieDB");
    frame.setSize(600, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(pane, "North");
    frame.add(sp, "Center");
    frame.add(textPane, "South");
    frame.setVisible(true);
    frame.pack();
    }
    

}
