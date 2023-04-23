package view.gui.mainmenu;

import util.Logger;
import util.ResourceLoader;
import view.components.Panel;
import view.components.custom.MyButton;
import view.enums.MenuState;

import java.awt.*;
import java.io.IOException;

/**
 * The gui representation of the main menu
 */
public class MainMenu {

    private final view.components.Panel panel;
    private Image background;
    private MyButton tutorialBtn;
    private MyButton newGameBtn;
    private MyButton saveBtn;
    private MyButton exitBtn;

    /**
     * Constructor of the MainMenu class
     * @param panel is the game's main Panel object
     */
    public MainMenu(view.components.Panel panel) {
        this.panel = panel;
        final int btnWidth = 300;
        final int btnHeight = 100;
        try {
            background = ResourceLoader.loadImage("city.png");
            tutorialBtn = new MyButton(0, 200, btnWidth, btnHeight, "tutorial");
            newGameBtn = new MyButton(0, 350, btnWidth, btnHeight, "newGame");
            saveBtn = new MyButton(0, 500, btnWidth, btnHeight, "save");
            exitBtn = new MyButton(0, 650, btnWidth, btnHeight, "exit");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Draw the main menu on the screen
     * @param panel is the game's main Panel object
     * @param gr is the graphics context of the main Panel object
     */
    public void draw(Panel panel, Graphics2D gr) {
        //draw the background
        gr.drawImage(background, 0, 0, panel.width(), panel.height(), null);

        final int btnWidth = 300;
        final int btnX = panel.width()/2 - btnWidth/2;
        
        //draw all buttons
        tutorialBtn.setX(btnX);
        tutorialBtn.draw(gr, panel.getMousePosition());
        newGameBtn.setX(btnX);
        newGameBtn.draw(gr, panel.getMousePosition());
        saveBtn.setX(btnX);
        saveBtn.draw(gr, panel.getMousePosition());
        exitBtn.setX(btnX);
        exitBtn.draw(gr, panel.getMousePosition());

    }

    /**
     * The main menu's click handler
     * @param p is the location of the click
     */
    public void click(Point p) {
        if (tutorialBtn.isHovered(p)) {
            Logger.log("Tutorial button clicked");
            panel.setState(MenuState.TUTORIAL);
        } else if (newGameBtn.isHovered(p)) {
            Logger.log("New game button clicked");
            panel.setState(MenuState.NEWGAME);
        } else if (saveBtn.isHovered(p)) {
            Logger.log("Save button clicked");
            panel.setState(MenuState.LOADGAME);
        } else if (exitBtn.isHovered(p)) {
            Logger.log("Exit button clicked");
            panel.exit();
        }
    }

}
