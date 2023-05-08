package controller.catastrophies;

import model.GameData;
import model.buildings.playerbuilt.FireDepartment;
import model.buildings.playerbuilt.Road;
import model.field.PlayableField;
import org.checkerframework.common.value.qual.IntRange;
import util.Logger;

import java.util.ArrayList;
import java.util.stream.IntStream;

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
        int number = (int) (Math.log(gameData.getPlayableFieldsWithBuildings().stream()
                .filter(f -> !(f.getBuilding() instanceof Road) && !(f.getBuilding() instanceof FireDepartment))
                .count()) * 4);

        ArrayList<PlayableField> playableFields = gameData.getPlayableFieldsWithBuildings();
        IntStream.range(0, number).forEach(i -> {
            int randomIndex = randomNumberGenerator(0, playableFields.size() - 1);
            playableFields.get(randomIndex).getBuilding().setOnFire();
        });

        Logger.log("Firestorm happening!");
    }
}
