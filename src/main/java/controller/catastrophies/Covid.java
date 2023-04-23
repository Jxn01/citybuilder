package controller.catastrophies;

import util.Logger;

/**
 * This class represents a covid catastrophe.
 */
public class Covid extends Catastrophe {
    private static Covid instance = null;

    /**
     * Constructor of the covid catastrophe.
     */
    private Covid() {}

    /**
     * Get the instance of the covid catastrophe.
     * @return the instance of the covid catastrophe
     */
    public static Covid getInstance() {
        if (instance == null) {
            instance = new Covid();
        }
        return instance;
    }

    @Override
    public void effect() {
        Logger.log("Covid catastrophe happening!");
    }
}
