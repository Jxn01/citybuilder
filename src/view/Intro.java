package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import static java.lang.Thread.sleep;
import res.ResourceLoader;

public class Intro {
    
    Image background;
    int alphaLevel = 255;
    Boolean fadeInComplete; 
    Boolean waitingComplete;
    Boolean fadeOutComplete; 
  
    public Intro() {
        alphaLevel = 255;
        fadeInComplete = false;
        waitingComplete = false;
        fadeOutComplete = false;       
        try {
            background = ResourceLoader.loadImage("res/background.png");
        } catch (IOException ex) { }
    }
    
    public void draw(Panel panel, Graphics2D gr) {     
        //paint the monkey
        gr.drawImage(background,0,0,1536 + 15,793,null);
          
        //paint a black rectangle on it
        Color myColour = new Color(0, 0, 0, alphaLevel);
        gr.setColor(myColour);
        gr.fillRect(0,0,1536 + 15,793);
        
        if(!fadeInComplete && alphaLevel >= 5){
            alphaLevel -= 5;
            return;
        }
        fadeInComplete = true;
        
        //wait 1,5 seconds
        if(!waitingComplete){
            try {
            sleep(1500);
            waitingComplete = true;
            } catch (InterruptedException e){}
            return;
        }
        
        //fade out phase
        if(!fadeOutComplete && alphaLevel <= 250){
            alphaLevel += 5;
            return;
        }
        fadeOutComplete = true;
        
        panel.setState(MenuState.MAINMENU);
    }
    
    public Image getBackground() {
        return background;
    }
    
}