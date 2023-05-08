package model.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import controller.GameManager;
import model.Coordinate;
import model.Person;
import model.buildings.Building;
import model.buildings.generated.GeneratedBuilding;
import model.buildings.generated.IndustrialWorkplace;
import model.buildings.generated.ResidentialBuilding;
import model.buildings.generated.ServiceWorkplace;
import model.buildings.playerbuilt.*;
import model.enums.UpgradeLevel;
import model.enums.Zone;
import util.Logger;
import view.enums.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a field on the map
 */

@JsonTypeName("playable")
public class PlayableField extends Field {
    private int moveInFactor;
    private Zone zone;
    private UpgradeLevel upgradeLevel;
    private Building building;

    /**
     * Constructor of the field
     *
     * @param coord is the coordinate of the field
     */
    public PlayableField(Coordinate coord) {
        super(coord);
    }

    @JsonCreator
    public PlayableField(@JsonProperty("coord") Coordinate coord, @JsonProperty("tile") Tile tile, @JsonProperty("moveInFactor") int moveInFactor, @JsonProperty("zone") Zone zone, @JsonProperty("upgradeLevel") UpgradeLevel upgradeLevel, @JsonProperty("building") Building building) {
        super(coord, tile);
        this.moveInFactor = moveInFactor;
        this.zone = zone;
        this.upgradeLevel = upgradeLevel;
        this.building = building;
    }

    /**
     * Builds a stadium-part on the field
     *
     * @param x    is the x coordinate of the field
     * @param y    is the y coordinate of the field
     * @param tile is the tile of the stadium
     */
    private static void buildStadium(int x, int y, Tile tile, Stadium stadium) {
        Field[][] fs = GameManager.getFields();
        if (isFieldEmpty(x, y)) {
            ((PlayableField) fs[x][y]).setBuilding(stadium);
            (fs[x][y]).setTile(tile);
            GameManager.getGameData().subtractFromBudget(GameManager.getStadiumBuildCost());
        }
    }

    /**
     * Demolishes the stadium at the given coordinates
     *
     * @param x x coordinate of the stadium
     * @param y y coordinate of the stadium
     */
    private static void demolishStadiumAt(int x, int y) {
        Field[][] fs = GameManager.getGameData().getFields();
        if (isFieldValid(x, y) && ((PlayableField) fs[x][y]).getBuilding() instanceof Stadium) {
            Building building = ((PlayableField) fs[x][y]).getBuilding();

            ((PlayableField) fs[x][y]).setBuilding(null);
            (fs[x][y]).resetTile();
            GameManager.getGraph().removeNode(new Coordinate(x, y));
        }
    }

    /**
     * Checks if the building can be built at the given coordinates
     *
     * @param x        x coordinate of the building
     * @param y        y coordinate of the building
     * @param building the building to be built
     * @return true if the building can be built, false otherwise
     */
    public static boolean canBuildThere(int x, int y, Tile building) {
        switch (building) {
            case ROAD -> {
                int partGraphsCount = GameManager.countDisconnectedGraphs(GameManager.getGraph());
                if (partGraphsCount == 0) {
                    return isFieldEmpty(x, y);
                } else {
                    return isFieldEmpty(x, y) && isNextToRoad(x, y);
                }
            }
            case STADIUM -> {
                return isFieldEmpty(x, y) && isFieldEmpty(x, y + 1) && isFieldEmpty(x + 1, y) && isFieldEmpty(x + 1, y + 1) && isStadiumNextToRoad(x, y);
            }
            default -> {
                return isFieldEmpty(x, y) && isNextToRoad(x, y);
            }
        }
    }

    /**
     * Checks if the stadium at the given coordinates is valid
     *
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
        return isFieldValid(x1, y1) && ((PlayableField) fs[x1][y1]).getBuilding() instanceof Stadium
                && isFieldValid(x2, y2) && ((PlayableField) fs[x2][y2]).getBuilding() instanceof Stadium
                && isFieldValid(x3, y3) && ((PlayableField) fs[x3][y3]).getBuilding() instanceof Stadium
                && isFieldValid(x4, y4) && ((PlayableField) fs[x4][y4]).getBuilding() instanceof Stadium;
    }

    /**
     * Check if a field is valid on the map
     *
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
     *
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
     *
     * @param y is the y index of the field
     * @param x is te x index of the field
     * @return is a boolean value
     */
    private static boolean isNextToRoad(int x, int y) {
        Field[][] fields = GameManager.getGameData().getFields();
        if (isFieldValid(x, y)) {
            return isFieldValid(x - 1, y) && ((PlayableField) fields[x - 1][y]).getBuilding() instanceof Road
                    || isFieldValid(x + 1, y) && ((PlayableField) fields[x + 1][y]).getBuilding() instanceof Road
                    || isFieldValid(x, y - 1) && ((PlayableField) fields[x][y - 1]).getBuilding() instanceof Road
                    || isFieldValid(x, y + 1) && ((PlayableField) fields[x][y + 1]).getBuilding() instanceof Road;
        } else return false;
        //balra, jobbra, alatta, felette
    }

    /**
     * Check if a stadium is next to a road
     *
     * @param x is the x index of the field
     * @param y is the y index of the field
     * @return is a boolean value
     */

    private static boolean isStadiumNextToRoad(int x, int y) { // x y is the top left tile of the stadium
        if (isFieldValid(x, y)) {
            return isNextToRoad(x, y) || isNextToRoad(x + 1, y) || isNextToRoad(x + 1, y + 1) || isNextToRoad(x, y + 1);
        } else return false;
    }

    /**
     * Builds a building on the field
     *
     * @param buildingType is the type of the building
     * @throws RuntimeException if there is already a building on the field, or if the building type is not specified
     */
    @CanIgnoreReturnValue
    public Building buildBuilding(Tile buildingType) throws RuntimeException {
        if (building != null) {

            Logger.log("Field at " + coord + " has a building on it, can't build!");
            throw new RuntimeException("There already is a building on this field!");

        } else if (buildingType == null) { //generatedbuilding
            switch (zone) {
                case RESIDENTIAL_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to ResidentialBuilding!");

                    building = new ResidentialBuilding(coord);

                    switch (upgradeLevel) {
                        case TOWN -> {
                            tile = Tile.HOUSE_1;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelOneMaxCapacity());
                        }
                        case CITY -> {
                            tile = Tile.HOUSE_2;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelTwoMaxCapacity());
                        }
                        case METROPOLIS -> {
                            tile = Tile.HOUSE_3;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelThreeMaxCapacity());
                        }
                    }
                }

                case INDUSTRIAL_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to IndustrialWorkplace!");

                    building = new IndustrialWorkplace(coord);

                    switch (upgradeLevel) {
                        case TOWN -> {
                            tile = Tile.FACTORY_1;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelOneMaxCapacity());
                        }
                        case CITY -> {
                            tile = Tile.FACTORY_2;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelTwoMaxCapacity());
                        }
                        case METROPOLIS -> {
                            tile = Tile.FACTORY_3;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelThreeMaxCapacity());
                        }
                    }
                }

                case SERVICE_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to ServiceWorkplace!");

                    building = new ServiceWorkplace(coord);

                    switch (upgradeLevel) {
                        case TOWN -> {
                            tile = Tile.SERVICE_1;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelOneMaxCapacity());
                        }
                        case CITY -> {
                            tile = Tile.SERVICE_2;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelTwoMaxCapacity());
                        }
                        case METROPOLIS -> {
                            tile = Tile.SERVICE_3;
                            ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelThreeMaxCapacity());
                        }
                    }
                }

                default -> {
                    Logger.log("Field at " + coord + " has no zone!");
                    throw new RuntimeException("Building type not specified! (Zone is null)");
                }
            }
            addToGraph(coord);

        } else if (zone == null) { //playerbuilt
            switch (buildingType) {
                case POLICE -> {
                    Logger.log("Building of field at " + coord + " set to PoliceStation!");

                    building = new PoliceStation(coord);
                    GameManager.subtractFromBudget(building.getBuildCost());
                    Logger.log("Current budget: " + GameManager.getBudget());

                    addToGraph(coord);
                }

                case FIRESTATION -> {
                    Logger.log("Building of field at " + coord + " set to FireDepartment!");

                    building = new FireDepartment(coord);
                    GameManager.getGameData().subtractFromBudget(building.getBuildCost());
                    Logger.log("Current budget: " + GameManager.getBudget());

                    addToGraph(coord);
                }

                case STADIUM -> {
                    Logger.log("Building of field at " + coord + " set to Stadium!");

                    int x = coord.getX();
                    int y = coord.getY();

                    if (isFieldEmpty(x, y) && isFieldEmpty(x, y + 1) && isFieldEmpty(x + 1, y) && isFieldEmpty(x + 1, y + 1)) {
                        Logger.log("Building of field at " + coord + " set to Stadium!");

                        Stadium st = new Stadium(coord, new Coordinate(x, y + 1), new Coordinate(x + 1, y), new Coordinate(x + 1, y + 1));

                        addToGraph(coord);

                        buildStadium(x, y, Tile.STADIUM_TOPLEFT, st);
                        buildStadium(x, y + 1, Tile.STADIUM_TOPRIGHT, st);
                        buildStadium(x + 1, y, Tile.STADIUM_BOTTOMLEFT, st);
                        buildStadium(x + 1, y + 1, Tile.STADIUM_BOTTOMRIGHT, st);

                        Logger.log("Current budget: " + GameManager.getBudget());
                    } else {
                        Logger.log("Can't build stadium at " + coord + "!");
                        throw new RuntimeException("Can't build stadium at " + coord + "!");
                    }
                }

                case FOREST -> {
                    Logger.log("Building of field at " + coord + " set to Forest!");

                    building = new Forest(coord);
                    GameManager.getGameData().subtractFromBudget(building.getBuildCost());
                    Logger.log("Current budget: " + GameManager.getBudget());

                    addToGraph(coord);
                }

                case ROAD -> {
                    Logger.log("Building of field at " + coord + " set to Road!");

                    building = new Road(coord);
                    GameManager.getGameData().subtractFromBudget(building.getBuildCost());
                    Logger.log("Current budget: " + GameManager.getBudget());

                    addToGraph(coord);
                }

                default -> {
                    Logger.log("Can't set building of field at " + coord + ", unrecognized building type!");
                    throw new RuntimeException("Unrecognized building type!");
                }
            }
            if (buildingType != Tile.STADIUM) tile = buildingType;
        }
        return building;
    }

    /**
     * Adds the coordinate (the field) to the graph and adds an edge to a road.
     *
     * @param coord is the coordinate of the field
     */
    private void addToGraph(Coordinate coord) {
        MutableGraph<Coordinate> graph = GameManager.getGraph();
        Field[][] fields = GameManager.getFields();
        int x = coord.getX();
        int y = coord.getY();

        if (((PlayableField) fields[x][y]).getBuilding() instanceof Road) {
            graph.addNode(coord);
            if (isFieldValid(x, y - 1) && ((PlayableField) fields[x][y - 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x, y - 1));
            }
            if (isFieldValid(x, y + 1) && ((PlayableField) fields[x][y + 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x, y + 1));
            }
            if (isFieldValid(x - 1, y) && ((PlayableField) fields[x - 1][y]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x - 1, y));
            }
            if (isFieldValid(x + 1, y) && ((PlayableField) fields[x + 1][y]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x + 1, y));
            }
        } else if (((PlayableField) fields[x][y]).getBuilding() instanceof Stadium) {

            if (isFieldValid(x - 1, y) && ((PlayableField) fields[x - 1][y]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x - 1, y));
            } else if (isFieldValid(x - 1, y + 1) && ((PlayableField) fields[x - 1][y + 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x - 1, y + 1));
            } else if (isFieldValid(x, y - 1) && ((PlayableField) fields[x][y - 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x, y - 1));
            } else if (isFieldValid(x, y + 2) && ((PlayableField) fields[x][y + 2]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x, y + 2));
            } else if (isFieldValid(x + 1, y - 1) && ((PlayableField) fields[x + 1][y - 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x + 1, y - 1));
            } else if (isFieldValid(x + 1, y + 2) && ((PlayableField) fields[x + 1][y + 2]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x + 1, y + 2));
            } else if (isFieldValid(x + 2, y) && ((PlayableField) fields[x + 2][y]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x + 2, y));
            } else if (isFieldValid(x + 2, y + 1) && ((PlayableField) fields[x + 2][y + 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x + 2, y + 1));
            }

        } else { // if the field is not a road, we only make an edge to one road (to prevent making circles with buildings)
            if (isFieldValid(x, y - 1) && ((PlayableField) fields[x][y - 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x, y - 1));
            } else if (isFieldValid(x, y + 1) && ((PlayableField) fields[x][y + 1]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x, y + 1));
            } else if (isFieldValid(x - 1, y) && ((PlayableField) fields[x - 1][y]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x - 1, y));
            } else if (isFieldValid(x + 1, y) && ((PlayableField) fields[x + 1][y]).getBuilding() instanceof Road) {
                graph.putEdge(coord, new Coordinate(x + 1, y));
            }
        }
    }

    /**
     * Marks the zone of the field
     *
     * @param newZone is the new zone of the field
     * @throws NullPointerException if the field has a zone already
     */
    public void markZone(Zone newZone) throws NullPointerException {
        if (building != null) {

            Logger.log("Field at " + coord + " has a building on it, can't mark zone!");
            throw new RuntimeException("Can't mark zone, there is a building on the field!");

        } else if (this.zone != null) {

            Logger.log("Field at " + coord + " has a zone already!");
            throw new NullPointerException("Field has a zone already!");

        } else {
            Logger.log("Field's zone at " + coord + " marked to " + newZone + "!");
            Logger.log("Current budget: " + GameManager.getBudget());

            this.zone = newZone;
            upgradeLevel = UpgradeLevel.TOWN;

            switch (zone) {
                case RESIDENTIAL_ZONE -> {
                    tile = Tile.RESIDENTIALZONE;
                    GameManager.subtractFromBudget(GameManager.getMarkResidentialCost());
                }
                case INDUSTRIAL_ZONE -> {
                    tile = Tile.FACTORYZONE;
                    GameManager.subtractFromBudget(GameManager.getMarkIndustrialCost());
                }
                case SERVICE_ZONE -> {
                    tile = Tile.SERVICEZONE;
                    GameManager.subtractFromBudget(GameManager.getMarkServiceCost());
                }
            }
        }
    }

    /**
     * Demolishes the building of the field
     *
     * @throws RuntimeException if there is no building on the field or if the building is public
     */
    public void demolishBuilding(boolean burntDown) throws RuntimeException {
        if (building == null) {

            Logger.log("Field at " + coord + " has no building on it, can't demolish!");
            throw new RuntimeException("There is no building on the field!");

        } else if (zone != null) {

            Logger.log("Field at " + coord + " has a zone on it, can't demolish!");
            throw new RuntimeException("Can't demolish public buildings! (There is a zone on the field!)");

        } else if (building instanceof Stadium) {
            int x = building.getX();
            int y = building.getY();
            // 1.: starting tile is top left corner
            // 2.: starting tile is bottom left corner
            // 3.: starting tile is top right corner
            // 4.: starting tile is bottom right corner
            if (isStadiumValid(x, y, x, y + 1, x + 1, y, x + 1, y + 1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y + 1);
                PlayableField.demolishStadiumAt(x + 1, y);
                PlayableField.demolishStadiumAt(x + 1, y + 1);

                if(!burntDown)
                    GameManager.addToBudget(building.getBuildCost());

                Logger.log("Current budget: " + GameManager.getBudget());
            } else if (isStadiumValid(x, y, x, y + 1, x - 1, y, x - 1, y + 1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y + 1);
                PlayableField.demolishStadiumAt(x - 1, y);
                PlayableField.demolishStadiumAt(x - 1, y + 1);

                if(!burntDown)
                    GameManager.addToBudget(building.getBuildCost());

                Logger.log("Current budget: " + GameManager.getBudget());
            } else if (isStadiumValid(x, y, x, y - 1, x + 1, y, x + 1, y - 1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y - 1);
                PlayableField.demolishStadiumAt(x + 1, y);
                PlayableField.demolishStadiumAt(x + 1, y - 1);

                if(!burntDown)
                    GameManager.addToBudget(building.getBuildCost());

                Logger.log("Current budget: " + GameManager.getBudget());
            } else if (isStadiumValid(x, y, x, y - 1, x - 1, y, x - 1, y - 1)) {
                Logger.log("Stadium at " + coord + " demolished!");

                PlayableField.demolishStadiumAt(x, y);
                PlayableField.demolishStadiumAt(x, y - 1);
                PlayableField.demolishStadiumAt(x - 1, y);
                PlayableField.demolishStadiumAt(x - 1, y - 1);

                if(!burntDown)
                    GameManager.addToBudget(building.getBuildCost());

                Logger.log("Current budget: " + GameManager.getBudget());
            } else {
                Logger.log("Stadium at " + coord + " can't be demolished!");
                throw new RuntimeException("Stadium can't be demolished!");
            }

        } else if (building instanceof Road) {
            MutableGraph<Coordinate> graph = GameManager.getGraph();
            var edges = graph.edges();

            if (graph.nodes().size() == 1) {
                graph.removeNode(coord);
            } else {
                MutableGraph<Coordinate> testGraph = GraphBuilder.undirected().allowsSelfLoops(false).build();
                for (var edge : edges) {
                    testGraph.putEdge(edge.nodeU(), edge.nodeV());
                }

                int dPartGraphsBefore = GameManager.countDisconnectedGraphs(testGraph);
                testGraph.removeNode(coord);
                int dPartGraphsAfter = GameManager.countDisconnectedGraphs(testGraph);
                if (dPartGraphsAfter <= dPartGraphsBefore) {
                    int x = building.getX();
                    int y = building.getY();
                    if (isFieldValid(x, y + 1) && ((PlayableField) GameManager.getFields()[x][y + 1]).getBuilding() != null && !(((PlayableField) GameManager.getFields()[x][y + 1]).getBuilding() instanceof Road) ||
                            isFieldValid(x, y - 1) && ((PlayableField) GameManager.getFields()[x][y - 1]).getBuilding() != null && !(((PlayableField) GameManager.getFields()[x][y - 1]).getBuilding() instanceof Road) ||
                            isFieldValid(x + 1, y) && ((PlayableField) GameManager.getFields()[x + 1][y]).getBuilding() != null && !(((PlayableField) GameManager.getFields()[x + 1][y]).getBuilding() instanceof Road) ||
                            isFieldValid(x - 1, y) && ((PlayableField) GameManager.getFields()[x - 1][y]).getBuilding() != null && !(((PlayableField) GameManager.getFields()[x - 1][y]).getBuilding() instanceof Road)) {

                        Logger.log("Road at " + coord + " can't be demolished, it would make a building unconnected!");
                        throw new RuntimeException("Road can't be demolished, it would make a building unconnected!");

                    } else {
                        Logger.log("Road at " + coord + " demolished!");

                        if(!burntDown)
                            GameManager.addToBudget(building.getBuildCost());
                        Logger.log("Current budget: " + GameManager.getBudget());

                        graph.removeNode(coord);
                        building = null;
                        resetTile();
                    }

                } else {
                    Logger.log("Road at " + coord + " can't be demolished, it would make the graph disconnected!");
                    throw new RuntimeException("Road can't be demolished, it would make the graph disconnected!");
                }
            }
        } else {
            Logger.log("Building of field at " + coord + " demolished!");

            if(!burntDown)
                GameManager.addToBudget(building.getBuildCost());
            Logger.log("Current budget: " + GameManager.getBudget());

            building = null;

            resetTile();
            GameManager.getGraph().removeNode(this.coord);
        }
        Logger.log("State of the graph: " + GameManager.getGraph());
    }

    public void demolishGeneratedBuilding(boolean burntDown) {
        if (building == null) {

            Logger.log("Field at " + coord + " has no building on it, can't demolish!");
            throw new RuntimeException("There is no building on the field!");
        } else {
            Logger.log("Building of field at " + coord + " demolished!");

            List<Person> people = new ArrayList<>(((GeneratedBuilding) this.getBuilding()).getPeople());
            if(burntDown){
                people.forEach(Person::decease);
            } else {
                if(this.getBuilding() instanceof ResidentialBuilding){
                    people.forEach(Person::evict);
                } else {
                    people.forEach(Person::fire);
                }
            }

            Logger.log("Current budget: " + GameManager.getBudget());

            building = null;
            zone = null;

            resetTile();
            GameManager.getGraph().removeNode(this.coord);
        }
        Logger.log("State of the graph: " + GameManager.getGraph());
    }

    /**
     * Deletes the zone of the field
     *
     * @throws RuntimeException if there is a building on the field or if the field is already empty
     */
    public void deleteZone() throws RuntimeException {
        if (building != null) {

            Logger.log("Field at " + coord + " has a building on it, can't delete zone!");
            throw new RuntimeException("Can't delete zone, there is a building on the field!");

        } else if (zone == null) {

            Logger.log("Field at " + coord + " has no zone!");
            throw new RuntimeException("Field is already empty!");

        } else {
            Logger.log("Field's zone at " + coord + " deleted!");
            Logger.log("Current budget: " + GameManager.getGameData().getBudget());

            GameManager.addToBudget(100); //exact amount is TODO
            zone = null;

            resetTile();
        }
    }

    /**
     * Upgrades the field
     *
     * @throws RuntimeException if the field can't be upgraded or if the field has no zone
     */
    public void upgrade() throws RuntimeException {
        if (upgradeLevel == null) {

            Logger.log("Field at " + coord + " can't be upgraded!");
            throw new NullPointerException("Field can't be upgraded!");

        } else if (zone == null) {

            Logger.log("Field at " + coord + " has no zone!");
            throw new NullPointerException("Field has no zone!");

        } else {
            switch (upgradeLevel) {
                case TOWN -> {
                    Logger.log("Field at " + coord + " upgraded to city level!");
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    upgradeLevel = UpgradeLevel.CITY;
                    GameManager.subtractFromBudget(GameManager.getLevelTwoUpgradeCost());

                    if (building != null) {
                        ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelTwoMaxCapacity());
                        switch (zone) {
                            case RESIDENTIAL_ZONE -> tile = Tile.HOUSE_2;
                            case INDUSTRIAL_ZONE -> tile = Tile.FACTORY_2;
                            case SERVICE_ZONE -> tile = Tile.SERVICE_2;
                        }
                    }
                }

                case CITY -> {
                    Logger.log("Field at " + coord + " upgraded to metropolis level!");
                    Logger.log("Current budget: " + GameManager.getGameData().getBudget());

                    upgradeLevel = UpgradeLevel.METROPOLIS;
                    GameManager.subtractFromBudget(GameManager.getLevelThreeUpgradeCost());

                    if (building != null) {
                        ((GeneratedBuilding) building).setMaxCapacity(GameManager.getLevelThreeMaxCapacity());
                        switch (zone) {
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
     *
     * @return the current capacity of the field
     * @throws RuntimeException if there is no building on the field, or if the building is not a generated building
     */
    @JsonIgnore
    public int getCurrentCapacity() throws RuntimeException {
        if (building == null) {

            Logger.log("Field at " + coord + " has no building on it, can't get current capacity!");
            throw new RuntimeException("There is no building on the field!");

        } else if (!(building instanceof GeneratedBuilding)) {

            Logger.log("Field at " + coord + " has a generated building on it, can't get current capacity!");
            throw new RuntimeException("Building is not a generated building!");

        } else {
            Logger.log("Current capacity of field at " + coord + " is " + ((GeneratedBuilding) building).getPeople().size() + "!");
            return ((GeneratedBuilding) building).getPeople().size();
        }
    }

    /**
     * Get the move-in-factor of the field
     *
     * @return the move in factor of the field
     */
    public int getMoveInFactor() {
        return moveInFactor;
    }

    /**
     * Set the move in factor of the field
     *
     * @param moveInFactor is the move in factor of the field
     */
    public void setMoveInFactor(int moveInFactor) {
        this.moveInFactor = moveInFactor;
    }

    /**
     * Calculate the move-in-factor of the field
     */
    @JsonIgnore
    public int calculateMoveInFactor() {
        int moveInFactor = 1;
        int policeRange = GameManager.getPoliceRange();
        int stadiumRange = GameManager.getStadiumRange();
        int forestRange = GameManager.getForestRange();
        int industrialRange = GameManager.getIndustrialRange();

        boolean hasPolice = Arrays.stream(GameManager.getFields())
                .flatMap(Arrays::stream)
                .filter(field -> field instanceof PlayableField)
                .map(field -> (PlayableField) field)
                .filter(field -> field.getBuilding() instanceof PoliceStation)
                .anyMatch(field -> calculateDistance(field.getCoord(), this.getCoord()) <= policeRange);

        boolean hasStadium = Arrays.stream(GameManager.getFields())
                .flatMap(Arrays::stream)
                .filter(field -> field instanceof PlayableField)
                .map(field -> (PlayableField) field)
                .filter(field -> field.getBuilding() instanceof Stadium)
                .anyMatch(field -> calculateDistance(field.getCoord(), this.getCoord()) <= stadiumRange);

        boolean hasForest = Arrays.stream(GameManager.getFields())
                .flatMap(Arrays::stream)
                .filter(field -> field instanceof PlayableField)
                .map(field -> (PlayableField) field)
                .filter(field -> field.getBuilding() instanceof Forest)
                .map(field -> (Forest) field.getBuilding())
                .filter(Forest::isGrown)
                .anyMatch(forest -> calculateDistance(forest.getCoords(), this.getCoord()) <= forestRange);

        boolean hasIndustrial = Arrays.stream(GameManager.getFields())
                .flatMap(Arrays::stream)
                .filter(field -> field instanceof PlayableField)
                .map(field -> (PlayableField) field)
                .filter(field -> field.getBuilding() instanceof IndustrialWorkplace)
                .anyMatch(field -> calculateDistance(field.getCoord(), this.getCoord()) <= industrialRange);

        if (hasPolice) moveInFactor++;
        if (hasStadium) moveInFactor++;
        if (hasForest) moveInFactor++;
        if (hasIndustrial) moveInFactor--;

        if (upgradeLevel != null) {
            switch (upgradeLevel) {
                case TOWN -> moveInFactor++;
                case CITY -> moveInFactor += 2;
                case METROPOLIS -> moveInFactor += 3;
            }
        }

        setMoveInFactor(moveInFactor);
        return moveInFactor;
    }

    @JsonIgnore
    private int calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

    /**
     * Get the zone of the field
     *
     * @return the zone of the field
     */
    public Zone getZone() {
        return zone;
    }

    /**
     * Set the zone of the field
     *
     * @param zone is the zone of the field
     */
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    /**
     * Get the upgrade level of the field
     *
     * @return the upgrade level of the field
     */
    public UpgradeLevel getUpgradeLevel() {
        return upgradeLevel;
    }

    /**
     * Set the upgrade level
     *
     * @param upgradeLevel is the upgrade level
     */
    public void setUpgradeLevel(UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    /**
     * Get the building of the field
     *
     * @return the building of the field
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Set the building of the field
     *
     * @param building is the building of the field
     */
    public void setBuilding(Building building) {
        if (building == null) {
            Logger.log("Building of field at " + coord + " set to null");
            this.building = null;
        } else {
            Logger.log("Building of field at " + coord + " set to " + building.getClass().getSimpleName());
            this.building = building;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayableField that)) return false;
        if (!super.equals(o)) return false;
        return getMoveInFactor() == that.getMoveInFactor() && getZone() == that.getZone() && getUpgradeLevel() == that.getUpgradeLevel() && Objects.equal(getBuilding(), that.getBuilding());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getMoveInFactor(), getZone(), getUpgradeLevel(), getBuilding());
    }

    @Override
    public String toString() {
        return "PlayableField{" +
                ", moveInFactor=" + moveInFactor +
                ", zone=" + zone +
                ", upgradeLevel=" + upgradeLevel +
                ", building=" + building +
                ", coord=" + coord +
                ", tile=" + tile +
                '}';
    }
}
