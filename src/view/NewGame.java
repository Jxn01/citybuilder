package view;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import res.ResourceLoader;

public class NewGame {
    
    Panel panel;
    Image background;
    private MyButton backBtn;
    private MyButton startBtn;
    private MyInput input;
    
    public NewGame(Panel panel){
        this.panel = panel;
         try {
            background = ResourceLoader.loadImage("res/newgamebg.png");
        } catch (IOException ex) { }
         backBtn = new MyButton(0,0,75,75,"back");
         startBtn = new MyButton(618,600,300,100,"start");
         input = new MyInput(700,225,300,30);
    }
    
    public void draw(Panel panel, Graphics2D gr){
        gr.drawImage(background,0,0,1536 + 15,793,null);
        backBtn.draw(gr,panel.getMousePosition());
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString(  "Új város neve: ",500,250);
        startBtn.draw(gr, panel.getMousePosition());
        input.draw(gr, panel.getMousePosition());
    }
    
    public void click(Point p){
        if(backBtn.isHovered(p)){
            panel.setState(MenuState.MAINMENU);
        }
        else if(startBtn.isHovered(p)){
            System.out.println("new game");
        }
    }
    
}
