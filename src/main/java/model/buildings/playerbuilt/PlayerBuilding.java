package model.buildings.playerbuilt;

import model.Coordinate;
import model.buildings.Building;

import java.awt.*;

public abstract class PlayerBuilding extends Building {
    protected int buildCost;
    protected int maintenanceCost;

    /**
     * Constructor of the player building
     * @param texture is the texture of the player building
     * @param coords is the coordinates of the player building
     * @param firePossibility is the fire possibility of the player building
     * @param isOnFire is the player building on fire
     * @param buildCost is the build cost of the player building
     * @param maintenanceCost is the maintenance cost of the player building
     */
    public PlayerBuilding(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, int buildCost, int maintenanceCost) {
        super(texture, coords, firePossibility, isOnFire);
        this.buildCost = buildCost;
        this.maintenanceCost = maintenanceCost;
    }

    /**
     * Get the build cost of the player building
     * @return the build cost of the player building
     */
    public int getBuildCost() {
        return buildCost;
    }

    /**
     * Set the build cost of the player building
     * @param buildCost is the new build cost of the player building
     */
    public void setBuildCost(int buildCost) {
        this.buildCost = buildCost;
        System.out.println("Build cost of building at" + coords.toString() + " set to " + buildCost);
    }

    /**
     * Get the maintenance cost of the player building
     * @return the maintenance cost of the player building
     */
    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    /**
     * Set the maintenance cost of the player building
     * @param maintenanceCost is the new maintenance cost of the player building
     */
    public void setMaintenanceCost(int maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
        System.out.println("Maintenance cost of building at" + coords.toString() + " set to " + maintenanceCost);
    }
}
