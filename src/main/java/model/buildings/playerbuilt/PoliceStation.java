package model.buildings.playerbuilt;

import model.Coordinate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;

public class PoliceStation extends RangedBuilding {

    /**
     * Constructor of the police station
     *
     * @param coords is the coordinates of the police station
     */
    public PoliceStation(Coordinate coords) {
        super(coords, 0.0, false, 0, 0, 0);

        firePossibility = 0.1;
        buildCost = 5000;
        maintenanceCost = 1250;
        range = 10; //TODO: Change this

        Logger.log("Police station created at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Police station statistics:\n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    @Override
    public void setOnFire() {
        Logger.log("Police station is on fire at " + coords.toString());
        onFire = true;
    }

    @Override
    public void extinguish() {
        Logger.log("Police station is extinguished at " + coords.toString());
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
}