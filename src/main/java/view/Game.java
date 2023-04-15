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
    private Map map;
    TopBar topBar;
    BottomBar bottomBar;

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
    
    //draw the game gui
    public void draw(Panel panel, Graphics2D gr) {
        //paint the map
        map.paint(gr);
        //paint the top bar
        topBar.draw(gr);
        //paint the bottom bar
        bottomBar.draw(gr);
    }
    
    public void click(Point p) {
        topBar.click(p);
        bottomBar.click(p);
    }
    
    public int getZoom(){
        return cameraMovementHandler.getZoom();
    }
    
    public int getCameraOffsetX() {
        return cameraMovementHandler.getCameraOffsetX();
    }

    public int getCameraOffsetY() {
        return cameraMovementHandler.getCameraOffsetY();
    }
    
    public void mouseWheelRotated(MouseWheelEvent e){
        cameraMovementHandler.mouseWheelRotated(e);
    }
    
    public void keyPressed(KeyEvent e) {
        bottomBar.keyPressed(e);
    }

    public Dimension getSize(){
        return panel.getSize();
    }
    
    public void exit(){
        panel.exit();
    }
    
    public Point getMousePosition(){
        return panel.getMousePosition();
    }
    
    public int getBalance(){
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
    
    public void setSelectedBuildingType(Tile selectedBuildingType) {
        map.setSelectedBuildingType(selectedBuildingType);
    }

    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.addAll(topBar.getMenuAreas());
        areas.addAll(bottomBar.getMenuAreas());
        return areas;
    }
}