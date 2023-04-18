package model.buildings;

import model.Coordinate;

import java.awt.*;

/**
 * This class represents a fire department.
 */
public class FireDepartment extends RangedBuilding {
    private int maxFireTrucks = 10;
    private int availableFireTrucks = 10;

    /**
     * Constructor of the fire department
     * @param texture is the texture of the fire department
     * @param coords is the coordinates of the fire department
     * @param firePossibility is the fire possibility of the fire department
     * @param isOnFire is the fire department on fire
     * @param buildCost is the build cost of the fire department
     * @param maintenanceCost is the maintenance cost of the fire department
     * @param range is the range of the fire department
     * @param maxFireTrucks is the maximum number of fire trucks of the fire department
     * @param availableFireTrucks is the available number of fire trucks of the fire department
     */
    public FireDepartment(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, int buildCost, int maintenanceCost, int range, int maxFireTrucks, int availableFireTrucks) {
        super(texture, coords, firePossibility, isOnFire, buildCost, maintenanceCost, range);
        this.maxFireTrucks = maxFireTrucks;
        this.availableFireTrucks = availableFireTrucks;
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
