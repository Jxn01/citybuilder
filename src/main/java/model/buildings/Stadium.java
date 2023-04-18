package model.buildings;

import model.Coordinate;

import java.awt.*;

public class Stadium extends RangedBuilding {

    /**
     * Constructor of the stadium
     * @param texture is the texture of the stadium
     * @param coords is the coordinates of the stadium
     * @param firePossibility is the fire possibility of the stadium
     * @param isOnFire is the stadium on fire
     * @param buildCost is the build cost of the stadium
     * @param maintenanceCost is the maintenance cost of the stadium
     * @param range is the range of the stadium
     */
    public Stadium(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, int buildCost, int maintenanceCost, int range) {
        super(texture, coords, firePossibility, isOnFire, buildCost, maintenanceCost, range);
    }

    @Override
    public String getStatistics() {
        System.out.println("Get statistics");
        return "1";
    }

    @Override
    public void setTexture() {
        System.out.println("Set texture");
    }

    @Override
    public void enableEffect() {

    }

    @Override
    public void disableEffect() {

    }

    @Override
    public void effect() {

    }
}
