package model.buildings;

/**
 * This interface represents a functional building.
 */
interface FunctionalBuilding {
    /**
     * Enable the effect of the building.
     */
    void enableEffect();

    /**
     * Disable the effect of the building.
     */
    void disableEffect();

    /**
     * Effect of the building.
     */
    void effect();
}