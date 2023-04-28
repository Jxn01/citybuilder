package view.gui.mainmenu;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
    private final Panel panel;
    private final MyButton backBtn;
    private final MyButton loadBtn;
    private final MyButton deleteBtn;
    private final MyRadioButtonGroup btnGrp;
    private final List<File> saveFiles;

    /**
     * Constructor of the LoadGame class
     * @param panel is the game's main Panel object
     */
    public LoadGame(Panel panel) {
        this.panel = panel;
        this.saveFiles = panel.getGameManager().readSaveFiles();

        try { background = ResourceLoader.loadImage("savebg.png"); }
        catch(IOException exc) { exc.printStackTrace(); }

        backBtn = new MyButton(0, 0, 75, 75, "back");
        loadBtn = new MyButton(50, 0, 300, 100, "load");
        deleteBtn = new MyButton(1200, 675, 300, 100, "delete");
        btnGrp = new MyRadioButtonGroup();
        if(saveFiles != null && !saveFiles.isEmpty()) {
            int i = 1;
            for(File sf : saveFiles) {
                btnGrp.add(new MyRadioButton(10, 80 + i++ * 60, 1516, 50, sf.getName(), "", ""));
            }
        }
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
        if(backBtn.isHovered(p)) {
            Logger.log("Back to main menu");
            panel.setState(MenuState.MAINMENU);
        } else if(btnGrp.isAnyBtnHovered(p)) {
            btnGrp.select(p);
        } else if(loadBtn.isHovered(p) && btnGrp.hasSelected()) {
            Logger.log("Loading game");
            int selected = btnGrp.getSelectedIndex();
            panel.getGameManager().loadSaveFile(saveFiles.get(selected));
            panel.setState(MenuState.GAME);
        } else if(deleteBtn.isHovered(p)) {
            Logger.log("Deleting save");
            if(btnGrp.hasSelected()) {
                panel.getGameManager().deleteSaveFile(saveFiles.get(btnGrp.getSelectedIndex()));
            }
            btnGrp.removeSelectedBtn();
        }
    }
}
