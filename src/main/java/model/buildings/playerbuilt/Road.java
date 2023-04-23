package model.buildings.playerbuilt;

import model.Coordinate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;

public class Road extends PlayerBuilding {

    /**
     * Constructor of the road
     * @param coords is the coordinates of the road
     */
    public Road(Coordinate coords) {
        super(null, coords, 0.0, false, 0, 0);

        try{
            texture = ResourceLoader.loadImage("road.png");
        }catch (IOException exc) {
            exc.printStackTrace();
            texture = null;
        }

        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this

        Logger.log("Road created at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Road statistics:\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        Logger.log(statistics);
        return statistics;
    }

    @Override
    public void setOnFire() {
        //Roads can't be on fire
    }

    @Override
    public void extinguish() {
        //Roads can't be on fire
    }
}
