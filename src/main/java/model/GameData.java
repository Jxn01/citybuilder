package model;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This class contains all the data that is needed to save the game.
 */
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

    /**
     * Constructor for GameData
     * @param startDate the date the game was started
     * @param currentDate the current date
     * @param inGameStartDate the date the game was started in game
     * @param inGameCurrentDate the current date in game
     * @param playTime the time the game has been played
     * @param population the population of the city
     * @param satisfaction the satisfaction of the city
     * @param budget the budget of the city
     * @param cityName the name of the city
     * @param gameOver if the game is over
     * @param saveFile the file the game is saved in
     * @param yearlyTaxes the yearly taxes of the city
     * @param fields the fields of the city
     * @param people the people of the city
     */
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

    /**
     * Getter for the start date
     * @return the start date
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Setter for the start date
     * @param startDate the start date
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for the current date
     * @return the current date
     */
    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    /**
     * Setter for the current date
     * @param currentDate the current date
     */
    public void setCurrentDate(LocalDateTime currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * Getter for the in game start date
     * @return the in game start date
     */
    public LocalDateTime getInGameStartDate() {
        return inGameStartDate;
    }

    /**
     * Setter for the in game start date
     * @param inGameStartDate the in game start date
     */
    public void setInGameStartDate(LocalDateTime inGameStartDate) {
        this.inGameStartDate = inGameStartDate;
    }

    /**
     * Getter for the in game current date
     * @return the in game current date
     */
    public LocalDateTime getInGameCurrentDate() {
        return inGameCurrentDate;
    }

    /**
     * Setter for the in game current date
     * @param inGameCurrentDate the in game current date
     */
    public void setInGameCurrentDate(LocalDateTime inGameCurrentDate) {
        this.inGameCurrentDate = inGameCurrentDate;
    }

    /**
     * Getter for the play time
     * @return the play time
     */
    public LocalTime getPlayTime() {
        return playTime;
    }

    /**
     * Setter for the play time
     * @param playTime the play time
     */
    public void setPlayTime(LocalTime playTime) {
        this.playTime = playTime;
    }

    /**
     * Getter for the population
     * @return the population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Setter for the population
     * @param population the population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Getter for the satisfaction
     * @return the satisfaction
     */
    public int getSatisfaction() {
        return satisfaction;
    }

    /**
     * Setter for the satisfaction
     * @param satisfaction the satisfaction
     */
    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    /**
     * Getter for the budget
     * @return the budget
     */
    public Integer getBudget() {
        return budget;
    }

    /**
     * Setter for the budget
     * @param budget the budget
     */
    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    /**
     * Getter for the city name
     * @return the city name
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Setter for the city name
     * @param cityName the city name
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Getter for the game over
     * @return if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Setter for the game over
     * @param gameOver if the game is over
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Getter for the save file
     * @return the save file
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * Setter for the save file
     * @param saveFile the save file
     */
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * Getter for the yearly taxes
     * @return the yearly taxes
     */
    public int getYearlyTaxes() {
        return yearlyTaxes;
    }

    /**
     * Setter for the yearly taxes
     * @param yearlyTaxes the yearly taxes
     */
    public void setYearlyTaxes(int yearlyTaxes) {
        this.yearlyTaxes = yearlyTaxes;
    }

    /**
     * Getter for the fields
     * @return the fields
     */
    public ArrayList<Field> getFields() {
        return fields;
    }

    /**
     * Setter for the fields
     * @param fields the fields
     */
    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    /**
     * Getter for the people
     * @return the people
     */
    public ArrayList<Person> getPeople() {
        return people;
    }

    /**
     * Setter for the people
     * @param people the people
     */
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
