package view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import res.ResourceLoader;

public class SavedGame {
    
    Image background;
    Panel panel;
    private MyButton backBtn;
    MyButton loadBtn; 
    MyButton deleteBtn;
    MyList list;
    
    public SavedGame(Panel panel){
        this.panel = panel;
         try {
            background = ResourceLoader.loadImage("res/saveBg.png");
        } catch (IOException ex) { }
        backBtn = new MyButton(0,0,75,75,"back");
        loadBtn = new MyButton(36,675,300,100,"load");
        deleteBtn = new MyButton(1200,675,300,100,"delete");
        list = new MyList(10,80,1516,550);
    }
    
    public void draw(Panel panel, Graphics2D gr){
        gr.drawImage(background,0,0,1536 + 15,793,null);
        backBtn.draw(gr,panel.getMousePosition());
        loadBtn.draw(gr,panel.getMousePosition());
        deleteBtn.draw(gr,panel.getMousePosition());
        list.draw(gr,panel.getMousePosition());
    }
    
    public void click(Point p){
        if(backBtn.isHovered(p)){
            panel.setState(MenuState.MAINMENU);
        }
    }
}
