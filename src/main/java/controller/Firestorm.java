package controller;

/**
 * This class represents a firestorm.
 */
public class Firestorm extends Catastrophy {
    private static Firestorm instance = null;

    /**
     * Constructor of the firestorm.
     */
    private Firestorm() {}

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
    protected void effect() {

    }
}
