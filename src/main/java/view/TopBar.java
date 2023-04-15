package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TopBar extends GameMenu {
    private final Rectangle topBarArea;
    private final Color topBarColor;
    MyButton hamburgerBtn;
    HamburgerMenu hamburgerMenu;
    
    public TopBar(Game game){
        super(game);
        hamburgerMenu = new HamburgerMenu(game);
        hamburgerBtn = new MyButton(0, 0, 40, 40, "hamburgerMenu");
        topBarArea = new Rectangle(0, 0, 1536, 40);
        topBarColor = Color.white;
    }
    
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
    
    private void paintTopBarArea(Graphics2D gr){
        gr.setColor(topBarColor);
        int x = topBarArea.x;
        int y = topBarArea.y;
        int width = topBarArea.width;
        int height = topBarArea.height;
        gr.fillRect(x, y, width, height);
    }
    
    @Override
    public void click(Point p){
        hamburgerMenu.click(p);
        if (hamburgerBtn.isHovered(p)) {
            Boolean negated = !hamburgerMenu.getIsOpen();
            hamburgerMenu.setIsOpen(negated);
        }
    }
        
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.add(topBarArea);
        areas.addAll(hamburgerMenu.getMenuAreas());
        return areas;
    }
    
}
