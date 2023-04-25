package controller;

import controller.catastrophies.Catastrophe;
import controller.catastrophies.Covid;
import controller.catastrophies.FinancialCrisis;
import controller.catastrophies.Firestorm;
import controller.interfaces.SaveManager;
import controller.interfaces.SpeedManager;
import model.GameData;
import org.javatuples.Pair;
import util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager {
    private double catastropheChance;
    private double hospitalChance;
    private GameData gameData;

    private SimulationSpeed simulationSpeed;

    private final ArrayList<Catastrophe> catastrophes;

    private ArrayList<File> saveFiles;

    /**
     * Constructor of the game manager.
     */
    public GameManager() {
        catastropheChance = 0.1;
        hospitalChance = 0.1;
        catastrophes = new ArrayList<>();
        catastrophes.add(FinancialCrisis.getInstance());
        catastrophes.add(Covid.getInstance());
        catastrophes.add(Firestorm.getInstance());

        setSimulationSpeed(SimulationSpeed.PAUSED);
        Logger.log("Game manager created.");
    }

    /**
     * This method initializes the game.
     */
    public void initGame(String cityName) {
        setGameData(new GameData(cityName));
        gameData.calculateAverageSatisfaction();
        gameData.calculatePopulation();
        Logger.log("Game initialized.");
    }

    /**
     * This method simulates.
     */
    public void nextDay() {
        Logger.log("A day passes...");
    }

    /**
     * Evokes a financial crisis.
     */
    public void evokeFinancialCrisis() {
        Logger.log("Financial crisis evoked.");
        catastrophes.get(0).effect();
    }

    /**
     * Evokes a covid pandemic.
     */
    public void evokeCovid() {
        Logger.log("Covid evoked.");
        catastrophes.get(1).effect();
    }

    /**
     * Evokes a firestorm.
     */
    public void evokeFirestorm() {
        Logger.log("Firestorm evoked.");
        catastrophes.get(2).effect();
    }

    @Override
    public ArrayList<File> readSaveFiles() {
        Logger.log("Reading save files...");
        saveFiles = new ArrayList<>();
        File[] files = new File("src/main/resources/saves").listFiles();
        if(files != null) {
            for(File file : files) {
                if(file.isFile() && file.getName().endsWith(".json")) {
                    saveFiles.add(file);
                }
            }
        }
        return saveFiles;
    }

    @Override
    public void deleteSaveFile(File file) {
        Logger.log("Deleting save file: " + file.getName());
        if(file.delete()) {
            Logger.log("Save file deleted.");
        } else {
            Logger.log("Save file could not be deleted.");
        }
    }

    @Override
    public void loadSaveFile(File file) {
        Logger.log("Loading save file: " + file.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            setGameData(objectMapper.readValue(file, GameData.class));
            Logger.log("Save file loaded.");
        } catch(Exception exc) {
            Logger.log("Save file could not be loaded.");
            exc.printStackTrace();
        }
    }

    @Override
    public void saveGame(GameData gameData) {
        if(gameData.getSaveFile() == null) {
            Logger.log("Game has not been saved yet, creating new save file...");
            File file = new File("src/main/resources/saves/" + gameData.getId() + ".json");
            try {

                if(file.createNewFile()) {
                    Logger.log("Save file created.");
                } else {
                    Logger.log("Save file already exists.");
                    Logger.log("Deleting old save file...");
                    if(file.delete()) {
                        Logger.log("Old save file deleted.");
                    } else {
                        Logger.log("Old save file could not be deleted.");
                    }
                }

                gameData.setSaveFile(file);
                Logger.log("Save file created.");

            } catch(Exception exc) {
                Logger.log("Save file could not be created.");
                exc.printStackTrace();
            }
        }
        Logger.log("Saving game...");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(gameData.getSaveFile(), gameData);
            Logger.log("Game saved.");
        } catch(Exception exc) {
            Logger.log("Game could not be saved.");
            exc.printStackTrace();
        }
    }



    /**
     * This method sets the taxes.
     */
    public void setTaxes(int taxes) {
        Logger.log("Taxes set to " + taxes);
    }

    /**
     * Getter for workplace distribution. (Service/Industry)
     */
    public double getWorkplaceDistribution() {
        return 0.0;
    }

    /**
     * Getter for the expenses.
     */
    public Pair<String, Integer> getExpenses() {
        return new Pair<>("", 0);
    }

    /**
     * Getter for the catastrophe chance.
     * @return the catastrophe chance
     */
    public double getCatastropheChance() {
        return catastropheChance;
    }

    /**
     * Setter for the catastrophe chance.
     * @param catastropheChance the new catastrophe chance
     */
    public void setCatastropheChance(double catastropheChance) {
        this.catastropheChance = catastropheChance;
        Logger.log("Catastrophe chance set to " + catastropheChance);
    }

    /**
     * Getter for the hospital chance.
     * @return the hospital chance
     */
    public double getHospitalChance() {
        return hospitalChance;
    }

    /**
     * Setter for the hospital chance.
     * @param hospitalChance the new hospital chance
     */
    public void setHospitalChance(double hospitalChance) {
        this.hospitalChance = hospitalChance;
        Logger.log("Hospital chance set to " + hospitalChance);
    }

    /**
     * Getter for the game data.
     * @return the game data
     */
    public GameData getGameData() {
        return gameData;
    }

    /**
     * Setter for the game data.
     * @param gameData the new game data
     */
    public void setGameData(GameData gameData) {
        this.gameData = gameData;
        Logger.log("Game data set to " + gameData.getId());
    }

    /**
     * Getter for the simulation speed.
     * @return the simulation speed
     */
    public SimulationSpeed getSimulationSpeed() {
        return simulationSpeed;
    }

    /**
     * Setter for the simulation speed.
     * @param simulationSpeed the new simulation speed
     */
    public void setSimulationSpeed(SimulationSpeed simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
        Logger.log("Simulation speed set to " + simulationSpeed);
    }

    /**
     * Getter for the save files
     * @return the save files
     */
    public ArrayList<File> getSaveFiles() {
        return saveFiles;
    }

    @Override
    public void timeStop() {
        Logger.log("Time stopped");
        simulationSpeed = SimulationSpeed.PAUSED;
    }

    @Override
    public void timeNormal() {
        Logger.log("Time flows normally");
        simulationSpeed = SimulationSpeed.NORMAL;
    }

    @Override
    public void timeFast() {
        Logger.log("Time flows fast");
        simulationSpeed = SimulationSpeed.FAST;
    }

    @Override
    public void timeFaster() {
        Logger.log("Time flows faster");
        simulationSpeed = SimulationSpeed.FASTER;
    }
}
