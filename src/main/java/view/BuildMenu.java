package view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class BuildMenu extends GameMenu{
    MyButton fireFighterBtn;
    MyButton roadBtn;
    MyButton statiumBtn;
    MyButton serviceZoneBtn;
    MyButton forestBtn;
    MyButton factoryZoneBtn;
    MyButton livingZoneBtn;
    MyButton policeStationBtn;
    
    public BuildMenu(Game game){
        super(game);
        fireFighterBtn = new MyButton(700, 753, 40, 40, "fireFighter");
        roadBtn = new MyButton(740, 753, 40, 40, "road");
        statiumBtn = new MyButton(780, 753, 40, 40, "stadium");
        serviceZoneBtn = new MyButton(820, 753, 40, 40, "serviceZone");
        forestBtn = new MyButton(860, 753, 40, 40, "forest");
        factoryZoneBtn = new MyButton(900, 753, 40, 40, "factoryZone");
        livingZoneBtn = new MyButton(940, 753, 40, 40, "livingZone");
        policeStationBtn = new MyButton(940, 753, 40, 40, "police");  
    }
    
    @Override
    public void draw(Graphics2D gr){
        fireFighterBtn.draw(gr, game.getMousePosition());
        roadBtn.draw(gr, game.getMousePosition());
        statiumBtn.draw(gr, game.getMousePosition());
        serviceZoneBtn.draw(gr, game.getMousePosition());
        forestBtn.draw(gr, game.getMousePosition());
        factoryZoneBtn.draw(gr, game.getMousePosition());
        livingZoneBtn.draw(gr, game.getMousePosition());
        policeStationBtn.draw(gr, game.getMousePosition());
    }
    
    @Override
    public void click(Point p){
        if(fireFighterBtn.isHovered(p)){
            game.setSelectedBuildingType(Tile.HOUSE);
        }
        else if(roadBtn.isHovered(p)){
            game.setSelectedBuildingType(Tile.ROAD);
        }
        else if(statiumBtn.isHovered(p)){
            game.setSelectedBuildingType(Tile.HOUSE);
        }
        else if(serviceZoneBtn.isHovered(p)){
            game.setSelectedBuildingType(Tile.HOUSE);
        }
        else if(factoryZoneBtn.isHovered(p)){
            game.setSelectedBuildingType(Tile.HOUSE);
        }
        else if(livingZoneBtn.isHovered(p)){
            game.setSelectedBuildingType(Tile.HOUSE);
        }
        else if(policeStationBtn.isHovered(p)){
            game.setSelectedBuildingType(Tile.HOUSE);
        }
    }
    
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        return areas;
    }
}
