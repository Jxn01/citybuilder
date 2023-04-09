package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class MyRadioButton {
    private Rectangle rect;
    private Boolean selected;

    public Boolean getSelected() {
        return selected;
    }
    private Boolean disabled;

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    public void setY(int y){
        rect.y = y;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    public String name;
    public String time;
    public String funds;
    
    public MyRadioButton(int x, int y, int width, int height, String n,String t,String f){
        selected = false;
        disabled = false;
        rect = new Rectangle(x,y,width,height);
        name = n;
        time = t;
        funds = f;
    }
    
    public Boolean isHovered(Point p){
        if(p!= null) {
            return rect.contains(p);
        }
        else return false;
    }
    
    public void draw(Graphics2D gr, Point cursorPos){
        if(selected){
            gr.setColor(Color.green);
        }
        else if(isHovered(cursorPos)){
            gr.setColor(Color.pink);
        }
        else {
            gr.setColor(Color.white);
        }
        gr.fillRect(rect.x, rect.y, rect.width, rect.height);
        
        gr.setColor(Color.DARK_GRAY);
        gr.drawRect(rect.x, rect.y, rect.width, rect.height);
        
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString(name + " ; " + time + " ; " + funds ,rect.x + 20,rect.y + (rect.height/2) + 10 );
    }
    
}
