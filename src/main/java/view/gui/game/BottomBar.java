package view.gui.game;

import util.Logger;
import view.components.custom.MyButton;
import view.gui.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * This class implements the bottom bar of the game gui
 */
public class BottomBar extends GameMenu {
    private final Rectangle bottomBarArea;
    private final Color bottomBarAreaColor;
    TaxMenu taxMenu;
    StatsMenu statsMenu;
    MyButton taxBtn;
    MyButton statsBtn;
    MyButton catastrophesBtn;
    BuildMenu buildMenu;
    CatastropheMenu catastropheMenu;
    TimeMenu timeMenu;

    /**
     * Constructor of the bottom bar
     * @param game is the main game object 
     */
    public BottomBar(Game game) {
        super(game);
        taxMenu = new TaxMenu(game);
        statsMenu = new StatsMenu(game);
        statsBtn = new MyButton(0, 0, 80, 40, "stats");
        taxBtn = new MyButton(80, 0, 40, 40, "tax");
        catastrophesBtn = new MyButton(120, 0, 80, 40, "catastrophes");
        bottomBarArea = new Rectangle(0, game.height() - 40, game.width(), 40);
        bottomBarAreaColor = Color.white;
        buildMenu = new BuildMenu(game);
        catastropheMenu = new CatastropheMenu(game);
        timeMenu = new TimeMenu(game);
    }
    
    /**
     * Draw the bottom bar on the screen
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr) {
        paintBottomMenuBar(gr);
        statsBtn.setY(game.height() - 40);
        statsBtn.draw(gr, game.getMousePosition());
        taxBtn.setY(game.height() - 40);
        taxBtn.draw(gr, game.getMousePosition());
        catastrophesBtn.setY(game.height() - 40);
        catastrophesBtn.draw(gr, game.getMousePosition());
        buildMenu.draw(gr);
        timeMenu.draw(gr);
        statsMenu.draw(gr);
        taxMenu.draw(gr);
        catastropheMenu.draw(gr);
    }
    
    /**
     * Draw the bottom bar's background area on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public void paintBottomMenuBar(Graphics2D gr) {
        gr.setColor(bottomBarAreaColor);
        int x = bottomBarArea.x;
        int y = game.height() - bottomBarArea.height;
        int width = game.width();
        int height = bottomBarArea.height;
        gr.fillRect(x,y,width,height);
    }

    /**
     * Upon a click event, roll the click event further into a submenu
     * @param p is the current cursor location
     */
    @Override
    public void click(Point p) {
        buildMenu.click(p);
        statsMenu.click(p);
        taxMenu.click(p);
        catastropheMenu.click(p);
        timeMenu.click(p);
        
        if(statsBtn.isHovered(p)) {
            Logger.log("Stats button clicked");

            taxMenu.setIsOpen(false);
            catastropheMenu.setIsOpen(false);
            boolean negated = !statsMenu.getIsOpen();
            statsMenu.setIsOpen(negated);

        } else if(taxBtn.isHovered(p)) {
            Logger.log("Tax button clicked");

            statsMenu.setIsOpen(false);
            catastropheMenu.setIsOpen(false);
            boolean negated = !taxMenu.getIsOpen();
            taxMenu.setIsOpen(negated);

        } else if(catastrophesBtn.isHovered(p)) {
            Logger.log("Catastrophes button clicked");

            statsMenu.setIsOpen(false);
            taxMenu.setIsOpen(false);
            boolean negated = !catastropheMenu.getIsOpen();
            catastropheMenu.setIsOpen(negated);
        }
    }
    
    /**
     * Upon key-press event, roll the key-press event further into a submenu
     * @param e is the key-press event
     */
    public void keyPressed(KeyEvent e) {
        taxMenu.keyPressed(e);
    }
    
    /**
     * Get the bottom bar's and all it's submenu areas as rectangles
     * This is important for click event exceptions
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.add(bottomBarArea);
        areas.addAll(statsMenu.getMenuAreas());
        areas.addAll(taxMenu.getMenuAreas());
        areas.addAll(catastropheMenu.getMenuAreas());
        areas.addAll(buildMenu.getMenuAreas());
        areas.addAll(timeMenu.getMenuAreas());
        return areas;
    }

    /**
     * Set the width of the bottom bar
     * @param width is the new width
     */
    public void setWidth(int width) {
        bottomBarArea.width = width;
    }

    public void setY(int y) {
        bottomBarArea.y = y;
    }
}
