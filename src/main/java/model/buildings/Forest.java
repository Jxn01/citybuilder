package model.buildings;

import model.Coordinate;

import java.awt.*;

/**
 * This class represents a forest.
 */
public class Forest extends RangedBuilding {
    private int growTime = 10;
    private int growStage = 0;

    /**
     * Constructor of the forest
     * @param texture is the texture of the forest
     * @param coords is the coordinates of the forest
     * @param firePossibility is the fire possibility of the forest
     * @param isOnFire is the forest on fire
     * @param buildCost is the build cost of the forest
     * @param maintenanceCost is the maintenance cost of the forest
     * @param range is the range of the forest
     * @param growTime is the grow time of the forest
     * @param growStage is the grow stage of the forest
     */
    public Forest(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, int buildCost, int maintenanceCost, int range, int growTime, int growStage) {
        super(texture, coords, firePossibility, isOnFire, buildCost, maintenanceCost, range);
        this.growTime = growTime;
        this.growStage = growStage;
    }

    /**
     * Grow the tree
     */
    public void grow() {
        System.out.println("The tree is growing.");
    }

    @Override
    public String getStatistics() {
        System.out.println("Get statistics");
        return "1";
    }

    @Override
    public void setTexture() {
        System.out.println("Set texture");
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
