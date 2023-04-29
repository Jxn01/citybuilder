package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
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

import static util.Date.*;

/**
 * This class contains all the data that is needed to save the game.
 */
public class GameData {
    private final int STARTER_MAP_SIZE = 51;
    private final int STARTER_NUMBER_OF_PEOPLE = 50;
    private final int STARTER_BUDGET = 100000;
    private final int STARTER_TAXES = 1000;
    private String id;
    private String cityName;
    private int budget;
    private String startDate;
    private String currentDate;
    private String inGameStartDate;
    private String inGameCurrentDate;
    private String playTime;
    private int yearlyTaxes;
    private int averageSatisfaction;
    private boolean gameOver;

    private Field[][] fields;
    @JsonDeserialize(using = GraphDeserializer.class)
    @JsonSerialize(using = GraphSerializer.class)
    private MutableGraph<Coordinate> graph;
    private List<Person> people;

    private File saveFile;

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

    public GameData(String cityName) {
        this.id = ("game_data_" + getLongDate(System.currentTimeMillis())).replaceAll("[\\s-:]", "_");
        this.startDate = getLongDate(System.currentTimeMillis());
        this.currentDate = getLongDate(System.currentTimeMillis());
        this.inGameStartDate = getDate(System.currentTimeMillis());
        this.inGameCurrentDate = getDate(System.currentTimeMillis());
        this.playTime = "00:00:01";
        budget = STARTER_BUDGET;
        this.cityName = cityName;
        this.gameOver = false;
        this.saveFile = null;
        this.yearlyTaxes = STARTER_TAXES;
        this.fields = new Field[51][51];
        this.graph = GraphBuilder.undirected().allowsSelfLoops(false).build();

        for (int i = 0; i < STARTER_MAP_SIZE; i++) {
            for (int j = 0; j < STARTER_MAP_SIZE; j++) { // 2 thick border, 49x49 playable area

                if (i == 0 || i == 1 || i == starterMapSize-2 || i == starterMapSize-1 || j == 0 || j == 1 || j == starterMapSize-2 || j == starterMapSize-1) {
                    this.fields[i][j] = new BorderField(new Coordinate(i, j));
                } else {
                    this.fields[i][j] = new PlayableField(new Coordinate(i, j));
                }
            }
        }

        this.people = new ArrayList<>();

        for (int i = 0; i < STARTER_NUMBER_OF_PEOPLE; i++) {
            this.people.add(new Person(true ));
        }

        Logger.log("New game data created: " + this.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
        Logger.log("Game data " + this.id + " city name set to: " + cityName);
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) { this.budget = budget; }

    public void subtractFromBudget(int amount) {
        this.budget -= amount;
    }

    public void addToBudget(int amount) {
        this.budget += amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        Logger.log("Game data " + this.id + " start date set to: " + startDate);
        this.startDate = startDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
        Logger.log("Game data " + this.id + " current date set to: " + currentDate);
    }

    public String getInGameStartDate() {
        return inGameStartDate;
    }

    public void setInGameStartDate(String inGameStartDate) {
        Logger.log("Game data " + this.id + " in game start date set to: " + inGameStartDate);
        this.inGameStartDate = inGameStartDate;
    }

    public String getInGameCurrentDate() {
        return inGameCurrentDate;
    }

    public void setInGameCurrentDate(String inGameCurrentDate) {
        this.inGameCurrentDate = inGameCurrentDate;
        Logger.log("Game data " + this.id + " in game current date set to: " + inGameCurrentDate);
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        Logger.log("Game data " + this.id + " play time set to: " + playTime);
        this.playTime = playTime;
    }

    public int getYearlyTaxes() {
        return yearlyTaxes;
    }

    public void setYearlyTaxes(int yearlyTaxes) {
        Logger.log("Game data " + this.id + " yearly taxes set to: " + yearlyTaxes);
        this.yearlyTaxes = yearlyTaxes;
    }

    private void calculateAverageSatisfaction() {
        int satisfaction = 0;
        for (Person person : people) {
            satisfaction += person.calculateSatisfaction();
        }
        satisfaction /= people.size();
        this.averageSatisfaction = satisfaction;
        Logger.log("Game data " + this.id + " average satisfaction calculated: " + satisfaction);
    }

    public int getAverageSatisfaction() {
        calculateAverageSatisfaction();
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

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        Logger.log("Game data " + this.id + " game over set to: " + gameOver);
        this.gameOver = gameOver;
    }

    public Field[][] getFields() {
        return fields;
    }
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
        ArrayList<PlayableField> result = new ArrayList<>();

        for (int i = 0; i < STARTER_MAP_SIZE; i++) {
            for (int j = 0; j < STARTER_MAP_SIZE; j++) {

                if (fields[i][j] instanceof PlayableField) {
                    if (((PlayableField) fields[i][j]).getBuilding() != null) {
                        result.add((PlayableField) fields[i][j]);
                    }
                }

            }
        }

        return result;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
        Logger.log("Game data " + this.id + " people set.");
    }

    public MutableGraph<Coordinate> getGraph() {
        return graph;
    }

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

    public void setSaveFile(File saveFile) {
        Logger.log("Game data " + this.id + " save file set to: " + saveFile.getAbsolutePath());
        this.saveFile = saveFile;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "id='" + id + '\'' +
                ", saveFile=" + saveFile +
                ", cityName='" + cityName + '\'' +
                ", population=" + getPopulation() +
                ", averageSatisfaction=" + averageSatisfaction +
                ", playTime='" + playTime + '\'' +
                ", inGameStartDate='" + inGameStartDate + '\'' +
                ", inGameCurrentDate='" + inGameCurrentDate + '\'' +
                ", currentDate='" + currentDate + '\'' +
                ", yearlyTaxes=" + yearlyTaxes +
                ", gameOver=" + gameOver +
                ", fields=" + Arrays.toString(fields) +
                ", people=" + people +
                '}';
    }
}
