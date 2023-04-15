package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class HamburgerMenu extends GameMenu{
    private final Rectangle hamburgerMenuArea;
    private final Color hamburgerMenuColor;
    MyButton saveBtn;
    MyButton newGameBtn;
    MyButton exitBtn;
    
    public HamburgerMenu(Game game){
        super(game);
        saveBtn = new MyButton(0, 40, 120, 40, "saveGame");
        newGameBtn = new MyButton(0, 90, 120, 40, "newGame");
        exitBtn = new MyButton(0, 140, 120, 40, "exit");
        hamburgerMenuArea = new Rectangle(0, 40, 120, 120);
        hamburgerMenuColor = Color.white;
    }

    @Override
    public void draw(Graphics2D gr){ 
        if(!getIsOpen()){
            return;
        }
        paintHamburgerMenuArea(gr);
        newGameBtn.draw(gr, game.getMousePosition());
        saveBtn.draw(gr, game.getMousePosition());
        exitBtn.draw(gr, game.getMousePosition());
    }
    
    private void paintHamburgerMenuArea(Graphics2D gr){
        gr.setColor(hamburgerMenuColor);
        int x = hamburgerMenuArea.x;
        int y = hamburgerMenuArea.y;
        int width = hamburgerMenuArea.width;
        int height = hamburgerMenuArea.height;
        gr.fillRect(x, y, width, height);
    }
    
    @Override
    public void click(Point p){
        if(saveBtn.isHovered(p)){
            System.out.println("save button clicked");
        }
        else if(newGameBtn.isHovered(p)){
            System.out.println("newgame button clicked");
        }
        else if(exitBtn.isHovered(p)){
            System.out.println("exit button clicked");
        }
    }
    
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        if(this.getIsOpen()){
            areas.add(hamburgerMenuArea);
        }
        return areas;
    }
}
