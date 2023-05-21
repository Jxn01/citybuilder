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
     * Burn down the building.
     */
    void burnDown();

    /**
     * Burn the building.
     */
    void burnTick();

    /**
     * Repair the building.
     */
    void repair();

    /**
     * Extinguish the building.
     */
    void extinguish();
}
