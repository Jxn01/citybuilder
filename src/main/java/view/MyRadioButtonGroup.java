package view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class MyRadioButtonGroup {
    
    private ArrayList<MyRadioButton> buttons;
    
    public MyRadioButtonGroup(){
        buttons = new ArrayList<>();
    }
    
    public void add(MyRadioButton btn){
        buttons.add(btn);
    }
       
    public Boolean isHovered(Point p){
        if(p!= null) {
            for(int i=0;i<buttons.size();++i){
                if(buttons.get(i).isHovered(p)) {
                    return true;
                }
            }
            return false;
        }
        else return false;
    }
    
    public void select(Point p){
        for(int i=0;i<buttons.size();++i){
            buttons.get(i).setSelected(false);
            if(buttons.get(i).isHovered(p)) {
                buttons.get(i).setSelected(true);
            }
        }
    }
    
    public Boolean hasSelected(){
        for(int i=0;i<buttons.size();++i){
            if(buttons.get(i).getSelected()){
                return true;
            }
        }
        return false;
    }
    
    public void deleteSelected(){
        for(int i=0;i<buttons.size();++i){
            if(buttons.get(i).getSelected()) {
                buttons.remove(i);
            }
        }
        for(int i=0;i<buttons.size();++i){
            buttons.get(i).setY(80 + i*60);
        }
    }
    
    public void draw(Graphics2D gr, Point cursorPos){
        for(int i=0;i<buttons.size();++i){
            buttons.get(i).draw(gr, cursorPos);
        }
    }
    
}