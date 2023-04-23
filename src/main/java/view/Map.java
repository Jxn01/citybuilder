package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import util.ResourceLoader;

/**
 * This class implements the map of the game using a 2D array of tiles
 */
public class Map {
    private final Game game;
    private Image grass_1,grass_2,grass_3, rocks, road, fireStation;
    private Image stadium,serviceZone,forest,factoryZone;
    private Image house_1,house_2,house_3,police,construction;
    private final Tile[][] tiles;
    private Tile selectedBuildingType;
    private Point selectedTile;
    
    private ArrayList<ArrayList<Point>> stadiums;
    
    /**
     * Constructor of the Map class
     * @param game is the main Game object
     */
    public Map(Game game){
        this.game = game;
        tiles = new Tile[51][51];
        selectedBuildingType = null;
        selectedTile = null;
        try {
            grass_1 = ResourceLoader.loadImage("GRASS_1.png");
            grass_2 = ResourceLoader.loadImage("GRASS_2.png");
            grass_3 = ResourceLoader.loadImage("GRASS_3.png");
            rocks = ResourceLoader.loadImage("PATHROCKS.png");
            fireStation = ResourceLoader.loadImage("FIRESTATION.png");
            road = ResourceLoader.loadImage("ROAD_1.png");
            stadium = ResourceLoader.loadImage("STADIUM_1.png");
            serviceZone = ResourceLoader.loadImage("SERVICE.png");
            forest = ResourceLoader.loadImage("FOREST_1.png");
            factoryZone = ResourceLoader.loadImage("FACTORY.png");
            house_1 = ResourceLoader.loadImage("HOUSE_1.png");
            house_2 = ResourceLoader.loadImage("HOUSE_2.png");
            house_3 = ResourceLoader.loadImage("HOUSE_3.png");
            police = ResourceLoader.loadImage("POLICE_1.png");
            construction = ResourceLoader.loadImage("CONSTRUCTION.png");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        stadiums = new ArrayList<>();
        constructMap();
    }
    
    /**
     * Paint the map with all of its tiles on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public void paint(Graphics2D gr) {
        int zoom = game.getZoom();
        int cameraOffsetX = game.getCameraOffsetX();
        int cameraOffsetY = game.getCameraOffsetY();
        
        for (int row = 0; row < 51; ++row) {
            for (int col = 0; col < 51; ++col) {
                
                Image img = tileToImg(tiles[row][col]);
                
                if(tiles[row][col] == Tile.STADIUM){
                    Point p = new Point(row,col);
                    for (ArrayList<Point> points : stadiums) {
                        if (points.get(0).equals(p)) {
                            gr.drawImage(img, col * (64 + zoom) + cameraOffsetX, row * (64 + zoom) + cameraOffsetY, 128 + zoom, 128 + zoom, null);
                        }
                    }
                }
                else {
                    gr.drawImage(img, col * (64 + zoom) + cameraOffsetX, row * (64 + zoom) + cameraOffsetY, 64 + zoom, 64 + zoom, null);
                }
                
   
            }
        }
        paintHover(gr);
        drawSelectedTile(gr);
    }
    
    private Image tileToImg(Tile tile){
        Image img;
        switch(tile){
            case GRASS_1 -> img = grass_1;
            case GRASS_2 -> img = grass_2;
            case GRASS_3 -> img = grass_3;
            case ROCKS -> img = rocks;
            case FIRESTATION -> img = fireStation;
            case ROAD -> img = road;
            case STADIUM -> img = stadium;
            case SERVICEZONE -> img = serviceZone;
            case FOREST -> img = forest;
            case FACTORYZONE -> img = factoryZone;
            case HOUSE_1 -> img = house_1;
            case HOUSE_2 -> img = house_2;
            case HOUSE_3 -> img = house_3;
            case POLICE -> img = police;
            case CONSTRUCTION -> img = construction;
            default -> img = grass_1;
        }
        return img;
    }
    
       /**
     * Paint the hover effect on the screen after the user
     * clicked on a building construction button
     * @param gr is the graphics context of the main Panel object
     */
    private void paintHover(Graphics2D gr) {
        //do nothing if the user didn't select a building type
        if(selectedBuildingType == null){
            return;
        }
        //do nothing if one of the submenus is hovered
        Point p = MouseInfo.getPointerInfo().getLocation();
        ArrayList<Rectangle> areas = game.getMenuAreas();
        for (Rectangle area : areas) {
            if (area.contains(p)) {
                return;
            }
        }

        long currentTime = System.currentTimeMillis();
        double randomDouble = (double) (currentTime % 1000) / 1000; // limit the value between 0 and 1
        double currentSeconds = (double) currentTime / 1000;
        
        int redVal;
        int greenVal;
        int alpha;
        
        int offsetX = game.getCameraOffsetX();
        int offsetY = game.getCameraOffsetY();
        int zoom = game.getZoom();
        int y = (p.y - offsetY) / (64 + zoom);
        int x = (p.x - offsetX) / (64 + zoom);
        
        //paint red effect if the player can NOT build there
        if(!isFieldEmpty(y, x) || !isFieldValid(y, x)){
            redVal = 255;
            greenVal = 0;
        }
        //paint green effect if the player CAN build there
        else{
            redVal = 0;
            greenVal = 255;
        }
        
        //make the effect pulsate once every 2 seconds
        if(currentSeconds % 2 == 0){
            alpha = (int) Math.ceil(randomDouble * 255);
        }
        else {
            alpha = 255 - (int) Math.ceil(randomDouble * 255);
        }
        
        Color color = new Color(redVal, greenVal, 0, alpha);
        gr.setColor(color);
        
        Image img = tileToImg(selectedBuildingType);
        
        if(selectedBuildingType == Tile.STADIUM){
            gr.drawImage(img, x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY, 128 + zoom, 128 + zoom, null);
            gr.fillRect(x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY, 128 + zoom, 128 + zoom);
            return;
        }
        
        
        gr.drawImage(img, x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY, 64 + zoom, 64 + zoom, null);
        gr.fillRect(x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY, 64 + zoom, 64 + zoom);
        
        
    }
    
    /**
     * Check if a field is valid on the map
     * @param y is the y index of the field
     * @param x is te x index of the field
     * @return a Boolean value
     */
    private boolean isFieldValid(int y, int x) {
        return tiles[y][x] != Tile.ROCKS;
    }

    /**
     * Check if a field is empty on the map
     * @param y is the y index of the field
     * @param x is te x index of the field
     * @return is a Boolean value
     */
    private boolean isFieldEmpty(int y, int x) {
        return tiles[y][x] == Tile.GRASS_1;
    }

    /**
     * Check if a field is next to a road
     * @param y is the y index of the field
     * @param x is te x index of the field
     * @return is a Boolean value
     */
    private boolean isNextToRoad(int y, int x) {
        if (!isFieldValid(y, x)) {
            return false;
        }else return tiles[y - 1][x] == Tile.ROAD || tiles[y + 1][x] == Tile.ROAD || tiles[y][x - 1] == Tile.ROAD || tiles[y][x + 1] == Tile.ROAD;
        //alatta, felette, balra, jobbra
    }
    
    /**
     * Construct the map 
     */
    public final void constructMap(){
        //CONSTRUCT THE MAP
        //<-- load game feature here
        for (int i = 0; i < 51; ++i) {
            for (int j = 0; j < 51; ++j) {
                //top and bottom borders
                if (i == 0 || i == 1 || i == 49 || i == 50) {
                    tiles[i][j] = Tile.ROCKS;
                }
                //left and right borders
                else if (j == 0 || j == 1 || j == 49 || j == 50) {
                    tiles[i][j] = Tile.ROCKS;

                //default: paint grass
                } else {
                    
                    Random rand = new Random();
                    int randomNum = rand.nextInt(3) + 1;
                    switch (randomNum) {
                        case 1 -> tiles[i][j] = Tile.GRASS_1;
                        case 2 -> tiles[i][j] = Tile.GRASS_2;
                        case 3 -> tiles[i][j] = Tile.GRASS_3;
                        default -> {
                        }
                    }
                    
                }

            }
        }
    }
    
    public void drawSelectedTile(Graphics2D gr){
        if(selectedTile == null){return;}

        gr.setColor(Color.CYAN);
        gr.setStroke(new BasicStroke(5));
        int cameraOffsetX = game.getCameraOffsetX();
        int cameraOffsetY = game.getCameraOffsetY();
        gr.drawRect(selectedTile.y * 64 + cameraOffsetX,selectedTile.x * 64 + cameraOffsetY,64,64);
        
        gr.setColor(Color.WHITE);
        gr.fillRect(selectedTile.y * 64 + cameraOffsetX - 64*2,selectedTile.x * 64 + cameraOffsetY - 64*3,64 * 5,64*3);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        
        gr.setColor(Color.BLACK);
        int x = selectedTile.y * 64 + cameraOffsetX - 64*2 + 10;
        int y = selectedTile.x * 64 + cameraOffsetY - 64*3;
        gr.drawString("Tile Stat 1: abababababa",x,y + 20);
        gr.drawString("Tile Stat 2: abababababa",x,y + 50);
        gr.drawString("Tile Stat 3: abababababa",x,y + 80);
        gr.drawString("Tile Stat 4: abababababa",x,y + 110);

        MyButton upgradeBtn = new MyButton(x, y + 140, 80, 40, "upgrade");
        MyButton delBtn = new MyButton(x + 100, y + 140, 80, 40, "delTile");
        upgradeBtn.draw(gr, game.getMousePosition());
        delBtn.draw(gr, game.getMousePosition());
        
    }
    
    public void click(Point p){
        
        //return if the user clicked one of the submenus
        ArrayList<Rectangle> menuAreas = game.getMenuAreas();
        for (Rectangle menuArea : menuAreas) {
            if (menuArea.contains(p)) {
                selectedTile = null;
                return;
            }
        }
        
        //what happens when te user selects a tile
        Point click = pointToXY(p);
        if(selectedBuildingType == null) {
            if(selectedTile == null){
                selectedTile = click;
            }
            else if(selectedTile.equals(click)){
                selectedTile = null;
            }
            else {
                selectedTile = click;
            }
            
            return;
        }
        
        build(click.x,click.y,selectedBuildingType);
    }

    /**
     * Set the selected building type after the user
     * clicked on a building construction button
     * @param selectedBuildingType is the new selected building type
     */
    public void setSelectedBuildingType(Tile selectedBuildingType) {
        this.selectedBuildingType = selectedBuildingType;
    }
    
    /**
     * Build the selected building type onto a tile
     * @param x is te x index of the field
     * @param y is te y index of the field
     * @param newTile is the new tile type
     */
    public void build(int x,int y,Tile newTile){
        
        game.setBalance(game.getBalance() - 100);
        game.setPopulation(game.getPopulation() + 50);
        
        //build not stadium
        tiles[x][y] = newTile;
        selectedBuildingType = null;
        
        //build stadium
        if(newTile == Tile.STADIUM){
            ArrayList<Point> array = new ArrayList<>();
            array.add(new Point(x,y));
            array.add(new Point(x,y+1));
            array.add(new Point(x+1,y));
            array.add(new Point(x+1,y+1));
            stadiums.add(array);
            
            tiles[x][y] = Tile.STADIUM;
            tiles[x][y+1] = Tile.STADIUM;
            tiles[x+1][y] = Tile.STADIUM;
            tiles[x+1][y+1] = Tile.STADIUM;
            return;
        }
        
        //remove stadium
        Point p = new Point(x,y);
        for(int i=0;i<stadiums.size();++i){
            if(newTile == Tile.GRASS_1 && stadiums.get(i).contains(p)){
                Point firstTile = stadiums.get(i).get(0);
                int tileX = firstTile.x;
                int tileY = firstTile.y;
                tiles[tileX][tileY] = Tile.GRASS_1;
                tiles[tileX][tileY+1] = Tile.GRASS_1;
                tiles[tileX+1][tileY] = Tile.GRASS_1;
                tiles[tileX+1][tileY+1] = Tile.GRASS_1;
                stadiums.remove(i);
                break;
            }
        }
            
    }
    
    /**
     * Given a point on the screen, return the corresponding field on the map
     * @param p is the current cursor position
     * @return a tile
     */
    public Tile pointToTile(Point p) {
        if(p == null){
            return null;
        }
        //do nothing if one of the submenus is hovered
        ArrayList<Rectangle> areas = game.getMenuAreas();
        for (Rectangle area : areas) {
            if (area.contains(p)) {
                return null;
            }
        }
       
        int offsetX = game.getCameraOffsetX();
        int offsetY = game.getCameraOffsetY();
        int zoom = game.getZoom();
        int y = (p.y - offsetY) / (64 + zoom);
        int x = (p.x - offsetX) / (64 + zoom);

        return tiles[y][x];
    }
    
    public Point pointToXY(Point p) {
        if(p == null){
            return null;
        }
        //do nothing if one of the submenus is hovered
        ArrayList<Rectangle> areas = game.getMenuAreas();
        for (Rectangle area : areas) {
            if (area.contains(p)) {
                return null;
            }
        }
       
        int offsetX = game.getCameraOffsetX();
        int offsetY = game.getCameraOffsetY();
        int zoom = game.getZoom();
        int y = (p.y - offsetY) / (64 + zoom);
        int x = (p.x - offsetX) / (64 + zoom);

        return new Point(y,x);
    }
}
