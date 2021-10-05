package Simulation;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingContainer;


public class MyFrame extends JFrame{
    MyPanel panel = new MyPanel();
    public MyFrame() {
    	
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("AjayTeja Reddy");
        add(panel);
        setSize(800,800);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

}
