package model.buildings.playerbuilt;

import model.Coordinate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;

public class Road extends PlayerBuilding {

    /**
     * Constructor of the road
     *
     * @param coords is the coordinates of the road
     */
    public Road(Coordinate coords) {
        super(coords, 0.0, false, 0, 0);

        buildCost = 500;
        maintenanceCost = 125;

        Logger.log("Road created at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Road statistics:\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    // TODO: Road can't be on fire
    @Override
    public void setOnFire() {
        Logger.log("Road is on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        //Road can't be on fire
    }
}
