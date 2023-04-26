package controller.catastrophies;

import model.GameData;
import model.field.PlayableField;
import util.Logger;

import java.util.ArrayList;

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

    @Override
    public void effect(GameData gameData) {
        ArrayList<PlayableField> fields = gameData.getPlayableFieldsWithBuildings();

        if (fields.size() == 0) {
            throw new RuntimeException("No playable fields found with building on it.");
        }

        int counter = randomNumberGenerator(1, 5);

        Logger.log("Number of buildings on fire: " + counter);

        while (counter > 0) {
            int randomIndex = randomNumberGenerator(0, fields.size() - 1);
            fields.get(randomIndex).getBuilding().setOnFire();
            counter--;
        }

        Logger.log("Firestorm happening!");
    }
}
