package view.gui;

import org.jetbrains.annotations.NotNull;
import view.components.Panel;
import view.enums.MenuState;
import view.enums.Tile;
import view.gui.game.BottomBar;
import view.gui.game.TopBar;
import view.gui.game.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

/**
 * The gui representation of the main game screen
 */
public class Game {

    private final Panel panel;
    private final @NotNull CameraMovementHandler cameraMovementHandler;
    private final @NotNull Map map;
    private final @NotNull TopBar topBar;
    private final @NotNull BottomBar bottomBar;
    private final @NotNull EventLog eventLog;

    /**
     * Constructor of the game class
     * it contains the MAIN GAME LOOP
     *
     * @param panel is the main Panel object of the game
     */
    public Game(@NotNull Panel panel) {
        this.panel = panel;
        topBar = new TopBar(this);
        bottomBar = new BottomBar(this);
        eventLog = new EventLog(this);
        cameraMovementHandler = new CameraMovementHandler(panel);
        map = new Map(this);

        //MAIN GAME LOOP
        Timer gameplayTimer = new Timer(40, actionEvent -> {
            //check if the camera collides with the edge of the game map
            cameraMovementHandler.checkMapBorder();
            //repaint the game gui
            panel.repaint();
        });
        gameplayTimer.start();
    }

    /**
     * Draw the gui of the game screen
     *
     * @param panel is the main Panel object of the game
     * @param gr    is the graphics context of the main Panel object
     */
    public void draw(Panel panel, @NotNull Graphics2D gr) {
        //paint the map
        map.paint(gr);
        //paint the top bar
        topBar.draw(gr);
        //paint the bottom bar
        bottomBar.draw(gr);
        //paint the event log area
        eventLog.draw(gr);
    }

    /**
     * The click event handler of the game screen
     *
     * @param p is the current cursor position
     */
    public void click(@NotNull Point p) {
        topBar.click(p);
        bottomBar.click(p);
        map.click(p);
    }

    /**
     * Getter for camera zoom
     *
     * @return zoom
     */
    public int getZoom() {
        return cameraMovementHandler.getZoom();
    }

    /**
     * Getter for the camera offset on the x-axis
     *
     * @return cameraOffsetX
     */
    public int getCameraOffsetX() {
        return cameraMovementHandler.getCameraOffsetX();
    }

    /**
     * Getter for the camera offset on the y-axis
     *
     * @return cameraOffsetY
     */
    public int getCameraOffsetY() {
        return cameraMovementHandler.getCameraOffsetY();
    }

    /**
     * The mouse-wheel event handler of the game screen
     *
     * @param e is the MouseWheelEvent
     */
    public void mouseWheelRotated(MouseWheelEvent e) {
        cameraMovementHandler.mouseWheelRotated(e);
    }

    /**
     * The key press event handler of the game screen
     *
     * @param e is the KeyEvent
     */
    public void keyPressed(@NotNull KeyEvent e) {
        bottomBar.keyPressed(e);
    }

    /**
     * Getter for panel size
     *
     * @return panel size
     */
    public Dimension getSize() {
        return panel.getSize();
    }

    /**
     * Exit the application
     */
    public void exit() {
        panel.exit();
    }

    /**
     * Getter for the current mouse position
     *
     * @return mouse position
     */
    public Point getMousePosition() {
        return panel.getMousePosition();
    }

    /**
     * Set the selected building type
     *
     * @param selectedBuildingType is the selected building type
     */
    public void setSelectedBuildingType(Tile selectedBuildingType) {
        map.setSelectedBuildingType(selectedBuildingType);
    }

    /**
     * Get the areas of every menu on the game screen as rectangles
     * this is important for click exceptions
     *
     * @return an ArrayList of Rectangle objects
     */
    public @NotNull ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.addAll(topBar.getMenuAreas());
        areas.addAll(bottomBar.getMenuAreas());
        areas.addAll(eventLog.getMenuAreas());
        return areas;
    }

    /**
     * Getter for the width of the panel
     *
     * @return width of the panel
     */
    public int width() {
        return panel.width();
    }

    /**
     * Getter for the height of the panel
     *
     * @return height of the panel
     */
    public int height() {
        return panel.height();
    }

    /**
     * Setter for the current menu state
     */
    public void setState(MenuState s) {
        panel.setState(s);
    }

    /**
     * Getter for the main Panel object
     *
     * @return panel
     */
    public Panel getPanel() {
        return panel;
    }
    
    public void log(String s){
        eventLog.log(s);
    }
    
    public void showCatastrophyIcon(@NotNull String name) {
        topBar.showCatastrophyIcon(name);
    }
}