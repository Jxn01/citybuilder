package model.buildings;

import model.Coordinate;

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
}
