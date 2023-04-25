package model.buildings.interfaces;

/**
 * This interface represents a flammable building.
 */
public interface Flammable {

    /**
     * Set the building on fire.
     */
    void setOnFire();

    /**
     * Extinguish the building.
     */
    void extinguish();
}
