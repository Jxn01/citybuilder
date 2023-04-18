package model.buildings;

import model.Coordinate;

import java.awt.*;

/**
 * This class represents a building.
 */
public abstract class Building {
    protected Image texture;
    protected Coordinate coords;
    protected double firePossibility;
    protected boolean isOnFire;

    /**
     * Constructor of the building
     * @param texture is the texture of the building
     * @param coords is the coordinates of the building
     * @param firePossibility is the fire possibility of the building
     * @param isOnFire is the building on fire
     */
    public Building(Image texture, Coordinate coords, double firePossibility, boolean isOnFire) {
        this.texture = texture;
        this.coords = coords;
        this.firePossibility = firePossibility;
        this.isOnFire = isOnFire;
    }
    // return value will be modified
    /**
     * Get the statistics of the building
     * @return the statistics of the building
     */
    protected abstract String getStatistics();

    // parameterlist will be modified

    /**
     * Set the texture of the building
     */
    protected abstract void updateTexture(String textureName);

    /**
     * Get the texture of the building
     * @return the texture of the building
     */
    public Image getTexture() {
        return texture;
    }

    /**
     * Update the texture of the building
     * @param texture is the new texture of the building
     */
    public void updateTexture(Image texture) {
        this.texture = texture;
    }

    /**
     * Get the coordinates of the building
     * @return the coordinates of the building
     */
    public Coordinate getCoords() {
        return coords;
    }

    /**
     * Set the coordinates of the building
     * @param coords is the coordinates of the building
     */
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }

    /**
     * Get the fire possibility of the building
     * @return the fire possibility of the building
     */
    public double getFirePossibility() {
        return firePossibility;
    }

    /**
     * Set the fire possibility of the building
     * @param firePossibility is the fire possibility of the building
     */
    public void setFirePossibility(double firePossibility) {
        this.firePossibility = firePossibility;
    }

    /**
     * Get the fire status of the building
     * @return the fire status of the building
     */
    public boolean isOnFire() {
        return isOnFire;
    }

    /**
     * Set the fire status of the building
     * @param onFire is the fire status of the building
     */
    public void setOnFire(boolean onFire) {
        isOnFire = onFire;
    }
}
