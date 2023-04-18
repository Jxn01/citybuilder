package model.buildings;

import model.Coordinate;

import java.awt.*;

public abstract class RangedBuilding extends PlayerBuilding implements FunctionalBuilding {
    protected int range;

    /**
     * Constructor of the ranged building
     * @param texture is the texture of the ranged building
     * @param coords is the coordinates of the ranged building
     * @param firePossibility is the fire possibility of the ranged building
     * @param isOnFire is the ranged building on fire
     * @param buildCost is the build cost of the ranged building
     * @param maintenanceCost is the maintenance cost of the ranged building
     * @param range is the range of the ranged building
     */
    public RangedBuilding(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, int buildCost, int maintenanceCost, int range) {
        super(texture, coords, firePossibility, isOnFire, buildCost, maintenanceCost);
        this.range = range;
    }
}
