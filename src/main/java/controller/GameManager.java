package controller;

import model.GameData;
import org.javatuples.Pair;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager{
    private double catastrophyChance;
    private double hospitalChance;
    private GameData gameData;

    private ArrayList<Catastrophy> catastrophies;

    /**
     * Constructor of the game manager.
     */
    public GameManager() {
        catastrophyChance = 0.1;
        hospitalChance = 0.1;
        catastrophies = new ArrayList<>();
        catastrophies.add(FinancialCrisis.getInstance());
        catastrophies.add(Covid.getInstance());
        catastrophies.add(Firestorm.getInstance());
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

    }

    /**
     * This method sets the taxes.
     */
    public void setTaxes(){

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
     * Getter for the catastrophy chance.
     * @return the catastrophy chance
     */
    public double getCatastrophyChance() {
        return catastrophyChance;
    }

    /**
     * Setter for the catastrophy chance.
     * @param catastrophyChance the new catastrophy chance
     */
    public void setCatastrophyChance(double catastrophyChance) {
        this.catastrophyChance = catastrophyChance;
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
    }

    @Override
    public ArrayList<File> listSaveFiles() {
        return null;
    }

    @Override
    public void loadSaveFile(File file) {

    }

    @Override
    public void saveGame(GameData gameData) {

    }

    @Override
    public void timeStop() {

    }

    @Override
    public void timeNormal() {

    }

    @Override
    public void timeFast() {

    }

    @Override
    public void timeFaster() {

    }
}
