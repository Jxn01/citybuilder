package controller;

/**
 * This class represents a covid catastrophy.
 */
public class Covid extends Catastrophy {
    private static Covid instance = null;

    /**
     * Constructor of the covid catastrophy.
     */
    private Covid() {}

    /**
     * Get the instance of the covid catastrophy.
     * @return the instance of the covid catastrophy
     */
    public static Covid getInstance() {
        if (instance == null) {
            instance = new Covid();
        }
        return instance;
    }

    @Override
    protected void effect() {

    }
}
