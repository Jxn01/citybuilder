package controller.catastrophies;

import model.GameData;
import util.Logger;

/**
 * This class represents a financial crisis.
 */
public class FinancialCrisis extends Catastrophe {
    private static FinancialCrisis instance = null;

    /**
     * Constructor of the financial crisis.
     */
    private FinancialCrisis() {
    }

    /**
     * Get the instance of the financial crisis.
     *
     * @return the instance of the financial crisis
     */
    public static FinancialCrisis getInstance() {
        if (instance == null) {
            instance = new FinancialCrisis();
        }
        return instance;
    }

    @Override
    public void effect(GameData gameData) {
        gameData.setBudget(gameData.getBudget() / 2);
        Logger.log("Financial crisis happening!");
    }
}
