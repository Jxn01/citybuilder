package controller.interfaces;

import model.GameData;

import java.io.File;
import java.util.List;

/**
 * This interface represents a save manager.
 */
public interface SaveManager {
    /**
     * This method returns the list of save files.
     *
     * @return The list of save files.
     */
    List<File> readSaveFiles();

    /**
     * This method loads a save file.
     *
     * @param file The file to load.
     */
    void loadSaveFile(File file);

    /**
     * This method saves the game.
     *
     * @param gameData The game data to save.
     */
    void saveGame(GameData gameData);

    /**
     * This method deletes a save file.
     *
     * @param file The file to delete.
     */
    void deleteSaveFile(File file);
}
