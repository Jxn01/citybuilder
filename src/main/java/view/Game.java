package view;

import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import util.ResourceLoader;

//Creates the 'canvas' of the game window where the game is drawn
public class Game {
    
    Panel panel;
    
    public class MovementState {
        public int xDirection;
        public int yDirection;
    }
    
    private int offsetX, offsetY;
    private int zoom;
    private double zoomRatio;
    private Image grass,rocks;
    private MovementState movementState;
    private Tile[][] tiles;
    
    public Game(Panel panel){
        this.panel = panel;

        //at constructing the object, 
        //load the images once, use them multiple times
        offsetX = 0;
        offsetY = 0;
        zoom = 0;
        zoomRatio = 1;
        try {
            grass = ResourceLoader.loadImage("TALLGRASS.png");
            rocks = ResourceLoader.loadImage("PATHROCKS.png");
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

        tiles = new Tile[51][51];
        
        for(int i=0;i<51;++i){
            for(int j=0;j<51;++j){
                //top and bottom
                if(i == 0 || i == 1 || i == 49 || i == 50){
                    tiles[i][j] = Tile.ROCKS;
                }
                //left and right
                else if(j == 0 || j == 1 || j == 49 || j == 50) {
                    tiles[i][j] = Tile.ROCKS;
                }
                else if(i == 25 && j == 25) {
                    tiles[i][j] = Tile.ROCKS;
                }
                else {
                    tiles[i][j] = Tile.GRASS;
                }
                
            }
        }
        
        movementState = new MovementState();
        InputMap im = panel.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();
        
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
            
        //the main game loop
        Timer gameplayTimer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //left wall
                if(offsetX + movementState.xDirection <= 0){
                    offsetX += movementState.xDirection;
                    //right wall
                    if(Math.abs(offsetX) + panel.getSize().width > 50*(64+zoom) ){
                        offsetX = -(50*(64+zoom) - panel.getSize().width);
                    }
                }  
                //top wall
                if(offsetY + movementState.yDirection <= 0) {
                    offsetY += movementState.yDirection;
                    //bottom wall
                    if(Math.abs(offsetY) + panel.getSize().height >= 50*(64+zoom) ) {
                        offsetY = -(50*(64+zoom) - panel.getSize().height);
                    }
                }
                
                //System.out.println(zoom);
                panel.repaint();
            }
        });
        gameplayTimer.start();
    }
    
    //what happens when we redraw the 'canvas'
    public void draw(Panel panel, Graphics2D gr) {
        paintMap(gr);
    }
    
    private void paintMap(Graphics2D gr){ 
        
        for(int row = 0;row < 51;++row){
            for(int col = 0;col < 51;++col){
                //image, x, y, width, height, observer
                if(tiles[row][col] == Tile.GRASS){
                    gr.drawImage(grass,col*(64+zoom) + offsetX,row*(64+zoom) + offsetY,64 + zoom,64 + zoom, null);
                }
                else if(tiles[row][col] == Tile.ROCKS){
                    gr.drawImage(rocks,col*(64+zoom) + offsetX,row*(64+zoom) + offsetY,64 + zoom,64 + zoom, null);
                }
                
            }      
        }
    }
    
    public void addToOffsetX(int val){offsetX += val;}
    public void addToOffsetY(int val){offsetY += val;}
    public void addToZoom(int val){
        //limit how big a tile can be shown on the map
        zoom += val;

        double currentTileSize = 64 + zoom;
        //System.out.println(currentTileSize);
        double currentTileRatio = 64 / currentTileSize;
        //System.out.println(currentTileRatio);
        zoomRatio = currentTileRatio;
        //offsetY *= currentTileRatio;
        
  
        if(zoom > 0) {
            zoom = 0;
        }
        //limit how small a tile can be shown on the map
        else if(Math.abs(offsetX) + panel.getSize().width > 50*(64+zoom) ) {
            zoom = (Math.abs(offsetX) + panel.getSize().width - 3200) / 50;
        }   
    }
    
    public int getOffsetX(){return offsetX;}
    public int getOffsetY(){return offsetY;}
     
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

