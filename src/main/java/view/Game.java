package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

/**
 * The gui representation of the main gamescreen
 */
public class Game {

    Panel panel;
    private int balance;
    private int population;

    private CameraMovementHandler cameraMovementHandler;
    private final Map map;
    TopBar topBar;
    BottomBar bottomBar;

    /**
     * Constructor of the game class
     * it contains the MAIN GAME LOOP
     * @param panel is the main Panel object of the game
     */
    public Game(Panel panel) {
        this.panel = panel;
        topBar = new TopBar(this);
        bottomBar = new BottomBar(this);
        cameraMovementHandler = new CameraMovementHandler(panel);
        map = new Map(this);
        balance = 10000;
        population = 100;

        //MAIN GAME LOOP
        Timer gameplayTimer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                //check if the camera collides with the edge of the game map
                cameraMovementHandler.checkMapBorder();
                //repaint the game gui
                panel.repaint();
            }
        });
        gameplayTimer.start();
    }
    
    /**
     * Draw the gui of the gamescreen
     * @param panel is the main Panel object of the game
     * @param gr is the graphics context of the main Panel object
     */
    public void draw(Panel panel, Graphics2D gr) {
        //paint the map
        map.paint(gr);
        //paint the top bar
        topBar.draw(gr);
        //paint the bottom bar
        bottomBar.draw(gr);
    }
    
    /**
     * The click event handler of the gamescreen
     * @param p is the current cursor position
     */
    public void click(Point p) {
        topBar.click(p);
        bottomBar.click(p);
        map.click(p);
    }
    
    /**
     * Getter for camera zoom
     * @return zoom
     */
    public int getZoom(){
        return cameraMovementHandler.getZoom();
    }
    
    /**
     * Getter for the camera offset on the x axis
     * @return cameraOffsetX
     */
    public int getCameraOffsetX() {
        return cameraMovementHandler.getCameraOffsetX();
    }

    /**
     * Getter for the camera offset on the y axis
     * @return cameraOffsetY
     */
    public int getCameraOffsetY() {
        return cameraMovementHandler.getCameraOffsetY();
    }
    
    /**
     * The mousewheel event handler of the gamescreen
     * @param e is the MouseWheelEvent
     */
    public void mouseWheelRotated(MouseWheelEvent e){
        cameraMovementHandler.mouseWheelRotated(e);
    }
    
    /**
     * The keypress event handler of the gamescreen
     * @param e is the KeyEvent
     */
    public void keyPressed(KeyEvent e) {
        bottomBar.keyPressed(e);
    }

    /**
     * Getter for panel size
     * @return panel size
     */
    public Dimension getSize(){
        return panel.getSize();
    }
    
    /**
     * Exit the application
     */
    public void exit(){
        panel.exit();
    }
    
    /**
     * Getter for the current mouse position
     * @return mouse position
     */
    public Point getMousePosition(){
        return panel.getMousePosition();
    }
    
    /**
     * Getter for the player balance (in dollars)
     * @return balance
     */
    public int getBalance(){
        return balance;
    }

    /**
     * Setter for the player balance (in dollars)
     * @param balance is the new balance
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    /**
     * Getter for city population
     * @return population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Setter for city population
     * @param population is the new population
     */
    public void setPopulation(int population) {
        this.population = population;
    }
    
    /**
     * Set the selected building type
     * @param selectedBuildingType is the selected building type
     */
    public void setSelectedBuildingType(Tile selectedBuildingType) {
        map.setSelectedBuildingType(selectedBuildingType);
    }

    /**
     * Get the areas of every menu on the gamescreen as rectangles
     * this is important for click exceptions
     * @return an ArrayList of Rectangle objects
     */
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.addAll(topBar.getMenuAreas());
        areas.addAll(bottomBar.getMenuAreas());
        return areas;
    }
    
    public int width(){
        return panel.width();
    }
    
    public int height(){
        return panel.height();
    }
    
    public void setState(MenuState s){
        panel.setState(s);
    }
    
    public void constructMap(){
        
        map.constructMap();
    }
}