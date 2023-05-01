package controller.interfaces;

/**
 * This interface represents the speed manager.
 */
public interface SpeedManager {
    /**
     * Set the simulation speed to pause.
     */
    void timeStop();

    /**
     * Set the simulation speed to normal.
     */
    void setTimeNormal();

    /**
     * Set the simulation speed to fast.
     */
    void setTimeFast();

    /**
     * Set the simulation speed to faster.
     */
    void setTimeFaster();
}
