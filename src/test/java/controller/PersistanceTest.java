package controller;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Date;
import util.Logger;

public class PersistanceTest {

    @Test
    public void saveAndLoadTest() {
        GameManager gm = new GameManager();
        GameData gd1 = new GameData("testcity", GameManager.getStarterBudget(), GameManager.getStarterTaxes(), GameManager.getStarterPeople(), GameManager.getStarterMapSize());
        gm.setGameData(gd1);
        Assertions.assertDoesNotThrow(() -> gm.saveGame(GameManager.getGameData()), "saveGame() should not throw an exception");
        Logger.log("Game saved successfully");
        Assertions.assertDoesNotThrow(() -> gm.loadSaveFile(GameManager.getGameData().getSaveFile()), "loadGame() should not throw an exception");
        gm.stopSimulation();
        Logger.log("Game loaded successfully");
        GameData gd2 = GameManager.getGameData();
        Assertions.assertEquals(gd1, gd2, "The loaded game should be the same as the saved game");
    }
}
