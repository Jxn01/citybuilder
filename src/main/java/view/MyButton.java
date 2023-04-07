package view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import util.ResourceLoader;

public class MyButton {
        
        public Rectangle rect;
        public Image img,imgHover;
        
        public MyButton(int x, int y, int width, int height,String imgLocation){
            rect = new Rectangle(x,y,width,height);
            try {
                img = ResourceLoader.loadImage(imgLocation + ".png");
                imgHover = ResourceLoader.loadImage(imgLocation + "Hover.png");
            } catch (IOException ex) {}
        }
        
        public Boolean isHovered(Point p){
            if(p!= null) {
                return rect.contains(p);
            }
            else return false;
        }
        
        public void draw(Graphics2D gr, Point cursorPos){
            if(!isHovered(cursorPos)){
                gr.drawImage(img,rect.x,rect.y,rect.width,rect.height,null);
            }
            else {
                gr.drawImage(imgHover,rect.x,rect.y,rect.width,rect.height,null);
            }

        }
}