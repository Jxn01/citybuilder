package controller.catastrophies;

import model.GameData;
import model.Person;
import util.Logger;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a covid catastrophe.
 */
public class Covid extends Catastrophe {
    private static Covid instance = null;

    /**
     * Constructor of the covid catastrophe.
     */
    private Covid() { }

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

    private int randomNumberGenerator(int max){
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1); // generates a random number between 0 and x (inclusive)
        return randomNumber;
    };

    @Override
    public void effect(GameData gameData) {
        Logger.log("Actual population: "+gameData.getPopulation());

        ArrayList<Person> people = gameData.getPeople();
        int deaths_count = people.size() / 10;

        while(deaths_count > 0){
            int randomIndex = randomNumberGenerator(people.size());

            people.get(randomIndex).decease();
            people.remove(randomIndex);

            deaths_count--;
        }

        Logger.log("Actual population: "+gameData.getPopulation());

        Logger.log("Covid catastrophe happening!");
    }
}