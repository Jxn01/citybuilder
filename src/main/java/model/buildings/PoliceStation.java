package model.buildings;

import model.Coordinate;

import java.awt.*;

public class PoliceStation extends RangedBuilding {

    /**
     * Constructor of the police station
     * @param texture is the texture of the police station
     * @param coords is the coordinates of the police station
     * @param firePossibility is the fire possibility of the police station
     * @param isOnFire is the police station on fire
     * @param buildCost is the build cost of the police station
     * @param maintenanceCost is the maintenance cost of the police station
     * @param range is the range of the police station
     */
    public PoliceStation(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, int buildCost, int maintenanceCost, int range) {
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