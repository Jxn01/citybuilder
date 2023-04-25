package controller.catastrophies;

import util.Logger;

/**
 * This class represents a firestorm.
 */
public class Firestorm extends Catastrophe {
    private static Firestorm instance = null;

    /**
     * Constructor of the firestorm.
     */
    private Firestorm() { }

    /**
     * Get the instance of the firestorm.
     * @return the instance of the firestorm
     */
    public static Firestorm getInstance() {
        if (instance == null) {
            instance = new Firestorm();
        }
        return instance;
    }

    @Override
    public void effect() {
        Logger.log("Firestorm happening!");
    }
}
