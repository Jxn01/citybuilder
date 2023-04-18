package controller.interfaces;

import model.GameData;

import java.io.File;
import java.util.ArrayList;

/**
 * This interface represents a save manager.
 */
public interface SaveManager {
    /**
     * This method returns the list of save files.
     * @return The list of save files.
     */
    ArrayList<File> listSaveFiles();
    /**
     * This method loads a save file.
     * @param file The file to load.
     */
    void loadSaveFile(File file);
    /**
     * This method saves the game.
     * @param gameData The game data to save.
     */
    void saveGame(GameData gameData);
}
