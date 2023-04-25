package model.field;

import model.Coordinate;
import model.buildings.*;
import model.buildings.generated.GeneratedBuilding;
import model.buildings.generated.IndustrialWorkplace;
import model.buildings.generated.ResidentialBuilding;
import model.buildings.generated.ServiceWorkplace;
import model.buildings.playerbuilt.*;
import model.enums.UpgradeLevel;
import model.enums.Zone;
import util.Logger;
import view.enums.Tile;

import static model.GameData.budget;
import static util.ResourceLoader.*;

import java.io.IOException;

/**
 * This class represents a field on the map
 */
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
    public PlayableField(Coordinate coord) throws IOException {
        super(coord);
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

                    try { switch(upgradeLevel) {
                            case TOWN -> texture = loadImage("HOUSE_1.png");
                            case CITY -> texture = loadImage("HOUSE_2.png");
                            case METROPOLIS -> texture = loadImage("HOUSE_3.png"); }
                    } catch(IOException exc) { exc.printStackTrace(); }
                }

                case INDUSTRIAL_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to IndustrialWorkplace!");

                    building = new IndustrialWorkplace(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);

                    try { switch(upgradeLevel) {
                        case TOWN -> texture = loadImage("FACTORY_LVL1.png");
                        case CITY -> texture = loadImage("FACTORY_LVL2.png");
                        case METROPOLIS -> texture = loadImage("FACTORY_LVL3.png"); }
                    } catch(IOException exc) { exc.printStackTrace(); }
                }

                case SERVICE_ZONE -> {
                    Logger.log("Building of field at " + coord + " set to ServiceWorkplace!");

                    building = new ServiceWorkplace(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);

                    try { switch(upgradeLevel) {
                        case TOWN -> texture = loadImage("SERVICE_LVL1.png");
                        case CITY -> texture = loadImage("SERVICE_LVL2.png");
                        case METROPOLIS -> texture = loadImage("SERVICE_LVL3.png");}
                    } catch(IOException exc) { exc.printStackTrace(); }
                }

                default -> {
                    Logger.log("Field at " + coord + " has no zone!");
                    throw new RuntimeException("Building type not specified! (Zone is null)");
                }
            }
        } else if(zone == null) { //playerbuilt
            switch (buildingType) {
                case POLICE -> {
                    Logger.log("Building of field at " + coord + " set to PoliceStation!");
                    Logger.log("Current budget: " + budget);

                    building = new PoliceStation(coord);
                    budget -= ((PoliceStation)building).getBuildCost();

                    try { texture = loadImage("POLICE_1.png"); }
                    catch(IOException exc) { exc.printStackTrace(); }
                }

                case FIRESTATION -> {
                    Logger.log("Building of field at " + coord + " set to FireDepartment!");
                    Logger.log("Current budget: " + budget);

                    building = new FireDepartment(coord);
                    budget -= ((FireDepartment)building).getBuildCost();

                    try { texture = loadImage("FIRESTATION.png"); }
                    catch(IOException exc) { exc.printStackTrace(); }
                }

                case FOREST -> {
                    Logger.log("Building of field at " + coord + " set to Forest!");
                    Logger.log("Current budget: " + budget);

                    building = new Forest(coord);
                    budget -= ((Forest)building).getBuildCost();

                    try { texture = loadImage("FOREST_1.png"); }
                    catch(IOException exc) { exc.printStackTrace(); }
                }

                case ROAD -> {
                    Logger.log("Building of field at " + coord + " set to Road!");
                    Logger.log("Current budget: " + budget);

                    building = new Road(coord);
                    budget -= ((Road)building).getBuildCost();

                    try { texture = loadImage("road_tile.png"); }
                    catch(IOException exc) { exc.printStackTrace(); }
                }

                default -> {
                    Logger.log("Can't set building of field at " + coord + ", unrecognized building type!");
                    throw new RuntimeException("Unrecognized building type!");
                }
            }
        }
    }

    /**
     * Builds a stadium on the field
     * @param stadium is the stadium to be built
     */
    public void buildStadium(Building stadium){
        if(building != null) {

            Logger.log("Field at " + coord + " has a building on it, can't build!");
            throw new RuntimeException("There already is a building on this field!");

        } else if(stadium == null) {

            Logger.log("Stadium is null!");
            throw new NullPointerException("Stadium can't be null!");

        } else {

            Logger.log("Stadium built on field at " + coord + "!");
            Logger.log("Current budget: " + budget);

            building = stadium;
            budget -= ((PlayerBuilding)building).getBuildCost();

            try { texture = loadImage("STADIUM_1.png"); }
            catch(IOException exc) { exc.printStackTrace(); }
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
            Logger.log("Current budget: " + budget);

            this.zone = newZone;
            upgradeLevel = UpgradeLevel.TOWN;
            maxCapacity = 20;
            budget -= 100; //exact amount is TODO

            try { switch(zone) {
                    case RESIDENTIAL_ZONE -> texture = loadImage("residential_zone.png");
                    case INDUSTRIAL_ZONE -> texture = loadImage("industrial_zone.png");
                    case SERVICE_ZONE -> texture = loadImage("service_zone.png"); }
            } catch(IOException exc) { exc.printStackTrace(); }
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

        } else {
            Logger.log("Building of field at " + coord + " demolished!");
            Logger.log("Current budget: " + budget);

            budget += ((PlayerBuilding)building).getBuildCost();
            building = null;

            try { texture = loadImage("GRASS_1.png"); }
            catch (IOException exc) { exc.printStackTrace(); }
        }
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
            Logger.log("Current budget: " + budget);

            budget += 100; //exact amount is TODO
            zone = null;

            try { texture = loadImage("GRASS_1.png"); }
            catch(IOException exc) { exc.printStackTrace(); }
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
                    Logger.log("Current budget: " + budget);

                    upgradeLevel = UpgradeLevel.CITY;
                    maxCapacity = 40; //TODO
                    budget -= 100; //TODO

                    if(building != null) {
                        ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                        try { switch(zone) {
                                case RESIDENTIAL_ZONE -> texture = loadImage("HOUSE_2.png");
                                case INDUSTRIAL_ZONE -> texture = loadImage("FACTORY_LVL2.png");
                                case SERVICE_ZONE -> texture = loadImage("SERVICE_LVL2.png"); }
                        } catch(IOException exc) { exc.printStackTrace(); }
                    }
                }

                case CITY -> {
                    Logger.log("Field at " + coord + " upgraded to metropolis level!");
                    Logger.log("Field at " + coord + " has a new max capacity of " + maxCapacity + "!");
                    Logger.log("Current budget: " + budget);

                    upgradeLevel = UpgradeLevel.METROPOLIS;
                    maxCapacity = 100; //TODO
                    budget -= 500;

                    if(building != null){
                        ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                        try{ switch(zone) {
                                case RESIDENTIAL_ZONE -> texture = loadImage("HOUSE_3.png");
                                case INDUSTRIAL_ZONE -> texture = loadImage("FACTORY_LVL3.png");
                                case SERVICE_ZONE -> texture = loadImage("SERVICE_LVL3.png");}
                        } catch(IOException exc){exc.printStackTrace();}
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
    public int getCurrentCapacity() throws RuntimeException {
        if(building == null) {

            Logger.log("Field at " + coord + " has no building on it, can't get current capacity!");
            throw new RuntimeException("There is no building on the field!");

        } else if(!(building instanceof GeneratedBuilding)) {

            Logger.log("Field at " + coord + " has a generated building on it, can't get current capacity!");
            throw new RuntimeException("The building on the field is not a generated building!");

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
        Logger.log("Building of field at " + coord + " set to " + building.getClass().getSimpleName());
        this.building = building;
    }
}
