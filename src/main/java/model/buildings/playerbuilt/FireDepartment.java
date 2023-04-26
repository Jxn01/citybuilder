package model.buildings.playerbuilt;

import model.Coordinate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;

/**
 * This class represents a fire department.
 */
public class FireDepartment extends RangedBuilding {
    private final int maxFireTrucks;
    private final int availableFireTrucks;

    /**
     * Constructor of the fire department
     *
     * @param coords is the coordinates of the fire department
     */
    public FireDepartment(Coordinate coords) {
        super(coords, 0.0, false, 0, 0, 0);

        firePossibility = 0.0;
        buildCost = 10000;
        maintenanceCost = 2500;
        range = 10; //TODO: Change this

        this.maxFireTrucks = 10;
        this.availableFireTrucks = 10;

        Logger.log("Fire department created at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Fire department statistics:\n";
        statistics += "Max fire trucks: " + maxFireTrucks + "\n";
        statistics += "Available fire trucks: " + availableFireTrucks + "\n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    // TODO: FireDepartment can't be on fire
    @Override
    public void setOnFire() {
        Logger.log("Firestation is on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        //cant be on fire
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

    @Override
    public String toString() {
        return "FireDepartment{" +
                "maxFireTrucks=" + maxFireTrucks +
                ", availableFireTrucks=" + availableFireTrucks +
                ", range=" + range +
                ", buildCost=" + buildCost +
                ", maintenanceCost=" + maintenanceCost +
                ", coords=" + coords +
                ", firePossibility=" + firePossibility +
                ", isOnFire=" + onFire +
                '}';
    }
}
