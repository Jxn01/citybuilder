package model;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import model.field.BorderField;
import model.field.Field;
import model.field.PlayableField;
import util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static util.Date.*;

/**
 * This class contains all the data that is needed to save the game.
 */
public class GameData {
    private final int starterMapSize = 51;
    private final int starterPeople = 50;
    private final int starterBudget = 100000;
    private final int starterTaxes = 1000;
    private String id;
    private String startDate;
    private String currentDate;
    private String inGameStartDate;
    private String inGameCurrentDate;
    private String playTime;
    public static Integer budget;
    private String cityName;
    private boolean gameOver;
    private File saveFile;
    private int yearlyTaxes;
    private int averageSatisfaction;

    private Field[][] fields;

    private Graph<Field> graph;
    private ArrayList<Person> people;

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
    public GameData(String startDate, String currentDate, String inGameStartDate, String inGameCurrentDate, String playTime, int budget, String cityName, boolean gameOver, File saveFile, int yearlyTaxes, Field[][] fields, ArrayList<Person> people, String id, Graph<Field> graph) {
        this.startDate = startDate;
        this.currentDate = currentDate;
        this.inGameStartDate = inGameStartDate;
        this.inGameCurrentDate = inGameCurrentDate;
        this.playTime = playTime;
        GameData.budget = budget;
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
    public GameData(String cityName) {
        this.id = "game_data_" + getLongDate(System.currentTimeMillis());
        this.startDate = getLongDate(System.currentTimeMillis());
        this.currentDate = getLongDate(System.currentTimeMillis());
        this.inGameStartDate = getDate(System.currentTimeMillis());
        this.inGameCurrentDate = getDate(System.currentTimeMillis());
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

                if (i == 0 || i == 1 || i == 49 || i == 50 || j == 0 || j == 1 || j == 49 || j == 50) {
                    this.fields[i][j] = new BorderField(new Coordinate(i, j));
                } else {
                    try {
                        this.fields[i][j] = new PlayableField(new Coordinate(i, j));
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        }

        this.people = new ArrayList<>();

        for (int i = 0; i < starterPeople; i++) {
            this.people.add(new Person());
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
     * Setter for the start date
     *
     * @param startDate the start date
     */
    public void setStartDate(String startDate) {
        Logger.log("Game data " + this.id + " start date set to: " + startDate);
        this.startDate = startDate;
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
     * Calculate the population size
     */

    /**
     * Getter for the population
     *
     * @return the population size
     */
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
     * Getter for the playable fields
     *
     * @return
     */
    public ArrayList<PlayableField> getPlayableFieldsWithBuildings() {
        ArrayList<PlayableField> result = new ArrayList<>();

        for (int i = 0; i < starterMapSize; i++) {
            for (int j = 0; j < starterMapSize; j++) {

                if (fields[i][j] instanceof PlayableField) {
                    if (((PlayableField) fields[i][j]).getBuilding() != null) {
                        result.add((PlayableField) fields[i][j]);
                    }
                }

            }
        }

        return result;
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
     * Getter for the graph
     *
     * @return the graph
     */
    public Graph<Field> getGraph() {
        return graph;
    }

    /**
     * Setter for the graph
     *
     * @param graph the graph
     */
    public void setGraph(Graph<Field> graph) {
        this.graph = graph;
    }

    /**
     * Getter for the people
     *
     * @return the people
     */
    public ArrayList<Person> getPeople() {
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
                ", graph=" + graph +
                ", people=" + people +
                '}';
    }
}
