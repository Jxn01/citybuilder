package view.gui;

import view.components.Panel;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.KeyStroke;


/**
 * This class implements the camera movement of the game
 */
public class CameraMovementHandler {
    private final MovementState movementState;
    private final InputMap im;
    private final ActionMap am;
    private int cameraOffsetX, cameraOffsetY;
    private int zoom;
    private final Panel panel;
    
    public CameraMovementHandler(Panel panel) {
        this.panel = panel;
        movementState = new MovementState();
        cameraOffsetX = 0;
        cameraOffsetY = 0;
        zoom = 0;
        im = panel.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        am = panel.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "down-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "down-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "up-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "up-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "left-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "right-released");

        am.put("down-pressed", new YDirectionAction(movementState, -12));
        am.put("down-released", new YDirectionAction(movementState, 0));
        am.put("up-pressed", new YDirectionAction(movementState, 12));
        am.put("up-released", new YDirectionAction(movementState, 0));
        am.put("left-pressed", new XDirectionAction(movementState, 12));
        am.put("left-released", new XDirectionAction(movementState, 0));
        am.put("right-pressed", new XDirectionAction(movementState, -12));
        am.put("right-released", new XDirectionAction(movementState, 0));
    }
    
    /**
     * Add offset to the camera on the x-axis
     * @param val is the value added to the x offset
     */
    public void addToOffsetX(int val) {
        cameraOffsetX += val;
    }

    /**
     * Add offset to the camera on the y-axis
     * @param val is the value added to the y offset
     */
    public void addToOffsetY(int val) {
        cameraOffsetY += val;
    }
    
    /**
     * Event handler for mouse wheel events
     * @param e is the MouseWheelEvent
     */
    public void mouseWheelRotated(MouseWheelEvent e) {
        /*
        if (e.getWheelRotation() < 0) {
            addToZoom(1, e.getPoint());
        } else {
            addToZoom(-1, e.getPoint());
        }
        */
    }

    /**
     * getter for zoom
     * @return zoom
     */
    public int getZoom() {
        return zoom;
    }
    
    /**
     * Add/subtract an int value to/from zoom
     * @param val is the added/subtracted int value
     * @param p is the current cursor location
     */
    public void addToZoom(int val, Point p) {
        int tileX = p.x / (64 + zoom);
        int tileY = p.y / (64 + zoom);
        System.out.println(tileX + " " + tileY);

        zoom += val;
        //limit how big a tile can be shown on the map
        if(zoom > 0) {
            zoom = 0;
        } else if(Math.abs(cameraOffsetX) + panel.getSize().width > 50 * (64 + zoom)) { //limit how small a tile can be shown on the map
            zoom = (Math.abs(cameraOffsetX) + panel.getSize().width - 3200) / 50;
        }
    }

    /**
     * getter for the camera offset on the x-axis
     * @return the camera offset on the x-axis
     */
    public int getCameraOffsetX() {
        return cameraOffsetX;
    }

    /**
     * getter for the camera offset on the y-axis
     * @return the camera offset on the y-axis
     */
    public int getCameraOffsetY() {
        return cameraOffsetY;
    }
    
    /**
     * Apply modifications to the camera offset if the camera reaches the border 
     * (prevent the player from moving the camera out of the playable area)
     */
    public void checkMapBorder() {
        //left wall
        if(cameraOffsetX + movementState.xDirection <= 0) {
            cameraOffsetX += movementState.xDirection;
            //right wall
            if(Math.abs(cameraOffsetX) + panel.getSize().width > 51 * (64 + zoom)) {
                cameraOffsetX = -(51 * (64 + zoom) - panel.getSize().width);
            }
        }
        //top wall
        if(cameraOffsetY + movementState.yDirection <= 0) {
            cameraOffsetY += movementState.yDirection;
            //bottom wall
            if(Math.abs(cameraOffsetY) + panel.getSize().height >= 51 * (64 + zoom)) {
                cameraOffsetY = -(51 * (64 + zoom) - panel.getSize().height);
            }
        }
    }
    
    /**
     * A movement state has an x and a y direction
     * which allows the player to move the camera
     * on both of the axes simultaneously
     */
    public class MovementState {
        public int xDirection;
        public int yDirection;
        
        public MovementState() {
            xDirection = 0;
            yDirection = 0;
        }
    }
    
    /**
     * This class contains all functions necessary to move the camera
     * on one axis. It is the superclass of YDirectionAction
     * and XDirectionAction
     */
    public abstract class AbstractDirectionAction extends AbstractAction {

        private final MovementState movementState;
        private final int value;

        public AbstractDirectionAction(MovementState movementState, int value) {
            this.movementState = movementState;
            this.value = value;
        }

        public MovementState getMovementState() {
            return movementState;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * This class is a derived class of the AbstractDirectionAction class
     * It implements the camera movement on the y-axis
     */
    public class YDirectionAction extends AbstractDirectionAction {

        public YDirectionAction(MovementState movementState, int value) {
            super(movementState, value);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getMovementState().yDirection = getValue();
        }
    }

    /**
     * This class is a derived class of the AbstractDirectionAction class
     * It implements the camera movement on the x-axis
     */
    public class XDirectionAction extends AbstractDirectionAction {

        public XDirectionAction(MovementState movementState, int value) {
            super(movementState, value);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getMovementState().xDirection = getValue();
        }
    }
}
