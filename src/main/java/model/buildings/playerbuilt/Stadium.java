package model.buildings.playerbuilt;
import model.Coordinate;
import util.Logger;
import util.ResourceLoader;
import java.io.IOException;


public class Stadium extends RangedBuilding {

    /**
     * Constructor of the stadium
     * @param coords is the coordinates of the stadium
     */
    public Stadium(Coordinate coords) {
        super(coords, 0.0, false, 0, 0, 0);

        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
        range = 10; //TODO: Change this

        Logger.log("Stadium created at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Stadium statistics:\n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    @Override
    public void setOnFire() {
        onFire = true;
        Logger.log("Stadium is on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        onFire = false;
        Logger.log("Stadium is extinguished at " + coords.toString());
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
