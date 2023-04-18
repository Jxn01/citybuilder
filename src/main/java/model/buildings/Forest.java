package model.buildings;

import model.Coordinate;
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
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        firePossibility = 0.1;
        buildCost = 1000; //TODO: Change this
        maintenanceCost = 100; //TODO: Change this
        range = 10; //TODO: Change this

        this.growTime = 10;
        this.growStage = 0;
    }

    /**
     * Grow the tree
     */
    public void grow() {
        if(growStage < growTime){
            growStage++;
        }
        System.out.println("The tree is growing.");
    }

    @Override
    public String getStatistics() {
        String statistics = "Forest statistics:\n";
        statistics += "Grow time: " + growTime + " years \n";
        statistics += "Forest age: " + growStage + " years \n";
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
