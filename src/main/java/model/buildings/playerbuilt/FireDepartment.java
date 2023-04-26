package model.buildings.playerbuilt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Coordinate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;

/**
 * This class represents a fire department.
 */
public class FireDepartment extends RangedBuilding {
    private int maxFireTrucks;
    private int availableFireTrucks;

    /**
     * Constructor of the fire department
     * @param coords is the coordinates of the fire department
     */
    public FireDepartment(Coordinate coords) {
        super(coords, 0.0, false, 0, 0, 0);

        firePossibility = 0.0;
        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
        range = 10; //TODO: Change this

        this.maxFireTrucks = 10;
        this.availableFireTrucks = 10;

        Logger.log("Fire department created at " + coords.toString());
    }

    @JsonCreator
    public FireDepartment(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("onFire") boolean onFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost, @JsonProperty("range") int range, @JsonProperty("maxFireTrucks") int maxFireTrucks, @JsonProperty("availableFireTrucks") int availableFireTrucks) {
        super(coords, firePossibility, onFire, buildCost, maintenanceCost, range);
        this.maxFireTrucks = maxFireTrucks;
        this.availableFireTrucks = availableFireTrucks;
    }

    /**
     * Get the max fire trucks of the fire department
     * @return the max fire trucks of the fire department
     */
    public int getMaxFireTrucks() {
        return maxFireTrucks;
    }

    /**
     * Set the max fire trucks of the fire department
     * @param maxFireTrucks is the max fire trucks of the fire department
     */
    public void setMaxFireTrucks(int maxFireTrucks) {
        this.maxFireTrucks = maxFireTrucks;
    }

    /**
     * Get the available fire trucks of the fire department
     * @return the available fire trucks of the fire department
     */
    public int getAvailableFireTrucks() {
        return availableFireTrucks;
    }

    /**
     * Set the available fire trucks of the fire department
     * @param availableFireTrucks is the available fire trucks of the fire department
     */
    public void setAvailableFireTrucks(int availableFireTrucks) {
        this.availableFireTrucks = availableFireTrucks;
    }

    @JsonIgnore
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

    @Override
    public void setOnFire() {
        //cant be on fire
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
