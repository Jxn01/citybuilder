package controller.catastrophies;

import controller.GameManager;
import model.GameData;
import model.Person;
import model.buildings.generated.ServiceWorkplace;
import util.Logger;

import java.util.List;

/**
 * This class represents a covid catastrophe.
 */
public class Covid extends Catastrophe {
    private static Covid instance = null;

    /**
     * Constructor of the covid catastrophe.
     */
    private Covid() {
    }

    /**
     * Get the instance of the covid catastrophe.
     *
     * @return the instance of the covid catastrophe
     */
    public static Covid getInstance() {
        if (instance == null) {
            instance = new Covid();
        }
        return instance;
    }

    @Override
    public void effect(GameData gameData) {
        Logger.log("Actual population: " + gameData.getPopulation());

        double hospitalModifier = gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof ServiceWorkplace)
                .count() * GameManager.getHospitalChance();

        List<Person> people = gameData.getPeople();
        int deaths_count = (int) (people.size() / (hospitalModifier + 10));

        while (deaths_count > 0) {
            int randomIndex = randomNumberGenerator(0, people.size() - 1);

            people.get(randomIndex).decease();
            people.remove(randomIndex);

            deaths_count--;
        }

        Logger.log("Covid catastrophe happening!");
        Logger.log("Actual population: " + gameData.getPopulation());
    }
}
