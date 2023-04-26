package controller.catastrophies;

import model.GameData;

import java.util.Random;

/**
 * This class represents a catastrophe.
 */
public abstract class Catastrophe {
    /**
     * Generates a random int number between 0 and max (parameter) inclusive
     *
     * @param max
     * @return a random int number
     */
    protected int randomNumberGenerator(int max) {
        Random random = new Random();
        return random.nextInt(max + 1);
    }

    /**
     * Effect of the catastrophe.
     */
    public abstract void effect(GameData gameData);
}
