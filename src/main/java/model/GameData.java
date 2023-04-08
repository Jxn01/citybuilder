package model;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class GameData {
    private LocalDateTime startDate;
    private LocalDateTime currentDate;
    private LocalDateTime inGameStartDate;
    private LocalDateTime inGameCurrentDate;
    private LocalTime playTime;
    private int population;
    private int satisfaction;
    private Integer budget;
    private String cityName;
    private boolean gameOver;
    private File saveFile;
    private int yearlyTaxes;
    private ArrayList<Field> fields; //matrix, row length is sqrt of size

    private ArrayList<Person> people;

    public GameData(LocalDateTime startDate, LocalDateTime currentDate, LocalDateTime inGameStartDate, LocalDateTime inGameCurrentDate, LocalTime playTime, int population, int satisfaction, int budget, String cityName, boolean gameOver, File saveFile, int yearlyTaxes, ArrayList<Field> fields, ArrayList<Person> people) {
        this.startDate = startDate;
        this.currentDate = currentDate;
        this.inGameStartDate = inGameStartDate;
        this.inGameCurrentDate = inGameCurrentDate;
        this.playTime = playTime;
        this.population = population;
        this.satisfaction = satisfaction;
        this.budget = budget;
        this.cityName = cityName;
        this.gameOver = gameOver;
        this.saveFile = saveFile;
        this.yearlyTaxes = yearlyTaxes;
        this.fields = fields;
        this.people = people;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDateTime currentDate) {
        this.currentDate = currentDate;
    }

    public LocalDateTime getInGameStartDate() {
        return inGameStartDate;
    }

    public void setInGameStartDate(LocalDateTime inGameStartDate) {
        this.inGameStartDate = inGameStartDate;
    }

    public LocalDateTime getInGameCurrentDate() {
        return inGameCurrentDate;
    }

    public void setInGameCurrentDate(LocalDateTime inGameCurrentDate) {
        this.inGameCurrentDate = inGameCurrentDate;
    }

    public LocalTime getPlayTime() {
        return playTime;
    }

    public void setPlayTime(LocalTime playTime) {
        this.playTime = playTime;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public int getYearlyTaxes() {
        return yearlyTaxes;
    }

    public void setYearlyTaxes(int yearlyTaxes) {
        this.yearlyTaxes = yearlyTaxes;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "startDate=" + startDate +
                ", currentDate=" + currentDate +
                ", inGameStartDate=" + inGameStartDate +
                ", inGameCurrentDate=" + inGameCurrentDate +
                ", playTime=" + playTime +
                ", population=" + population +
                ", satisfaction=" + satisfaction +
                ", budget=" + budget +
                ", cityName='" + cityName + '\'' +
                ", gameOver=" + gameOver +
                ", saveFile=" + saveFile +
                ", yearlyTaxes=" + yearlyTaxes +
                ", fields=" + fields +
                ", people=" + people +
                '}';
    }
}
