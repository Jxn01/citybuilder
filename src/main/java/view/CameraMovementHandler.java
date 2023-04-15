package view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.KeyStroke;

public class CameraMovementHandler {
    private MovementState movementState;
    private InputMap im;
    private ActionMap am;
    private int cameraOffsetX, cameraOffsetY;
    private int zoom;
    private Panel panel;
    
    public CameraMovementHandler(Panel panel){
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
     
    public void addToOffsetX(int val) {
        cameraOffsetX += val;
    }

    public void addToOffsetY(int val) {
        cameraOffsetY += val;
    }
    
    public void mouseWheelRotated(MouseWheelEvent e){
        if (e.getWheelRotation() < 0) {
            addToZoom(1, e.getPoint());
        } else {
            addToZoom(-1, e.getPoint());
        }
    }

    public int getZoom() {
        return zoom;
    }
    
    

    public void addToZoom(int val, Point p) {
        int tileX = p.x / (64 + zoom);
        int tileY = p.y / (64 + zoom);
        System.out.println(tileX + " " + tileY);

        zoom += val;
        //limit how big a tile can be shown on the map
        if (zoom > 0) {
            zoom = 0;
        }
        //limit how small a tile can be shown on the map
        else if (Math.abs(cameraOffsetX) + panel.getSize().width > 50 * (64 + zoom)) {
            zoom = (Math.abs(cameraOffsetX) + panel.getSize().width - 3200) / 50;
        }
    }

    public int getCameraOffsetX() {
        return cameraOffsetX;
    }

    public int getCameraOffsetY() {
        return cameraOffsetY;
    }
    
    public void checkMapBorder(){
        //left wall
        if (cameraOffsetX + movementState.xDirection <= 0) {
            cameraOffsetX += movementState.xDirection;
            //right wall
            if (Math.abs(cameraOffsetX) + panel.getSize().width > 51 * (64 + zoom)) {
                cameraOffsetX = -(51 * (64 + zoom) - panel.getSize().width);
            }
        }
        //top wall
        if (cameraOffsetY + movementState.yDirection <= 0) {
            cameraOffsetY += movementState.yDirection;
            //bottom wall
            if (Math.abs(cameraOffsetY) + panel.getSize().height >= 51 * (64 + zoom)) {
                cameraOffsetY = -(51 * (64 + zoom) - panel.getSize().height);
            }
        }
    }
    
    public class MovementState {
        public int xDirection;
        public int yDirection;
        
        public MovementState(){
            xDirection = 0;
            yDirection = 0;
        }
    }
    
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

    public class YDirectionAction extends AbstractDirectionAction {

        public YDirectionAction(MovementState movementState, int value) {
            super(movementState, value);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getMovementState().yDirection = getValue();
        }

    }

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
