package model.buildings.playerbuilt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import controller.GameManager;
import model.Coordinate;
import model.buildings.Building;
import model.field.PlayableField;
import util.Logger;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Road.class, name = "road"),
        @JsonSubTypes.Type(value = RangedBuilding.class, name = "ranged")
})

public abstract class PlayerBuilding extends Building {
    protected int buildCost;
    protected int maintenanceCost;

    /**
     * Constructor of the player building
     *
     * @param coords          is the coordinates of the player building
     * @param firePossibility is the fire possibility of the player building
     * @param isOnFire        is the player building on fire
     * @param buildCost       is the build cost of the player building
     * @param maintenanceCost is the maintenance cost of the player building
     */
    @JsonCreator
    public PlayerBuilding(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("isOnFire") boolean isOnFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost) {
        super(coords, firePossibility, isOnFire);

        this.buildCost = buildCost;
        this.maintenanceCost = maintenanceCost;
    }

    /**
     * Get the maintenance cost of the player building
     *
     * @return the maintenance cost of the player building
     */
    @Override
    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    /**
     * Set the maintenance cost of the player building
     *
     * @param maintenanceCost is the new maintenance cost of the player building
     */
    public void setMaintenanceCost(int maintenanceCost) {
        Logger.log("Maintenance cost of building at" + coords.toString() + " set to " + maintenanceCost);
        this.maintenanceCost = maintenanceCost;
    }

    /**
     * Get the build cost of the player building
     *
     * @return the build cost of the player building
     */
    @Override
    public int getBuildCost() {
        return buildCost;
    }

    /**
     * Set the build cost of the player building
     *
     * @param buildCost is the new build cost of the player building
     */
    public void setBuildCost(int buildCost) {
        Logger.log("Build cost of building at" + coords.toString() + " set to " + buildCost);
        this.buildCost = buildCost;
    }

    @Override
    public void burnDown(){
        Logger.log("Building at " + coords.toString() + " burned down");
        ((PlayableField)GameManager.getFields()[coords.getX()][coords.getY()]).demolishBuilding(true);
    }
}
