package model;

import model.buildings.Building;
import model.buildings.FireDepartment;
import model.buildings.Forest;
import model.buildings.IndustrialWorkplace;
import model.buildings.PoliceStation;
import model.buildings.ResidentialBuilding;
import model.buildings.Road;
import model.buildings.ServiceWorkplace;
import model.buildings.Stadium;

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
     * @param c is the coordinate of the field
     */
    public Field(Coordinate c) {
        this.coord = c;
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
    }

    /**
     * Get the move in factor of the field
     * @return the move in factor of the field
     */
    public int getMoveInFactor() {
        return moveInFactor;
    }

    /**
     * Set the move in factor of the field
     * @param moveInFactor is the move in factor of the field
     */
    public void setMoveInFactor(int moveInFactor) {
        this.moveInFactor = moveInFactor;
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
     * Set the zone of the field
     * @param zone is the zone of the field
     */
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    /**
     * Get the upgrade level of the field
     * @return the upgrade level of the field
     */
    public UpgradeLevel getUpgradeLevel() {
        return upgradeLevel;
    }

    /**
     * Set the upgrade level of the field
     * @param upgradeLevel is the upgrade level of the field
     */
    public void setUpgradeLevel(UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
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
    }

    /**
     * Upgrades the field
     * @param budget is the budget of the player
     * @throws RuntimeException if the field can't be upgraded or if the field has no zone
     */
    public void upgrade(Integer budget) throws RuntimeException {
        if (upgradeLevel == null) {
            throw new NullPointerException("Field can't be upgraded!");
        }
        if (zone == null) {
            throw new NullPointerException("Field has no zone!");
        }
        switch (upgradeLevel) {
            case TOWN -> {
                upgradeLevel = UpgradeLevel.CITY;
                budget -= 100;
            } //exact amount is TODO
            case CITY -> {
                upgradeLevel = UpgradeLevel.METROPOLIS;
                budget -= 500;
            } //exact amount is TODO
            case METROPOLIS -> throw new RuntimeException("UpgradeLevel is already max!");
        }
    }

    /**
     * Deletes the zone of the field
     * @param budget is the budget of the player
     * @throws RuntimeException if there is a building on the field or if the field is already empty
     */
    public void deleteZone(Integer budget) throws RuntimeException {
        if (building != null) {
            throw new RuntimeException("Can't delete zone, there is a building on the field!");
        }
        if (zone == null) {
            throw new RuntimeException("Field is already empty!");
        }
        zone = null;
        budget += 100; //exact amount is TODO
    }


    /**
     * Demolishes the building of the field
     * @param budget is the budget of the player
     * @throws RuntimeException if there is no building on the field or if the building is public
     */
    public void demolishBuilding(Integer budget) throws RuntimeException {
        if (building == null) {
            throw new RuntimeException("There is no building on the field!");
        }
        if (zone != null) {
            throw new RuntimeException("Can't demolish public buildings! (There is a zone on the field!)");
        }
        building = null;
        // call animation
        budget += 100; //exact amount is TODO
    }

    /**
     * Builds a building on the field
     * @param buildingType is the type of the building
     * @param budget is the budget of the player
     * @throws RuntimeException if there is already a building on the field or if the building type is not specified
     */
    public void buildBuilding(String buildingType, Integer budget) throws RuntimeException {
        if (building != null) {
            throw new RuntimeException("There already is a building on this field!");
        }
        if (buildingType == null || buildingType.equals("")) {
            switch (zone) {
                //case RESIDENTIAL_ZONE -> building = new ResidentialBuilding();
                //case INDUSTRIAL_ZONE -> building = new IndustrialWorkplace();
                //case SERVICE_ZONE -> building = new ServiceWorkplace();
                default -> throw new RuntimeException("Building type not specified! (Zone is null)");
            }
        } else {
            switch (buildingType) {
                case "policestation" -> {
                    //building = new PoliceStation();
                    budget -= 100;
                } //exact amount is TODO
                case "stadium" -> {
                    //building = new Stadium();
                    budget -= 100;
                } //exact amount is TODO
                case "firedepartment" -> {
                    //building = new FireDepartment();
                    budget -= 100;
                } //exact amount is TODO
                case "forest" -> {
                    //building = new Forest();
                    budget -= 100;
                } //exact amount is TODO
                case "road" -> {
                    //building = new Road();
                    budget -= 100;
                } //exact amount is TODO
                default -> throw new RuntimeException("Unrecognized building type!");
            }
        }
    }

    /**
     * Gets the current capacity of the field
     * @return the current capacity of the field
     * @throws RuntimeException if there is no building on the field or if the building is not a residential building
     */
    public int getCurrentCapacity() throws RuntimeException {
        if (building == null) {
            throw new RuntimeException("There is no building on the field!");
        } else if (!(building instanceof ResidentialBuilding)) {
            throw new RuntimeException("The building on the field is not a residential building!");
        }
        return ((ResidentialBuilding) building).getPeople().size();
    }
}
