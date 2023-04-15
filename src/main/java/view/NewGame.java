package view;

import util.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * The gui representation of the NewGame submenu
 */
public class NewGame {

    Panel panel;
    Image background;
    private MyButton backBtn;
    private MyButton startBtn;
    private MyInputField input;

    /**
     * Constructor of the NewGame class
     * @param panel is the game's main Panel object
     */
    public NewGame(Panel panel) {
        this.panel = panel;
        try {
            background = ResourceLoader.loadImage("newgamebg.png");
        } catch (IOException ex) {}
        backBtn = new MyButton(0, 0, 75, 75, "back");
        startBtn = new MyButton(618, 600, 300, 100, "start");
        input = new MyInputField(700, 220, 300, 40);
    }

    /**
     * Draw the NewGame submenu on the screen
     * @param panel is the game's main Panel object
     * @param gr is the graphics context of the main Panel object
     */
    public void draw(Panel panel, Graphics2D gr) {
        gr.drawImage(background, 0, 0, 1536 + 15, 793, null);
        backBtn.draw(gr, panel.getMousePosition());
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString("Új város neve: ", 500, 250);
        startBtn.draw(gr, panel.getMousePosition());
        input.draw(gr, panel.getMousePosition());
    }

    /**
     * The NewGame submenu's click handler
     * @param p is the location of the click
     */
    public void click(Point p) {
        if (backBtn.isHovered(p)) {
            panel.setState(MenuState.MAINMENU);
        } else if (startBtn.isHovered(p)) {
            panel.setState(MenuState.GAME);
        }
    }

    /**
     * The NewGame submenu's kespress handler
     * @param e is the KeyEvent of the keypress
     */
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == KeyEvent.VK_BACK_SPACE) {
            input.deletelLast();
        } else if (Character.isLetter(c) || Character.isDigit(c)) {
            input.add(c);
        }
    }

}
