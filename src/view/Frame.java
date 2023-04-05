package view;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;

//The window of the game
public class Frame extends JFrame { 
    private Panel panel;

    public Frame(){  
        panel = new Panel(this);
        add(panel);      
        setTitle("Animation demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true); 
    }
    
    public void init(){
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setResizable(false); 
    }
    
    public static void main(String[] args) {
        Frame testing = new Frame();
        testing.init();
    }
    
    public void exit(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
}
