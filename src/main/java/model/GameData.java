package model;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains all the data that is needed to save the game.
 */
public class GameData {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime currentDate;
    private LocalDateTime inGameStartDate;
    private LocalDateTime inGameCurrentDate;
    private LocalTime playTime;
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
     * @param budget the budget of the city
     * @param cityName the name of the city
     * @param gameOver if the game is over
     * @param saveFile the file the game is saved in
     * @param yearlyTaxes the yearly taxes of the city
     * @param fields the fields of the city
     * @param people the people of the city
     */
    public GameData(LocalDateTime startDate, LocalDateTime currentDate, LocalDateTime inGameStartDate, LocalDateTime inGameCurrentDate, LocalTime playTime, int budget, String cityName, boolean gameOver, File saveFile, int yearlyTaxes, ArrayList<Field> fields, ArrayList<Person> people) {
        this.startDate = startDate;
        this.currentDate = currentDate;
        this.inGameStartDate = inGameStartDate;
        this.inGameCurrentDate = inGameCurrentDate;
        this.playTime = playTime;
        this.budget = budget;
        this.cityName = cityName;
        this.gameOver = gameOver;
        this.saveFile = saveFile;
        this.yearlyTaxes = yearlyTaxes;
        this.fields = fields;
        this.people = people;
        this.id = "game_data" + new Date().getTime();
        System.out.println("Game data created: " + this.id);
        System.out.println(this);
    }

    public GameData() {
        this.id = "game_data" + new Date().getTime();
        System.out.println("New game data created: " + this.id);
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
        System.out.println("Game data " + this.id + " start date set to: " + startDate);
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
        System.out.println("Game data " + this.id + " current date set to: " + currentDate);
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
        System.out.println("Game data " + this.id + " in game start date set to: " + inGameStartDate);
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
        System.out.println("Game data " + this.id + " in game current date set to: " + inGameCurrentDate);
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
        System.out.println("Game data " + this.id + " play time set to: " + playTime);
    }

    /**
     * Getter for the population
     * @return the population
     */
    public int getPopulation() {
        return people.size();
    }

    /**
     *  Calculates the average satisfaction of the people
     *  @return the average satisfaction
     */
    public int calculateAverageSatisfaction() {
        int satisfaction = 0;
        for(Person person : people) {
            satisfaction += person.calculateSatisfaction();
        }
        satisfaction /= people.size();
        System.out.println("Game data " + this.id + " average satisfaction calculated: " + satisfaction);
        return satisfaction;
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
        System.out.println("Game data " + this.id + " budget set to: " + budget);
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
        System.out.println("Game data " + this.id + " city name set to: " + cityName);
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
        System.out.println("Game data " + this.id + " game over set to: " + gameOver);
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
        System.out.println("Game data " + this.id + " save file set to: " + saveFile.getAbsolutePath());
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
        System.out.println("Game data " + this.id + " yearly taxes set to: " + yearlyTaxes);
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
        System.out.println("Game data " + this.id + " fields set.");
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
        System.out.println("Game data " + this.id + " people set.");
    }

    /**
     * Getter for the id
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the id
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", currentDate=" + currentDate +
                ", inGameStartDate=" + inGameStartDate +
                ", inGameCurrentDate=" + inGameCurrentDate +
                ", playTime=" + playTime +
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
