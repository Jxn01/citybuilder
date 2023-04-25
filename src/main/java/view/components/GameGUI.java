package view.components;

import controller.GameManager;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * This class creates the window of the game
 */
public class GameGUI extends JFrame {
    private final Panel panel;

    public GameGUI() {
        //maximize the JFrame
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //add the panel
        panel = new Panel(this);
        add(panel);
    
        setTitle("City Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        
        // Wait for the frame to become visible
        try { Thread.sleep(100); }
        catch(InterruptedException exc) { exc.printStackTrace(); }

        // Validate the container to ensure the panel's size is calculated
        getContentPane().validate();
        setResizable(false);
    }

    /**
     * Initialize the game window after creation
     */
    public void init() { }

    /**
     * Exit the program
     */
    public void exit() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
