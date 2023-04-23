package view;

import util.ResourceLoader;

import java.awt.*;
import java.io.IOException;

/**
 * This class represents a button on the gui
 */
public class MyButton {

    public Rectangle rect;
    public Image img, imgHover;

    /**
     * Constructor of the button class
     * @param x is the starting horizontal coordinate of the button
     * @param y is the starting vertical coordinate of the button
     * @param width is the button's width
     * @param height is the button's height
     * @param imgLocation is the button's image
     */
    public MyButton(int x, int y, int width, int height, String imgLocation) {
        rect = new Rectangle(x, y, width, height);
        try {
            img = ResourceLoader.loadImage(imgLocation + ".png");
            imgHover = ResourceLoader.loadImage(imgLocation + "Hover.png");
        } catch (IOException ex) {
        }
    }

    /**
     * Checks if the button is hovered
     * @param p is the current cursor location
     * @return a Boolean value
     */
    public Boolean isHovered(Point p) {
        if (p != null) {
            return rect.contains(p);
        } else return false;
    }

    /**
     * Draw the NewGame submenu on the screen
     * @param gr is the graphics context of the main Panel object
     * @param p is the current cursor position
     */
    public void draw(Graphics2D gr, Point p) {
        if (!isHovered(p)) {
            gr.drawImage(img, rect.x, rect.y, rect.width, rect.height, null);
        } else {
            gr.drawImage(imgHover, rect.x, rect.y, rect.width, rect.height, null);
        }

    }
    
    public void drawHovered(Graphics2D gr){
        gr.drawImage(imgHover, rect.x, rect.y, rect.width, rect.height, null);
    }
    
    public void setX(int x){
        rect.x = x;
    }
    
    public void setY(int y){
        rect.y = y;
    }
}