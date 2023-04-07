package controller;

import model.GameData;
import org.javatuples.Pair;

import java.io.File;
import java.util.ArrayList;

public class GameManager implements SaveManager, SpeedManager{
    private double catastrophyChance;
    private double hospitalChance;
    private GameData gameData;

    public GameManager() {
        catastrophyChance = 0.1;
        hospitalChance = 0.1;
    }

    public void initGame(){

    }

    public void nextDay(){

    }

    public void setTaxes(){

    }

    public double getWorkplaceDistr(){
        return 0.0;
    }

    public Pair<String, Integer> getExpenses(){
        return new Pair<>("", 0);
    }

    public void upgradeMap(){

    }

    public double getCatastrophyChance() {
        return catastrophyChance;
    }

    public void setCatastrophyChance(double catastrophyChance) {
        this.catastrophyChance = catastrophyChance;
    }

    public double getHospitalChance() {
        return hospitalChance;
    }

    public void setHospitalChance(double hospitalChance) {
        this.hospitalChance = hospitalChance;
    }

    public GameData getGameData() {
        return gameData;
    }

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
