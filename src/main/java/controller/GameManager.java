package controller;

import controller.catastrophies.Catastrophe;
import controller.catastrophies.Covid;
import controller.catastrophies.FinancialCrisis;
import controller.catastrophies.Firestorm;
import controller.interfaces.SaveManager;
import controller.interfaces.SpeedManager;
import model.GameData;
import org.javatuples.Pair;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager {
    private double catastropheChance;
    private double hospitalChance;
    private GameData gameData;

    private ArrayList<Catastrophe> catastrophies;

    /**
     * Constructor of the game manager.
     */
    public GameManager() {
        catastropheChance = 0.1;
        hospitalChance = 0.1;
        catastrophies = new ArrayList<>();
        catastrophies.add(FinancialCrisis.getInstance());
        catastrophies.add(Covid.getInstance());
        catastrophies.add(Firestorm.getInstance());

        System.out.println("Game manager created.");
    }

    /**
     * This method initializes the game.
     */
    public void initGame(){

    }

    /**
     * This method simulates.
     */
    public void nextDay(){
        System.out.println("A day passes...");
    }

    /**
     * This method sets the taxes.
     */
    public void setTaxes(int taxes){
        System.out.println("Taxes set to " + taxes);
    }

    /**
     * Getter for workplace distribution. (Service/Industry)
     */
    public double getWorkplaceDistr(){
        return 0.0;
    }

    /**
     * Getter for the expenses.
     */
    public Pair<String, Integer> getExpenses(){
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
        System.out.println("Catastrophe chance set to " + catastropheChance);
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
        System.out.println("Hospital chance set to " + hospitalChance);
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
        System.out.println("Game data set to " + gameData.getId());
    }

    @Override
    public ArrayList<File> listSaveFiles() {
        return null;
    }

    @Override
    public void loadSaveFile(File file) {
        System.out.println("Loading save file: " + file.getName());
    }

    @Override
    public void saveGame(GameData gameData) {
        System.out.println("Saving game: " + gameData.getSaveFile().getName());
    }

    @Override
    public void timeStop() {
        System.out.println("Time stopped");
    }

    @Override
    public void timeNormal() {
        System.out.println("Time flows normally");
    }

    @Override
    public void timeFast() {
        System.out.println("Time flows fast");
    }

    @Override
    public void timeFaster() {
        System.out.println("Time flows faster");
    }
}
