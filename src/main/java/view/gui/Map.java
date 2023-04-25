package view.gui;

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

import model.Coordinate;
import model.buildings.Building;
import model.buildings.playerbuilt.*;
import model.enums.Zone;
import util.Logger;
import util.ResourceLoader;
import view.components.custom.MyButton;
import view.enums.Tile;
import model.field.*;

/**
 * This class implements the map of the game using a 2D array of tiles
 */
public class Map {
    private final Game game;
    private Image rocks, grass_1, grass_2, grass_3, house_1, house_2, house_3, service_1, service_2, service_3, factory_1, factory_2, factory_3, serviceZone, factoryZone, residentialZone, road, fireStation, stadium, forest, police, construction;
    private Tile selectedBuildingType;
    private Point selectedTile;
    private Field[][] fields;
    private ArrayList<ArrayList<Point>> stadiums;
    
    /**
     * Constructor of the Map class
     * @param game is the main Game object
     */
    public Map(Game game) {
        this.game = game;
        try {
            rocks = ResourceLoader.loadImage("PATHROCKS.png");
            grass_1 = ResourceLoader.loadImage("GRASS_1.png");
            grass_2 = ResourceLoader.loadImage("GRASS_2.png");
            grass_3 = ResourceLoader.loadImage("GRASS_3.png");
            house_1 = ResourceLoader.loadImage("HOUSE_1.png");
            house_2 = ResourceLoader.loadImage("HOUSE_2.png");
            house_3 = ResourceLoader.loadImage("HOUSE_3.png");
            service_1 = ResourceLoader.loadImage("SERVICE_LVL1.png");
            service_2 = ResourceLoader.loadImage("SERVICE_LVL2.png");
            service_3 = ResourceLoader.loadImage("SERVICE_LVL3.png");
            factory_1 = ResourceLoader.loadImage("FACTORY_LVL1.png");
            factory_2 = ResourceLoader.loadImage("FACTORY_LVL2.png");
            factory_3 = ResourceLoader.loadImage("FACTORY_LVL3.png");
            serviceZone = ResourceLoader.loadImage("service_zone.png");
            factoryZone = ResourceLoader.loadImage("industrial_zone.png");
            residentialZone = ResourceLoader.loadImage("residential_zone.png");
            fireStation = ResourceLoader.loadImage("FIRESTATION.png");
            road = ResourceLoader.loadImage("road_tile.png");
            stadium = ResourceLoader.loadImage("STADIUM_1.png");
            forest = ResourceLoader.loadImage("FOREST_1.png");
            police = ResourceLoader.loadImage("POLICE_1.png");
            construction = ResourceLoader.loadImage("CONSTRUCTION.png");
        } catch(IOException exc) { exc.printStackTrace(); }
        stadiums = new ArrayList<>();
    }
    
    /**
     * Paint the map with all of its tiles on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public void paint(Graphics2D gr) {
        fields = game.getPanel().getGameManager().getGameData().getFields();
        int zoom = game.getZoom();
        int cameraOffsetX = game.getCameraOffsetX();
        int cameraOffsetY = game.getCameraOffsetY();
        
        for(int row = 0; row < 51; ++row) {
            for(int col = 0; col < 51; ++col) {
                Image img = tileToImg(fields[row][col].getTile());
                if(row > 1 && col > 1 && row < 49 && col < 49 && ((PlayableField) fields[row][col]).getBuilding() instanceof Stadium) {
                    Point p = new Point(row, col);
                    for(ArrayList<Point> points : stadiums) {
                        if(points.get(0).equals(p)) {
                            gr.drawImage(img, col * (64 + zoom) + cameraOffsetX, row * (64 + zoom) + cameraOffsetY -64, 128 + zoom, 128 + zoom, null);
                        }
                    }
                } else {
                    gr.drawImage(img, col * (64 + zoom) + cameraOffsetX, row * (64 + zoom) + cameraOffsetY, 64 + zoom, 64 + zoom, null);
                }
            }
        }
        paintHover(gr);
        drawSelectedTile(gr);
    }

    /**
     * Build the selected building type onto a tile
     * @param x is te x index of the field
     * @param y is te y index of the field
     * @param newTile is the new tile type
     */
    public void build(int x, int y, Tile newTile) {
        if(newTile != null) {
            switch (newTile) {
                case SERVICEZONE -> ((PlayableField) fields[x][y]).markZone(Zone.SERVICE_ZONE);
                case RESIDENTIALZONE -> ((PlayableField) fields[x][y]).markZone(Zone.RESIDENTIAL_ZONE);
                case FACTORYZONE -> ((PlayableField) fields[x][y]).markZone(Zone.INDUSTRIAL_ZONE);

                case STADIUM -> {
                    ArrayList<Point> array = new ArrayList<>();
                    array.add(new Point(x + 1, y));
                    array.add(new Point(x + 1, y + 1));
                    array.add(new Point(x + 2, y));
                    array.add(new Point(x + 2, y + 1));
                    stadiums.add(array);

                    Building st = new Stadium(new Coordinate(x, y));

                    ((PlayableField) fields[x][y]).buildStadium(st);
                    ((PlayableField) fields[x][y + 1]).buildStadium(st);
                    ((PlayableField) fields[x + 1][y]).buildStadium(st);
                    ((PlayableField) fields[x + 1][y + 1]).buildStadium(st);
                }

                case GRASS_1 -> {
                    Point p = new Point(x, y);
                    boolean stadiumFound = false;
                    for (int i = 0; i < stadiums.size(); i++) {
                        if (stadiums.get(i).contains(p)) {
                            Point firstTile = stadiums.get(i).get(0);
                            int tileX = firstTile.x;
                            int tileY = firstTile.y;

                            ((PlayableField) fields[tileX][tileY]).demolishBuilding();
                            ((PlayableField) fields[tileX][tileY + 1]).demolishBuilding();
                            ((PlayableField) fields[tileX - 1][tileY]).demolishBuilding();
                            ((PlayableField) fields[tileX - 1][tileY + 1]).demolishBuilding();
                            stadiums.remove(i);
                            stadiumFound = true;
                            break;
                        }
                    }
                    if (!stadiumFound) {
                        try {
                            ((PlayableField) fields[x][y]).demolishBuilding();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }

                        try {
                            ((PlayableField) fields[x][y]).deleteZone();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                }

                default -> {
                    try {
                        ((PlayableField) fields[x][y]).buildBuilding(newTile);
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
            selectedBuildingType = null;
        }
    }

    /**
     * Draw the selected tile on the screen
     * @param p is the point where the user clicked
     */
    public void click(Point p) {
        if(submenuHovered(p)) {
            selectedTile = null;
        } else {
            Point click = pointToXY(p);
            if (selectedBuildingType != null && p.y < game.height() - 40) {
                if (selectedBuildingType != Tile.GRASS_1) {
                    if (isFieldEmpty(click.x, click.y)) {
                        if (selectedBuildingType != Tile.ROAD) {
                            if (selectedBuildingType == Tile.STADIUM) {
                                if (isFieldEmpty(click.x, click.y + 1) && isFieldEmpty(click.x - 1, click.y) && isFieldEmpty(click.x - 1, click.y + 1) && stadiumIsNextToRoad(click.x, click.y)) {
                                    build(click.x - 1, click.y, selectedBuildingType);
                                } else {
                                    Logger.log("You can only build a stadium on 4 tiles!");
                                }
                            } else if (isNextToRoad(click.x, click.y)) {
                                build(click.x, click.y, selectedBuildingType);
                            } else {
                                Logger.log("You can only build next to a road!");
                            }
                        } else {
                            build(click.x, click.y, selectedBuildingType);
                        }
                    }
                } else {
                    build(click.x, click.y, selectedBuildingType);
                }
            } else if (selectedTile == null) {
                selectedTile = click;
            } else if (selectedTile.equals(click)) {
                selectedTile = null;
            } else {
                selectedTile = click;
            }
        }
    }

    /**
     * Paint the hover effect on the screen after the user
     * clicked on a building construction button
     * @param gr is the graphics context of the main Panel object
     */
    private void paintHover(Graphics2D gr) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        if(!submenuHovered(p) && selectedBuildingType != null){
            long currentTime = System.currentTimeMillis();
            double randomDouble = (double) (currentTime % 1000) / 1000; // limit the value between 0 and 1
            double currentSeconds = (double) currentTime / 1000;

            int redVal;
            int greenVal;
            int alpha;

            int offsetX = game.getCameraOffsetX();
            int offsetY = game.getCameraOffsetY();
            int zoom = game.getZoom();
            int x = (p.x - offsetX) / (64 + zoom);
            int y = (p.y - offsetY) / (64 + zoom);

            //paint red effect if the player can NOT build there
            if(!isFieldEmpty(y-1, x) || !isFieldValid(y-1, x) || (selectedBuildingType != Tile.ROAD) && !isNextToRoad(y-1, x)) {
                redVal = 255;
                greenVal = 0;
            } else { //paint green effect if the player CAN build there
                redVal = 0;
                greenVal = 255;
            }

            if(selectedBuildingType == Tile.STADIUM) {
                if(!isFieldEmpty(y-1, x) || !isFieldEmpty(y-1, x + 1) || !isFieldEmpty(y - 2, x) || !isFieldEmpty(y - 2, x + 1) || !stadiumIsNextToRoad(y-1, x)) {
                    redVal = 255;
                    greenVal = 0;
                } else {
                    redVal = 0;
                    greenVal = 255;
                }
            }

            //make the effect pulsate once every 2 seconds
            if(currentSeconds % 2 == 0) {
                alpha = (int) Math.ceil(randomDouble * 255);
            } else {
                alpha = 255 - (int) Math.ceil(randomDouble * 255);
            }

            Color color = new Color(redVal, greenVal, 0, alpha);
            gr.setColor(color);

            Image img = tileToImg(selectedBuildingType);

            if(selectedBuildingType == Tile.STADIUM) {
                gr.drawImage(img, x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 128, 128 + zoom, 128 + zoom, null);
                gr.fillRect(x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 128, 128 + zoom, 128 + zoom);
            } else {
                gr.drawImage(img, x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 64, 64 + zoom, 64 + zoom, null);
                gr.fillRect(x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 64, 64 + zoom, 64 + zoom);
            }
        }
    }

    /**
     * Paint the context menu of a tile on the screen
     * @param gr is the graphics context of the main Panel object
     */
    public void drawSelectedTile(Graphics2D gr) {
        if(selectedTile != null) {
            Field field = fields[selectedTile.x][selectedTile.y];
            if(field instanceof PlayableField && ((PlayableField) field).getBuilding() != null) {
                gr.setColor(Color.CYAN);
                gr.setStroke(new BasicStroke(5));
                int cameraOffsetX = game.getCameraOffsetX();
                int cameraOffsetY = game.getCameraOffsetY();
                gr.drawRect(selectedTile.y * 64 + cameraOffsetX, selectedTile.x * 64 + cameraOffsetY, 64, 64);

                gr.setColor(Color.WHITE);
                gr.fillRect(selectedTile.y * 64 + cameraOffsetX - 64 * 2, selectedTile.x * 64 + cameraOffsetY - 64 * 3, 64 * 5, 64 * 3);
                gr.setFont(new Font("TimesRoman", Font.PLAIN, 18));

                gr.setColor(Color.BLACK);
                int x = selectedTile.y * 64 + cameraOffsetX - 64 * 2 + 10;
                int y = selectedTile.x * 64 + cameraOffsetY - 64 * 3;

                String stats = ((PlayableField) field).getBuilding().getStatistics();
                String[] statsArray = stats.split("\n");
                for(int i = 1; i <= statsArray.length; i++) {
                    gr.drawString(statsArray[i - 1], x, y + 30 * i);
                }

                MyButton upgradeBtn = new MyButton(x, y + 140, 80, 40, "upgrade");
                MyButton delBtn = new MyButton(x + 100, y + 140, 80, 40, "delTile");
                upgradeBtn.draw(gr, game.getMousePosition());
                delBtn.draw(gr, game.getMousePosition());
            }
        }
    }

    /**
     * Check if a field is valid on the map
     * @param x is the x index of the field
     * @param y is the y index of the field
     * @return a boolean value
     */
    private boolean isFieldValid(int x, int y) {
        return fields[x][y] instanceof PlayableField;
    }

    /**
     * Check if a field is empty on the map
     * @param x is the x index of the field
     * @param y is the y index of the field
     * @return is a boolean value
     */
    private boolean isFieldEmpty(int x, int y) {
        return fields[x][y] instanceof PlayableField && ((PlayableField) fields[x][y]).getBuilding() == null && ((PlayableField) fields[x][y]).getZone() == null;
    }

    /**
     * Check if a field is next to a road
     * @param y is the y index of the field
     * @param x is te x index of the field
     * @return is a boolean value
     */
    private boolean isNextToRoad(int x, int y) {
        if(isFieldValid(x, y)) {
                return isFieldValid(x - 1, y) && ((PlayableField) fields[x - 1][y]).getBuilding() instanceof Road
                    || isFieldValid(x + 1, y) && ((PlayableField) fields[x + 1][y]).getBuilding() instanceof Road
                    || isFieldValid(x, y - 1) && ((PlayableField) fields[x][y - 1]).getBuilding() instanceof Road
                    || isFieldValid(x, y + 1) && ((PlayableField) fields[x][y + 1]).getBuilding() instanceof Road;
        } else return false;
        //balra, jobbra, alatta, felette
    }

    /**
     * Check if a stadium is next to a road
     * @param x is the x index of the field
     * @param y is the y index of the field
     * @return is a boolean value
     */

    private boolean stadiumIsNextToRoad(int x, int y) { // x y is the bottom left tile of the stadium
        if(isFieldValid(x, y)) {
            return isNextToRoad(x, y) || isNextToRoad(x - 1, y) || isNextToRoad(x - 1, y + 1) || isNextToRoad(x, y + 1);
        } else return false;
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
     * Given a point on the screen, return the corresponding field on the map
     * @param p is the current cursor position
     * @return a point
     */
    public Point pointToXY(Point p) {
        if(submenuHovered(p) || p == null) return null;
       
        int offsetX = game.getCameraOffsetX();
        int offsetY = game.getCameraOffsetY();
        int zoom = game.getZoom();
        int x = (p.x - offsetX) / (64 + zoom);
        int y = (p.y - offsetY) / (64 + zoom);

        return new Point(y,x);
    }

    public Field pointToField(Point p) {
        if (p != null){
            int offsetX = game.getCameraOffsetX();
            int offsetY = game.getCameraOffsetY();
            int zoom = game.getZoom();
            int x = (p.x - offsetX) / (64 + zoom);
            int y = (p.y - offsetY) / (64 + zoom);

            return fields[y][x];
        } else {
            return null;
        }
    }

    /**
     * Check if the mouse is hovering over a submenu
     * @param p is the current cursor position
     * @return true if the mouse is hovering over a submenu
     */
    private boolean submenuHovered(Point p) {
        for(Rectangle area : game.getMenuAreas()) {
            if(area.contains(p)) {
                return true;
            }
        }
        return false;
    }


    private Image tileToImg(Tile tile) {
        Image img = null;
        switch(tile) {
            case ROCKS -> img = rocks;
            case GRASS_1 -> img = grass_1;
            case GRASS_2 -> img = grass_2;
            case GRASS_3 -> img = grass_3;
            case HOUSE_1 -> img = house_1;
            case HOUSE_2 -> img = house_2;
            case HOUSE_3 -> img = house_3;
            case SERVICE_1 -> img = service_1;
            case SERVICE_2 -> img = service_2;
            case SERVICE_3 -> img = service_3;
            case FACTORY_1 -> img = factory_1;
            case FACTORY_2 -> img = factory_2;
            case FACTORY_3 -> img = factory_3;
            case RESIDENTIALZONE -> img = residentialZone;
            case FACTORYZONE -> img = factoryZone;
            case SERVICEZONE -> img = serviceZone;
            case FIRESTATION -> img = fireStation;
            case ROAD -> img = road;
            case STADIUM -> img = stadium;
            case FOREST -> img = forest;
            case POLICE -> img = police;
            case CONSTRUCTION -> img = construction;
        }
        return img;
    }
}


