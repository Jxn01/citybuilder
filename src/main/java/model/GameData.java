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
    private String cityName;
    private int budget;
    private String startDate;
    private String currentDate;
    private String inGameStartDate;
    private String inGameCurrentDate;
    private int days = 0;
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
        this.budget = starterBudget;
        this.cityName = cityName;
        this.gameOver = false;
        this.saveFile = null;
        this.yearlyTaxes = starterTaxes;
        this.fields = new Field[starterMapSize][starterMapSize];
        this.graph = GraphBuilder.undirected().allowsSelfLoops(false).build();

        for (int i = 0; i < starterMapSize; i++) {
            for (int j = 0; j < starterMapSize; j++) { // 2 thick border

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

    public void setBudget(int budget) {
        this.budget = budget;
    }

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
        //Logger.log("Game data " + this.id + " in game current date set to: " + inGameCurrentDate); spammy
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        Logger.log("Game data " + this.id + " play time set to: " + playTime);
        this.playTime = playTime;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void increaseDays() {
        days++;
    }

    public int getYearlyTaxes() {
        return yearlyTaxes;
    }

    public void setYearlyTaxes(int yearlyTaxes) {
        Logger.log("Game data " + this.id + " yearly taxes set to: " + yearlyTaxes);
        this.yearlyTaxes = yearlyTaxes;
    }

    public void calculateAverageSatisfaction() {
        this.averageSatisfaction = people.parallelStream().mapToInt(Person::calculateSatisfaction).sum() / people.size();
        //Logger.log("Game data " + this.id + " average satisfaction calculated: " + satisfaction); spammy
    }

    public int getAverageSatisfaction() {
        return averageSatisfaction;
    }

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

    public ArrayList<PlayableField> getPlayableFieldsWithBuildings() {
        return ((ArrayList<PlayableField>) Arrays.stream(fields)
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField) f)
                .filter(f -> f.getBuilding() != null)
                .collect(Collectors.toList()));
    }

    public MutableGraph<Coordinate> getGraph() {
        return graph;
    }

    public void setGraph(MutableGraph<Coordinate> graph) {
        this.graph = graph;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
        Logger.log("Game data " + this.id + " people set.");
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        Logger.log("Game data " + this.id + " save file set to: " + saveFile.getAbsolutePath());
        this.saveFile = saveFile;
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
        return getBudget() == gameData.getBudget() && isGameOver() == gameData.isGameOver() && getYearlyTaxes() == gameData.getYearlyTaxes() && getAverageSatisfaction() == gameData.getAverageSatisfaction() && Objects.equal(getId(), gameData.getId()) && Objects.equal(getStartDate(), gameData.getStartDate()) && Objects.equal(getCurrentDate(), gameData.getCurrentDate()) && Objects.equal(getInGameStartDate(), gameData.getInGameStartDate()) && Objects.equal(getPlayTime(), gameData.getPlayTime()) && Objects.equal(getCityName(), gameData.getCityName()) && Objects.equal(getSaveFile(), gameData.getSaveFile()) && Objects.equal(getGraph(), gameData.getGraph()) && Objects.equal(getPeople(), gameData.getPeople()) && fieldsEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getStartDate(), getCurrentDate(), getInGameStartDate(), getInGameCurrentDate(), getPlayTime(), getBudget(), getCityName(), isGameOver(), getSaveFile(), getYearlyTaxes(), getAverageSatisfaction(), getFields(), getGraph(), getPeople());
    }
}
