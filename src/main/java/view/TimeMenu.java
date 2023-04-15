package view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TimeMenu extends GameMenu{
    MyButton pauseBtn;
    MyButton speed1Btn;
    MyButton speed2Btn;
    MyButton speed3Btn;
    
    public TimeMenu(Game game){
        super(game);
        speed3Btn = new MyButton(1495, 753, 40, 40, "3xspeed");
        speed2Btn = new MyButton(1455, 753, 40, 40, "2xspeed");
        speed1Btn = new MyButton(1415, 753, 40, 40, "1xspeed");
        pauseBtn = new MyButton(1375, 753, 40, 40, "pause");
    }
    
    @Override
    public void draw(Graphics2D gr){
        speed3Btn.draw(gr, game.getMousePosition());
        speed2Btn.draw(gr, game.getMousePosition());
        speed1Btn.draw(gr, game.getMousePosition());
        pauseBtn.draw(gr, game.getMousePosition());
    }
    
    @Override
    public void click(Point p){
        if(pauseBtn.isHovered(p)){
            System.out.println("pause button clicked");
        }
        else if(speed1Btn.isHovered(p)){
            System.out.println("1x speed button clicked");
        }
        else if(speed2Btn.isHovered(p)){
            System.out.println("2x speed button clicked");
        }
        else if(speed3Btn.isHovered(p)){
            System.out.println("3x speed button clicked");
        }
    }
    
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        return areas;
    }
}
