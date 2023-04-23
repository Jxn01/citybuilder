package model.buildings.playerbuilt;

import model.Coordinate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;

/**
 * This class represents a forest.
 */
public class Forest extends RangedBuilding {
    private final int growTime;
    private int growStage;

    /**
     * Constructor of the forest
     * @param coords is the coordinates of the forest
     */
    public Forest(Coordinate coords) {
        super(null, coords, 0.0, false, 0, 0, 0);

        try{
            texture = ResourceLoader.loadImage("forest.png");
        }catch (IOException exc) {
            exc.printStackTrace();
            texture = null;
        }

        firePossibility = 0.1;
        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
        range = 10; //TODO: Change this

        this.growTime = 10;
        this.growStage = 0;

        Logger.log("Forest created at " + coords.toString());
    }

    /**
     * Grow the forest
     */
    public void grow() {
        if(growStage < growTime){
            growStage++;
        }
        Logger.log("Forest grew to " + growStage + " years old");
    }

    @Override
    public String getStatistics() {
        String statistics = "Forest statistics:\n";
        statistics += "Grow time: " + growTime + " years \n";
        statistics += "Forest age: " + growStage + " years \n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        Logger.log(statistics);
        return statistics;
    }

    @Override
    public void setOnFire() {
        onFire = true;
        Logger.log("Forest is on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        onFire = false;
        Logger.log("Forest is extinguished at " + coords.toString());
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
                ", texture=" + texture +
                ", coords=" + coords +
                ", firePossibility=" + firePossibility +
                ", isOnFire=" + onFire +
                '}';
    }
}
