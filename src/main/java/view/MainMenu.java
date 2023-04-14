package view;

import util.ResourceLoader;

import java.awt.*;
import java.io.IOException;

public class MainMenu {

    private Panel panel;
    private Image background;
    private MyButton tutorialBtn;
    private MyButton newGameBtn;
    private MyButton saveBtn;
    private MyButton exitBtn;

    public Image getBackground() {
        return background;
    }

    public MainMenu(Panel panel) {
        this.panel = panel;
        try {
            background = ResourceLoader.loadImage("city.png");

            tutorialBtn = new MyButton(618, 200, 300, 100, "tutorial");
            newGameBtn = new MyButton(618, 350, 300, 100, "newGame");
            saveBtn = new MyButton(618, 500, 300, 100, "save");
            exitBtn = new MyButton(618, 650, 300, 100, "exit");
        } catch (IOException ex) {
        }
    }

    public void draw(Panel panel, Graphics2D gr) {
        //draw the background
        gr.drawImage(background, 0, 0, 1536 + 15, 793, null);

        //draw all buttons
        tutorialBtn.draw(gr, panel.getMousePosition());
        newGameBtn.draw(gr, panel.getMousePosition());
        saveBtn.draw(gr, panel.getMousePosition());
        exitBtn.draw(gr, panel.getMousePosition());

    }

    public void click(Point p) {
        if (tutorialBtn.isHovered(p)) {
            panel.setState(MenuState.TUTORIAL);
        } else if (newGameBtn.isHovered(p)) {
            panel.setState(MenuState.NEWGAME);
        } else if (saveBtn.isHovered(p)) {
            panel.setState(MenuState.LOADGAME);
        } else if (exitBtn.isHovered(p)) {
            panel.exit();
        }
    }

}
