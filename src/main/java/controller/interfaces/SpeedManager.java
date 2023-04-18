package controller.interfaces;

/**
 * This interface represents the speed manager.
 */
public interface SpeedManager {
    /**
     * Set the simulation speed to paused.
     */
    void timeStop();
    /**
     * Set the simulation speed to normal.
     */
    void timeNormal();
    /**
     * Set the simulation speed to fast.
     */
    void timeFast();
    /**
     * Set the simulation speed to faster.
     */
    void timeFaster();
}
