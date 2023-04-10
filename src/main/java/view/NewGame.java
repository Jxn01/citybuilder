package view;

import util.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class NewGame {

    Panel panel;
    Image background;
    private MyButton backBtn;
    private MyButton startBtn;
    private MyInput input;


    public NewGame(Panel panel) {
        this.panel = panel;
        try {
            background = ResourceLoader.loadImage("newgamebg.png");
        } catch (IOException ex) {
        }
        backBtn = new MyButton(0, 0, 75, 75, "back");
        startBtn = new MyButton(618, 600, 300, 100, "start");
        input = new MyInput(700, 220, 300, 40);
    }

    public void draw(Panel panel, Graphics2D gr) {
        gr.drawImage(background, 0, 0, 1536 + 15, 793, null);
        backBtn.draw(gr, panel.getMousePosition());
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString("Új város neve: ", 500, 250);
        startBtn.draw(gr, panel.getMousePosition());
        input.draw(gr, panel.getMousePosition());

        //gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        //gr.drawString(  "Új város neve: ",500,250);
    }

    public void click(Point p) {
        if (backBtn.isHovered(p)) {
            panel.setState(MenuState.MAINMENU);
        } else if (startBtn.isHovered(p)) {
            panel.setState(MenuState.GAME);
        }
    }

    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == KeyEvent.VK_BACK_SPACE) {
            input.del();
        } else if (Character.isLetter(c) || Character.isDigit(c)) {
            input.add(c);
        }
    }

}
