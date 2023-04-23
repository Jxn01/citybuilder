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
    MyButton catastrophiesBtn;
    BuildMenu buildMenu;
    CatastrophiesMenu catastrophiesMenu;
    TimeMenu timeMenu;
    

    /**
     * Constructor of the bottom bar
     * @param game is the main game object 
     */
    public BottomBar(Game game){
        super(game);
        taxMenu = new TaxMenu(game);
        statsMenu = new StatsMenu(game);
        statsBtn = new MyButton(0, 0, 80, 40, "stats");
        taxBtn = new MyButton(80, 0, 40, 40, "tax");
        catastrophiesBtn = new MyButton(120, 0, 80, 40, "catastrophies");
        bottomBarArea = new Rectangle(0, 753, 1536, 40);
        bottomBarAreaColor = Color.white;
        buildMenu = new BuildMenu(game);
        catastrophiesMenu = new CatastrophiesMenu(game);
        timeMenu = new TimeMenu(game);
    }
    
    /**
     * Draw the bottom bar on the screen
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr){   
        paintBottomMenubar(gr);
        statsBtn.setY(game.height() - 40);
        statsBtn.draw(gr, game.getMousePosition());
        taxBtn.setY(game.height() - 40);
        taxBtn.draw(gr, game.getMousePosition());
        catastrophiesBtn.setY(game.height() - 40);
        catastrophiesBtn.draw(gr, game.getMousePosition());
        buildMenu.draw(gr);
        timeMenu.draw(gr);
        statsMenu.draw(gr);
        taxMenu.draw(gr);
        catastrophiesMenu.draw(gr);
    }
    
    /**
     * Draw the bottom bar's background area on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public void paintBottomMenubar(Graphics2D gr){
        gr.setColor(bottomBarAreaColor);
        int x = bottomBarArea.x;
        int y = game.height() - bottomBarArea.height;
        int width = game.width();
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
        catastrophiesMenu.click(p);
        timeMenu.click(p);
        
        if(statsBtn.isHovered(p)){
            taxMenu.setIsOpen(false);
            catastrophiesMenu.setIsOpen(false);
            Boolean negated = !statsMenu.getIsOpen();
            statsMenu.setIsOpen(negated);
        }
        else if(taxBtn.isHovered(p)){
            statsMenu.setIsOpen(false);
            catastrophiesMenu.setIsOpen(false);
            Boolean negated = !taxMenu.getIsOpen();
            taxMenu.setIsOpen(negated);
        }
        else if(catastrophiesBtn.isHovered(p)){
            statsMenu.setIsOpen(false);
            taxMenu.setIsOpen(false);
            Boolean negated = !catastrophiesMenu.getIsOpen();
            catastrophiesMenu.setIsOpen(negated);
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
        areas.addAll(catastrophiesMenu.getMenuAreas());
        areas.addAll(buildMenu.getMenuAreas());
        areas.addAll(timeMenu.getMenuAreas());
        return areas;
    }
    
    public void setWidth(int width){
        bottomBarArea.width = width;
    }
    
    public void setY(int y){
        bottomBarArea.y = y;
    }
}
