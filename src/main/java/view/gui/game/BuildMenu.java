package view.gui.game;

import util.Logger;
import view.gui.Game;
import view.components.custom.MyButton;
import view.enums.Tile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * This class implements the build menu of the game gui
 */
public class BuildMenu extends GameMenu {
    MyButton deleteBtn;
    MyButton fireFighterBtn;
    MyButton roadBtn;
    MyButton stadiumBtn;
    MyButton serviceZoneBtn;
    MyButton forestBtn;
    MyButton factoryZoneBtn;
    MyButton livingZoneBtn;
    MyButton policeStationBtn;
    
    /**
     * Constructor of the build menu
     * @param game is the main game object
     */
    public BuildMenu(Game game){
        super(game);
        deleteBtn = new MyButton(620, 753, 40, 40, "x");
        fireFighterBtn = new MyButton(700, 753, 40, 40, "fireFighter");
        roadBtn = new MyButton(740, 753, 40, 40, "road");
        stadiumBtn = new MyButton(780, 753, 40, 40, "stadium");
        serviceZoneBtn = new MyButton(820, 753, 40, 40, "serviceZone");
        forestBtn = new MyButton(860, 753, 40, 40, "forest");
        factoryZoneBtn = new MyButton(900, 753, 40, 40, "factoryZone");
        livingZoneBtn = new MyButton(940, 753, 40, 40, "livingZone");
        policeStationBtn = new MyButton(980, 753, 40, 40, "police"); 
    }
    
    /**
     * Draw the build menu on the screen
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr){
        deleteBtn.setY(game.height() - 40);
        deleteBtn.draw(gr, game.getMousePosition());
        fireFighterBtn.setY(game.height() - 40);
        fireFighterBtn.draw(gr, game.getMousePosition());
        roadBtn.setY(game.height() - 40);
        roadBtn.draw(gr, game.getMousePosition());
        stadiumBtn.setY(game.height() - 40);
        stadiumBtn.draw(gr, game.getMousePosition());
        serviceZoneBtn.setY(game.height() - 40);
        serviceZoneBtn.draw(gr, game.getMousePosition());
        forestBtn.setY(game.height() - 40);
        forestBtn.draw(gr, game.getMousePosition());
        factoryZoneBtn.setY(game.height() - 40);
        factoryZoneBtn.draw(gr, game.getMousePosition());
        livingZoneBtn.setY(game.height() - 40);
        livingZoneBtn.draw(gr, game.getMousePosition());
        policeStationBtn.setY(game.height() - 40);
        policeStationBtn.draw(gr, game.getMousePosition());
    }
    
    /**
     * Upon a click event, call the appropriate button's functionality
     * @param p p is the current cursor location
     */
    @Override
    public void click(Point p){
        if(deleteBtn.isHovered(p)){
            Logger.log("Delete button clicked");
            game.setSelectedBuildingType(Tile.GRASS_1);
        }
        else if(fireFighterBtn.isHovered(p)){
            Logger.log("Firefighter button clicked");
            game.setSelectedBuildingType(Tile.FIRESTATION);
        }
        else if(roadBtn.isHovered(p)){
            Logger.log("Road button clicked");
            game.setSelectedBuildingType(Tile.ROAD);
        }
        else if(forestBtn.isHovered(p)){
            Logger.log("Forest button clicked");
            game.setSelectedBuildingType(Tile.FOREST);
        }
        else if(stadiumBtn.isHovered(p)){
            Logger.log("Stadium button clicked");
            game.setSelectedBuildingType(Tile.STADIUM);
        }
        else if(serviceZoneBtn.isHovered(p)){
            Logger.log("Service zone button clicked");
            game.setSelectedBuildingType(Tile.SERVICEZONE);
        }
        else if(factoryZoneBtn.isHovered(p)){
            Logger.log("Factory zone button clicked");
            game.setSelectedBuildingType(Tile.FACTORYZONE);
        }
        else if(livingZoneBtn.isHovered(p)){
            Logger.log("Living zone button clicked");
            game.setSelectedBuildingType(Tile.RESIDENTIALZONE);
        }
        else if(policeStationBtn.isHovered(p)){
            Logger.log("Police station button clicked");
            game.setSelectedBuildingType(Tile.POLICE);
        }
    }
    
    /**
     * Get the build menu's area as rectangles (empty array = inside bottom bar)
     * This is important for click event exceptions
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        return areas;
    }
}
