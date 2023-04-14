package view;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * This class creates the window of the game
 */
public class Frame extends JFrame {
    private Panel panel;

    public Frame() {
        panel = new Panel(this);
        add(panel);
        setTitle("City builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Initialize the game window after creation
     */
    public void init() {
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setResizable(false);
    }

    /**
     * Exit the program
     */
    public void exit() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
