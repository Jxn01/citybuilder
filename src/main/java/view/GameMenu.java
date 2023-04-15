package view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class GameMenu {
    private Boolean isOpen;
    Game game;

    public GameMenu(Game game){
        this.game = game;
        isOpen = false;
    }
    
    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
    
    public abstract void draw(Graphics2D gr);
    public abstract void click(Point p);
    public abstract ArrayList<Rectangle> getMenuAreas();
}

