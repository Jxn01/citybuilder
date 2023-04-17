package view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * This class summarizes the functionalty of a game menu
 */
public abstract class GameMenu {
    private Boolean isOpen;
    Game game;

    /**
     * Constructor of a game menu
     * @param game is the main Game object
     */
    public GameMenu(Game game){
        this.game = game;
        isOpen = false;
    }
    
    /**
     * Getter for open
     * @return open
     */
    public Boolean getIsOpen() {
        return isOpen;
    }

    /**
     * Setter for open
     * @param isOpen is the new value for open
     */
    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
    
    /**
     * Draw the gamemenu on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public abstract void draw(Graphics2D gr);
    
    /**
     * The click event handler of the game menu
     * @param p is the current mouse position
     */
    public abstract void click(Point p);
    
    /**
     * Get the game menu's and all it's submenu areas as rectangles
     * This is important for click event exceptions
     * @return an arraylist of rectangles
     */
    public abstract ArrayList<Rectangle> getMenuAreas();
}

