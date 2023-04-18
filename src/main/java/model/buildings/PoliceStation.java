package model.buildings;

import model.Coordinate;
import util.ResourceLoader;

import java.io.IOException;

public class PoliceStation extends RangedBuilding {

    /**
     * Constructor of the police station
     * @param coords is the coordinates of the police station
     */
    public PoliceStation(Coordinate coords) {
        super(null, coords, 0.0, false, 0, 0, 0);

        try{
            texture = ResourceLoader.loadImage("police.png");
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        firePossibility = 0.1;
        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
        range = 10; //TODO: Change this
    }

    @Override
    public String getStatistics() {
        String statistics = "Police station statistics:\n";
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