package view;

import java.awt.*;
import java.io.IOException;

import util.ResourceLoader;

/**
 * The gui representation of the LoadGame submenu
 */
public class LoadGame {

    private Image background;
    private Panel panel;
    private MyButton backBtn;
    private MyButton loadBtn;
    private MyButton deleteBtn;
    private MyRadioButton btn1, btn2, btn3, btn4, btn5;
    private MyRadioButtonGroup btnGrp;

    /**
     * Constructor of the LoadGame class
     * @param panel is the game's main Panel object
     */
    public LoadGame(Panel panel) {
        this.panel = panel;
        try {
            background = ResourceLoader.loadImage("saveBg.png");
        } catch (IOException ex) {}
        backBtn = new MyButton(0, 0, 75, 75, "back");
        loadBtn = new MyButton(36, 675, 300, 100, "load");
        deleteBtn = new MyButton(1200, 675, 300, 100, "delete");
        btn1 = new MyRadioButton(10, 80, 1516, 50, "Mentés1", "1:54", "16.000$");
        btn2 = new MyRadioButton(10, 140, 1516, 50, "Mentés2", "3:11", "2.000$");
        btn3 = new MyRadioButton(10, 200, 1516, 50, "Mentés3", "0:10", "-5.000$");
        btn4 = new MyRadioButton(10, 260, 1516, 50, "Mentés4", "42:42", "350$");
        btn5 = new MyRadioButton(10, 320, 1516, 50, "Mentés5", "2:01", "420$");
        btnGrp = new MyRadioButtonGroup();
        btnGrp.add(btn1);
        btnGrp.add(btn2);
        btnGrp.add(btn3);
        btnGrp.add(btn4);
        btnGrp.add(btn5);
    }

    /**
     * Draw the LoadGame submenu on the screen
     * @param panel is the game's main Panel object
     * @param gr is the graphics context of the main Panel object
     */
    public void draw(Panel panel, Graphics2D gr) {
        gr.drawImage(background, 0, 0, 1536 + 15, 793, null);
        backBtn.draw(gr, panel.getMousePosition());
        loadBtn.draw(gr, panel.getMousePosition());
        deleteBtn.draw(gr, panel.getMousePosition());
        btnGrp.draw(gr, panel.getMousePosition());
    }

    /**
     * The NewGame submenu's click handler
     * @param p is the location of the click
     */
    public void click(Point p) {
        if (backBtn.isHovered(p)) {
            panel.setState(MenuState.MAINMENU);
        } else if (btnGrp.isAnyBtnHovered(p)) {
            btnGrp.select(p);
        } else if (loadBtn.isHovered(p)) {
            if (btnGrp.hasSelected()) {
                panel.setState(MenuState.GAME);
            }
        } else if (deleteBtn.isHovered(p)) {
            btnGrp.removeSelectedBtn();
        }
    }
}
