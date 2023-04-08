package controller;

import model.GameData;

import java.io.File;
import java.util.ArrayList;

public interface SaveManager {
    ArrayList<File> listSaveFiles();
    void loadSaveFile(File file);
    void saveGame(GameData gameData);
}
