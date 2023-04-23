package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * This class implements the top bar of the game gui
 */
public class TopBar extends GameMenu {
    private final Rectangle topBarArea;
    private final Color topBarColor;
    MyButton hamburgerBtn;
    HamburgerMenu hamburgerMenu;
    
    /**
     * Constructor of the top bar
     * @param game is the main game object 
     */
    public TopBar(Game game){
        super(game);
        hamburgerMenu = new HamburgerMenu(game);
        hamburgerBtn = new MyButton(0, 0, 40, 40, "hamburgerMenu");
        topBarArea = new Rectangle(0, 0, 1536, 40);
        topBarColor = Color.white;
    }
    
    /**
     * Draw the bottom top on the screen
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr){
        paintTopBarArea(gr);
  
        hamburgerMenu.draw(gr);
        hamburgerBtn.draw(gr, game.getMousePosition());
        gr.setColor(Color.black);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        gr.drawString("Költségvetés:" + game.getBalance() + "$", 40, 30);
        gr.drawString("Elégedettség: 85%", 400, 30);
        gr.drawString("Populáció :"+ game.getPopulation() +" ezer", 700, 30);
        gr.drawString("Idő: 135.nap", 1000, 30);  
    }
    
    /**
     * Draw the top bar's background area on the screen
     * @param gr is the graphics context of the main Panel object
     */
    private void paintTopBarArea(Graphics2D gr){
        gr.setColor(topBarColor);
        int x = topBarArea.x;
        int y = topBarArea.y;
        int width = game.width();
        int height = topBarArea.height;
        gr.fillRect(x, y, width, height);
    }
    
    /**
     * Upon a click event, roll the click event further into a submenu
     * @param p is the current cursor location
     */
    @Override
    public void click(Point p){
        hamburgerMenu.click(p);
        if (hamburgerBtn.isHovered(p)) {
            Boolean negated = !hamburgerMenu.getIsOpen();
            hamburgerMenu.setIsOpen(negated);
        }
    }
        
    /**
     * Get the top bar's and all it's submenu areas as rectangles
     * This is important for click event exceptions
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.add(topBarArea);
        areas.addAll(hamburgerMenu.getMenuAreas());
        return areas;
    }
    
    public void setWidth(int width){
        topBarArea.width = width;
    }
    
}
