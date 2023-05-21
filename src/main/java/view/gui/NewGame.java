package view.gui;

import org.jetbrains.annotations.NotNull;
import util.Logger;
import util.ResourceLoader;
import view.components.Panel;
import view.components.custom.MyButton;
import view.components.custom.MyInputField;
import view.enums.MenuState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * The gui representation of the NewGame submenu
 */
public class NewGame {

    private final @NotNull MyButton backBtn;
    private final @NotNull MyButton startBtn;
    private final @NotNull MyInputField input;
    view.components.Panel panel;
    Image background;

    /**
     * Constructor of the NewGame class
     *
     * @param panel is the game's main Panel object
     */
    public NewGame(view.components.Panel panel) {
        this.panel = panel;
        try {
            background = ResourceLoader.loadImage("newgamebg.png");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        backBtn = new MyButton(0, 0, 75, 75, "back");
        startBtn = new MyButton(618, 600, 300, 100, "start");
        input = new MyInputField(700, 220, 300, 40);
    }

    /**
     * Draw the NewGame submenu on the screen
     *
     * @param panel is the game's main Panel object
     * @param gr    is the graphics context of the main Panel object
     */
    public void draw(@NotNull Panel panel, @NotNull Graphics2D gr) {
        final int btnWidth = 300;
        final int btnHeight = 100;
        final int btnX = panel.width() / 2 - btnWidth / 2;
        final int btnY = panel.height() - (btnHeight + 50);
        final int labelX = panel.width() / 2 - 250;
        final int inputX = labelX + 200;


        gr.drawImage(background, 0, 0, panel.width(), panel.height(), null);
        backBtn.draw(gr, panel.getMousePosition());
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        //500
        gr.drawString("Új város neve: ", labelX, 250);
        startBtn.setX(btnX);
        startBtn.setY(btnY);
        startBtn.draw(gr, panel.getMousePosition());
        input.setX(inputX);
        input.draw(gr, panel.getMousePosition());
    }

    /**
     * The NewGame submenu's click handler
     *
     * @param p is the location of the click
     */
    public void click(Point p) {
        if (backBtn.isHovered(p)) {
            Logger.log("Back button clicked");
            panel.setState(MenuState.MAINMENU);
        } else if (startBtn.isHovered(p)) {
            Logger.log("Start button clicked");
            Logger.log("New city name: " + input.getText());
            panel.getGameManager().initGame(input.getText());
            panel.setState(MenuState.GAME);
        }
    }

    /**
     * The NewGame submenu's key press handler
     *
     * @param e is the KeyEvent of the key press
     */
    public void keyPressed(@NotNull KeyEvent e) {
        char c = e.getKeyChar();
        if (c == KeyEvent.VK_BACK_SPACE) {
            input.deleteLast();
        } else if (Character.isLetter(c) || Character.isDigit(c)) {
            input.add(c);
        }
    }

}
