package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class MyInput {
    public Rectangle rect;
    private String text;
    private int MAXLENGTH;
    
    public MyInput(int x, int y, int width, int height){
        MAXLENGTH = 12;
        rect = new Rectangle(x,y,width,height);
        text = "";
    }
    
    public Boolean isHovered(Point p){
        if(p!= null) {
            return rect.contains(p);
        }
        else return false;
    }
    
    public void add(char c){
        if(text.length() < MAXLENGTH) {
            text += c;
        }
    }
    
    public void del(){
        if(text.length() > 0){
            text = text.substring(0, text.length() - 1);
        }
    }

    public String getText() {
        return text;
    }
    
    public void draw(Graphics2D gr, Point cursorPos){
        gr.setColor(Color.white);
        gr.fillRect(rect.x, rect.y, rect.width, rect.height);
        gr.setColor(Color.black);
        gr.drawRect(rect.x, rect.y, rect.width, rect.height);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        
        //make the input line "blink"
        long timestamp = System.currentTimeMillis() / 1000;
        if(timestamp % 2 == 0){
            gr.drawString(text + "|",rect.x,rect.y + rect.height - 10);
        }
        else {
            gr.drawString(text,rect.x,rect.y + rect.height - 10);
        }
    }
    
}
