package model;

import model.buildings.*;
import model.buildings.generated.GeneratedBuilding;
import model.buildings.generated.IndustrialWorkplace;
import model.buildings.generated.ResidentialBuilding;
import model.buildings.generated.ServiceWorkplace;
import model.buildings.playerbuilt.*;
import model.enums.UpgradeLevel;
import model.enums.Zone;
import util.Logger;

/**
 * This class represents a field on the map
 */
public class Field {
    private int maxCapacity;
    private int moveInFactor;
    private final Coordinate coord;
    private Zone zone;
    private UpgradeLevel upgradeLevel;
    private Building building;

    /**
     * Constructor of the field
     * @param coord is the coordinate of the field
     */
    public Field(Coordinate coord) {
        this.coord = coord;
        Logger.log("Field created at " + coord.toString());
    }

    /**
     * Upgrades the field
     * @param budget is the budget of the player
     * @throws RuntimeException if the field can't be upgraded or if the field has no zone
     */
    public void upgrade(Integer budget) throws RuntimeException {
        if (upgradeLevel == null) {
            Logger.log("Field at " + coord + " can't be upgraded!");
            throw new NullPointerException("Field can't be upgraded!");
        }
        if (zone == null) {
            Logger.log("Field at " + coord + " has no zone!");
            throw new NullPointerException("Field has no zone!");
        }
        switch (upgradeLevel) {
            case TOWN -> {
                upgradeLevel = UpgradeLevel.CITY;
                maxCapacity = 40; //TODO
                if(building != null){
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                }
                budget -= 100; //TODO
                Logger.log("Field at " + coord + " upgraded to city level!");
                Logger.log("Field at " + coord + " has a new max capacity of " + maxCapacity + "!");
                Logger.log("Current budget: " + budget);
            }
            case CITY -> {
                upgradeLevel = UpgradeLevel.METROPOLIS;
                maxCapacity = 100; //TODO
                if(building != null){
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                }
                budget -= 500;
                Logger.log("Field at " + coord + " upgraded to metropolis level!");
                Logger.log("Field at " + coord + " has a new max capacity of " + maxCapacity + "!");
                Logger.log("Current budget: " + budget);
            }
            case METROPOLIS -> {
                Logger.log("Field at " + coord + " is already max level!");
                throw new RuntimeException("UpgradeLevel is already max!");
            }
        }
    }

    /**
     * Marks the zone of the field
     * @param budget is the budget of the player
     * @param newZone is the new zone of the field
     * @throws NullPointerException if the field has a zone already
     */
    public void markZone(Zone newZone, Integer budget) throws NullPointerException{
        if(newZone == null) {
            Logger.log("Field's zone at " + coord + " can't be marked!");
            throw new NullPointerException("Zone can't be null!");
        }
        if(this.zone == null) {
            this.zone = newZone;
            maxCapacity = 20;
            budget -= 100; //exact amount is TODO
            Logger.log("Field's zone at " + coord + " marked to " + zone+"!");
            Logger.log("Current budget: " + budget);
        }
    }

    /**
     * Deletes the zone of the field
     * @param budget is the budget of the player
     * @throws RuntimeException if there is a building on the field or if the field is already empty
     */
    public void deleteZone(Integer budget) throws RuntimeException {
        if (building != null) {
            Logger.log("Field at " + coord + " has a building on it, can't delete zone!");
            throw new RuntimeException("Can't delete zone, there is a building on the field!");
        }
        if (zone == null) {
            Logger.log("Field at " + coord + " has no zone!");
            throw new RuntimeException("Field is already empty!");
        }
        zone = null;
        budget += 100; //exact amount is TODO
        Logger.log("Field's zone at " + coord + " deleted!");
        Logger.log("Current budget: " + budget);
    }

    /**
     * Demolishes the building of the field
     * @param budget is the budget of the player
     * @throws RuntimeException if there is no building on the field or if the building is public
     */
    public void demolishBuilding(Integer budget) throws RuntimeException {
        if (building == null) {
            Logger.log("Field at " + coord + " has no building on it, can't demolish!");
            throw new RuntimeException("There is no building on the field!");
        }
        if (zone != null) {
            Logger.log("Field at " + coord + " has a zone on it, can't demolish!");
            throw new RuntimeException("Can't demolish public buildings! (There is a zone on the field!)");
        }
        budget += ((PlayerBuilding)building).getBuildCost();
        building = null;
        Logger.log("Building of field at " + coord + " demolished!");
        Logger.log("Current budget: " + budget);
    }

    /**
     * Builds a building on the field
     * @param buildingType is the type of the building
     * @param budget is the budget of the player
     * @throws RuntimeException if there is already a building on the field, or if the building type is not specified
     */
    public void buildBuilding(String buildingType, Integer budget) throws RuntimeException {
        if (building != null) {
            Logger.log("Field at " + coord + " has a building on it, can't build!");
            throw new RuntimeException("There already is a building on this field!");
        }
        if (buildingType == null || buildingType.equals("")) {
            switch (zone) {
                case RESIDENTIAL_ZONE -> {
                    building = new ResidentialBuilding(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                    Logger.log("Building of field at " + coord + " set to ResidentialBuilding!");
                }
                case INDUSTRIAL_ZONE -> {
                    building = new IndustrialWorkplace(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                    Logger.log("Building of field at " + coord + " set to IndustrialWorkplace!");
                }
                case SERVICE_ZONE -> {
                    building = new ServiceWorkplace(coord);
                    ((GeneratedBuilding) building).setMaxCapacity(maxCapacity);
                    Logger.log("Building of field at " + coord + " set to ServiceWorkplace!");
                }
                default -> {
                    Logger.log("Field at " + coord + " has no zone!");
                    throw new RuntimeException("Building type not specified! (Zone is null)");
                }
            }
        } else {
            switch (buildingType) {
                case "policestation" -> {
                    building = new PoliceStation(coord);
                    budget -= ((PoliceStation)building).getBuildCost();
                    Logger.log("Building of field at " + coord + " set to PoliceStation!");
                    Logger.log("Current budget: " + budget);
                }
                case "stadium" -> {
                    building = new Stadium(coord);
                    budget -= ((Stadium)building).getBuildCost();
                    Logger.log("Building of field at " + coord + " set to Stadium!");
                    Logger.log("Current budget: " + budget);
                }
                case "firedepartment" -> {
                    building = new FireDepartment(coord);
                    budget -= ((FireDepartment)building).getBuildCost();
                    Logger.log("Building of field at " + coord + " set to FireDepartment!");
                    Logger.log("Current budget: " + budget);
                }
                case "forest" -> {
                    building = new Forest(coord);
                    budget -= ((Forest)building).getBuildCost();
                    Logger.log("Building of field at " + coord + " set to Forest!");
                    Logger.log("Current budget: " + budget);
                }
                case "road" -> {
                    building = new Road(coord);
                    budget -= ((Road)building).getBuildCost();
                    Logger.log("Building of field at " + coord + " set to Road!");
                    Logger.log("Current budget: " + budget);
                }
                default -> {
                    Logger.log("Can't set building of field at " + coord + ", unrecognized building type!");
                    throw new RuntimeException("Unrecognized building type!");
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
        if (building == null) {
            Logger.log("Field at " + coord + " has no building on it, can't get current capacity!");
            throw new RuntimeException("There is no building on the field!");
        } else if (!(building instanceof GeneratedBuilding)) {
            Logger.log("Field at " + coord + " has a generated building on it, can't get current capacity!");
            throw new RuntimeException("The building on the field is not a generated building!");
        }
        Logger.log("Current capacity of field at " + coord + " is " + ((GeneratedBuilding) building).getPeople().size() + "!");
        return ((GeneratedBuilding) building).getPeople().size();
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
        this.maxCapacity = maxCapacity;
        Logger.log("Max capacity of field at " + coord + " set to " + maxCapacity);
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
     * Get the coordinate of the field
     * @return the coordinate of the field
     */
    public Coordinate getCoord() {
        return coord;
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
        this.building = building;
        Logger.log("Building of field at " + coord + " set to " + building.getClass().getSimpleName());
    }
}
