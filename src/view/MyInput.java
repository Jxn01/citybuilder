package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class MyInput {
    public Rectangle rect;
    
    public MyInput(int x, int y, int width, int height){
        rect = new Rectangle(x,y,width,height);
    }
    
    public Boolean isHovered(Point p){
        if(p!= null) {
            return rect.contains(p);
        }
        else return false;
    }
    
    public void draw(Graphics2D gr, Point cursorPos){
            gr.setColor(Color.white);
            gr.fillRect(rect.x, rect.y, rect.width, rect.height);
            gr.setColor(Color.black);
            gr.drawRect(rect.x, rect.y, rect.width, rect.height);
            /*
            if(!isHovered(cursorPos)){
                
            }
            else {
                
            }
            */
    }
    
}
