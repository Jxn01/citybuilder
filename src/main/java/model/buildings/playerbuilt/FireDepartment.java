package model.buildings.playerbuilt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.GameManager;
import model.Coordinate;
import model.Person;
import model.buildings.Building;
import model.buildings.generated.GeneratedBuilding;
import model.enums.Effect;
import model.field.Field;
import model.field.PlayableField;
import util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents a fire department.
 */
public class FireDepartment extends RangedBuilding {
    private int maxFireTrucks;
    private int availableFireTrucks;

    /**
     * Constructor of the fire department
     *
     * @param coords is the coordinates of the fire department
     */
    public FireDepartment(Coordinate coords) {
        super(coords, 0.0, false, GameManager.getFireStationBuildCost(), GameManager.getFireStationMaintenanceCost(), GameManager.getFireStationRange());

        this.maxFireTrucks = GameManager.getMaxFiretrucks();
        this.availableFireTrucks = GameManager.getMaxFiretrucks();

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
     *
     * @return the max fire trucks of the fire department
     */
    public int getMaxFireTrucks() {
        return maxFireTrucks;
    }

    /**
     * Set the max fire trucks of the fire department
     *
     * @param maxFireTrucks is the max fire trucks of the fire department
     */
    public void setMaxFireTrucks(int maxFireTrucks) {
        this.maxFireTrucks = maxFireTrucks;
    }

    /**
     * Get the available fire trucks of the fire department
     *
     * @return the available fire trucks of the fire department
     */
    public int getAvailableFireTrucks() {
        return availableFireTrucks;
    }

    /**
     * Set the available fire trucks of the fire department
     *
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

    // TODO: FireDepartment can't be on fire
    @Override
    public void setOnFire() {
        Logger.log("Firestation is on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        //cant be on fire
    }

    /**
     * The effect of the Fire Station.
     */
    @Override
    public void effect() {
        Field[][] fields = GameManager.getFields();
        ArrayList<Building> buildingsWithinRange = Arrays.stream(fields)
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField) f)
                .map(PlayableField::getBuilding)
                .filter(Objects::nonNull)
                .filter(f -> calculateDistance(f.getCoords(), coords) <= range)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        buildingsWithinRange.forEach(b -> b.setFirePossibility(0.0));
    }

    /**
     * Calculates the distance between two coordinates.
     * @param c1 the first coordinate
     * @param c2 the second coordinate
     * @return the distance between the two coordinates
     */
    private int calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
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
