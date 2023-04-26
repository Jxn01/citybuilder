package model.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import controller.GameManager;
import model.Coordinate;
import model.buildings.*;
import model.buildings.generated.*;
import model.buildings.playerbuilt.*;
import model.enums.UpgradeLevel;
import model.enums.Zone;
import util.Logger;
import view.enums.Tile;

/**
 * This class represents a field on the map
 */

@JsonTypeName("playable")
public class PlayableField extends Field {
    private int maxCapacity;
    private int moveInFactor;
    private Zone zone;
    private UpgradeLevel upgradeLevel;
    private Building building;

    /**
     * Constructor of the field
     * @param coord is the coordinate of the field
     */
    public PlayableField(Coordinate coord) {
        super(coord);
    }

    @JsonCreator
    public PlayableField(@JsonProperty("coord") Coordinate coord, @JsonProperty("tile") Tile tile, @JsonProperty("maxCapacity") int maxCapacity, @JsonProperty("moveInFactor") int moveInFactor, @JsonProperty("zone") Zone zone, @JsonProperty("upgradeLevel") UpgradeLevel upgradeLevel, @JsonProperty("building") Building building) {
        super(coord, tile);
        this.maxCapacity = maxCapacity;
        this.moveInFactor = moveInFactor;
        this.zone = zone;
        this.upgradeLevel = upgradeLevel;
        this.building = building;
    }

    /**
     * Builds a building on the field
     * @param buildingType is the type of the building
     * @throws RuntimeException if there is already a building on the field, or if the building type is not specified
     */
    public void buildBuilding(Tile buildingType) throws RuntimeException {
        if(building != null) {

            Logger.log("Field at " + coord + " has a building on it, can't build!");
            throw new RuntimeException("There already is a building on this field!");

        } else if(buildingType == null) { //generatedbuilding
            switch(zone) {
                case RESIDENTIAL_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to ResidentialBuilding!");

                    building = new ResidentialBuilding(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);

                    switch(upgradeLevel) {
                        case TOWN -> tile = Tile.HOUSE_1;
                        case CITY -> tile = Tile.HOUSE_2;
                        case METROPOLIS -> tile = Tile.HOUSE_3;
                    }
                }

                case INDUSTRIAL_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to IndustrialWorkplace!");

                    building = new IndustrialWorkplace(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);

                    switch(upgradeLevel) {
                        case TOWN -> tile = Tile.FACTORY_1;
                        case CITY -> tile = Tile.FACTORY_2;
                        case METROPOLIS -> tile = Tile.FACTORY_3;
                    }
                }

                case SERVICE_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to ServiceWorkplace!");

                    building = new ServiceWorkplace(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);

                    switch(upgradeLevel) {
                        case TOWN -> tile = Tile.SERVICE_1;
                        case CITY -> tile = Tile.SERVICE_2;
                        case METROPOLIS -> tile = Tile.SERVICE_3;
                    }
                }

                default -> {
                    Logger.log("Field at " + coord + " has no zone!");
                    throw new RuntimeException("Building type not specified! (Zone is null)");
                }
            }
            GameManager.getGraph().addNode(this.coord);

        } else if(zone == null) { //playerbuilt
            switch (buildingType) {
                case POLICE -> {
                    Logger.log("Building of field at " + coord + " set to PoliceStation!");

                    building = new PoliceStation(coord);
                    GameManager.getGameData().subtractFromBudget(((PoliceStation)building).getBuildCost());
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    GameManager.getGraph().addNode(this.coord);
                }

                case FIRESTATION -> {
                    Logger.log("Building of field at " + coord + " set to FireDepartment!");

                    building = new FireDepartment(coord);
                    GameManager.getGameData().subtractFromBudget(((FireDepartment)building).getBuildCost());
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    GameManager.getGraph().addNode(this.coord);
                }

                case STADIUM -> {
                    Logger.log("Building of field at " + coord + " set to Stadium!");

                    Field[][] fields = GameManager.getGameData().getFields();
                    int x = coord.getX();
                    int y = coord.getY();

                    if(isFieldEmpty(x, y) && isFieldEmpty(x, y+1) && isFieldEmpty(x+1, y) && isFieldEmpty(x+1, y+1)) {
                        Logger.log("Building of field at " + coord + " set to Stadium!");

                        Stadium st = new Stadium(coord, new Coordinate(x, y+1), new Coordinate(x+1, y), new Coordinate(x+1, y+1));

                        buildStadium(x, y, Tile.STADIUM_TOPLEFT, st);
                        buildStadium(x, y+1, Tile.STADIUM_TOPRIGHT, st);
                        buildStadium(x+1, y, Tile.STADIUM_BOTTOMLEFT, st);
                        buildStadium(x+1, y+1, Tile.STADIUM_BOTTOMRIGHT, st);

                        Logger.log("Current budget: " + GameManager.getGameData().getBudget());
                    } else {
                        Logger.log("Can't build stadium at " + coord + "!");
                        throw new RuntimeException("Can't build stadium at " + coord + "!");
                    }
                }

                case FOREST -> {
                    Logger.log("Building of field at " + coord + " set to Forest!");

                    building = new Forest(coord);
                    GameManager.getGameData().subtractFromBudget(((Forest)building).getBuildCost());
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    GameManager.getGraph().addNode(this.coord);
                }

                case ROAD -> {
                    Logger.log("Building of field at " + coord + " set to Road!");

                    building = new Road(coord);
                    GameManager.getGameData().subtractFromBudget(((Road)building).getBuildCost());
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    GameManager.getGraph().addNode(this.coord);
                }

                default -> {
                    Logger.log("Can't set building of field at " + coord + ", unrecognized building type!");
                    throw new RuntimeException("Unrecognized building type!");
                }
            }
            if(buildingType != Tile.STADIUM) tile = buildingType;
        }
    }

    /**
     * Builds a stadium-part on the field
     * @param x is the x coordinate of the field
     * @param y is the y coordinate of the field
     * @param tile is the tile of the stadium
     */
    private static void buildStadium(int x, int y, Tile tile, Stadium stadium) {
        Field[][] fs = GameManager.getGameData().getFields();
        if(isFieldEmpty(x, y)) {
            ((PlayableField)fs[x][y]).setBuilding(stadium);
            (fs[x][y]).setTile(tile);
            GameManager.getGameData().subtractFromBudget(stadium.getBuildCost());
            GameManager.getGraph().addNode(new Coordinate(x, y));
        }
    }

    /**
     * Marks the zone of the field
     * @param newZone is the new zone of the field
     * @throws NullPointerException if the field has a zone already
     */
    public void markZone(Zone newZone) throws NullPointerException{
        if(building != null) {

            Logger.log("Field at " + coord + " has a building on it, can't mark zone!");
            throw new RuntimeException("Can't mark zone, there is a building on the field!");

        } else if(this.zone != null) {

            Logger.log("Field at " + coord + " has a zone already!");
            throw new NullPointerException("Field has a zone already!");

        } else {
            Logger.log("Field's zone at " + coord + " marked to " + zone+"!");
            Logger.log("Current budget: " + GameManager.getGameData().getBudget());

            this.zone = newZone;
            upgradeLevel = UpgradeLevel.TOWN;
            maxCapacity = 20;
            GameManager.getGameData().subtractFromBudget(100); // exact amount is todo

            switch(zone) {
                case RESIDENTIAL_ZONE -> tile = Tile.RESIDENTIALZONE;
                case INDUSTRIAL_ZONE -> tile = Tile.FACTORYZONE;
                case SERVICE_ZONE -> tile = Tile.SERVICEZONE;
            }
        }
    }

    /**
     * Demolishes the building of the field
     * @throws RuntimeException if there is no building on the field or if the building is public
     */
    public void demolishBuilding() throws RuntimeException {
        if(building == null) {

            Logger.log("Field at " + coord + " has no building on it, can't demolish!");
            throw new RuntimeException("There is no building on the field!");

        } else if(zone != null) {

            Logger.log("Field at " + coord + " has a zone on it, can't demolish!");
            throw new RuntimeException("Can't demolish public buildings! (There is a zone on the field!)");

        } else if(building instanceof Stadium) {
            int x = building.getCoords().getX();
            int y = building.getCoords().getY();
            Field[][] fs = GameManager.getGameData().getFields();
            // 1.: starting tile is top left corner
            // 2.: starting tile is bottom left corner
            // 3.: starting tile is top right corner
            // 4.: starting tile is bottom right corner
            if(isStadiumValid(x, y, x, y+1, x+1, y, x+1, y+1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y+1);
                PlayableField.demolishStadiumAt(x+1, y);
                PlayableField.demolishStadiumAt(x+1, y+1);

                Logger.log("Current budget: " + GameManager.getGameData().getBudget());
            } else if(isStadiumValid(x, y, x, y+1, x-1, y, x-1, y+1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y+1);
                PlayableField.demolishStadiumAt(x-1, y);
                PlayableField.demolishStadiumAt(x-1, y+1);

                Logger.log("Current budget: " + GameManager.getGameData().getBudget());
            } else if(isStadiumValid(x, y, x, y-1, x+1, y, x+1, y-1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y-1);
                PlayableField.demolishStadiumAt(x+1, y);
                PlayableField.demolishStadiumAt(x+1, y-1);

                Logger.log("Current budget: " + GameManager.getGameData().getBudget());
            } else if(isStadiumValid(x, y, x, y-1, x-1, y, x-1, y-1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y-1);
                PlayableField.demolishStadiumAt(x-1, y);
                PlayableField.demolishStadiumAt(x-1, y-1);

                Logger.log("Current budget: " + GameManager.getGameData().getBudget());
            } else {
                Logger.log("Stadium at " + coord + " can't be demolished!");
                throw new RuntimeException("Stadium can't be demolished!");
            }

        } else {
            Logger.log("Building of field at " + coord + " demolished!");

            GameManager.getGameData().addToBudget(((PlayerBuilding)building).getBuildCost());
            Logger.log("Current budget: " + GameManager.getGameData().getBudget());

            building = null;

            resetTile();
            GameManager.getGraph().removeNode(this.coord);
        }
    }

    /**
     * Demolishes the stadium at the given coordinates
     * @param x x coordinate of the stadium
     * @param y y coordinate of the stadium
     */
    private static void demolishStadiumAt(int x, int y) {
        Field[][] fs = GameManager.getGameData().getFields();
        if(isFieldValid(x, y) && ((PlayableField)fs[x][y]).getBuilding() instanceof Stadium) {
            Building building = ((PlayableField)fs[x][y]).getBuilding();
            GameManager.getGameData().addToBudget(((Stadium)building).getBuildCost());

            ((PlayableField)fs[x][y]).setBuilding(null);
            (fs[x][y]).resetTile();
            GameManager.getGraph().removeNode(new Coordinate(x, y));
        }
    }

    /**
     * Checks if the building can be built at the given coordinates
     * @param x x coordinate of the building
     * @param y y coordinate of the building
     * @param building the building to be built
     * @return true if the building can be built, false otherwise
     */
    public static boolean canBuildThere(int x, int y, Tile building) {
        switch(building) {
            case ROAD -> { return isFieldEmpty(x, y); }
            case STADIUM -> { return isFieldEmpty(x, y) && isFieldEmpty(x, y+1) && isFieldEmpty(x+1, y) && isFieldEmpty(x+1, y+1) && isStadiumNextToRoad(x, y); }
            default -> { return isFieldEmpty(x, y) && isNextToRoad(x, y); }
        }
    }

    /**
     * Checks if the stadium at the given coordinates is valid
     * @param x1 x coordinate of the first stadium-part (top left corner)
     * @param y1 y coordinate of the first stadium-part (top left corner)
     * @param x2 x coordinate of the second stadium-part (top right corner)
     * @param y2 y coordinate of the second stadium-part (top right corner)
     * @param x3 x coordinate of the third stadium-part (bottom left corner)
     * @param y3 y coordinate of the third stadium-part (bottom left corner)
     * @param x4 x coordinate of the fourth stadium-part (bottom right corner)
     * @param y4 y coordinate of the fourth stadium-part (bottom right corner)
     * @return a boolean value if the stadium is valid
     */
    private static boolean isStadiumValid(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        Field[][] fs = GameManager.getGameData().getFields();
        return isFieldValid(x1, y1) && ((PlayableField)fs[x1][y1]).getBuilding() instanceof Stadium
                && isFieldValid(x2, y2) && ((PlayableField)fs[x2][y2]).getBuilding() instanceof Stadium
                && isFieldValid(x3, y3) && ((PlayableField)fs[x3][y3]).getBuilding() instanceof Stadium
                && isFieldValid(x4, y4) && ((PlayableField)fs[x4][y4]).getBuilding() instanceof Stadium;
    }

    /**
     * Check if a field is valid on the map
     * @param x is the x index of the field
     * @param y is the y index of the field
     * @return a boolean value
     */
    private static boolean isFieldValid(int x, int y) {
        Field[][] fields = GameManager.getGameData().getFields();
        return fields[x][y] instanceof PlayableField;
    }

    /**
     * Check if a field is empty on the map
     * @param x is the x index of the field
     * @param y is the y index of the field
     * @return is a boolean value
     */
    private static boolean isFieldEmpty(int x, int y) {
        Field[][] fields = GameManager.getGameData().getFields();
        return fields[x][y] instanceof PlayableField && ((PlayableField) fields[x][y]).getBuilding() == null && ((PlayableField) fields[x][y]).getZone() == null;
    }

    /**
     * Check if a field is next to a road
     * @param y is the y index of the field
     * @param x is te x index of the field
     * @return is a boolean value
     */
    private static boolean isNextToRoad(int x, int y) {
        Field[][] fields = GameManager.getGameData().getFields();
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

    private static boolean isStadiumNextToRoad(int x, int y) { // x y is the top left tile of the stadium
        if(isFieldValid(x, y)) {
            return isNextToRoad(x, y) || isNextToRoad(x + 1, y) || isNextToRoad(x + 1, y + 1) || isNextToRoad(x, y + 1);
        } else return false;
    }

    /**
     * Deletes the zone of the field
     * @throws RuntimeException if there is a building on the field or if the field is already empty
     */
    public void deleteZone() throws RuntimeException {
        if(building != null) {

            Logger.log("Field at " + coord + " has a building on it, can't delete zone!");
            throw new RuntimeException("Can't delete zone, there is a building on the field!");

        }else if(zone == null) {

            Logger.log("Field at " + coord + " has no zone!");
            throw new RuntimeException("Field is already empty!");

        } else {
            Logger.log("Field's zone at " + coord + " deleted!");
            Logger.log("Current budget: " + GameManager.getGameData().getBudget());

            GameManager.getGameData().addToBudget(100); //exact amount is TODO
            zone = null;

            resetTile();
        }
    }

    /**
     * Upgrades the field
     * @throws RuntimeException if the field can't be upgraded or if the field has no zone
     */
    public void upgrade() throws RuntimeException {
        if(upgradeLevel == null) {

            Logger.log("Field at " + coord + " can't be upgraded!");
            throw new NullPointerException("Field can't be upgraded!");

        } else if(zone == null) {

            Logger.log("Field at " + coord + " has no zone!");
            throw new NullPointerException("Field has no zone!");

        } else {
            switch (upgradeLevel) {
                case TOWN -> {
                    Logger.log("Field at " + coord + " upgraded to city level!");
                    Logger.log("Field at " + coord + " has a new max capacity of " + maxCapacity + "!");
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    upgradeLevel = UpgradeLevel.CITY;
                    maxCapacity = 40; //TODO
                    GameManager.getGameData().addToBudget(100); //exact amount is TODO

                    if(building != null) {
                        ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                        switch(zone) {
                            case RESIDENTIAL_ZONE -> tile = Tile.HOUSE_2;
                            case INDUSTRIAL_ZONE -> tile = Tile.FACTORY_2;
                            case SERVICE_ZONE -> tile = Tile.SERVICE_2;
                        }
                    }
                }

                case CITY -> {
                    Logger.log("Field at " + coord + " upgraded to metropolis level!");
                    Logger.log("Field at " + coord + " has a new max capacity of " + maxCapacity + "!");
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    upgradeLevel = UpgradeLevel.METROPOLIS;
                    maxCapacity = 100; //TODO
                    GameManager.getGameData().addToBudget(500); //exact amount is TODO

                    if(building != null){
                        ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                        switch(zone) {
                            case RESIDENTIAL_ZONE -> tile = Tile.HOUSE_3;
                            case INDUSTRIAL_ZONE -> tile = Tile.FACTORY_3;
                            case SERVICE_ZONE -> tile = Tile.SERVICE_3;
                        }
                    }
                }

                case METROPOLIS -> {
                    Logger.log("Field at " + coord + " is already max level!");
                    throw new RuntimeException("UpgradeLevel is already max!");
                }
            }
        }
    }

    /**
     * Gets the current capacity of the field
     * @return the current capacity of the field
     * @throws RuntimeException if there is no building on the field, or if the building is not a generated building
     */
    @JsonIgnore
    public int getCurrentCapacity() throws RuntimeException {
        if(building == null) {

            Logger.log("Field at " + coord + " has no building on it, can't get current capacity!");
            throw new RuntimeException("There is no building on the field!");

        } else if(!(building instanceof GeneratedBuilding)) {

            Logger.log("Field at " + coord + " has a generated building on it, can't get current capacity!");
            throw new RuntimeException("Building is not a generated building!");

        } else {
            Logger.log("Current capacity of field at " + coord + " is " + ((GeneratedBuilding) building).getPeople().size() + "!");
            return ((GeneratedBuilding) building).getPeople().size();
        }
    }

    /**
     * Get the max capacity of the field
     * @return the max capacity of the field
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Set the max capacity of the field
     * @param maxCapacity is the max capacity of the field
     */
    public void setMaxCapacity(int maxCapacity) {
        Logger.log("Max capacity of field at " + coord + " set to " + maxCapacity);
        this.maxCapacity = maxCapacity;
    }

    /**
     * Get the move-in-factor of the field
     * @return the move in factor of the field
     */
    public int getMoveInFactor() {
        return moveInFactor;
    }

    /**
     * Calculate the move-in-factor of the field
     */
    @JsonIgnore
    public int calculateMoveInFactor() {
        int moveInFactor = 0;
        Logger.log("Move in factor of field at " + coord + " is " + moveInFactor);
        return moveInFactor;
    }

    /**
     * Get the zone of the field
     * @return the zone of the field
     */
    public Zone getZone() {
        return zone;
    }

    /**
     * Get the upgrade level of the field
     * @return the upgrade level of the field
     */
    public UpgradeLevel getUpgradeLevel() {
        return upgradeLevel;
    }

    /**
     * Get the building of the field
     * @return the building of the field
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Set the building of the field
     * @param building is the building of the field
     */
    public void setBuilding(Building building) {
        if(building == null) {
            Logger.log("Building of field at " + coord + " set to null");
            this.building = null;
        } else {
            Logger.log("Building of field at " + coord + " set to " + building.getClass().getSimpleName());
            this.building = building;
        }
    }

    /**
     * Get the tile of the field
     * @return the tile of the field
     */
    public void setMoveInFactor(int moveInFactor) {
        this.moveInFactor = moveInFactor;
    }

    /**
     * Get the tile of the field
     * @return the tile of the field
     */
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    /**
     * Get the tile of the field
     * @return the tile of the field
     */
    public void setUpgradeLevel(UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }
}
