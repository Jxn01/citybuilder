package controller.catastrophies;

import model.GameData;
import model.field.PlayableField;
import util.Logger;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a firestorm.
 */
public class Firestorm extends Catastrophe {
    private static Firestorm instance = null;

    /**
     * Constructor of the firestorm.
     */
    private Firestorm() {
    }

    /**
     * Get the instance of the firestorm.
     *
     * @return the instance of the firestorm
     */
    public static Firestorm getInstance() {
        if (instance == null) {
            instance = new Firestorm();
        }
        return instance;
    }

    private int randomNumberGenerator(int max) {
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1); // generates a random number between 0 and x (inclusive)
        return randomNumber;
    }

    @Override
    public void effect(GameData gameData) {
        ArrayList<PlayableField> fields = gameData.getPlayableFieldsWithBuildings();
        int counter = randomNumberGenerator(5);

        Logger.log("Number of buildings on fire: " + counter);

        while (counter > 0) {
            int randomIndex = randomNumberGenerator(fields.size());
            fields.get(randomIndex).getBuilding().setOnFire();
            counter--;
        }

        Logger.log("Firestorm happening!");
    }
}
