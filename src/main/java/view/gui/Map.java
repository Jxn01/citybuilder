package view.gui;

import controller.GameManager;
import model.enums.UpgradeLevel;
import model.enums.Zone;
import model.field.Field;
import model.field.PlayableField;
import util.Logger;
import util.ResourceLoader;
import view.components.custom.MyButton;
import view.enums.Tile;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static model.field.PlayableField.canBuildThere;

/**
 * This class implements the map of the game using a 2D array of tiles
 */
public class Map {
    private final Game game;
    private final Rectangle drawnMenuRectangle = new Rectangle(0, 0, 0, 0);
    private final MyButton upgradeBtn = new MyButton(0, 0, 0, 0, "upgrade");
    private Image rocks, grass_1, grass_2, grass_3, house_1, house_2, house_3, service_1, service_2, service_3, factory_1, factory_2, factory_3, serviceZone, factoryZone, residentialZone, road, fireStation, stadium, forest, police, construction, stadium_topleft, stadium_topright, stadium_bottomleft, stadium_bottomright;
    private Tile selectedBuildingType;
    private Point selectedTile;
    private Field[][] fields;

    /**
     * Constructor of the Map class
     *
     * @param game is the main Game object
     */
    public Map(Game game) {
        this.game = game;

        game.getPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // right click
                    selectedBuildingType = null;
                }
            }
        });

        game.getMenuAreas().add(drawnMenuRectangle);
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
            stadium_topleft = ResourceLoader.loadImage("STADIUM_TOPLEFT.png");
            stadium_topright = ResourceLoader.loadImage("STADIUM_TOPRIGHT.png");
            stadium_bottomleft = ResourceLoader.loadImage("STADIUM_BOTTOMLEFT.png");
            stadium_bottomright = ResourceLoader.loadImage("STADIUM_BOTTOMRIGHT.png");
            forest = ResourceLoader.loadImage("FOREST_1.png");
            police = ResourceLoader.loadImage("POLICE_1.png");
            construction = ResourceLoader.loadImage("CONSTRUCTION.png");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Paint the map with all of its tiles on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    public void paint(Graphics2D gr) {
        fields = GameManager.getGameData().getFields();
        int zoom = game.getZoom();
        int cameraOffsetX = game.getCameraOffsetX();
        int cameraOffsetY = game.getCameraOffsetY();

        for (int row = 0; row < 51; ++row) {
            for (int col = 0; col < 51; ++col) {
                Image img = tileToImg(fields[row][col].getTile());
                gr.drawImage(img, col * (64 + zoom) + cameraOffsetX, row * (64 + zoom) + cameraOffsetY, 64 + zoom, 64 + zoom, null);
            }
        }
        paintHover(gr);
        drawSelectedTile(gr);
    }

    /**
     * Build the selected building type onto a tile
     *
     * @param x       is te x index of the field
     * @param y       is te y index of the field
     * @param newTile is the new tile type
     */
    public void build(int x, int y, Tile newTile) {
        if (newTile != null) {
            switch (newTile) {
                case SERVICEZONE -> ((PlayableField) fields[x][y]).markZone(Zone.SERVICE_ZONE);
                case RESIDENTIALZONE -> ((PlayableField) fields[x][y]).markZone(Zone.RESIDENTIAL_ZONE);
                case FACTORYZONE -> ((PlayableField) fields[x][y]).markZone(Zone.INDUSTRIAL_ZONE);

                case GRASS_1 -> {
                    Point p = new Point(x, y);

                    if (((PlayableField) fields[x][y]).getZone() == null) {
                        try {
                            ((PlayableField) fields[x][y]).demolishBuilding();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    } else {
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
            Logger.log("Part graphs: " + GameManager.countDisconnectedGraphs(GameManager.getGraph()));
            Logger.log("Graph node count: " + GameManager.getGraph().nodes().size());
            Logger.log("Graph edge count: " + GameManager.getGraph().edges().size());
        }
    }

    /**
     * Upgrade the selected building
     *
     * @param x is the x index of the field
     * @param y is the y index of the field
     */
    public void upgrade(int x, int y) {
        try {
            ((PlayableField) fields[x][y]).upgrade();
            selectedTile = null;
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Draw the selected tile on the screen
     *
     * @param p is the point where the user clicked
     */
    public void click(Point p) {
        if (submenuHovered(p)) {
            selectedTile = null;
            return;
        }

        Point click = pointToXY(p);
        if (selectedBuildingType != null && p.y < game.height() - 40) {
            if (selectedBuildingType != Tile.GRASS_1 && canBuildThere(click.x, click.y, selectedBuildingType)) {
                build(click.x, click.y, selectedBuildingType);
            } else {
                build(click.x, click.y, selectedBuildingType);
            }
        } else if (selectedTile != null && selectedTile.equals(click)) {
            selectedTile = null;
        } else {
            selectedTile = click;
        }
    }


    /**
     * Paint the hover effect on the screen after the user
     * clicked on a building construction button
     *
     * @param gr is the graphics context of the main Panel object
     */
    private void paintHover(Graphics2D gr) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        if (!submenuHovered(p) && selectedBuildingType != null) {
            long currentTime = System.currentTimeMillis();
            double randomDouble = (double) (currentTime % 1000) / 1000; // limit the value between 0 and 1
            double currentSeconds = (double) currentTime / 1000;

            int redVal;
            int greenVal;
            int alpha;

            int offsetX = game.getCameraOffsetX();
            int offsetY = game.getCameraOffsetY();
            int zoom = game.getZoom();
            int x = ((p.x - offsetX) / (64 + zoom));
            int y = (p.y - offsetY) / (64 + zoom);

            //paint green effect if the player CAN build there
            if (canBuildThere(y - 1, x, selectedBuildingType)) {
                redVal = 0;
                greenVal = 255;
            } else { //paint red effect if the player can NOT build there
                redVal = 255;
                greenVal = 0;
            }

            //make the effect pulsate once every 2 seconds
            if (currentSeconds % 2 == 0) {
                alpha = (int) Math.ceil(randomDouble * 255);
            } else {
                alpha = 255 - (int) Math.ceil(randomDouble * 255);
            }

            Color color = new Color(redVal, greenVal, 0, alpha);
            gr.setColor(color);

            Image img = tileToImg(selectedBuildingType);

            if (selectedBuildingType == Tile.STADIUM) {
                gr.drawImage(img, x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 64, 128 + zoom, 128 + zoom, null);
                gr.fillRect(x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 64, 128 + zoom, 128 + zoom);
            } else {
                gr.drawImage(img, x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 64, 64 + zoom, 64 + zoom, null);
                gr.fillRect(x * (64 + zoom) + offsetX, y * (64 + zoom) + offsetY - 64, 64 + zoom, 64 + zoom);
            }
        }
    }

    /**
     * Paint the context menu of a tile on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    public void drawSelectedTile(Graphics2D gr) {
        if (selectedTile != null) {
            Field field = fields[selectedTile.x][selectedTile.y];
            if (field instanceof PlayableField && ((PlayableField) field).getBuilding() != null) {

                int cameraOffsetX = game.getCameraOffsetX();
                int cameraOffsetY = game.getCameraOffsetY();

                setRectangleAttributes(selectedTile.y * 64 + cameraOffsetX - 64 * 2, selectedTile.x * 64 + cameraOffsetY - 64 * 3, 64 * 5, 64 * 3);

                gr.setColor(Color.CYAN);
                gr.setStroke(new BasicStroke(5));
                gr.drawRect(selectedTile.y * 64 + cameraOffsetX, selectedTile.x * 64 + cameraOffsetY, 64, 64);

                gr.setColor(Color.WHITE);
                gr.fillRect(selectedTile.y * 64 + cameraOffsetX - 64 * 2, selectedTile.x * 64 + cameraOffsetY - 64 * 3, 64 * 5, 64 * 3);
                gr.setFont(new Font("TimesRoman", Font.PLAIN, 18));

                gr.setColor(Color.BLACK);
                int x = selectedTile.y * 64 + cameraOffsetX - 64 * 2 + 10;
                int y = selectedTile.x * 64 + cameraOffsetY - 64 * 3;

                String stats = ((PlayableField) field).getBuilding().getStatistics();
                String[] statsArray = stats.split("\n");
                for (int i = 1; i <= statsArray.length; i++) {
                    gr.drawString(statsArray[i - 1], x, y + 30 * i);
                }

                if (((PlayableField) field).getZone() != null && ((PlayableField) field).getUpgradeLevel() != UpgradeLevel.METROPOLIS) {
                    setUpgradeButtonAttributes(x, y + 140, 80, 40);
                    upgradeBtn.draw(gr, game.getMousePosition());
                }
            } else {
                setRectangleAttributes(0, 0, 0, 0);
            }
        } else {
            setRectangleAttributes(0, 0, 0, 0);
        }
    }

    /**
     * Sets the attributes of the upgrade button
     *
     * @param x      is the x coordinate of the button
     * @param y      is the y coordinate of the button
     * @param width  is the width of the button
     * @param height is the height of the button
     */
    private void setUpgradeButtonAttributes(int x, int y, int width, int height) {
        upgradeBtn.setX(x);
        upgradeBtn.setY(y);
        upgradeBtn.setWidth(width);
        upgradeBtn.setHeight(height);
    }

    /**
     * Sets the attributes of the rectangle that is used to draw the logical part of the context menu
     *
     * @param x      is the x coordinate of the rectangle
     * @param y      is the y coordinate of the rectangle
     * @param width  is the width of the rectangle
     * @param height is the height of the rectangle
     */
    private void setRectangleAttributes(int x, int y, int width, int height) {
        drawnMenuRectangle.x = x;
        drawnMenuRectangle.y = y;
        drawnMenuRectangle.width = width;
        drawnMenuRectangle.height = height;
    }

    /**
     * Set the selected building type after the user
     * clicked on a building construction button
     *
     * @param selectedBuildingType is the new selected building type
     */
    public void setSelectedBuildingType(Tile selectedBuildingType) {
        this.selectedBuildingType = selectedBuildingType;
    }

    /**
     * Given a point on the screen, return the corresponding field on the map
     *
     * @param p is the current cursor position
     * @return a point
     */
    public Point pointToXY(Point p) {
        if (submenuHovered(p) || p == null) return null;

        int offsetX = game.getCameraOffsetX();
        int offsetY = game.getCameraOffsetY();
        int zoom = game.getZoom();
        int x = (p.x - offsetX) / (64 + zoom);
        int y = (p.y - offsetY) / (64 + zoom);

        return new Point(y, x);
    }

    /**
     * Check if the mouse is hovering over a submenu
     *
     * @param p is the current cursor position
     * @return true if the mouse is hovering over a submenu
     */
    private boolean submenuHovered(Point p) {
        for (Rectangle area : game.getMenuAreas()) {
            if (area.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private Image tileToImg(Tile tile) {
        Image img = null;
        switch (tile) {
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
            case STADIUM_BOTTOMLEFT -> img = stadium_bottomleft;
            case STADIUM_BOTTOMRIGHT -> img = stadium_bottomright;
            case STADIUM_TOPLEFT -> img = stadium_topleft;
            case STADIUM_TOPRIGHT -> img = stadium_topright;
            case FOREST -> img = forest;
            case POLICE -> img = police;
            case CONSTRUCTION -> img = construction;
        }
        return img;
    }
}