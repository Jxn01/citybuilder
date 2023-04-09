package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
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
    private Image grass,rocks,house;
    private MovementState movementState;
    private Tile[][] tiles;
    
    Boolean hamburgerMenu;
    Boolean taxMenu;
    Boolean statsMenu;
    
    MyButton hamburgerBtn;
    MyButton pauseBtn;
    MyButton speed1Btn;
    MyButton speed2Btn;
    MyButton speed3Btn;
    
    MyButton taxBtn;
    MyButton statsBtn;
    
    MyButton saveBtn;
    MyButton newGameBtn;
    MyButton exitBtn;
    
    MyButton xBtn;
    
    public Game(Panel panel){
        this.panel = panel;

        //at constructing the object, 
        //load the images once, use them multiple times
        offsetX = 0;
        offsetY = 0;
        zoom = 0;
        try {
            grass = ResourceLoader.loadImage("TALLGRASS.png");
            rocks = ResourceLoader.loadImage("PATHROCKS.png");
            house = ResourceLoader.loadImage("HOUSE.png");
            hamburgerBtn = new MyButton(0,0,40,40,"hamburgerMenu");
            
            speed3Btn = new MyButton(1495,753,40,40,"3xspeed");
            speed2Btn = new MyButton(1455,753,40,40,"2xspeed");
            speed1Btn = new MyButton(1415,753,40,40,"1xspeed");
            pauseBtn = new MyButton(1375,753,40,40,"pause");
            
            taxBtn = new MyButton(80,753,40,40,"tax");
            statsBtn = new MyButton(0,753,80,40,"stats");
            
            saveBtn = new MyButton(0,40,120,40,"saveGame");
            newGameBtn = new MyButton(0,90,120,40,"newGame");
            exitBtn = new MyButton(0,140,120,40,"exit");
            
            xBtn = new MyButton(1228,50,40,40,"x");
            
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

        hamburgerMenu = false;
        statsMenu = false;
        taxMenu = false;
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
                    tiles[i][j] = Tile.HOUSE;
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
                    if(Math.abs(offsetX) + panel.getSize().width > 51*(64+zoom) ){
                        offsetX = -(51*(64+zoom) - panel.getSize().width);
                    }
                }  
                //top wall
                if(offsetY + movementState.yDirection <= 0) {
                    offsetY += movementState.yDirection;
                    //bottom wall
                    if(Math.abs(offsetY) + panel.getSize().height >= 51*(64+zoom) ) {
                        offsetY = -(51*(64+zoom) - panel.getSize().height);
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
        paintTopBar(gr);
        paintBottomBar(gr);
        if(hamburgerMenu) {
            paintHamburgerMenu(gr); 
        }
        if(statsMenu){
            paintStatsMenu(gr);
        }
        if(taxMenu){
            paintTaxMenu(gr);
        }
    }
    
    private void paintStatsMenu(Graphics2D gr){
        gr.setColor(Color.white);
        gr.fillRect(268,50,1000,690);
        
        xBtn.draw(gr,panel.getMousePosition());
       
        gr.setColor(Color.black);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString(  "Statisztikák",700,80);
        
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        gr.drawString(  "Bevételek",278,120);
        gr.drawString(  "- Bevétel1: ababababababababababababab",278,160);
        gr.drawString(  "- Bevétel2: ababababababababababababab",278,200);
        gr.drawString(  "- Bevétel3: ababababababababababababab",278,240);
        gr.drawString(  "- Bevétel4: ababababababababababababab",800,160);
        gr.drawString(  "- Bevétel5: ababababababababababababab",800,200);
        gr.drawString(  "- Bevétel6: ababababababababababababab",800,240);
        
        gr.drawString(  "Kiadások",278,320);
        gr.drawString(  "- Kiadás1: ababababababababababababab",278,360);
        gr.drawString(  "- Kiadás2: ababababababababababababab",278,400);
        gr.drawString(  "- Kiadás3: ababababababababababababab",278,440);
        gr.drawString(  "- Kiadás4: ababababababababababababab",800,360);
        gr.drawString(  "- Kiadás5: ababababababababababababab",800,400);
        gr.drawString(  "- Kiadás6: ababababababababababababab",800,440);
        
        gr.drawString(  "Építési adatok",278,520);
        gr.drawString(  "- Építési adat1: ababababababababababababab",278,560);
        gr.drawString(  "- Építési adat2: ababababababababababababab",278,600);
        gr.drawString(  "- Építési adat3: ababababababababababababab",278,640);
        gr.drawString(  "- Építési adat4: ababababababababababababab",800,560);
        gr.drawString(  "- Építési adat5: ababababababababababababab",800,600);
        gr.drawString(  "- Építési adat6: ababababababababababababab",800,640);
    }
    
    private void paintTaxMenu(Graphics2D gr){
        
    }
    
    private void paintHamburgerMenu(Graphics2D gr){
        gr.setColor(Color.white);
        gr.fillRect(0,40,120,120);
        newGameBtn.draw(gr,panel.getMousePosition());
        saveBtn.draw(gr,panel.getMousePosition());
        exitBtn.draw(gr,panel.getMousePosition());
    }
    
    private void paintTopBar(Graphics2D gr){
        gr.setColor(Color.red);
        gr.fillRect(0,0,panel.getSize().width,40);
        hamburgerBtn.draw(gr,panel.getMousePosition());
        gr.setColor(Color.black);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        gr.drawString(  "Költségvetés: 30.000$",40,30);
        gr.drawString(  "Elégedettség: 85%",400,30);
        gr.drawString(  "Populáció: 1,2 millió",700,30);
        gr.drawString(  "Idő: 135.nap",1000,30);
    }
    
    private void paintBottomBar(Graphics2D gr){
        gr.setColor(Color.GREEN);
        gr.fillRect(0,panel.getSize().height - 40,panel.getSize().width,40);
        speed3Btn.draw(gr,panel.getMousePosition());
        speed2Btn.draw(gr,panel.getMousePosition());
        speed1Btn.draw(gr, panel.getMousePosition());
        pauseBtn.draw(gr, panel.getMousePosition());
        
        statsBtn.draw(gr, panel.getMousePosition());
        taxBtn.draw(gr, panel.getMousePosition());
        
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
                else if(tiles[row][col] == Tile.HOUSE){
                    gr.drawImage(house,col*(64+zoom) + offsetX,row*(64+zoom) + offsetY,64 + zoom,64 + zoom, null);
                }
                
            }      
        }
    }
    
    public void click(Point p){
        pointToTile(p);
        if(hamburgerBtn.isHovered(p)){
            hamburgerMenu = !hamburgerMenu;
        }
        else if(taxBtn.isHovered(p)){
            taxMenu = !taxMenu;
        }
        else if(statsBtn.isHovered(p)){
            statsMenu = !statsMenu;
        }
        
        if(statsMenu){
            if(xBtn.isHovered(p)){
                statsMenu = !statsMenu;
            }
        }
    }
    
    public void pointToTile(Point p){
        int x = (p.x - offsetX) /(64+zoom);
        int y = (p.y - offsetY) /(64+zoom);
        tiles[y][x] = Tile.HOUSE;
    }
    
    
    public void addToOffsetX(int val){offsetX += val;}
    public void addToOffsetY(int val){offsetY += val;}
    public void addToZoom(int val,Point p){
          
        int tileX = p.x / (64+zoom);
        int tileY = p.y / (64+zoom);
        System.out.println(tileX + " " + tileY);
        
        zoom += val;
        
        //offsetX += -tileX * (64+zoom);
        //offsetY += -tileY * (64+zoom);
        
        //limit how big a tile can be shown on the map
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

