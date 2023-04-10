package view;

import javax.swing.*;
import java.awt.event.WindowEvent;

//The window of the game
public class Frame extends JFrame {
    private Panel panel;

    public Frame() {
        panel = new Panel(this);
        add(panel);
        setTitle("Animation demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void init() {
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setResizable(true);
    }

    public void exit() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
