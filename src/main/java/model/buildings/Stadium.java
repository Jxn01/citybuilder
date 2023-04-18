package model.buildings;

import model.Coordinate;

import java.io.IOException;

import util.ResourceLoader;


public class Stadium extends RangedBuilding {

    /**
     * Constructor of the stadium
     * @param coords is the coordinates of the stadium
     */
    public Stadium(Coordinate coords) {
        super(null, null, 0.0, false, 0, 0, 0);

        try{
            texture = ResourceLoader.loadImage("stadium.png");
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
        range = 10; //TODO: Change this
        this.coords = coords;
    }

    @Override
    public String getStatistics() {
        String statistics = "Stadium statistics:\n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        System.out.println(statistics);
        return statistics;
    }

    @Override
    public void updateTexture(String textureName) {
        System.out.println("No effect");
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
