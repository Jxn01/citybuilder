package model.buildings.playerbuilt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Coordinate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;

/**
 * This class represents a forest.
 */
public class Forest extends RangedBuilding {
    private int growTime;
    private int growStage;

    /**
     * Constructor of the forest
     * @param coords is the coordinates of the forest
     */
    public Forest(Coordinate coords) {
        super(coords, 0.0, false, 0, 0, 0);

        firePossibility = 0.1;
        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
        range = 10; //TODO: Change this

        this.growTime = 10;
        this.growStage = 0;

        Logger.log("Forest created at " + coords.toString());
    }

    @JsonCreator
    public Forest(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("onFire") boolean onFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost, @JsonProperty("range") int range, @JsonProperty("growTime") int growTime, @JsonProperty("growStage") int growStage) {
        super(coords, firePossibility, onFire, buildCost, maintenanceCost, range);
        this.growTime = growTime;
        this.growStage = growStage;
    }

    /**
     * Grow the forest
     */
    public void grow() {
        if(growStage < growTime) {
            growStage++;
            Logger.log("Forest grew to " + growStage + " years old");
        } else {
            Logger.log("Forest is fully grown");
        }
    }

    @JsonIgnore
    @Override
    public String getStatistics() {
        String statistics = "Forest statistics:\n";
        statistics += "Grow time: " + growTime + " years \n";
        statistics += "Forest age: " + growStage + " years \n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    /**
     * Get the grow time of the forest
     * @return the grow time of the forest
     */
    public int getGrowTime() {
        return growTime;
    }

    /**
     * Set the grow time of the forest
     * @param growTime is the grow time of the forest
     */
    public void setGrowTime(int growTime) {
        this.growTime = growTime;
    }

    /**
     * Get the grow stage of the forest
     * @return the grow stage of the forest
     */
    public int getGrowStage() {
        return growStage;
    }

    /**
     * Set the grow stage of the forest
     * @param growStage is the grow stage of the forest
     */
    public void setGrowStage(int growStage) {
        this.growStage = growStage;
    }

    @Override
    public void setOnFire() {
        Logger.log("Forest is on fire at " + coords.toString());
        onFire = true;
    }

    @Override
    public void extinguish() {
        Logger.log("Forest is extinguished at " + coords.toString());
        onFire = false;
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
        return "Forest{" +
                "growTime=" + growTime +
                ", growStage=" + growStage +
                ", range=" + range +
                ", buildCost=" + buildCost +
                ", maintenanceCost=" + maintenanceCost +
                ", coords=" + coords +
                ", firePossibility=" + firePossibility +
                ", isOnFire=" + onFire +
                '}';
    }
}
