package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import util.ResourceLoader;

/**
 * This class implements the map of the game using a 2D array of tiles
 */
public class Map {
    private Game game;
    private Image grass, rocks, road, house;
    private Tile[][] tiles;
    private Tile selectedBuildingType;
    
    /**
     * Constructor of the Map class
     * @param game is the main Game object
     */
    public Map(Game game){
        this.game = game;
        tiles = new Tile[51][51];
        selectedBuildingType = null;
        try {
            constructMap();
            grass = ResourceLoader.loadImage("TALLGRASS.png");
            rocks = ResourceLoader.loadImage("PATHROCKS.png");
            road = ResourceLoader.loadImage("road_tile.png");
            house = ResourceLoader.loadImage("HOUSE.png");
        } catch (IOException ex) {}
        
    }
    
    /**
     * Paint the map with all of it's tiles on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public void paint(Graphics2D gr) {
        int zoom = game.getZoom();
        int cameraOffsetX = game.getCameraOffsetX();
        int cameraOffsetY = game.getCameraOffsetY();
        
        for (int row = 0; row < 51; ++row) {
            for (int col = 0; col < 51; ++col) {
                //
                Image img = null;
                switch (tiles[row][col]) {
                    case GRASS -> img = grass;
                    case ROCKS -> img = rocks;
                    case ROAD -> img = road;
                    case HOUSE -> img = house;
                    default -> {
                    }
                }
                gr.drawImage(img, col * (64 + zoom) + cameraOffsetX, row * (64 + zoom) + cameraOffsetY, 64 + zoom, 64 + zoom, null);
            }
        }
        paintHover(gr);
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
        for(int i=0;i<areas.size();++i){
            if(areas.get(i).contains(p)){
                return;
            }
        }

        long currentTime = System.currentTimeMillis();
        double randomDouble = (double) (currentTime % 1000) / 1000; // limit the value between 0 and 1
        double currentSeconds = currentTime / 1000;
        
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
        
        Image img = null;
        switch(selectedBuildingType){
            case ROAD: 
                img = road;
                break;
            case GRASS: 
                img = grass;
                break;
            case ROCKS: 
                img = rocks;
                break;
            default: 
            img = rocks;
            break;
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
        if (tiles[y][x] == Tile.ROCKS) {
            return false;
        }
        return true;
    }

    /**
     * Check if a field is empty on the map
     * @param y is the y index of the field
     * @param x is te x index of the field
     * @return is a Boolean value
     */
    private boolean isFieldEmpty(int y, int x) {
        if (tiles[y][x] == Tile.GRASS) {
            return true;
        }
        return false;
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
        }
        if (tiles[y - 1][x] == Tile.ROAD || tiles[y + 1][x] == Tile.ROAD) {
            return true;
        }
        if (tiles[y][x - 1] == Tile.ROAD || tiles[y][x + 1] == Tile.ROAD) {
            return true;
        }
        return false;
    }
    
    /**
     * Construct the map 
     */
    public final void constructMap(){
        //CONSTRUCT THE MAP
        //<-- load game feature here
        for (int i = 0; i < 51; ++i) {
            for (int j = 0; j < 51; ++j) {
                //top and bottom
                if (i == 0 || i == 1 || i == 49 || i == 50) {
                    tiles[i][j] = Tile.ROCKS;
                }
                //left and right
                else if (j == 0 || j == 1 || j == 49 || j == 50) {
                    tiles[i][j] = Tile.ROCKS;
                } else if (i == 25 && j == 25) {
                    tiles[i][j] = Tile.HOUSE;
                } else {
                    tiles[i][j] = Tile.GRASS;
                }

            }
        }
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
     * @param newTile 
     */
    public void build(int x,int y,Tile newTile){
        game.setBalance(game.getBalance() - 100);
        game.setPopulation(game.getPopulation() + 50);
        tiles[x][y] = newTile;
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
        for(int i=0;i<areas.size();++i){
            if(areas.get(i).contains(p)){
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
}
