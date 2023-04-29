package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import controller.GameManager;
import model.field.BorderField;
import model.field.Field;
import model.field.PlayableField;
import util.GraphDeserializer;
import util.GraphSerializer;
import util.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static util.Date.getDate;
import static util.Date.getLongDate;

/**
 * This class contains all the data that is needed to save the game.
 */
public class GameData {
    private String id;
    private final String startDate;
    private String currentDate;
    private String inGameStartDate;
    private String inGameCurrentDate;
    private String playTime;
    private int budget;
    private String cityName;
    private boolean gameOver;
    private File saveFile;
    private int yearlyTaxes;
    private int averageSatisfaction;

    private Field[][] fields;

    @JsonDeserialize(using = GraphDeserializer.class)
    @JsonSerialize(using = GraphSerializer.class)
    private MutableGraph<Coordinate> graph;
    private List<Person> people;

    /**
     * Constructor for GameData
     *
     * @param startDate         the date the game was started
     * @param currentDate       the current date
     * @param inGameStartDate   the date the game was started in game
     * @param inGameCurrentDate the current date in game
     * @param playTime          the time the game has been played
     * @param budget            the budget of the city
     * @param cityName          the name of the city
     * @param gameOver          if the game is over
     * @param saveFile          the file the game is saved in
     * @param yearlyTaxes       the yearly taxes of the city
     * @param fields            the fields of the city
     * @param people            the people of the city
     */
    @JsonCreator
    public GameData(@JsonProperty("startDate") String startDate, @JsonProperty("currentDate") String currentDate, @JsonProperty("inGameStartDate") String inGameStartDate, @JsonProperty("inGameCurrentDate") String inGameCurrentDate, @JsonProperty("playTime") String playTime, @JsonProperty("budget") int budget, @JsonProperty("cityName") String cityName, @JsonProperty("gameOver") boolean gameOver, @JsonProperty("saveFile") File saveFile, @JsonProperty("yearlyTaxes") int yearlyTaxes, @JsonProperty("fields") Field[][] fields, @JsonProperty("people") ArrayList<Person> people, @JsonProperty("id") String id, @JsonProperty("graph") MutableGraph<Coordinate> graph) {
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
        this.graph = graph;
        this.people = people;
        this.id = id;
        Logger.log("Game data created: " + this.id);
        Logger.log(String.valueOf(this));
    }

    /**
     * Constructor for GameData
     *
     * @param cityName the name of the city
     */
    public GameData(String cityName, int starterBudget, int starterTaxes, int starterPeople, int starterMapSize) {
        this.id = ("game_data_" + getLongDate(System.currentTimeMillis())).replaceAll("[\\s-:]", "_");
        this.startDate = getLongDate(System.currentTimeMillis());
        this.currentDate = getLongDate(System.currentTimeMillis());
        this.inGameStartDate = getDate(0);
        this.inGameCurrentDate = getDate(0);
        this.playTime = "00:00:01";
        budget = starterBudget;
        this.cityName = cityName;
        this.gameOver = false;
        this.saveFile = null;
        this.yearlyTaxes = starterTaxes;
        this.fields = new Field[51][51];
        this.graph = GraphBuilder.undirected().allowsSelfLoops(false).build();

        for (int i = 0; i < starterMapSize; i++) {
            for (int j = 0; j < starterMapSize; j++) { // 2 thick border, 49x49 playable area

                if (i == 0 || i == 1 || i == starterMapSize - 2 || i == starterMapSize - 1 || j == 0 || j == 1 || j == starterMapSize - 2 || j == starterMapSize - 1) {
                    this.fields[i][j] = new BorderField(new Coordinate(i, j));
                } else {
                    this.fields[i][j] = new PlayableField(new Coordinate(i, j));
                }
            }
        }

        this.people = new ArrayList<>();

        for (int i = 0; i < starterPeople; i++) {
            this.people.add(new Person(true));
        }

        Logger.log("New game data created: " + this.id);
    }

    /**
     * Getter for the start date
     *
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Getter for the current date
     *
     * @return the current date
     */
    public String getCurrentDate() {
        return currentDate;
    }

    /**
     * Setter for the current date
     *
     * @param currentDate the current date
     */
    public void setCurrentDate(String currentDate) {
        Logger.log("Game data " + this.id + " current date set to: " + currentDate);
        this.currentDate = currentDate;
    }

    /**
     * Getter for the in game start date
     *
     * @return the in game start date
     */
    public String getInGameStartDate() {
        return inGameStartDate;
    }

    /**
     * Setter for the in game start date
     *
     * @param inGameStartDate the in game start date
     */
    public void setInGameStartDate(String inGameStartDate) {
        Logger.log("Game data " + this.id + " in game start date set to: " + inGameStartDate);
        this.inGameStartDate = inGameStartDate;
    }

    /**
     * Getter for the in game current date
     *
     * @return the in game current date
     */
    public String getInGameCurrentDate() {
        return inGameCurrentDate;
    }

    /**
     * Setter for the in game current date
     *
     * @param inGameCurrentDate the in game current date
     */
    public void setInGameCurrentDate(String inGameCurrentDate) {
        Logger.log("Game data " + this.id + " in game current date set to: " + inGameCurrentDate);
        this.inGameCurrentDate = inGameCurrentDate;
    }

    /**
     * Getter for the play time
     *
     * @return the play time
     */
    public String getPlayTime() {
        return playTime;
    }

    /**
     * Setter for the play time
     *
     * @param playTime the play time
     */
    public void setPlayTime(String playTime) {
        Logger.log("Game data " + this.id + " play time set to: " + playTime);
        this.playTime = playTime;
    }

    /**
     * Calculates the average satisfaction of the people
     */
    public void calculateAverageSatisfaction() {
        int satisfaction = 0;
        for (Person person : people) {
            satisfaction += person.calculateSatisfaction();
        }
        satisfaction /= people.size();
        this.averageSatisfaction = satisfaction;
        Logger.log("Game data " + this.id + " average satisfaction calculated: " + satisfaction);
    }

    /**
     * Getter for the average satisfaction
     *
     * @return the average satisfaction
     */
    public int getAverageSatisfaction() {
        return averageSatisfaction;
    }

    /**
     * Getter for the population
     *
     * @return the population size
     */
    @JsonIgnore
    public int getPopulation() {
        return people.size();
    }

    /**
     * Getter for the city name
     *
     * @return the city name
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Setter for the city name
     *
     * @param cityName the city name
     */
    public void setCityName(String cityName) {
        Logger.log("Game data " + this.id + " city name set to: " + cityName);
        this.cityName = cityName;
    }

    /**
     * Getter for the game over
     *
     * @return if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Setter for the game over
     *
     * @param gameOver if the game is over
     */
    public void setGameOver(boolean gameOver) {
        Logger.log("Game data " + this.id + " game over set to: " + gameOver);
        this.gameOver = gameOver;
    }

    /**
     * Getter for the save file
     *
     * @return the save file
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * Setter for the save file
     *
     * @param saveFile the save file
     */
    public void setSaveFile(File saveFile) {
        Logger.log("Game data " + this.id + " save file set to: " + saveFile.getAbsolutePath());
        this.saveFile = saveFile;
    }

    /**
     * Getter for the yearly taxes
     *
     * @return the yearly taxes
     */
    public int getYearlyTaxes() {
        return yearlyTaxes;
    }

    /**
     * Setter for the yearly taxes
     *
     * @param yearlyTaxes the yearly taxes
     */
    public void setYearlyTaxes(int yearlyTaxes) {
        Logger.log("Game data " + this.id + " yearly taxes set to: " + yearlyTaxes);
        this.yearlyTaxes = yearlyTaxes;
    }

    /**
     * Getter for the fields
     *
     * @return the fields
     */
    public Field[][] getFields() {
        return fields;
    }

    /**
     * Setter for the fields
     *
     * @param fields the fields
     */
    public void setFields(Field[][] fields) {
        Logger.log("Game data " + this.id + " fields set.");
        this.fields = fields;
    }

    /**
     * Getter for the playable fields
     *
     * @return the playable fields
     */
    public ArrayList<PlayableField> getPlayableFieldsWithBuildings() {
        return ((ArrayList<PlayableField>) Arrays.stream(fields)
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField) f)
                .filter(f -> f.getBuilding() != null)
                .collect(Collectors.toList()));
    }

    /**
     * Getter for the graph
     *
     * @return the graph
     */

    public MutableGraph<Coordinate> getGraph() {
        return graph;
    }

    /**
     * Setter for the graph
     *
     * @param graph the graph
     */
    public void setGraph(MutableGraph<Coordinate> graph) {
        this.graph = graph;
    }

    /**
     * Getter for the people
     *
     * @return the people
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * Setter for the people
     *
     * @param people the people
     */
    public void setPeople(ArrayList<Person> people) {
        Logger.log("Game data " + this.id + " people set.");
        this.people = people;
    }

    /**
     * Getter for the id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the id
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the budget
     *
     * @return the budget
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Setter for the budget
     *
     * @param budget the budget
     */
    public void setBudget(int budget) {
        this.budget = budget;
    }

    /**
     * Substracts the given amount from the budget
     *
     * @param amount the amount to substract
     */
    public void subtractFromBudget(int amount) {
        this.budget -= amount;
    }

    /**
     * Adds the given amount to the budget
     *
     * @param amount the amount to add
     */
    public void addToBudget(int amount) {
        this.budget += amount;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "id='" + id + '\'' +
                ", startDate='" + startDate + '\'' +
                ", currentDate='" + currentDate + '\'' +
                ", inGameStartDate='" + inGameStartDate + '\'' +
                ", inGameCurrentDate='" + inGameCurrentDate + '\'' +
                ", playTime='" + playTime + '\'' +
                ", budget=" + budget +
                ", cityName='" + cityName + '\'' +
                ", gameOver=" + gameOver +
                ", saveFile=" + saveFile +
                ", yearlyTaxes=" + yearlyTaxes +
                ", averageSatisfaction=" + averageSatisfaction +
                ", fields=" + Arrays.toString(fields) +
                ", graph=" + graph +
                ", people=" + people +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameData gameData)) return false;
        boolean fieldsEqual = false;
        for (int i = 0; i < GameManager.getStarterMapSize(); i++) {
            for (int j = 0; j < GameManager.getStarterMapSize(); j++) {
                if (fields[i][j].equals(gameData.fields[i][j])) {
                    fieldsEqual = true;
                } else {
                    fieldsEqual = false;
                    break;
                }
            }
        }
        return getBudget() == gameData.getBudget() && isGameOver() == gameData.isGameOver() && getYearlyTaxes() == gameData.getYearlyTaxes() && getAverageSatisfaction() == gameData.getAverageSatisfaction() && Objects.equal(getId(), gameData.getId()) && Objects.equal(getStartDate(), gameData.getStartDate()) && Objects.equal(getCurrentDate(), gameData.getCurrentDate()) && Objects.equal(getInGameStartDate(), gameData.getInGameStartDate()) && Objects.equal(getInGameCurrentDate(), gameData.getInGameCurrentDate()) && Objects.equal(getPlayTime(), gameData.getPlayTime()) && Objects.equal(getCityName(), gameData.getCityName()) && Objects.equal(getSaveFile(), gameData.getSaveFile()) && Objects.equal(getGraph(), gameData.getGraph()) && Objects.equal(getPeople(), gameData.getPeople()) && fieldsEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getStartDate(), getCurrentDate(), getInGameStartDate(), getInGameCurrentDate(), getPlayTime(), getBudget(), getCityName(), isGameOver(), getSaveFile(), getYearlyTaxes(), getAverageSatisfaction(), getFields(), getGraph(), getPeople());
    }
}
