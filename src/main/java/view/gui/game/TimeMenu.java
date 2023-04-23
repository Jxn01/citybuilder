package view.gui.game;

import util.Logger;
import view.Game;
import view.components.custom.MyButton;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * This class implements the time menu of the game gui
 */
public class TimeMenu extends GameMenu {
    
    public enum Speed {
        PAUSE,
        ONEX,
        TWOX,
        THREEX
    }
    
    MyButton pauseBtn;
    MyButton speed1Btn;
    MyButton speed2Btn;
    MyButton speed3Btn;
    Speed gameSpeed;

    
    /**
     * Constructor of the time menu
     * @param game is the main Game object 
     */
    public TimeMenu(Game game){
        super(game);
        speed3Btn = new MyButton(1495, 753, 40, 40, "3xspeed");
        speed2Btn = new MyButton(1455, 753, 40, 40, "2xspeed");
        speed1Btn = new MyButton(1415, 753, 40, 40, "1xspeed");
        pauseBtn = new MyButton(1375, 753, 40, 40, "pause");
        gameSpeed = Speed.ONEX;
    }
    
    /**
     * Draw the time menu on the screen
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr){
        speed3Btn.setY(game.height() - 40);
        speed3Btn.setX(game.width() - 40);
        if(gameSpeed == Speed.THREEX){
            speed3Btn.drawHovered(gr);
        }
        else {
            speed3Btn.draw(gr, game.getMousePosition());
        }
        
        speed2Btn.setX(game.width() - 80);
        speed2Btn.setY(game.height() - 40);
        if(gameSpeed == Speed.TWOX){
            speed2Btn.drawHovered(gr);
        }
        else {
            speed2Btn.draw(gr, game.getMousePosition());
        }
        
        speed1Btn.setX(game.width() - 120);
        speed1Btn.setY(game.height() - 40);
        if(gameSpeed == Speed.ONEX){
            speed1Btn.drawHovered(gr);
        }
        else {
            speed1Btn.draw(gr, game.getMousePosition());
        }
        
        pauseBtn.setX(game.width() - 160);
        pauseBtn.setY(game.height() - 40);
        if(gameSpeed == Speed.PAUSE){
            pauseBtn.drawHovered(gr);
        }
        else {
            pauseBtn.draw(gr, game.getMousePosition());
        }
    }
    
    /**
     * Click event handler of the time menu
     * @param p is the current mouse position
     */
    @Override
    public void click(Point p){
        if(pauseBtn.isHovered(p)){
            Logger.log("Pause button clicked");
            gameSpeed = Speed.PAUSE;
        }
        else if(speed1Btn.isHovered(p)){
            Logger.log("1x speed button clicked");
            gameSpeed = Speed.ONEX;
        }
        else if(speed2Btn.isHovered(p)){
            Logger.log("2x speed button clicked");
            gameSpeed = Speed.TWOX;
        }
        else if(speed3Btn.isHovered(p)){
            Logger.log("3x speed button clicked");
            gameSpeed = Speed.THREEX;
        }
    }
    
    /**
     * Get the time menu's area as a rectangle
     * This is important for click event exceptions
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        return areas;
    }
}
