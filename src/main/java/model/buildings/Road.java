package model.buildings;

import model.Coordinate;
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
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
    }

    @Override
    public String getStatistics() {
        String statistics = "Road statistics:\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        System.out.println(statistics);
        return statistics;
    }

    @Override
    public void updateTexture(String textureName) {
        System.out.println("No effect");
    }
}
