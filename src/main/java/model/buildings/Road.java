package model.buildings;

import model.Coordinate;

import java.awt.*;

public class Road extends PlayerBuilding {

    /**
     * Constructor of the road
     * @param texture is the texture of the road
     * @param coords is the coordinates of the road
     * @param firePossibility is the fire possibility of the road
     * @param isOnFire is the road on fire
     * @param buildCost is the build cost of the road
     * @param maintenanceCost is the maintenance cost of the road
     */
    public Road(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, int buildCost, int maintenanceCost) {
        super(texture, coords, firePossibility, isOnFire, buildCost, maintenanceCost);
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
}
