package view.gui.mainmenu;

import java.awt.*;
import java.io.IOException;

import util.Logger;
import util.ResourceLoader;
import view.components.Panel;
import view.components.custom.MyButton;
import view.components.custom.MyRadioButton;
import view.components.custom.MyRadioButtonGroup;
import view.enums.MenuState;

/**
 * The gui representation of the LoadGame submenu
 */
public class LoadGame {

    private Image background;
    private final view.components.Panel panel;
    private final MyButton backBtn;
    private final MyButton loadBtn;
    private final MyButton deleteBtn;
    private final MyRadioButton btn1;
    private final MyRadioButton btn2;
    private final MyRadioButton btn3;
    private final MyRadioButton btn4;
    private final MyRadioButton btn5;
    private final MyRadioButtonGroup btnGrp;

    /**
     * Constructor of the LoadGame class
     * @param panel is the game's main Panel object
     */
    public LoadGame(view.components.Panel panel) {
        this.panel = panel;
        try {
            background = ResourceLoader.loadImage("savebg.png");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        backBtn = new MyButton(0, 0, 75, 75, "back");
        loadBtn = new MyButton(50, 0, 300, 100, "load");
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
        final int btnWidth = 300;
        final int btnHeight = 100;
        
        gr.drawImage(background, 0, 0, panel.width(), panel.height(), null);
        backBtn.draw(gr, panel.getMousePosition());
        loadBtn.setY(panel.height()-50 - btnHeight);
        loadBtn.draw(gr, panel.getMousePosition());
        deleteBtn.setY(panel.height()-50 - btnHeight);
        deleteBtn.setX(panel.width() - 50 - btnWidth);
        deleteBtn.draw(gr, panel.getMousePosition());
        btnGrp.setWidth(panel.width() - 100 );
        btnGrp.setX(50);   
        btnGrp.draw(gr, panel.getMousePosition());
    }

    /**
     * The NewGame submenu's click handler
     * @param p is the location of the click
     */
    public void click(Point p) {
        if (backBtn.isHovered(p)) {
            Logger.log("Back to main menu");
            panel.setState(MenuState.MAINMENU);
        } else if (btnGrp.isAnyBtnHovered(p)) {
            btnGrp.select(p);
        } else if (loadBtn.isHovered(p)) {
            if (btnGrp.hasSelected()) {
                Logger.log("Loading game");
                panel.setState(MenuState.GAME);
            }
        } else if (deleteBtn.isHovered(p)) {
            Logger.log("Deleting save");
            btnGrp.removeSelectedBtn();
        }
    }
}
