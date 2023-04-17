package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
    BuildMenu buildMenu;
    TimeMenu timeMenu;

    /**
     * Constructor of the bottom bar
     * @param game is the main game object 
     */
    public BottomBar(Game game){
        super(game);
        taxMenu = new TaxMenu(game);
        statsMenu = new StatsMenu(game);
        taxBtn = new MyButton(80, 753, 40, 40, "tax");
        statsBtn = new MyButton(0, 753, 80, 40, "stats");
        bottomBarArea = new Rectangle(0, 753, 1536, 40);
        bottomBarAreaColor = Color.white;
        buildMenu = new BuildMenu(game);
        timeMenu = new TimeMenu(game);
    }
    
    /**
     * Draw the bottom bar on the screen
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr){   
        paintBottomMenubar(gr);
        statsBtn.draw(gr, game.getMousePosition());
        taxBtn.draw(gr, game.getMousePosition());
        buildMenu.draw(gr);
        timeMenu.draw(gr);
        statsMenu.draw(gr);
        taxMenu.draw(gr);
    }
    
    /**
     * Draw the bottom bar's background area on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public void paintBottomMenubar(Graphics2D gr){
        gr.setColor(bottomBarAreaColor);
        int x = bottomBarArea.x;
        int y = bottomBarArea.y;
        int width = bottomBarArea.width;
        int height = bottomBarArea.height;
        gr.fillRect(x,y,width,height);
    }

    /**
     * Upon a click event, roll the click event further into a submenu
     * @param p is the current cursor loaction
     */
    @Override
    public void click(Point p){
        buildMenu.click(p);
        statsMenu.click(p);
        taxMenu.click(p);
        timeMenu.click(p);
        
        if(statsBtn.isHovered(p)){
            taxMenu.setIsOpen(false);
            Boolean negated = !statsMenu.getIsOpen();
            statsMenu.setIsOpen(negated);
        }
        else if(taxBtn.isHovered(p)){
            statsMenu.setIsOpen(false);
            Boolean negated = !taxMenu.getIsOpen();
            taxMenu.setIsOpen(negated);
        }       
    }
    
    /**
     * Upon keypress event, roll the keypress event further into a submenu
     * @param e is the keypress event
     */
    public void keyPressed(KeyEvent e){
        taxMenu.keyPressed(e);
    }
    
    /**
     * Get the bottom bar's and all it's submenu areas as rectangles
     * This is important for click event exceptions
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.add(bottomBarArea);
        areas.addAll(statsMenu.getMenuAreas());
        areas.addAll(taxMenu.getMenuAreas());
        areas.addAll(buildMenu.getMenuAreas());
        areas.addAll(timeMenu.getMenuAreas());
        return areas;
    }
}
