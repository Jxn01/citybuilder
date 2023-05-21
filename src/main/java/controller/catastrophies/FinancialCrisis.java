package controller.catastrophies;

import model.GameData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Logger;

/**
 * This class represents a financial crisis.
 */
public class FinancialCrisis extends Catastrophe {
    private static @Nullable FinancialCrisis instance = null;

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
    public static @NotNull FinancialCrisis getInstance() {
        if (instance == null) {
            instance = new FinancialCrisis();
        }
        return instance;
    }

    @Override
    public void effect(@NotNull GameData gameData) {
        if (gameData.getBudget() < 0) {
            gameData.setBudget(gameData.getBudget() + (gameData.getBudget() / 2));
        } else {
            gameData.setBudget(gameData.getBudget() / 2);
        }

        Logger.log("Financial crisis happening!");
    }
}
